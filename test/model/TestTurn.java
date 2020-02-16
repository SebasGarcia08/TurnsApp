package model;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestTurn {

	@Test
	public void testTurnEncapsulation() {
		String id = "A00";
		Turn turn = new Turn(id, null);
		assertEquals( "Turns is not encapsulating attributes properly", id, turn.getId());
		assertEquals( "Turns is not encapsulating attributes properly", null, turn.getUser());
		assertEquals("Turns class is not assgining 'On hold...' as a default value for state attribute", Turn.ON_HOLD, turn.getState());
	}
}
