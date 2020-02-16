package model;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestUser {

	@Test
	public void testUserEncapsulation() {
		String n = "Sebas", s = "García", id = "123", tod = User.CC, cpn = "3150550123", a = "Some Address";
		Turn t = new Turn("A00", null);
		User usr = new User(n, s, id, tod, cpn, a, t);
		
		assertEquals("User class is not encapsulating Turn properly ", t, usr.getTurn());
		assertEquals("User class is not encapsulating id properly ", id, usr.getId());
	}

}
