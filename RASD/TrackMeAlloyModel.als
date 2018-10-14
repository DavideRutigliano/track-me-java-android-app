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

sig Name, Surname, Age, Mail, Weight, Height, Location{}

sig Individual extends User{
//	name: Name,
//	surname: Surname,
//	age: Age,
//	mail: lone Mail,
//	weight: Weight,
//	height: Height,
	location: Location,
	incomingRequests: set Request,
	ssn: SSN
}
fact uniqueSSN{
	no disj i1,i2: Individual | i1.ssn = i2.ssn
}
fact uniqueLocation{
	no disj l1,l2: Location | l1 = l2
}

sig ThirdParty extends User{
	organization: Name,
	subscribedUsers: set Individual,
	vat: VAT
}
fact uniqueVAT{
	no disj p1,p2: ThirdParty | p1.vat = p2.vat
}

abstract sig Request{
	sender: ThirdParty, 
	receiver: Individual
}
fact queueRequest {
	all r: Request | r in r.receiver.incomingRequests
}
fact subscribeUser {
	all r: Request | r.receiver in r.sender.subscribedUsers
}

sig IndividualRequest extends Request{
	ssn: SSN
}
/* In this model we are assuming that all individual requests sent by
  * third-parties are always accepted by individual
 */
fact noSpamRequest{
	no disj r1,r2: IndividualRequest | r1.sender = r2.sender and r1.receiver = r2.receiver
}
fact needSSN{
	all r: IndividualRequest | r.ssn = r.receiver.ssn
}
fact uniqueReceiver{
	no disj r1,r2: IndividualRequest, i: Individual | r1 in i.incomingRequests and r2 in i.incomingRequests
}

sig GroupRequest extends Request{
	returnedLines: Int
}
/* The constraint on the anonymity should be something like
  *  returnedLines >= anonymityLimit (e.g. almost one thousand lines returned)
  */
fact checkAnonymity{
	all r: GroupRequest | r.returnedLines > 0
}

pred makeOneRequest(r: Request, p: ThirdParty, i: Individual){
	#Run = 0
	#AutomatedSos = 0
	#Track = 0
	#Time = 0
	#Date = 0
	#Duration = 0
	i.incomingRequests = i.incomingRequests + r
	p.subscribedUsers = p.subscribedUsers + i
}

run makeOneRequest for 2

pred Data4HelpComplete{
	#Run = 0
	#Track = 0
	#Time = 0
	#Date = 0
	#Duration = 0
	#AutomatedSos = 0
	#Individual = 3
	#Ambulance = 0
	#ThirdParty = 1
	#IndividualRequest = 2
	#GroupRequest = 1
}
//run Data4HelpComplete for 4

sig Ambulance{}

/* AUTOMATED SOS */
sig AutomatedSos{
	provider: ThirdParty,
	customers: set Individual,
	ambulance: Ambulance
}
fact enableSos{
	all a:AutomatedSos | a.customers = a.provider.subscribedUsers
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
//run enableAutomatedSos for 1

pred automatedSosComplete{
	#Track = 0
	#Time = 0
	#Date = 0
	#Duration = 0
	#Individual = 3
	#IndividualRequest = 3
	#GroupRequest = 0
	#ThirdParty = 1
	#AutomatedSos = 1
}
//run automatedSosComplete for 4

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
	#IndividualRequest = 1
	#Athlete = 1
	#Run = 2
	r.athletes = r.athletes + a
}
//run enrollToRun for 2

pred watchRun(s: Spectator, r: Run){
	#AutomatedSos = 0
	#Ambulance = 0
	#IndividualRequest = 1
	#Athlete = 1
	#Spectator = 1
	#Run = 2
	#Request = 1
	r.spectators = r.spectators + s
}
run watchRun

pred track4RunComplete{
	#Organizer = 1
	#Athlete = 2
	#Spectator = 1
	#Run = 1
	#AutomatedSos = 0
	#Ambulance = 0
	#GroupRequest = 0
	#IndividualRequest = 1
}
//run track4RunComplete for 4
