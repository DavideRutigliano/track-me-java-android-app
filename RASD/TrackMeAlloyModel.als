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
	organizer: Organizer,
	athletes: set Athlete,
	spectators: set Spectator
}

sig Athlete extends Individual{
	enrolledRuns: set Run
}

sig Spectator extends Individual{
	watchedRuns : set Run,
}

sig Organizer extends User{
	organizedRuns : set Run
}

fact dataAtomicity{
	all u:Username | some user:User | user.username = u
	all p:Password | some user:User | user.password = p
	all n:Name | some i:Individual | some t:ThirdParty | i.name = n or t.organization = n
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
	all disj r1,r2: Run, a: Athlete | isSameDate[r1,r2] => (a not in r1.athletes or a not in r2.athletes)
}

-- a spectator can't enroll 2 runs that happens in the same date/time
fact noMultipleWatch{
	all disj r1,r2: Run, s: Spectator | isSameDate[r1,r2] => (s not in r1.spectators or s not in r2.spectators)
}

fact athletesCantWatch{
	no i: Individual, a: Athlete, s: Spectator | (i.ssn = a.ssn and i.ssn = a.ssn) and #a.enrolledRuns > 0 and a.enrolledRuns = s.watchedRuns
}

fact setOrganizer{
	all o: Organizer, r: Run | r in o.organizedRuns => r.organizer = o
}

fact athleteEnrolled{
	all a: Athlete, r: Run | r in a.enrolledRuns => a in a.enrolledRuns.athletes
}

fact spectatorWatching {
	all s: Spectator, r: Run | r in s.watchedRuns => s in s.watchedRuns.spectators
}

pred makeOneRequest(r: Request, p: ThirdParty, i: Individual){
	#IndividualRequest = 1
	#GroupRequest = 1
	i.incomingRequests = i.incomingRequests + r
	p.subscribedUsers = p.subscribedUsers + i
}

pred hasAnonimity[r: GroupRequest]{
	#r.receiver > 1000	
}

pred makeRequests(r: Request, p: ThirdParty, i: Individual){
	#IndividualRequest = 2
	#GroupRequest = 2
	i.incomingRequests = i.incomingRequests + r
	p.subscribedUsers = p.subscribedUsers + i
}

pred enableAutomatedSos(a: AutomatedSos, p: ThirdParty){
	a.provider = p
}

pred isRunner[a:Athlete]{
	some r: Run | r in a.enrolledRuns
}

pred isWatcher[s:Spectator]{
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

pred createRun[r:Run, o:Organizer]{
	r.organizer = o	
}

pred enrollRun[r:Run, a:Athlete]{
	//r in a.enrolledRuns
}

pred watchRun[r:Run, s:Spectator]{
	//r in s.watchedRuns
}

pred Track4Run {
	createRun[Run, Organizer]
	enrollRun[Run, Athlete]
	watchRun[Run, Spectator]
	#Request = 0
	#AutomatedSos = 0
	#Ambulance = 0
}
//run enrollToRun for 2 but 1 ThirdParty

pred watchRun{
	#Run = 1
	#Request = 0	
	#AutomatedSos = 0
	#Ambulance = 0
	#Athlete = 1
	#Spectator = 1
}

pred show{}

run show for 3 but 1 Individual
