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
}

sig IndividualRequest extends Request{
	receiver: Individual,
	ssn: Ssn,
	accepted: Bool
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
	enrolled: set Run
}

sig Spectator extends Individual{
	watch: set Run,
}

sig Organizer extends User{
	organized: set Run
}

fact atomicity{
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

fact uniqueUsername{
	no disj u1,u2: User | u1.username = u2.username
}

fact uniqueSsn{
	no disj i1,i2: Individual | i1.ssn = i2.ssn
}

fact uniqueVat{
	no disj p1,p2: ThirdParty | p1.vat = p2.vat
}
fact uniqueName{
	no disj p1,p2: ThirdParty | p1.organization = p2.organization
}
fact noIndividualThirdParty{
	no i: Individual, p: ThirdParty | i.name = p.organization
}
fact sendRequest{
	all r: Request, p: ThirdParty | r.sender = p => r in p.sentRequests
}
fact subscribedUsers{
	all r: IndividualRequest, p: ThirdParty, i: Individual |  i in p.subscribedUsers iff (r.sender = p and r.receiver = i and isTrue[r.accepted])
}

fact subscribeGroups{
	all r: GroupRequest, p: ThirdParty | r.sender = p => r.receiver in p.subscribedUsers
}

fact uniqueIdentifier{
	no disj i1,i2: Individual | i1.ssn = i2.ssn
	no disj i1,i2: ThirdParty | i1.vat = i2.vat
}

fact queueRequest {
	all r: IndividualRequest, i: Individual | r in i.incomingRequests iff (r.receiver = i and isTrue[r.accepted])
}
fact noSpamRequest{
	no disj r1,r2: IndividualRequest, i: Individual | r1.sender = r2.sender and r1.receiver.ssn = i.ssn and r2.receiver.ssn = i.ssn
}
fact needSsn{
	all r: IndividualRequest, i: Individual | r.ssn = i.ssn => i.ssn = r.receiver.ssn
}

fact grantAnonimity{
	all r: GroupRequest, i: Individual | (i in r.receiver and #r.receiver >= 2) => r in i.incomingRequests
	//no r: GroupRequest, i: Individual | (i in r.receiver and #r.receiver < 2) => r in i.incomingRequests
}

fact enabledSosMeansCustomer{
	all a: AutomatedSos, i: Individual | i in a.customers iff isTrue[i.enableSos] 
}

fact uniqueAutomatedSosService {
	no disj a1, a2: AutomatedSos, p: ThirdParty | a1.provider = p and a2.provider = p
}
fact uniqueAmbulance{
	no disj a1, a2: AutomatedSos, a: Ambulance | a in a1.ambulances and a in a2.ambulances
}
fact multipleSos{
	no disj a1,a2: AutomatedSos, i: Individual | i in a1.customers and i in a2.customers 
}
fact onlyAvailableAmbulances{
	all a: Ambulance, s: AutomatedSos | a in s.ambulances iff isTrue[a.available]
}

fact noDupicatedRun{
	no disj r1,r2: Run | r1.track = r2.track and r1.date = r2.date and r1.time = r2.time
}
fact noMultipleEnrollement{
	all disj r1,r2: Run, a: Athlete | (r1.date = r2.date and r1.time = r2.time) => (a not in r1.athletes and a not in r2.athletes)
}
fact noMultipleWatch{
	all disj r1,r2: Run, s: Spectator | (r1.date = r2.date and r1.time = r2.time) => (s not in r1.spectators and s not in r2.spectators)
}
fact athletesCantWatch{
	no i: Individual, a: Athlete, s: Spectator | i.ssn = a.ssn and i.ssn = a.ssn and #a.enrolled > 0 and a.enrolled = s.watch
}

fact setOrganizer{
	all o: Organizer, r: Run | r in o.organized => r.organizer = o
}

fact athleteEnrolled{
	all a: Athlete, r: Run | r in a.enrolled => a in a.enrolled.athletes
}

fact spectatorWatching {
	all s: Spectator, r: Run | r in s.watch => s in s.watch.spectators
}

pred makeOneRequest(r: Request, p: ThirdParty, i: Individual){
	#IndividualRequest = 1
	#GroupRequest = 1
	i.incomingRequests = i.incomingRequests + r
	p.subscribedUsers = p.subscribedUsers + i
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

pred runAutomatedSos(a: AutomatedSos, p: ThirdParty){
	#Ambulance = 2
	#Run = 0
	a.provider = p
}

pred createRun{
	#Run = 1
	#Request = 0	
	#AutomatedSos = 0
	#Ambulance = 0
}
//run createRun for 1 but 1 ThirdParty

pred enrollToRun{
	#Run = 1
	#Request = 0	
	#AutomatedSos = 0
	#Ambulance = 0
	#Athlete = 1
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
//run watchRun for 3 but 1 ThirdParty

run TrackMe{
	#AutomatedSos = 1
	#Run = 1
} for 4 but 2 Individual, 1 GroupRequest
