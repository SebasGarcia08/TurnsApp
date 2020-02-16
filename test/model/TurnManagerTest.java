package model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import CustomExceptions.BlankRequiredFieldException;
import CustomExceptions.InvalidInputException;
import CustomExceptions.UserAlreadyHasATurnException;
import CustomExceptions.UserAlreadyRegisteredException;
import CustomExceptions.UserNotFoundException;

/**
 * 
 * @author sebastian
 *
 */
public class TurnManagerTest {

/* 
─█▀▀█ ▀▀█▀▀ ▀▀█▀▀ ░█▀▀█ ▀█▀ ░█▀▀█ ░█─░█ ▀▀█▀▀ ░█▀▀▀ ░█▀▀▀█ 
░█▄▄█ ─░█── ─░█── ░█▄▄▀ ░█─ ░█▀▀▄ ░█─░█ ─░█── ░█▀▀▀ ─▀▀▀▄▄ 
░█─░█ ─░█── ─░█── ░█─░█ ▄█▄ ░█▄▄█ ─▀▄▄▀ ─░█── ░█▄▄▄ ░█▄▄▄█	
*/
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

/*

		░██████╗░█████╗░███████╗███╗░░██╗░█████╗░██████╗░██╗░█████╗░░██████╗
		██╔════╝██╔══██╗██╔════╝████╗░██║██╔══██╗██╔══██╗██║██╔══██╗██╔════╝
		╚█████╗░██║░░╚═╝█████╗░░██╔██╗██║███████║██████╔╝██║██║░░██║╚█████╗░
		░╚═══██╗██║░░██╗██╔══╝░░██║╚████║██╔══██║██╔══██╗██║██║░░██║░╚═══██╗
		██████╔╝╚█████╔╝███████╗██║░╚███║██║░░██║██║░░██║██║╚█████╔╝██████╔╝
		╚═════╝░░╚════╝░╚══════╝╚═╝░░╚══╝╚═╝░░╚═╝╚═╝░░╚═╝╚═╝░╚════╝░╚═════╝░
*/
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
	 * cellphoneNumber=”422”, address=”Cra 107 # 121-20 Desepaz borough”, 
	 * turn= object from class Turn with id="A00", state="On hold...".
	 * 
	 */
	public void threeUsrsSetup() {
		this.manager = new TurnsManager();
		ArrayList<User> usrs = new ArrayList<User>();
		User seyerman = new User("Melchor Manuel", "Reyes Garcia", "123", User.CC, "321", "", null);
		User elMonitorMasBuenaGenteDelMundo = new User("Daniel", "Nose Nose", "124", User.IC, "421", "", null);
		User elonMuskElMasCrack = new User("Elon", "Musk", "125", "FI", "422", "Cra 107 # 121-20 Desepaz borough", 
								  new Turn(manager.generateNextTurnId(manager.lastTurn), null));
		this.turns = new ArrayList<Turn>();
		elonMuskElMasCrack.getTurn().setUser(elonMuskElMasCrack);
		turns.add(elonMuskElMasCrack.getTurn());
		usrs.add(seyerman);
		usrs.add(elMonitorMasBuenaGenteDelMundo);
		usrs.add(elonMuskElMasCrack);
		this.users = usrs;
		manager.setTurns(turns);
		manager.setUsers(usrs);
	}
	
/*
			████████╗███████╗░██████╗████████╗  ░█████╗░░█████╗░░██████╗███████╗░██████╗
			╚══██╔══╝██╔════╝██╔════╝╚══██╔══╝  ██╔══██╗██╔══██╗██╔════╝██╔════╝██╔════╝
			░░░██║░░░█████╗░░╚█████╗░░░░██║░░░  ██║░░╚═╝███████║╚█████╗░█████╗░░╚█████╗░
			░░░██║░░░██╔══╝░░░╚═══██╗░░░██║░░░  ██║░░██╗██╔══██║░╚═══██╗██╔══╝░░░╚═══██╗
			░░░██║░░░███████╗██████╔╝░░░██║░░░  ╚█████╔╝██║░░██║██████╔╝███████╗██████╔╝
			░░░╚═╝░░░╚══════╝╚═════╝░░░░╚═╝░░░  ░╚════╝░╚═╝░░╚═╝╚═════╝░╚══════╝╚═════╝░
*/
	
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
		exceptionRule.expectMessage("User with id 123 is already registered.");
		
		try {
			manager.addUser("Melchor Manuel", "Reyes Garcia", "123", User.CC, "321", "", null);
		} catch (BlankRequiredFieldException e) {
			fail("App should not throw this exception: fields are valid.");
		}	
	}
	
	@Test
	public void testNotAddingWithoutRequiredFields() {
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
			fail("Tmanager.registerTurn(\"125\");his should not be the exceptions thrown");
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
	// Test cases for registering turn method
	// ------------------------------------------------------------------------------------------------
	@Test	
	public void testRegisteringTurnToUserThatDoesNotHaveOne() {
		threeUsrsSetup();
		try {
			manager.registerTurn("123");
		} catch (UserNotFoundException e) {
			fail("App is not able to found created user.");
		} catch (UserAlreadyHasATurnException e) {
			fail("App is overriding user's turn.");
		}
		assertEquals("App is not assgining right initial turn.", "A00", manager.searchUser("123").getTurn().getId());
	}
	
	@Test
	public void testRegisteringTurnToUserThatAlreadyHaveOne()  throws UserAlreadyHasATurnException {
		threeUsrsSetup();
		exceptionRule.expect(UserAlreadyHasATurnException.class);
		String expectedMsg = "User with id " + 125 + " already has turn " 
				  + "A00" + " assgined and its state is: " + Turn.ON_HOLD;
		exceptionRule.expectMessage(expectedMsg);
		try {
			manager.registerTurn("125");
		} catch (UserNotFoundException e) {
			fail("UserNotFoundException should not be thrown when trying to register some turn to another user.");
		} 
	}
		
	@Test
	public void testTurnIdGeneration() {
		emptyObjsSetup();
		int n = 26*100;
		String[] allPossibleCombinationsOfTurns = new String[n];
		int cont = 0;
		String previuosTurnId, subsequentTurnId;
		int idxOfNextElementInArr;
		
		// Create array that contains ALL possible turns.
		for(int i = (int) 'A' ; i <= (int) 'Z'; i++) 
			for(int j=0; j < 100; j++) {
				previuosTurnId =  ((char) i) + ((j < 10) ? "0" + j : String.valueOf(j));
				allPossibleCombinationsOfTurns[cont] = previuosTurnId;
				cont++;
			}
		
		for(int i = 0; i < n; i++) {
			previuosTurnId = allPossibleCombinationsOfTurns[i];
			idxOfNextElementInArr = ( (i == n-1) ? (i%(n-1)) : i+1 );
			subsequentTurnId = allPossibleCombinationsOfTurns[ idxOfNextElementInArr ];
			assertEquals("App is not generating subsequent turn id code.", subsequentTurnId, manager.generateNextTurnId(new Turn(previuosTurnId, null)));
		}
	}
	
	// ------------------------------------------------------------------------------------------------
	// Test cases for attend functionality
	// ------------------------------------------------------------------------------------------------
	@Test
	public void testAttendTurnWhenThereAreMoreToAttend() {
		emptyObjsSetup();
		
		try {
			manager.addUser("Primero", "Alguien", "123", User.IC, "", "", null);
			manager.addUser("Segundo", "Alguien", "234", User.CC, "", "", null);
		} catch (UserAlreadyRegisteredException e1) {
			fail("Exception shouldn't be thrown: valid fields and no duplicate user");
		} catch (InvalidInputException e1) {
			fail("Inputs are valid");
		}catch (BlankRequiredFieldException e1) {
			fail("No reqired field is blank");
		}
		
		try {
			manager.registerTurn("123");
			manager.registerTurn("234");
		} catch (UserNotFoundException e) {
			fail("User is not saved.");
		} catch (UserAlreadyHasATurnException e) {
			fail("Turn is assgined to turn aribtrarly.");
		}
		
		assertEquals("Turn not added correctly", "A00", manager.searchUser("123").getTurn().getId());
		assertEquals("Turn not added correctly", "A01", manager.searchUser("234").getTurn().getId());
		assertEquals("App is not able to keep track of current turn to be attended","A00" , manager.getCurrentTurn().getId());
		assertEquals("App is not allowing to consult to the next turn to be attended", "A01", manager.consultNextTurnToBeAttended().getId());
		
		// Attend another Turn that is not the current. So that only remains A00 to be attended.
		 manager.dispatchTurn(manager.searchTurn("A01"), Turn.ATTENDED);
		 try {
			 manager.consultNextTurnToBeAttended();
			 fail("This should throw an exception");
		 } catch(NoSuchElementException e) {
			 assertTrue("App is not consulting the next correctly", "There are no next turn to be attended." == e.getMessage());
		 }
		 
		 manager.dispatchTurn(manager.searchTurn("A00"), Turn.USER_NOT_PRESENT);
		 
		 try {
			manager.getCurrentTurn(); 
		 } catch(NoSuchElementException e) {
			 assertTrue("App is not responding preperly", "There are not turns in 'On hold' state." == e.getMessage());
		 }
	}
	
}