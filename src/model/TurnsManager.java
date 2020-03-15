package model;

import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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
	 * Number of integer places for codes generated. 
	 * E.g., if numberOfPossibleDigits = 2, then the possible codes are 26 x 10!/(10 - 2) = 11793600 (11 million possible codes)
	 */
	public final static int EXPONENT_OF_POSSIBLE_NUMBER_OF_DIGITS = 2;
	public static final long NUMBER_OF_POSSIBLE_IDS = 26 * (TurnsManager.factorial(10)/(10-EXPONENT_OF_POSSIBLE_NUMBER_OF_DIGITS));
	
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
	public static final String REPORTS_FOLDER = "reports/";
	
	/**
	 * This ArrayList will be filled once and only once with the names inside NAMES_FILE_PATH
	 */
	public static ArrayList<String> NAMES = new ArrayList<String>();
	
	/**
	 * This ArrayList will be filled once and only once with the surnames inside SURNAMES_FILE_PATH
	 */
	public static ArrayList<String> SURNAMES = new ArrayList<String>();
	public static final float  CHANGE_TIME_DURATION = (float) 0.25;
	public DateTime starting_datetime_turn = dateTime;

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
	
	public static long factorial(int number) {
        long result = 1;

        for (int factor = 2; factor <= number; factor++) {
            result *= factor;
        }

        return result;
    }

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
		if (binarySearchUserById(id) != null)	throw new UserAlreadyRegisteredException(id);

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
	 * Searches by the code of a user using binary search
	 * @return User found, can be null if not found; otherwise, the object searched.
	 */
	public User binarySearchUserById( String id ) {
		// SORTING USSING ANONYMOUS CLASS AND REVERSE ORDER
		Collections.sort( users, Collections.reverseOrder(new Comparator<User>() {
					@Override
					public int compare(User o1, User o2) {
						return  o1.getId().compareTo(o2.getId());
					}
			}));
			
			int start = 0;
			int end = users.size() - 1;
			boolean wasFound = false; 
			User found = null;
			while( start <= end && !wasFound) {
				int mid = (start + end) / 2;
				if( users.get(mid).getId().compareTo(id) < 0)
					end = mid -1;
				else if( users.get(mid).getId().compareTo(id) > 0)
					start = mid+1;
				else {
					wasFound = true;
					found = users.get(mid); 
				} 	
			}
	        
	        return found; 
	    }
	
	/**
	 * Sorts users by name and surnames using BubbleSort
	 */
	public ArrayList<User> getSortedUsersByNameAndSurname(ArrayList<User> users) {
		Comparator<User> comp = new UserNameAndSurnameComparator();
		int n = users.size();
        for (int i = 0; i < n-1; i++)
            for (int j = 0; j < n-i-1; j++)
                if ( comp.compare(users.get(j), users.get(j+1)) > 0)
                {
                    // swap temp and arr[i]
                    User temp = users.get(j);
                    users.set(j, users.get(j+1));
                    users.set(j+1, temp);
                }
        return users;
	}
	
	/**
	 * Sorts users in descending order by their number of absences using Insertion sort 
	 */
	public ArrayList<User> getSortedUsersByNumberOfAbsences(ArrayList<User> users) {	  
        int n = users.size(); 
        for (int i = 1; i < n; ++i) { 
            User key = users.get(i); 
            int j = i - 1; 
            
            while (j >= 0 && users.get(j).numberOfAbsences < key.numberOfAbsences) { 
                users.set(j + 1, users.get(j));
                j = j - 1; 
            }
            
            users.set(j+1, key); 
        }
        return users;
	}
	
	/**
	 * Sort turns by their ending datetime in ascending order (from earliest to oldest) using Selection Sort
	 */
	public ArrayList<Turn> getSortedTurnsByEndingDateTime(ArrayList<Turn> turns ) {
		int n = turns.size(); 
        for (int i = 0; i < n-1; i++) { 
            // Find the minimum element in unsorted array 
            int min_idx = i; 
            for (int j = i+1; j < n; j++) 
                if ( turns.get(j).getEndingDateTime().isBefore( turns.get(min_idx).getEndingDateTime() )) 
                    min_idx = j; 
 
            Turn temp = turns.get(min_idx); 
            turns.set(min_idx, turns.get(i));
            turns.set(i, temp);
        }
        return turns;
	}
	
	/**
	 * Filters by all the turns whose user is the specified in parameters
	 * @param userId, String, the id of the user.
	 * @return ArrayList<Turn>, all turns requested by user.
	 */
	public ArrayList<Turn> getAllTurnsRequestedByUser( String userId ) throws UserNotFoundException {
		if( binarySearchUserById(userId) == null ) throw new UserNotFoundException(userId);
		ArrayList<Turn> filteredTurns =  turns.stream()
					.filter(x -> x.getUser().getId().equals(userId))
					.collect(Collectors.toCollection(ArrayList::new));
		if( filteredTurns.isEmpty() )
			throw new NoSuchElementException("User with id "+ userId + " has no requested turns.");
		return filteredTurns;
	}

	/**
	 * Filters by all the turns whose user is the specified in parameters
	 * @param userId, String, the id of the user.
	 * @return ArrayList<Turn>, all turns requested by user.
	 */
	public ArrayList<User> getAllUserThatHadTurn(String turnId) throws NoSuchElementException{
		if( binarySearchTurnById(turnId) == null ) throw new NoSuchElementException( "There are no turn with id " + turnId);
		ArrayList<User> filtereduUsers = new ArrayList<User>();
		for(Turn t : turns) {
			if(t.getId().equals(turnId))
				filtereduUsers.add(t.getUser());
		}
		if(filtereduUsers.isEmpty())
			throw new NoSuchElementException("There are no users that requested turn " + turnId);
		return filtereduUsers;
	}
	
	/**
	 * Generates a csv report of users with specified headers
	 * @param turns, ArrayLsit, the turns
	 * @param titles, List<String> headers
	 * @param filename, String
	 * @throws IOException if an error occurred during creating file
	 */
	public void generateCSVReportForTurns(ArrayList<Turn> turns, List<String> titles, String filename) throws IOException {
		String header = String.join(",", titles);
		BufferedWriter  bw = new BufferedWriter(new FileWriter(new File( REPORTS_FOLDER + filename )));
		bw.write(header + "\n");
		turns.forEach(x -> {
			try {
				bw.write( x + "\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		bw.close();
	}
	
	/**
	 * Generates a csv report of turns with specified headers
	 * @param turns, ArrayLsit, the turns
	 * @param titles, List<String> headers
	 * @param filename, String
	 * @throws IOException if an error occurred during creating file
	 */
	public void generateCSVReportForUsers(ArrayList<User> turns, List<String> titles, String filename) throws IOException {
		String header = String.join(",", titles);
		BufferedWriter  bw = new BufferedWriter(new FileWriter(new File( REPORTS_FOLDER + filename )));
		bw.write(header + "\n");
		turns.forEach(x -> {
			try {
				bw.write( x + "\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		bw.close();
	}
	
	
		
	/**
	 * Sequentially searches Turn by its id. 
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
	 * Searches by the id of a Turn using binary search
	 * @return Turn found, can be null if not found; otherwise, the object searched.
	 */
	public Turn binarySearchTurnById( String id ) {
		// Anonymous class
		// SORTING USSING ANONYMOUS CLASS AND REVERSE ORDER
		Collections.sort( turns, Collections.reverseOrder(new Comparator<Turn>() {
					@Override
					public int compare(Turn o1, Turn o2) {
						return  o1.getId().compareTo(o2.getId());
					}
			}));
			
			int start = 0;
			int end = turns.size() - 1;
			boolean wasFound = false; 
			Turn found = null;
			while( start <= end && !wasFound) {
				int mid = (start + end) / 2;
				if( turns.get(mid).getId().compareTo(id) < 0)
					end = mid -1;
				else if( turns.get(mid).getId().compareTo(id) > 0)
					start = mid+1;
				else {
					wasFound = true;
					found = turns.get(mid); 
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
	}

	/**
	 * Registers next available turn to user with id and turn type specified.
	 * @param usrId, String, identification number of user to whom the turn will be assigned. Cannot be null.
	 * <b>post: <b> turn was registered successfully if parameter was not null, user was found and didn't have a turn assigned already; otherwise, corresponding exception was thrown.
	 * After setting turn to user, lasTurn was updated.
	 * @throws UserNotFoundException if user with the specified id is not found.
	 * @throws UserAlreadyHasATurnException, if user already has a turns assigned.
	 * @throws BannedUserException, if user is within the days of suspension period
	 */
	public void registerTurn(String usrId, int turnTypeIdx) throws UserAlreadyHasATurnException, UserNotFoundException, BannedUserException {
		User usr = binarySearchUserById(usrId);
		if(usr == null) 				throw new UserNotFoundException(usrId);
		if(turnTypes.isEmpty()) throw new NoSuchElementException("There are no turntypes added yet.");
		if( usr.getLastBannedDateTime() != null && !usr.getLastBannedDateTime().isBefore(this.dateTime) )
			throw new BannedUserException(usrId, usr.getLastBannedDateTime());		
		
		else if (usr.getTurn() != null) throw new UserAlreadyHasATurnException(usr);
		else {
			TurnType tt =  turnTypes.get(turnTypeIdx);
			// If is the first turn, then set it to the current datetime; else, set it to the last turn added 
			if( allTurnsWereAttended() || turns.size() == 0 )
				starting_datetime_turn = DateTime.copyOf(dateTime);
			else
				starting_datetime_turn = DateTime.copyOf(turns.get(turns.size()-1).getEndingDateTime()); 
			// Sets the dateTime limits of the turn
			DateTime startingDateTime = DateTime.copyOf(starting_datetime_turn);
			DateTime endingDateTime = DateTime.copyOf(startingDateTime);
			endingDateTime.plusMillis( DateTime.minutes2Millis( tt.getDurationMinutes() + CHANGE_TIME_DURATION ) );
			
			// Reinitializes turns id's if it starts in the current day but ends after midnight (i.e. in the next day)
			if(startingDateTime.isBefore(dateTime.asMidnight()) && endingDateTime.isAfter(dateTime.asMidnight())) {
				this.lastTurn = new Turn("A-1", null, null);
				startingDateTime = dateTime.asMidnight();
				startingDateTime.plusHours(1);
				endingDateTime = DateTime.copyOf(startingDateTime);
				endingDateTime.plusMillis( DateTime.minutes2Millis( tt.getDurationMinutes() + CHANGE_TIME_DURATION ) );
			}
			
			Turn turn = new Turn(generateNextTurnId(lastTurn.getId()), usr, tt);
			turn.setStartingDateTime(startingDateTime);
			turn.setEndingDateTime( endingDateTime ); 
			usr.setTurn(turn);
			turns.add(turn);
			lastTurn.setId(turn.getId());
		}
	}
	
	/**
	 * Answers whether all turns were already attended.
	 * @return boolean, answer if all turns were attended.
	 */
	public boolean allTurnsWereAttended() {
		boolean ans = true;
		for(int i = 0; i < turns.size() && ans; i++) {
			if( turns.get(i).getState().equals(Turn.ON_HOLD) )
				ans = false;
		}
		return ans;
	}
	
	/**
	 * Attends all turns pending until the current datetime, randomly assigning the state of every turn. 
	 * @throws NoSuchElementException
	 */
	public void attendAllTurnsUpToTheCurrentDateTime() throws NoSuchElementException{
		if( !users.isEmpty() ) {
			for(User x : users) {
				if(x.getTurn() != null && 
				   x.getTurn().getState().equals(Turn.ON_HOLD) && 
				   x.getTurn().getEndingDateTime().isBefore(this.dateTime)) {
						String randomState = ( Math.random() > 0.5 ) ? Turn.ATTENDED : Turn.USER_NOT_PRESENT; 
						dispatchTurn(x.getTurn(), randomState);
						System.out.println( x.getId() +  ": Changed to " + randomState);
				}
			}
		} else {
			throw new NoSuchElementException("There are no turns registered yet");
		}
	}
	
	/**
	 * Returns a string summarizing the pending turns to be attended.
	 * @return String, table containing the information about the pending turns to be attended.
	 */
	public String sendTurnsQueue() {
		String header = "\t\t\t\t   QUEUED TURNS \n\n\t\tUSER ID\t\tCOMPLETE NAMES\t\t\tID\t\tTURN'S BEGENNING DATETIME\tTURN'S ENDING DATETIME(+15 secs)";
		String res = header +"\n";
		for( User u : users) {
			if(u.getTurn() != null && u.getTurn().getState().equals(Turn.ON_HOLD)){
				DateTime startingDateTime = DateTime.copyOf(u.getTurn().getStartingDateTime());
				DateTime endingDateTime = DateTime.copyOf(startingDateTime);
				endingDateTime.plusMillis(DateTime.minutes2Millis(u.getTurn().getTurnTpye().getDurationMinutes()));				
				res += "\t\t" + u.getId()+ "\t\t"+u.getNames() + " " + u.getSurnames() +"\t\t"+u.getTurn().getId()+"\t"+u.getTurn().getStartingDateTime()+"\t\t"+u.getTurn().getEndingDateTime()+"\n";
			}
		}
		if(res.equals(header + "\n"))
			res = "\tNo turns queued yet...";
		return res;
	}
		
	/**
	 * @return the turnTypes
	 */
	public ArrayList<TurnType> getTurnTypes() {
		return turnTypes;
	}

	/**
	 * Generates next turn id ranging from A + 10!/(10 - 7) to Z + 10!/(10 - 7).
	 * @param currentTurn, Turn, previous turn, based on this the next turn will be generated. 
	 * E.g. if currentTurn was A0, next turn will be A1; if currentTurn was Z99, next turn will be A00.
	 * @return String, id of the next turn. Its first character is a letter and the rest are numbers.
	 */
	public static String generateNextTurnId(String currTurnId) {
		// Reinitialize turns if day changes		
		long numDigits = (long) Math.pow(10, EXPONENT_OF_POSSIBLE_NUMBER_OF_DIGITS);
		char letter = currTurnId.charAt(0);
		int number = Integer.parseInt(currTurnId.substring(1, currTurnId.length()));
		int ASCIICurrLetter = (int) letter;
		int nextASCIILetter = (ASCIICurrLetter == 90) ? 65 : ASCIICurrLetter+1;
		long nextTurnNumber = (long) ( (number < (numDigits - 1 ) ) ? number + 1 : 0);
		int num_zeros = (int) (String.valueOf(numDigits).length() - String.valueOf(nextTurnNumber).length());
		String zeros = new String(new char[num_zeros-1]).replace("\0", "0");
		char nextTurnLetter = (number < (numDigits -1 ) ) ? letter : (char) nextASCIILetter;
		String nextTurnId = nextTurnLetter + zeros + nextTurnNumber;
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
		User usr = searchUser(turn.getUser().getId());
		usr.setTurn(null);
		if(state.equals(Turn.USER_NOT_PRESENT)) usr.numberOfAbsences++;
		if(usr.numberOfAbsences % 2 == 0) {
			DateTime bannedDateTime = DateTime.copyOf(this.dateTime);
			bannedDateTime.plusDays(2);
			usr.setLastBannedDateTime(bannedDateTime);
		}
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
		loadNames();
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
	 * Generates a random composed name based on text files allocated in /data folder
	 * @return String, composed random name.
	 * @throws IOException 
	 */
	public String generateComposedRandomName() throws IOException {
		loadNames();
		int randomIdx1 = (int) Math.floor( Math.random() * LENGTH_OF_NAMES_AND_SURNAMES_FILE);
		int randomIdx2 =  (int) Math.floor( Math.random() * LENGTH_OF_NAMES_AND_SURNAMES_FILE);
		
		while(randomIdx1 == randomIdx2) {
			randomIdx1 = (int) Math.floor( Math.random() * LENGTH_OF_NAMES_AND_SURNAMES_FILE);
			randomIdx2 =  (int) Math.floor( Math.random() * LENGTH_OF_NAMES_AND_SURNAMES_FILE);	
		}
		
		String name1 = NAMES.get(randomIdx1);
		String name2 = NAMES.get(randomIdx2);
		return name1 + " " + name2; 
	}
	
	/**
	 * Generates a random composed surname based on text files allocated in /data folder
	 * @return String, composed random surname.
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
	 * Generates a random composed surname based on text files allocated in /data folder
	 * @return String, composed random name.
	 */
	public String generateRandomSurname() throws IOException{
		loadSurnames();
		int randomIdx1 = (int) Math.floor( Math.random() * LENGTH_OF_NAMES_AND_SURNAMES_FILE);
		return SURNAMES.get(randomIdx1);
	} 
	
	/**
	 * Generates n number of users with random fields
	 * @param numUsers int, number of users to generate.
	 * @throws IOException
	 */
	public int generateRandomUsers(int numUsers) throws IOException {
		int numberOfSuccesfullyRegisteredUsers = 0;
		while(numberOfSuccesfullyRegisteredUsers < numUsers) {
			  String id = String.valueOf( (int) (Math.random() * NUMBER_OF_POSSIBLE_IDS));
			  String name = generateComposedRandomName();
			  String surnames = generateRandomSurname();
			  int idxOfTod = (int) (Math.random() * User.TYPES_OF_DOCUMENTS.length);
			  String tod = User.TYPES_OF_DOCUMENTS[idxOfTod];
			  try {
				addUser(name, surnames, id, tod, "", "", null);
				numberOfSuccesfullyRegisteredUsers++;
			} catch (UserAlreadyRegisteredException | InvalidInputException | BlankRequiredFieldException e) {
				continue;
			}
		}
		return users.size();
	}
	
	/**
	 * This method simulates the registration of specified number of turns in a given period of time.
	 * @param numDays, int. The number of days in which turns are going to be randomly generated.
	 * @param numTurnsPerDay, int. The number of turns per day.
	 * @throws TurnsLimitExceededException if: 
	 */
	public void registerTurnsPerDay(int numDays, int numTurnsPerDay) throws TurnsLimitExceededException {
		int numberOfPossible = calculateNumberOfUsersAvailable();
		if( (numDays * numTurnsPerDay) <= numberOfPossible) {
			int daysLeft = numDays;
			while(daysLeft > 0) {
				registerTurns(numTurnsPerDay);
				turns.get(turns.size()-1).getEndingDateTime().plusDays(1);
				reInitializeTurnsIds();
				daysLeft--;
			} 
		}else {
			throw new TurnsLimitExceededException(numberOfPossible);
		}
	}
	
	/**
	 * Re-intiializes turn ids
	 */
	public void reInitializeTurnsIds() {
		this.lastTurn = new Turn("A-1", null, null);
	}
	
	/**
	 * Registers randomly the number of turns specified in parameters within the current datetime.
	 * @param numTurns, int, the number of turns to be generated randomly.. 
	 * @throws NoSuchElementException if there are no turntypes added yet.
	 */
	public void registerTurns(int numTurns) throws NoSuchElementException {
		int numTurnsRegisteredSuccesfully = 0;
		int i = 0;
		while( numTurnsRegisteredSuccesfully < numTurns ) {
			int randomIdxTurnType = (int) (Math.random() * (turnTypes.size() - 1)); 
			try {
				registerTurn(users.get(i).getId(), randomIdxTurnType);
				numTurnsRegisteredSuccesfully++;
			} catch (UserNotFoundException | UserAlreadyHasATurnException | BannedUserException  e) {
				continue;
			}finally {
				i++;
			}
		}
	}
	
	/**
	 * Filters the users that are able to take a turn. 
	 * @return ArrayList<User>, number of users that, according to the rules of the program, are able to register a turn
	 */
	public ArrayList<User> getUsersAvailable(){
		return users.stream().filter(u -> 
			u.getTurn() == null && (u.getLastBannedDateTime() == null || u.getLastBannedDateTime().isBefore(dateTime) )
		).collect(Collectors.toCollection(ArrayList::new));
	}
	
	/**
	 * Claculates the number of users that are able to take a turn. 
	 * @return int, number of users that, according to the rules of the program, are able to register a turn.
	 */
	public int calculateNumberOfUsersAvailable() {
		int x = 0;
		for(User u : users)
			if(u.getTurn() == null && ( u.getLastBannedDateTime() == null || u.getLastBannedDateTime().isBefore(dateTime) ) )
				x++;				
		return x;
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
	
	/**
	 * Send string with datetime
	 * @return String, datetime. 
	 */
	public String sendDateTime() {
		return dateTime.toString();
	}
	
	/**
	 * Modifies the current datetime of the system based on the user's selection.
	 * @param strDate, String, the string containing the date. Must follow this pattern: YYYY-MM-DD hh:mm:ss
	 * @throws Exception if the new dateTime is older than the current one.
	 */
	public void updateDateTimeManually(String strDate) throws Exception {
		strDate = strDate.trim();
		String[] dateAndTime = strDate.split(" ");
		// YY-MM-DD
		int[] date = Arrays.stream(dateAndTime[0].split("-")).mapToInt(Integer::parseInt).toArray();
		// hh:mm:ss
		int[] time = Arrays.stream(dateAndTime[1].split(":")).mapToInt(Integer::parseInt).toArray();
		DateTime dt = new DateTime(date[0], date[1], date[2], time[0], time[1], time[2]);
		if( dt.isBefore(this.dateTime) )
			throw new Exception("Invalid datetime. New datetime cannot be older than current");
		else
			this.dateTime = dt;
	}
	
	/**
	 * Updates milliseconds to the current datetime
	 * @param milliseconds
	 */
	public void updateDateTimeByMillis(long milliseconds) {
		dateTime.plusMillis(milliseconds);
	}
	
	/**
	 * Synchronizes the sysemt's datetime with the computer's datetime.
	 */
	public void updateDateTimeBySystem() {
		this.dateTime = DateTime.now();
	}
	
	/**
	 * Adds a turn type.
	 * @param name, String, the name of the turnType.
	 * @param duration, float, the minutes of duration of the turn type.
	 */
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