module TrackMe

/* DATA 4 HELP */
sig Username, Password, SSN, VAT{}

abstract sig User{
	username: Username,
	password: Password
}
fact uniqueUsername{
	no disj u1,u2: User | u1.username = u2.username
}

sig Name, Surname, Mail, Location, Criterion{}

sig Individual extends User{
	name: Name,
	surname: Surname,
	age: Int,
	mail: some Mail,
	weight: Int,
	height: Int,
	location: Location,
	incomingRequests: set Request,
	sosEnabled: Int,
	ssn: SSN
}{
	age > 0
	weight > 0
	height > 0
	sosEnabled = 1 or sosEnabled = 0
}
fact enabledIsBoolean{
	all r: Individual | r.sosEnabled = 0 or r.sosEnabled = 1
}
fact uniqueSSN{
	no disj i1,i2: Individual | i1.ssn = i2.ssn
}
fact uniqueLocation{
	no disj l1,l2: Location | l1 = l2
}
assert correctData{
	all i: Individual | i.age > 0 and i.weight > 0 and i.height > 0
}
//check correctData

sig ThirdParty extends User{
	organization: Name,
	subscribedUsers: set Individual,
	vat: VAT
}
fact uniqueVAT{
	no disj p1,p2: ThirdParty | p1.vat = p2.vat
}
fact uniqueName{
	no disj p1,p2: ThirdParty | p1.organization = p2.organization
}
fact noIndividualThirdParty{
	no i: Individual, p: ThirdParty | i.name = p.organization
}

abstract sig Request{
	sender: ThirdParty, 
	receiver: Individual
}

sig IndividualRequest extends Request{
	ssn: SSN,
	accepted: Int
}{
	accepted = 0 or accepted = 1
}
fact acceptedIsBoolean{
	all r: IndividualRequest | r.accepted = 1 or r.accepted = 0
}
/* In this model we are assuming that all individual requests sent by
  * third-parties are always accepted by individual
 */
fact acceptRequest {
	all r: IndividualRequest, i: Individual, p:ThirdParty | r.accepted = 1 => (r in i.incomingRequests and i in p.subscribedUsers)
}
fact refuseRequest {
	all r: IndividualRequest, i: Individual, p:ThirdParty | r.accepted = 0 => ( r not in i.incomingRequests and i not in p.subscribedUsers)
}
assert consistencyCheck{
	no r1,r2: IndividualRequest | r1.receiver = r2.receiver and r1.sender = r2.sender and r1.accepted = 1 and r2.accepted = 0
}
//check consistencyCheck
fact noSpamRequest{
	no disj r1,r2: IndividualRequest, i: Individual | r1.sender = r2.sender and r1.receiver.ssn = i.ssn and r2.receiver.ssn = i.ssn
}
fact needSSN{
	all r: IndividualRequest | r.ssn = r.receiver.ssn
}

sig GroupRequest extends Request{
	searchCriterion: Criterion,
	returnedLines: Int
}{
returnedLines > 0
}
/* The constraint on the anonymity should be something like
  *  returnedLines >= anonymityLimit (e.g. almost one thousand lines returned)
  * but alloy hadles only 4bit Int thus we used 7 as limit
  */
fact queueGroupRequests{
	all g: GroupRequest, i: Individual, p: ThirdParty | g.returnedLines > 7 => g in i.incomingRequests and g.receiver in p.subscribedUsers
}
fact noMultipleGroupRequests{
	no disj r1,r2: GroupRequest, p: ThirdParty, c: Criterion | r1.searchCriterion = c and r2.searchCriterion = c and r1.sender = p and r2.sender = p
}
assert checkAnonymity{
	all r: GroupRequest | r.returnedLines > 0
}
//check checkAnonymity

pred makeOneRequest(r: Request, p: ThirdParty, i: Individual){
	#Run = 0
	#AutomatedSos = 0
	#Ambulance = 0
	#Track = 0
	#Time = 0
	#Date = 0
	#Duration = 0
	#IndividualRequest = 1
	#GroupRequest = 1
	i.incomingRequests = i.incomingRequests + r
	p.subscribedUsers = p.subscribedUsers + i
}

//run makeOneRequest for 2 but 1 Individual, 1 ThirdParty

pred Data4HelpComplete{
	#Run = 0
	#Track = 0
	#Time = 0
	#Date = 0
	#Duration = 0
	#AutomatedSos = 0
	#Individual = 1
	#Ambulance = 0
	#ThirdParty = 2
	#IndividualRequest = 2
	#GroupRequest = 0
}
//run Data4HelpComplete for 3

sig Ambulance{}

/* AUTOMATED SOS */
sig AutomatedSos{
	provider: ThirdParty,
	customers: set Individual,
	ambulance: some Ambulance
}
fact enableSos{
	all a: AutomatedSos, i: Individual | i.sosEnabled = 1 => i in a.customers
}
fact disableSos{
	all a: AutomatedSos, i: Individual | i.sosEnabled = 0 => i not in a.customers
}
fact uniqueAutomatedSosService {
	no disj a1, a2: AutomatedSos, p: ThirdParty | a1.provider = p and a2.provider = p
}
fact uniqueAmbulance{
	no disj a1, a2: AutomatedSos | a1.ambulance in a2.ambulance or a2.ambulance in a1.ambulance
}
fact multipleSos{
	no disj a1,a2: AutomatedSos, i: Individual | i in a1.customers and i in a2.customers 
}
pred enableAutomatedSos(a: AutomatedSos, p: ThirdParty){
	#Run = 0
	#Track = 0
	#Time = 0
	#Date = 0
	#Duration = 0
	a.provider = p
	a.customers = p.subscribedUsers
}
//run enableAutomatedSos for 1 but 1 ThirdParty, 1 Request

pred runAutomatedSos(a: AutomatedSos, p: ThirdParty){
	#Run = 0
	#Track = 0
	#Time = 0
	#Date = 0
	#Duration = 0
	#Request = 1
}
//run runAutomatedSos for 2 but 1 AutomatedSos

pred automatedSosComplete{
	#Track = 0
	#Time = 0
	#Date = 0
	#Duration = 0
	#Individual = 2
	#IndividualRequest = 1
	#GroupRequest = 1
	#ThirdParty = 2
	#AutomatedSos = 2
	#Ambulance = 3
}
run automatedSosComplete for 6

/* TRACK 4 RUN */
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
fact noRunWhitoutRequest{
	no disj r: Run, p: ThirdParty, i: IndividualRequest |  r.organizer = p and i.accepted  = 0
}
fact noDupicatedRun{
	no disj r1,r2: Run | r1.track = r2.track and r1.date = r2.date and r1.time = r2.time
}
fact noMultipleEnrollement{
	no disj r1,r2: Run, a: Athlete | r1.date = r2.date and r1.time = r2.time and a in r1.athletes and a in r2.athletes
}
fact noMultipleWatch{
	no disj r1,r2: Run, s: Spectator |  r1.date = r2.date and r1.time = r2.time and s in r1.spectators and s in r2.spectators
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
	enrolled: lone Run
}
fact athleteEnrolled{
	all a: Athlete | a in a.enrolled.athletes
}

sig Spectator extends User{
	watch: lone Run,
}
fact spectatorWatching {
	all s: Spectator | s in s.watch.spectators
}

pred createNewRun(o: Organizer, r: Run){
	#AutomatedSos = 0
	#Request = 1
	#Run = 1
	#Ambulance = 0
	o.organized = o.organized + r
}
//run createNewRun for 2

pred enrollToRun(a: Athlete, r: Run){
	#AutomatedSos = 0
	#Ambulance = 0
	#Request = 1
	#Athlete = 1
	#Run = 1
	r.athletes = r.athletes + a
}
//run enrollToRun for 2

pred watchRun(s: Spectator, r: Run){
	#AutomatedSos = 0
	#Ambulance = 0
	#IndividualRequest = 1
	#Athlete = 1
	#Spectator = 1
	#Run = 1
	#Request = 1
	r.spectators = r.spectators + s
}
//run watchRun

pred track4RunComplete{
	#Organizer = 1
	#Athlete = 2
	#Spectator = 1
	#Run = 1
	#AutomatedSos = 0
	#Ambulance = 0
	#GroupRequest = 0
	#IndividualRequest = 0
}
//run track4RunComplete for 4

pred TrackMe(){
	#Ambulance = 3
	#AutomatedSos = 2
	#Individual = 2
	#IndividualRequest = 4
	#ThirdParty = 2
	#Organizer = 1
	#Run = 2
	#Athlete = 2
	#Spectator = 1
}
run TrackMe for 8
