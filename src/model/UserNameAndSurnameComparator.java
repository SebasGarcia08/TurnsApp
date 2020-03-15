package model;

import java.util.Comparator;

public class UserNameAndSurnameComparator implements Comparator<User> {

	@Override
	public int compare(User o1, User o2) {
		if( o1.getNames().compareTo(o2.getNames()) < 0) return -1;
		else if ( o1.getNames().compareTo(o2.getNames()) > 0 ) return 1;
		else {
			if( o1.getSurnames().compareTo(o2.getSurnames()) < 0) return -1;
			else if ( o1.getSurnames().compareTo(o2.getSurnames()) > 0) return 1;
			else return 0;
		}
	}
	
}
