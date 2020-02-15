package model;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import CustomExceptions.BlankRequiredFieldException;
import CustomExceptions.InvalidInputException;
import CustomExceptions.UserAlreadyRegisteredException;

/**
 * 
 * @author sebastian
 *
 */
public class TurnManagerTest {

	// ------------------------------------------------------------------------------------------------
	// Attributes
	// ------------------------------------------------------------------------------------------------
	private ArrayList<User> users;
	private ArrayList<Turn> turns;
	private TurnsManager manager;
	
	/**
	 * Attributes for object from class User.
	 */
	private String names, surnames, id, typeOfDocument, cellphoneNumber, address;
	private Turn turn;
	
	/**
	 * Attributes for object from class Turn.
	 */
	private String turnId, state;
	private User user;
	
	/**
	 * In order to deal with exceptions.
	 */
	@Rule
	public ExpectedException exceptionRule = ExpectedException.none();

	// ------------------------------------------------------------------------------------------------
	// SCENARIOS
	// ------------------------------------------------------------------------------------------------

	/**
	 * FIRST SCNEARIO:
	 * An object of class TurnsManager with two initialized (and empty) 
	 * ArrayLists of objects of class Users and Turns. 
	 */
	public void emptyObjsSetup() {
		manager = new TurnsManager();
		this.users = new ArrayList<User>();
		this.turns = new ArrayList<Turn>();
	}
	
	/**
	 * SECOND SCENARIO:
	 * 
	 * An object of class TurnsManager object with the following 3 users added 
	 * and an empty ArrayList of objects of type Turn: 
	 * 
	 * usr1: object from User class with names=”Melchor Manuel”, 
	 * surnames=”Reyes García”, id=”123”, typeOfDocument=”CC”, 
	 * cellphoneNumber=”321”,  address=””, turn=null.
	 * 
	 * usr2: object from User class with names=”Daniel”, 
	 * surnames=” Gasparín Ordoniez”, id=”124”, typeOfDocument=”IC”, 
	 * cellphoneNumber=”421”, address=””, turn=null.
	 * 
	 * usr3: object from User class with names=”Elon”, 
	 * surnames=”Musk”, id=”125”, typeOfDocument=”FI”, 
	 * cellphoneNumber=”422”, address=”Cra 107 # 121-20 Desepaz borough”, turn=null.
	 * 
	 */
	public void threeUsrsSetup() {
		ArrayList<User> usrs = new ArrayList<User>();
		User usr1 = new User("Melchor Manuel", "Reyes Garcia", "123", User.CC, "321", "", null);
		User usr2 = new User("Daniel", "Gasparin Ordoniez", "124", User.IC, "421", "", null);
		User usr3 = new User("Elon", "Musk", "125", "FI", "422", "Cra 107 # 121-20 Desepaz borough", null);
		usrs.add(usr1);
		usrs.add(usr2);
		usrs.add(usr3);
		this.users = usrs;
		this.turns = new ArrayList<Turn>();
		this.manager = new TurnsManager();
		manager.setTurns(turns);
		manager.setUsers(usrs);
	}
	
	// ------------------------------------------------------------------------------------------------
	// Test cases
	// ------------------------------------------------------------------------------------------------
	
	// ------------------------------------------------------------------------------------------------
	// 		Adding functional requirement.
	// ------------------------------------------------------------------------------------------------
	@Test
	public void testAddingReq() {
		testAddingWhenUserDoesNotExists();
		testNotAddingDuplicatedUser();
		testNotAddingWithoutRequiredFields();
		testNotAddingWhenAlphabeticFieldsAreInvalid();
		testNotAddingWhenNumericFieldsAreInvalid();
	}
	
	// ------------------------------------------------------------------------------------------------
	// 		Searching method.
	// ------------------------------------------------------------------------------------------------
	@Test
	public void testSearching() {
		testSearchWhenThereAreNoUsers();
		testSearchExistingUser();
	}
	
	// ------------------------------------------------------------------------------------------------
	// 		Test cases for Adding functional requirement.
	// ------------------------------------------------------------------------------------------------
	@Test
	public void testAddingWhenUserDoesNotExists() {
		emptyObjsSetup();
		names = "Sebastian"; surnames="Garcia Acosta"; id="123456"; typeOfDocument=User.IC; cellphoneNumber=""; address=""; turn = null;
		User usr = new User(names, surnames, id, typeOfDocument, cellphoneNumber, address, turn);
		try {
			manager.addUser(names, surnames, id, typeOfDocument, cellphoneNumber, address, turn);
		}catch(UserAlreadyRegisteredException | BlankRequiredFieldException | InvalidInputException  e ) {
			fail("App should not throw exceptions in this case: arguments are valid.");
		}
		assertTrue("User not added correctly", manager.getUsers().get(0).equals(usr));
	}
	
	@Test
	public void testNotAddingDuplicatedUser() {
		threeUsrsSetup();
		exceptionRule.expect(UserAlreadyRegisteredException.class);
		exceptionRule.expectMessage("User with id 123 is already registered, cannot overwrite information");
		
		try {
			manager.addUser("Melchor Manuel", "Reyes Garcia", "123", User.CC, "321", "", null);
		} catch (BlankRequiredFieldException e) {
			fail("App should not throw this exception: fields are valid.");
		}	
	}
	
	@Test
	public void testNotAddingWithoutRequiredFields()  {
		emptyObjsSetup();
		String expectedMsg = "Parameters: names, document number, surnames, type of document must be filled.";
		boolean testPassed = false;
		try {
			manager.addUser("", "", "", "", "", "", null);
			fail("Adding method should not allow adding user with blank required fields.");
		} catch (UserAlreadyRegisteredException | InvalidInputException e) {
			fail("This should not be the exceptions thrown in this case.");
		} catch(BlankRequiredFieldException e) {
			testPassed = e.getMessage().equals(expectedMsg);
		}
		assertTrue("User signup method does not work properly.", testPassed);
	}
	
	@Test
	public void testNotAddingWhenAlphabeticFieldsAreInvalid(){
		emptyObjsSetup();
		exceptionRule.expect(InvalidInputException.class);
		exceptionRule.expectMessage("Invalid format for fields: names, surnames. Fields must contain only alphabetic characters.");
		try {
			manager.addUser("Noobmaster69", "Salvador123", "123", User.CC, "", "", null);
		} catch (UserAlreadyRegisteredException | BlankRequiredFieldException e) {
			fail("This should not be the exceptions thrown");
		}
	}
	
	@Test
	public void testNotAddingWhenNumericFieldsAreInvalid() {
		emptyObjsSetup();
		exceptionRule.expect(InvalidInputException.class);
		exceptionRule.expectMessage("Invalid format for fields: document number, cellphone number. Fields must contain only numerical characters.");
		try {
			manager.addUser("Nando", "Salvador Angulo", "id3ntity", User.CC, "abs", "", null);
		} catch (UserAlreadyRegisteredException | BlankRequiredFieldException e) {
			fail("This should not be the exceptions thrown");
		}
	}
	
	// ------------------------------------------------------------------------------------------------
	// Test cases for searching method
	// ------------------------------------------------------------------------------------------------
	@Test
	public void testSearchWhenThereAreNoUsers() {
		emptyObjsSetup();
		assertEquals("Searching method does not work when searching non-existing user", null, manager.searchUser("36378532"));
	}
	
	@Test
	public void testSearchExistingUser() {
		threeUsrsSetup();
		assertEquals("Search method does not work when searching existing user.", "123", manager.searchUser("123").getId());
	}
	
	// ------------------------------------------------------------------------------------------------
	// Test cases for searching method
	// ------------------------------------------------------------------------------------------------
	
}
