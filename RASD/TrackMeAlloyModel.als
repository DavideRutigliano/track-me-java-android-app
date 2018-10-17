module trackMe

sig Username, Password{}

abstract sig User{
	username: Username,
	password: Password
}
fact uniqueUsername{
	no disj u1,u2: User | u1.username = u2.username
}

sig Name, Ssn, Vat{}

sig Individual extends User{
	name: Name,
	incomingRequests: set Request,
	ssn: Ssn,
	enableSos: Int
}
fact enableNotDisabled{
	all i: Individual | i.enableSos != 1 => i.enableSos = 0
}
fact disableNotEnabled{
	all i: Individual | i.enableSos != 0 => i.enableSos = 1
}
fact uniqueSsn{
	no disj i1,i2: Individual | i1.ssn = i2.ssn
}
assert enableIsBool{
	all i: Individual | i.enableSos = 0 or i.enableSos = 1
}
check enableIsBool

sig ThirdParty extends User{
	organization: Name,
	sentRequests: set Request,
	subscribedUsers: set Individual,
	vat: Vat
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
fact subscribeUsers{
	all r: IndividualRequest, p: ThirdParty, i: Individual | (r.sender = p and r.receiver = i and r.accepted = 1) => i in p.subscribedUsers
}
fact unsubscribedUsers{
	no r: IndividualRequest, p: ThirdParty | r.accepted = 0 and r.receiver in p.subscribedUsers
}
fact subscribeGroups{
	all r: GroupRequest, p: ThirdParty | r.sender = p => r.receiver in p.subscribedUsers
}

fact uniqueIdentifier{
	no disj i1,i2: Individual | i1.ssn = i2.ssn
	no disj i1,i2: ThirdParty | i1.vat = i2.vat
}

abstract sig Request{
	sender: ThirdParty, 
}

sig IndividualRequest extends Request{
	receiver: Individual,
	ssn: Ssn,
	accepted: Int
}
fact queueRequest {
	all r: IndividualRequest, i: Individual | (r.receiver = i and r.accepted = 1) => r in i.incomingRequests
}
fact denyRequest{
	all r: IndividualRequest, i: Individual | r.accepted = 0 => r not in i.incomingRequests
}
fact noSpamRequest{
	no disj r1,r2: IndividualRequest, i: Individual | r1.sender = r2.sender and r1.receiver.ssn = i.ssn and r2.receiver.ssn = i.ssn
}
fact needSsn{
	all r: IndividualRequest, i: Individual | r.ssn = i.ssn => i.ssn = r.receiver.ssn
}
fact acceptedNotRefused{
	all r: IndividualRequest | r.accepted != 1 => r.accepted = 0
}
fact refusedNotAccepted{
	all r: IndividualRequest | r.accepted != 0 => r.accepted = 1
}
assert acceptedIsBoolean{
	all r: IndividualRequest | r.accepted = 1 or r.accepted = 0
}
check acceptedIsBoolean
assert consistencyCheck{
	all r1,r2: IndividualRequest | ( r1.receiver = r2.receiver and r1.sender = r2.sender ) => ( r1.accepted = 1 or r2.accepted = 0 )
}
check consistencyCheck

sig GroupRequest extends Request{
	receiver: some Individual
}
fact doNotQueueRequest{
	no r: GroupRequest, i: Individual | r in i.incomingRequests
}

pred makeOneRequest(r: Request, p: ThirdParty, i: Individual){
	#IndividualRequest = 1
	#GroupRequest = 1
	i.incomingRequests = i.incomingRequests + r
	p.subscribedUsers = p.subscribedUsers + i
}
run makeOneRequest for 2 but 1 Individual, 1 ThirdParty, 0 AutomatedSos, 0 Run

pred makeRequests(r: Request, p: ThirdParty, i: Individual){
	#IndividualRequest = 2
	#GroupRequest = 2
	i.incomingRequests = i.incomingRequests + r
	p.subscribedUsers = p.subscribedUsers + i
}
run makeRequests for 4 but 2 Individual, 1 ThirdParty

sig Ambulance{
	available: Int
}
fact availableNotUnavailable{
	all a: Ambulance | a.available != 1 => a.available = 0
}
fact unavailableNotAvailable{
	all a: Ambulance | a.available != 0 => a.available = 1
}
assert availabilityIsBoolean{
	all a: Ambulance | a.available = 1 or a.available = 0
}
check availabilityIsBoolean

sig AutomatedSos{
	provider: ThirdParty,
	customers: set Individual,
	ambulances: some Ambulance
}
fact enableSos{
	all a: AutomatedSos, i: Individual | i.enableSos = 1 => i in a.customers
}
fact disableSos{
	no a: AutomatedSos, i: Individual | i.enableSos = 0 and i in a.customers
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
	all a: Ambulance, s: AutomatedSos | a.available = 1 => a in s.ambulances
}
fact noUnavailableAmbulances{
	no a: Ambulance, s: AutomatedSos | a.available = 0 and a in s.ambulances
}

pred enableAutomatedSos(a: AutomatedSos, p: ThirdParty){
	a.provider = p
}
run enableAutomatedSos for 1

pred runAutomatedSos(a: AutomatedSos, p: ThirdParty){
	#Ambulance = 2
	a.provider = p
}
run runAutomatedSos for 2 but 2 AutomatedSos

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
fact noDupicatedRun{
	no disj r1,r2: Run | r1.track = r2.track and r1.date = r2.date and r1.time = r2.time
}
fact noMultipleEnrollement{
	all disj r1,r2: Run, a: Athlete | (r1.date = r2.date and r1.time = r2.time) => (a not in r1.athletes and a not in r2.athletes)
}
fact noMultipleWatch{
	all disj r1,r2: Run, s: Spectator | (r1.date = r2.date and r1.time = r2.time) => (s not in r1.spectators and s not in r2.spectators)
}
assert athletesCantWatch{
	no a: Athlete, s: Spectator | a.enrolled = s.watch
}

sig Organizer extends ThirdParty{
	organized: set Run
}
fact setOrganizer{
	all o: Organizer | o.organized.organizer = o
}

sig Athlete extends Individual{
	enrolled: set Run
}
fact athleteEnrolled{
	all a: Athlete, r: Run | r in a.enrolled => a in a.enrolled.athletes
}

sig Spectator extends Individual{
	watch: Run,
}
fact spectatorWatching {
	all s: Spectator, r: Run | r in s.watch => s in s.watch.spectators
}

pred createRun{
	#Run = 1
	#Request = 0	
	#AutomatedSos = 0
	#Ambulance = 0
}
run createRun for 1 but 1 ThirdParty

pred enrollToRun{
	#Run = 1
	#Request = 0	
	#AutomatedSos = 0
	#Ambulance = 0
	#Athlete = 1
}
run enrollToRun for 2 but 1 ThirdParty

pred watchRun{
	#Run = 1
	#Request = 0	
	#AutomatedSos = 0
	#Ambulance = 0
	#Athlete = 2
	#Spectator = 1
}
run watchRun for 4 but 1 ThirdParty

run TrackMe{
	#AutomatedSos = 1
	#Run = 1
} for 3 but 1 ThirdParty, 2 Individual