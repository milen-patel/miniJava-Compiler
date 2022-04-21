class References {
	public static void main(String[] args) {
		Apartment aI = new Apartment();
		Apartment aN = null;
		System.out.println(aI.numRooms);
	//	System.out.println(aN.numRooms); // Invalid dereference on null;
		Room r = null;
		System.out.println(r.numRooms); // Valid null dereference
		System.out.println(aN.owner.numPeople); // Valid
		
		aI.a = new Room();
		aI.b = new Room();
		aI.c = new Room();
		aI.a.tenant = Person.createPerson();
		aI.b.tenant = Person.createPerson();
		aI.c.tenant = Person.createPerson();
		System.out.println(aI.a.tenant.getSSN() + aI.b.tenant.getSSN() + aI.c.tenant.getSSN());
		
		Apartment.owner = new Person();
		if (Apartment.owner == aI.owner) {
			System.out.println(4);
		}
	}
}

class Apartment {
	static Person owner;
	int numRooms; 
	Room a;
	Room b;
	Room c;
}

class Room {
	static int numRooms;
	Person tenant;
}

class Person {
	public static int numPeople;
	private static int nextSSN;
	private int SSN;
	
	public static Person createPerson() {
		Person p = new Person();
		p.SSN = nextSSN;
		nextSSN = nextSSN + 1;
		numPeople = numPeople + 1;
		return p;
	}
	
	public int getSSN() {
		int SSN = this.SSN;
		return SSN;
	}
}