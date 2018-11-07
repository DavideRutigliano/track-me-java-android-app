module trackMe
open util/boolean

sig Username, Password{}

abstract sig User{
	username: Username,
	password: Password
}

sig Name, OrganizationName, Ssn, Vat,Position{}

sig Individual extends User{
	name: Name,
	incomingRequests: set IndividualRequest,
	ssn: Ssn,
	enableSos: Bool,
	position:Position
}

abstract sig Request{
	sender: ThirdParty,
	receiver: some Individual,
	accepted : Bool 
}

sig IndividualRequest extends Request{
	ssn: Ssn,
}

sig GroupRequest extends Request{}

sig ThirdParty extends User{
	organization: OrganizationName,
	sentRequests: set Request,
	subscribedUsers: set Individual,
	vat: Vat
}

sig Ambulance{
	available: Bool
}

sig AutomatedSos{
	provider: ThirdParty,
	customers: set Individual,
	dispatcher: AmbulanceDispatcher
}

sig AmbulanceDispatcher{
	ambulances: some Ambulance
}

sig Accident{
	position:Position,
	ambulance:Ambulance
}

sig Track, Duration, Date, Time{}

sig Run{
	state: RunState,
	organizer: Organizer,
	track: Track,
	duration: Duration,
	date: Date,
	time: Time,
}

abstract sig RunState{}

one sig Created extends RunState{}
one sig Started extends RunState{}
one sig Closed extends RunState{}
one sig Deleted extends RunState{}

sig Athlete extends Individual{
	enrolledRuns: set Run
}

sig Spectator extends Individual{
	watchedRuns : set Run
}

sig Organizer extends Individual{
	organizedRuns : set Run
}

-----------------------> FACTS <-----------------------

fact dataUniqueness{
	no disj u1,u2: User | u1.username = u2.username //username
	no disj i1,i2: Individual | i1.ssn = i2.ssn //SSN
	no disj p1,p2: ThirdParty | p1.vat = p2.vat //VAT
	no disj p1,p2: ThirdParty | p1.organization = p2.organization //organization name
}

----------------------- DATA4HELP -----------------------

fact individualRequestsAreUnary{
	all r:IndividualRequest | #r.receiver = 1
}

fact sentRequestAreRecorded{
	all r: Request, t: ThirdParty | (r in t.sentRequests) iff (r.sender = t)
}

fact receivedRequestAreRecorded {
	all r: IndividualRequest, i: Individual |( r in i.incomingRequests) iff (r.receiver = i)
}

fact requestSsnPointAtCorrectReceiver{
	all r: IndividualRequest, i: Individual | (r.ssn = i.ssn) iff (i.ssn = r.receiver.ssn)
}

-- a groupRequest will be accepted only if it is anonymous
fact grantAnonimity{
	all r: GroupRequest | isTrue[r.accepted] iff hasAnonimity[r]
}

-- to subscribe to an individual's new data, a third party must have sent a request that has been accepted
fact subscriptionMustBeAccepted{
	all t:ThirdParty, i:Individual, r:IndividualRequest | i in t.subscribedUsers => (requestBetween[r, t, i] and isTrue[r.accepted])
}

----------------------- AUTOMATEDSOS -----------------------

-- a Third party can provide only one automated-sos service
fact uniqueAutomatedSosService {
	all t: ThirdParty | no disj a1, a2: AutomatedSos | enabledService[a1, t] and enabledService[a2, t]
}

-- an Individual can be an Automated-SOS customer only if he has activated the service
fact enabledSosMeansCustomer{
	all a: AutomatedSos, i: Individual | isCustomer[i, a] => isTrue[i.enableSos] 
}

-- an Individual can be subscribed to only one Automated-SOS provider
fact justOneAutomatedSosSub{
	all i: Individual | no disj a1,a2: AutomatedSos | isCustomer[i, a1] and isCustomer[i, a2]
}

-- ambulances are managed by some dispatcher
fact ambulancesAreManaged{
	all a:Ambulance | some d:AmbulanceDispatcher | a in d.ambulances
}

-- ambulances can have only one dispatcher
fact ambulancesArentShared{
	all a:Ambulance | all disj d1,d2:AmbulanceDispatcher | a in d1.ambulances => a not in d2.ambulances
}

-- an accident must have happened where there is an individual
fact accidentHasAnIndividualPosition{
	all a:Accident | some i:Individual | a.position = i.position
}

-- an ambulance associated with an accident is not available
fact ambulancesAvailability{
	all a:Ambulance, acc: Accident | isManaging[a, acc] => isFalse[a.available]
}

-- an accident must has managed by only ony ambulance 
fact ambulanceManagesOnlyOneAccident{
	all disj acc1,acc2:Accident | all a:Ambulance | isManaging[a, acc1] => not isManaging[a, acc2]
}

fact dispatcherWorksForAutomatedSos{
	all d:AmbulanceDispatcher | some a:AutomatedSos | a.dispatcher = d
}

----------------------- TRACK4RUN -----------------------

fact organizedRunsAreRecorded{
	all r:Run, o:Organizer | (r.organizer = o) iff (r in o.organizedRuns)
}

-- there can't exist two runs that have the same track in the same date
fact noDuplicatedRun{
	no disj r1,r2: Run | isSameRun[r1,r2]
}

-- an athlete can't enroll 2 runs that happens in the same date/time
fact noMultipleEnrollement{
	all a:Athlete | all disj r1,r2: Run | (isEnrolled[r1,a] and isEnrolled[r2,a]) => not isSameDate[r1,r2]
}

-- a spectator can't enroll 2 runs that happens in the same date/time
fact noMultipleWatch{
	all s:Spectator | all disj r1,r2:Run | (isEnrolled[r1,s] and isEnrolled[r2,s]) => not isSameDate[r1,r2]
}

-- an athlete can't watch the runs where he is also enrolled
fact athletesCantWatch{
	no s: Spectator, a: Athlete | isSameIndividual[s,a] and #a.enrolledRuns > 0 and hasCommonRuns[s,a]
}

-- if a run exists it must be organized by some organizer
fact runMustBeOrganized{
	all r:Run | some o:Organizer | hasOrganized[r,o]
}

-- athletes can enroll to a run only if that run state is "created"
fact athletesEnrollOnlyCreatedRuns{
	all a:Athlete, r:Run | r in a.enrolledRuns => r.state = Created 
}

-- spectator can enroll to a run only if that run state is "started"
fact spectatorsEnrollOnlyCreatedRuns{
	all s:Spectator, r:Run | r in s.watchedRuns => r.state = Started 
}

-----------------------> PREDICATES <-----------------------

----------------------- DATA4HELP -----------------------

pred isSameRequest[r1,r2:Request]{
	r1.receiver = r2.receiver and r1.sender = r2.sender	
}

pred requestBetween[r:Request, t:ThirdParty, i:Individual]{
	r.sender = t and r.receiver = i 	
}

pred isSubscribedToData[t:ThirdParty, i:Individual]{
	i in t.subscribedUsers
}

pred hasAnonimity[r: GroupRequest]{
	#r.receiver > 1000	
}

-------------------- AUTOMATED-SOS ---------------------

pred enabledService[a: AutomatedSos, p: ThirdParty]{
	a.provider = p
}

pred isCustomer[i:Individual, a:AutomatedSos]{
	i in a.customers		
}

pred isManaging[a:Ambulance, acc:Accident]{
	acc.ambulance = a
}

----------------------- TRACK4RUN -----------------------

pred isSameIndividual[s:Spectator, a:Athlete]{
	s.ssn = a.ssn
}

pred hasCommonRuns[s:Spectator, a:Athlete]{
	a.enrolledRuns & s.watchedRuns != none
}

pred isEnrolled[r: Run, a:Athlete]{
	r in a.enrolledRuns
}

pred isEnrolled[r: Run, s:Spectator]{
	r in s.watchedRuns
}

pred hasOrganized[r:Run, o:Organizer]{
	r in o.organizedRuns
}

pred isSameDate[r1, r2 : Run]{
	r1.date = r2.date and r1.time = r2.time
}

pred isSameRun [r1, r2 : Run]{
	isSameDate[r1,r2] and r1.track = r2.track	
}

---------------------------------------------------------
pred disableData4Help{
	#Request = 0
	#ThirdParty.subscribedUsers = 0
}

pred disableAutomatedSos{
	#AutomatedSos = 0
	#Ambulance = 0
}

pred disableTrack4Run{
	#Athlete = 0
	#Spectator = 0
	#Organizer = 0
	#Run = 0
}

pred data4Help{
	disableTrack4Run
	disableAutomatedSos
	some IndividualRequest
	some GroupRequest
}

pred automatedSos{
	disableData4Help
	disableTrack4Run
	some Ambulance
	some AutomatedSos
	#AutomatedSos.customers > 0	
}

pred track4Run{
	disableData4Help
	disableAutomatedSos
	some Organizer
	some Athlete
	some Spectator
	some Run
}

pred showAll{}

//run data4Help for 3 but exactly 1 Individual
//run automatedSos for 3 but exactly 1 Individual
//run track4Run for 3 but exactly 1 Run
run showAll for 3
