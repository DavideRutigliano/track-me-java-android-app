//definition of the model
module TrackMe

sig User {
	username: one String,
	password: one String
}

sig Name{
	string: one String
}

sig Surname{
	surname: one String
}

sig Mail{
	mail: one String
}

sig SSN{
	ssn: one String
}

sig Country{
	country: one String
}

sig VAT{
	vat: one String
}

sig Age{
	age: one Int
}{
	#age > 0
}

sig Subscribed{
	subscribedUsersNumber: one Int
}{
	#subscribedUsersNumber >= 0
}

sig Individual extends User{
	name: Name -> lone Name,
	surname: Surname -> one Surname,
	age: Age -> one Age,
	mail: Mail-> one Mail,
	ssn: this -> one SSN
}

sig ThirdParty extends User{
	organization: Name -> lone Name,
	country: Country -> lone Country,
	mail: Mail-> one Mail,
	vat: this -> one SSN,
	subscribedUsers: Individual -> set Individual,
	subscribedUsersNumber: Subscribed -> one Subscribed
}





//assertions


//facts
