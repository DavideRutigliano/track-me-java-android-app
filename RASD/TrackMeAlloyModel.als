module trackMe
open util/boolean

sig Username, Password{}

abstract sig User{
	username: Username,
	password: Password
}

sig Name, Ssn, Vat{}

sig Individual extends User{
	name: Name,
	incomingRequests: set Request,
	ssn: Ssn,
	enableSos: Bool
}

abstract sig Request{
	sender: ThirdParty,
	accepted : Bool 
}

sig IndividualRequest extends Request{
	receiver: Individual,
	ssn: Ssn,
}

sig GroupRequest extends Request{
	receiver: some Individual
}

sig ThirdParty extends User{
	organization: Name,
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
	ambulances: some Ambulance
}

sig Track, Duration, Date, Time{}

sig Run{
	track: Track,
	duration: Duration,
	date: Date,
	time: Time,
}

sig Athlete extends Individual{
	enrolledRuns: set Run
}

sig Spectator extends Individual{
	watchedRuns : set Run
}

sig Organizer extends Individual{
	organizedRuns : set Run
}

fact dataAtomicity{
	all u:Username | some user:User | user.username = u
	all p:Password | some user:User | user.password = p
	all n:Name | some i:Individual,  t:ThirdParty | i.name = n or t.organization = n
	all s:Ssn | some i:Individual | i.ssn = s
	all v:Vat | some t:ThirdParty | t.vat= v
	all t:Track | some r:Run | r.track = t
	all d:Duration | some r:Run | r.duration = d	
	all d:Date | some r:Run | r.date = d	
	all t:Time | some r:Run | r.time = t		
}

fact dataUniqueness{
	no disj u1,u2: User | u1.username = u2.username 						//username
	no disj i1,i2: Individual | i1.ssn = i2.ssn											//SSN
	no disj p1,p2: ThirdParty | p1.vat = p2.vat										//VAT
	no disj p1,p2: ThirdParty | p1.organization = p2.organization		//organization name
}

fact noIndividualIsThirdParty{
	no i: Individual, p: ThirdParty | i.name = p.organization
}

fact sentRequestAreRecorded{
	all r: Request, p: ThirdParty | r.sender = p => r in p.sentRequests
}

fact subscribedUsersAreRecorded{
	all r: IndividualRequest, p: ThirdParty, i: Individual |  i in p.subscribedUsers iff (r.sender = p and r.receiver = i and isTrue[r.accepted])
}

fact subscribedGroupsAreRecorded{
	all r: GroupRequest, p: ThirdParty | r.sender = p => r.receiver in p.subscribedUsers
}

fact queueRequest {
	all r: IndividualRequest, i: Individual | r in i.incomingRequests iff (r.receiver = i and isTrue[r.accepted])
}

fact noSpamRequest{
	no disj r1,r2: IndividualRequest, i: Individual | r1.sender = r2.sender and r1.receiver.ssn = i.ssn and r2.receiver.ssn = i.ssn
}

fact requestSsnPointAtCorrectReceiver{
	all r: IndividualRequest, i: Individual | r.ssn = i.ssn iff i.ssn = r.receiver.ssn
}

-- a groupRequest can be accepted only if it is anonymous
fact grantAnonimity{
	all r: GroupRequest | isTrue[r.accepted] iff hasAnonimity[r]
}
-- to subscribe to an individual's new data, a third party must have sent a request that has been accepted
fact subscriptionMustBeAccepted{
	all t:ThirdParty, i:Individual | some r:IndividualRequest | i in t.subscribedUsers => requestBetween[r, t, i] and isTrue[r.accepted]
}

-- an Individual can be an Automated-SOS customer only if he has activated the service
fact enabledSosMeansCustomer{
	all a: AutomatedSos, i: Individual | i in a.customers iff isTrue[i.enableSos] 
}

fact uniqueAutomatedSosService {
	no disj a1, a2: AutomatedSos, p: ThirdParty | a1.provider = p and a2.provider = p
}

-- an Individual can be subscribed to only one Automated-SOS provider
fact justOneAutomatedSosSub{
	no disj a1,a2: AutomatedSos, i: Individual | i in a1.customers and i in a2.customers 
}

fact onlyAvailableAmbulances{
	all a: Ambulance, s: AutomatedSos | a in s.ambulances iff isTrue[a.available]
}

-- there can't exist two runs that have the same track in the same date
fact noDuplicatedRun{
	no disj r1,r2: Run | isSameRun[r1,r2]
}

-- an athlete can't enroll 2 runs that happens in the same date/time
fact noMultipleEnrollement{
	all disj a:Athlete, r1,r2: Run | (isEnrolled[r1,a] and isEnrolled[r2,a]) => not isSameDate[r1,r2]
}

-- a spectator can't enroll 2 runs that happens in the same date/time
fact noMultipleWatch{
	all disj s:Spectator, r1,r2:Run | (isEnrolled[r1,s] and isEnrolled[r2,s]) => not isSameDate[r1,r2]
}

fact athletesCantWatch{
	no s: Spectator, a: Athlete | isSameIndividual[s,a] and #a.enrolledRuns > 0 and hasSameRuns[s,a]
}

fact runMustBeOrganized{
	all r:Run | some o:Organizer | hasOrganized[r,o]
}

pred requestBetween[r:IndividualRequest, t:ThirdParty, i:Individual]{
	r.sender = t and r.receiver = i 	
}

pred isSubscribedToData[t:ThirdParty, i:Individual]{
	i in t.subscribedUsers
}

pred hasAnonimity[r: GroupRequest]{
	#r.receiver > 1000	
}

pred enableAutomatedSos(a: AutomatedSos, p: ThirdParty){
	a.provider = p
}

pred isSameIndividual[s:Spectator, a:Athlete]{
	s.ssn = a.ssn
}

pred hasSameRuns[a1,a2:Athlete]{
	a1.enrolledRuns = a2.enrolledRuns
}

pred hasSameRuns[s1,s2:Spectator]{
	s1.watchedRuns = s2.watchedRuns
}

pred hasSameRuns[s:Spectator, a:Athlete]{
	a.enrolledRuns = s.watchedRuns
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

pred hasRuns[a:Athlete]{
	some r: Run | r in a.enrolledRuns
}

pred hasRuns[s:Spectator]{
	some r: Run | r in s.watchedRuns
}

pred isSameDate[r1, r2 : Run]{
	r1.date = r2.date and r1.time = r2.time
}

pred isSameRun [r1, r2 : Run]{
	isSameDate[r1,r2] and r1.track = r2.track	
}

pred runAutomatedSos(a: AutomatedSos, p: ThirdParty){
	#Ambulance = 2
	#Run = 0
	a.provider = p
}

pred disableData4Help{
	#Request = 0
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

pred Track4Run {
	#Request = 0
	#AutomatedSos = 0
	#Ambulance = 0
	#Individual = 2
}

pred watchRun{
	#Run = 1
	#Request = 0	
	#AutomatedSos = 0
	#Ambulance = 0
	#Athlete = 1
	#Spectator = 1
}

pred groupRequest{
	disableAutomatedSos
	disableTrack4Run
	#Individual = 3
	//some GroupRequest
}

pred data4Help{
	disableTrack4Run
	disableAutomatedSos
	some IndividualRequest
	some GroupRequest
}

run data4Help for 3
