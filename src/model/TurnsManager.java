package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import CustomExceptions.*;

/**
 * This class represents the turns handler and is in charge of the logic of the problem.
 * For more details about the context of the problem and the problem itself, got to: 
 * https://docs.google.com/document/d/1d--ndivkbnoNeZ0nUT8eUpYm-FtVlvE13vZZVe3kn4g/edit?usp=sharing
 * @author Sebastián García Acosta
 *
 */
public class TurnsManager implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// ------------------------------------------------------------------------------------------------
	// Attributes
	// ------------------------------------------------------------------------------------------------
	
	/*
	 * ArrayList containing the <br>unique</br> users registered. 
	 */
	private ArrayList<User> users;
	
	/**
	 * ArrayList containing the turns registered. 
	 * Its length could be longer than users ArrayList due to the fact that a user can have multiple turns throughout the
	 * application life.  
	 */
	private ArrayList<Turn> turns;
	
	private DateTime dateTime;
	private ArrayList<TurnType> turnTypes;
	
	/**
	 * Turn, this attribute is used to indicate what is the last turn assigned. Next turns assigned will be based on this.
	 */
	public Turn lastTurn;
	
	/**
	 * This final array is used to indicate the required fields needed to register a user. 
	 */
	public static final String[] REQUIRED_FIELDS = {"names", "surnames", "type of document", "document number"};
	
	/**
	 * This constant indicates the maximum number of names allocated within the data folder.
	 */
	public static final int LENGTH_OF_NAMES_AND_SURNAMES_FILE = 999;
	public static final String NAMES_FILE_PATH = "data/names.txt";
	public static final String SURNAMES_FILE_PATH = "data/surnames.txt";
	
	/**
	 * This ArrayList will be filled once and only once with the names inside NAMES_FILE_PATH
	 */
	public static ArrayList<String> NAMES = new ArrayList<String>();
	
	/**
	 * This ArrayList will be filled once and only once with the surnames inside SURNAMES_FILE_PATH
	 */
	public static ArrayList<String> SURNAMES = new ArrayList<String>();
	public static final float  CHANGE_TIME_DURATION = (float) 0.25;

	// ------------------------------------------------------------------------------------------------
	// Constructors
	// ------------------------------------------------------------------------------------------------
	
	/**
	 * Creates an TurnsManager object
	 * <b>post: </b> a TurnsManager object was created and its attributes were initialized. 
	 */
	public TurnsManager() {	
		this.dateTime = DateTime.now();
		this.turns = new ArrayList<Turn>();
		this.users = new ArrayList<User>();
		this.lastTurn = new Turn("A-1", null, null);
		this.turnTypes = new ArrayList<TurnType>();
	}

	// ------------------------------------------------------------------------------------------------
	// Methods
	// ------------------------------------------------------------------------------------------------

	/**
	 * Adds a user to the turns manager if and only if potential user's fields meet the following rules: <br>
	 * @param n, String, name. REQUIRED FIELD. Must be alphabetic.
	 * @param s, String, surnames. REQUIRED FIELD. Must be alphabetic.
	 * @param id, String id. REQUIRED FIELD. Must be numerical.
	 * @param tod, String, type of document. REQUIRED FIELD. Possible values: CC, CE, PA, TI, RC (see User class for detailed explanation).
	 * @param cpn, String cell-phone number. Must be numerical.
	 * @param a, String, address. Optional field.
	 * @param t, Turn, turn assigned to user. Initially is null.
	 * <b>All parameters (n, s, id, cpn, a, t) are not null. <b>
	 * <b>post:</b> User was added successfully if all required fields were not blank, all fields were in their corresponding format, and its id was unique. Otherwise, the corresponding exception was thrown.    
	 * @throws UserAlreadyRegisteredException if another user with same id is found.
	 * @throws BlankRequiredFieldException if one of the REQUIRED FIELDS indicated is blank. 
	 * @throws InvalidInputException if one of the numeric fields indicated above as numerical does not contain only numbers.
	 */
	public void addUser(String n, String s, String id, String tod, String cpn, String a, Turn t)
			throws UserAlreadyRegisteredException, BlankRequiredFieldException, InvalidInputException {
		// ------------------------------------------------------------------------------------------------
		// Validate that user is not already registered
		// ------------------------------------------------------------------------------------------------		
		if (searchUser(id) != null)	throw new UserAlreadyRegisteredException(id);

		// ------------------------------------------------------------------------------------------------
		// Validate that required fields are not blank
		// ------------------------------------------------------------------------------------------------		
		String[] required_args = {n, s, tod, id};
		Map<String, String> requiredFields = IntStream.range(0, REQUIRED_FIELDS.length).boxed()
											.collect( Collectors.toMap(i -> REQUIRED_FIELDS[i], i -> required_args[i]));
		
		ArrayList<String> blankFieldsList = requiredFields.entrySet().stream()
										.filter(item -> (item.getValue().trim().length() == 0 || item.getValue() == null ))
										.map(Map.Entry::getKey)
										.collect(Collectors.toCollection(ArrayList::new));
		
		if (!blankFieldsList.isEmpty())	throw new BlankRequiredFieldException(blankFieldsList);
		
		// ------------------------------------------------------------------------------------------------
		// Validate that alphabetic fields contains only letters
		// ------------------------------------------------------------------------------------------------		
		String[] alphabeticFields = {n, s, tod};
		Map<String, String> alphabeticFieldsDict = IntStream.range(0, alphabeticFields.length).boxed()
			    .collect(Collectors.toMap(i -> REQUIRED_FIELDS[i], i -> alphabeticFields[i]));		

		ArrayList<String> wrongAlphabeticFields = alphabeticFieldsDict.entrySet().stream()
												  .filter( item -> !item.getValue()
														  			.replaceAll("\\s+","")
														  			.chars()
														  			.allMatch(Character::isLetter))
												  .map(Map.Entry::getKey)
												  .collect(Collectors.toCollection(ArrayList::new));
		
		if(!wrongAlphabeticFields.isEmpty()) throw new InvalidInputException(wrongAlphabeticFields, "alphabetic characters");

		// ------------------------------------------------------------------------------------------------
		// Validate that numeric fields contain only numbers
		// ------------------------------------------------------------------------------------------------
		ArrayList<String> invalidNumericFields = new ArrayList<String>();
		if (!id.matches("\\d+")) invalidNumericFields.add("document number");
		if ( ! (cpn.trim().length() == 0 || cpn == null) && !cpn.matches("\\d+")) invalidNumericFields.add("cellphone number");
		
		if(!invalidNumericFields.isEmpty()) throw new InvalidInputException(invalidNumericFields, "numerical characters");
		users.add(new User(n, s, id, tod, cpn, a, t));
	}
	
	/**
	 * Searches a user by its id. 
	 * @param id, String, identification number of user.
	 * @return User object if found; otherwise, null.
	 */
	public User searchUser(String id) {
		User found = null;
		boolean wasFound = false;
		for(int i=0; i < users.size() && !wasFound; i++) {
			User turn_i = users.get(i);
			if(turn_i.getId().equalsIgnoreCase(id)) {
				found = turn_i;
				wasFound = true;
			}
		}
		return found;
	}
	
	/**
	 * Searches a Turn by its id. 
	 * @param id, String, identification code of turn to be searched.
	 * @return Turn object if found; otherwise, null.
	 */
	public Turn searchTurn(String id) {
		Turn found = null;
		boolean wasFound = false;
		for(int i=0; i < turns.size() && !wasFound; i++) {
			Turn turn_i = turns.get(i);
			if(turn_i.getId().equalsIgnoreCase(id)) {
				found = turn_i;
				wasFound = true;
			}
		}
		return found;
	}
	
	/**
	 * Adds a new TurnType
	 * @param name String, the name.
	 * @param duration float, the duration.
	 */
	public void registerTurnType(String name, float duration) {
		turnTypes.add(new TurnType(name, duration));
		DateTime staringDateTime = this.dateTime;
	}

	/**
	 * Registers next available turn to user with id specified.
	 * @param id, String, identification number of user to whom the turn will be assigned. Cannot be null.
	 * <b>post: <b> turn was registered successfully if parameter was not null, user was found and didn't have a turn assigned already; otherwise, corresponding exception was thrown.
	 * After setting turn to user, lasTurn was updated.
	 * @throws UserNotFoundException if user with the specified id is not found.
	 * @throws UserAlreadyHasATurnException, if user already has a turns assigned.
	 */
	public void registerTurn(String id, int turnTypeIdx) throws UserAlreadyHasATurnException, UserNotFoundException {
		User usr = searchUser(id);
		if(usr == null) 				throw new UserNotFoundException(id);
		else if (usr.getTurn() != null) throw new UserAlreadyHasATurnException(usr);
		else {
			TurnType tt =  turnTypes.get(turnTypeIdx);
			Turn turn = new Turn(generateNextTurnId(lastTurn.getId()), usr, tt);
			// Sets the dateTime limits of the turn
			DateTime startingDateTime = DateTime.copyOf(this.dateTime);
			turn.setStartingDateTime(startingDateTime);
			DateTime endingDateTime = DateTime.copyOf(startingDateTime);
			endingDateTime.plusMillis( DateTime.minutes2Millis( tt.getDurationMinutes() + CHANGE_TIME_DURATION ) );
			turn.setEndingDateTime( endingDateTime ); 
			usr.setTurn(turn);
			turns.add(turn);
			System.out.println(turn.toString());
			lastTurn.setId(turn.getId());
		}
	}

	/**
	 * @return the turnTypes
	 */
	public ArrayList<TurnType> getTurnTypes() {
		return turnTypes;
	}

	/**
	 * @param turnTypes the turnTypes to set
	 */
	public void setTurnTypes(ArrayList<TurnType> turnTypes) {
		this.turnTypes = turnTypes;
	}

	public void setDateTime(DateTime dateTime) {
		this.dateTime = dateTime;
	}

	/**
	 * Generates next turn id ranging from A00 to Z99.
	 * @param currentTurn, Turn, previous turn, based on this the next turn will be generated. 
	 * E.g. if currentTurn was A0, next turn will be A1; if currentTurn was Z99, next turn will be A00.
	 * @return String, id of the next turn. Its first character is a letter and the rest are numbers.
	 */
	public String generateNextTurnId(String currTurnId) {
		char letter = currTurnId.charAt(0);
		int number = Integer.parseInt(currTurnId.substring(1, currTurnId.length()));
		int ASCIICurrLetter = (int) letter;
		int nextASCIILetter = (ASCIICurrLetter == 90) ? 65 : ASCIICurrLetter+1;
		int nextTurnNumber = (number < 99) ? number + 1 : 0;
		char nextTurnLetter = (number < 99) ? letter : (char) nextASCIILetter;
		String nextTurnId = String.valueOf(nextTurnLetter) + ((nextTurnNumber < 10) ? "0" + nextTurnNumber : String.valueOf(nextTurnNumber));
		return nextTurnId;
	}
	
	/**
	 * Dispatches the current turn: sets the state of current turn to either ATTENDED or USER_NOT_PRESENT. 
	 * @param state, String, possible values are: "Attended." or "User not present".
	 * <b>post:<b> After update the state of the current turn, user that had that turn could register another turn. (User's field Turn will be set to null).
	 * @throws NoSuchElementException, if there are no turns registered whose state is ON_HOLD, i.e. all turns registered were already dispatched.
	 */
	public void dispatchTurn(Turn turn, String state) throws NoSuchElementException {
		if(turn == null) throw new NoSuchElementException("Turn not found");
		turn.setState(state);
		searchUser(turn.getUser().getId()).setTurn(null);;
	}
	
	/**
	 * Consults what is the next turn to be attended.
	 * @return Turn, next turn to be attended.
	 */
	public Turn consultNextTurnToBeAttended() throws NoSuchElementException {
		Turn turn;
		try {
			turn = turns.stream().filter(obj -> obj.getState().contentEquals(Turn.ON_HOLD))
					.skip(1).findAny().get();
		} catch(NoSuchElementException e) { throw new NoSuchElementException("There are no next turn to be attended."); }
		return turn;
	}
	
	/**
	 * Return the first turn that has not be dispatched.
	 * @return Turn, the first turn that has not been dispatched.
	 * @throws NoSuchElementException, if all turns are already dispatched, i.e. there are no turns waiting to be attended.
	 */
	public Turn getCurrentTurn() throws NoSuchElementException {
		Turn curr_turn;
		try {
			curr_turn = turns.stream().filter(obj -> obj.getState().contentEquals(Turn.ON_HOLD)).findFirst().get();
		} catch(NoSuchElementException e) { throw new NoSuchElementException("There are no turns in 'On hold' state."); }
		return curr_turn;
	}
	
	/**
	 * Generates up to 1000² unique random names based on the ones hosted in /data folder.
	 * @param number, number of random composed names to be created.
	 * @return ArrayList<String> that contains the random names generated. Its size is number - 1.
	 * @throws IOException, if file archive could not be loaded.
	 */
	public ArrayList<String> generateRandomComposedNames(int number) throws IOException{
		ArrayList<String> composedNames = new ArrayList<String>();
		loadSurnames();
		for(int i = 0; i < number; i++) {
			int randomIdx1 = (int) Math.floor( Math.random() * LENGTH_OF_NAMES_AND_SURNAMES_FILE);
			int randomIdx2 =  (int) Math.floor( Math.random() * LENGTH_OF_NAMES_AND_SURNAMES_FILE);
			
			while(randomIdx1 == randomIdx2) {
				randomIdx1 = (int) Math.floor( Math.random() * LENGTH_OF_NAMES_AND_SURNAMES_FILE);
				randomIdx2 =  (int) Math.floor( Math.random() * LENGTH_OF_NAMES_AND_SURNAMES_FILE);	
			}
			
			String name1 = NAMES.get(randomIdx1);
			String name2 = NAMES.get(randomIdx2);
			String composedName = name1 + " " + name2;
			composedNames.add( composedName );
		}
		return composedNames;
	} 
	
	/**
	 * Generates up to 1000 unique random surnames based on the ones hosted in /data folder.
	 * @param number, number of random surnames to be created.
	 * @return ArrayList<String> that contains the random names generated. Its size is number - 1.
	 * @throws IOException, if file archive could not be loaded.
	 */
	public ArrayList<String> generateRandomSurnames(int number) throws IOException{
		ArrayList<String> surnames = new ArrayList<String>();
		loadSurnames();
		for(int i = 0; i < number; i++) {
			int randomIdx = (int) Math.floor( Math.random() * LENGTH_OF_NAMES_AND_SURNAMES_FILE);
			String surname = SURNAMES.get(randomIdx);
			surnames.add( surname );
		}
		return surnames;
	} 
	
	/**
	 * Stores the names allocated in names.txt file into NAMES array for later using if and only if NAMES array is empty.
	 * <b>post: </b> This method will be executed only once in the lifetime of the program.
	 * @throws IOException
	 */
	public void loadNames() throws IOException {
		BufferedReader br = new BufferedReader( new FileReader( new File( NAMES_FILE_PATH )));
		String s;
		if(NAMES.isEmpty()) 
			while( (s = br.readLine()) != null ) 
				NAMES.add(s);			
		
		br.close();
	}
	
	/**
	 * Stores the names allocated in surnames.txt file into SURNAMES array for later using if and only if SURNAMES array is empty.
	 * <b>post: </b> This method will be executed only once in the lifetime of the program.
	 * @throws IOException
	 */
	public void loadSurnames() throws IOException {
		BufferedReader br = new BufferedReader( new FileReader( new File( SURNAMES_FILE_PATH )));
		String s;
		if(SURNAMES.isEmpty()) 
			while( (s = br.readLine()) != null ) 
				SURNAMES.add(s);			
		
		br.close();
	}
	
	public String sendDateTime() {
		return dateTime.toString();
	}
	
	//TODO- Documentation
	public DateTime getDateTime() {
		return this.dateTime;
	}
	
	public void updateDateTimeManually(String strDate) throws Exception {
		strDate = strDate.trim();
		String[] dateAndTime = strDate.split(" ");
		// YY-MM-DD
		int[] date = Arrays.stream(dateAndTime[0].split("-")).mapToInt(Integer::parseInt).toArray();
		// hh:mm:ss
		int[] time = Arrays.stream(dateAndTime[1].split(":")).mapToInt(Integer::parseInt).toArray();
		
		DateTime dt = new DateTime(date[0], date[1], date[2], time[0], time[1], time[2]);

		if( dt.isBefore(dt) )
			throw new Exception("Invalid datetime");
		else
			this.dateTime = dt;
	}
	
	public void updateDateTimeByMillis(long milliseconds) {
		dateTime.plusMillis(milliseconds);
	}
	
	public void updateDateTimeBySystem() {
		this.dateTime = DateTime.now();
	}
	
	public void addTurnType(String name, float duration) {
		TurnType tt = new TurnType(name, duration + CHANGE_TIME_DURATION);
		turnTypes.add(tt);
	}
	
	// ------------------------------------------------------------------------------------------------
	// Getters and setters
	// ------------------------------------------------------------------------------------------------

	/**
	 * Returns users.
	 * @return ArrayList<User>, users.
	 */
	public ArrayList<User> getUsers() {
		return this.users;
	}

	/**
	 * Replace current users for the ones passed as parameter.
	 * @param users, ArrayList<User>.
	 */
	public void setUsers(ArrayList<User> users) {
		this.users = users;
	}

	/**
	 * Returns turns.
	 * @return ArrayList<Turn>, turns registered.
	 */
	public ArrayList<Turn> getTurns() {
		return this.turns;
	}

	/**
	 * Update turns.
	 * @param turns, ArrayList<Turn> turn.
	 */
	public void setTurns(ArrayList<Turn> turns) {
		this.turns = turns;
	}

	/**
	 * Updates current turn.
	 * @param currentTurn, Turn.
	 */
	public void setLastTurn(Turn lastTurn) {
		this.lastTurn = lastTurn;
	}

}