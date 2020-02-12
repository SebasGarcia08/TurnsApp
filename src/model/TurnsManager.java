package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import CustomExceptions.*;

public class TurnsManager {

	private ArrayList<User> users;
	private ArrayList<Turn> turns;
	public Turn currentTurn;

	public TurnsManager() {
		this.turns = new ArrayList<Turn>();
		this.users = new ArrayList<User>();
		this.currentTurn = new Turn("A00");
	}

	/**
	 * 
	 * @param n
	 * @param id
	 * @param tod
	 * @param cpn
	 * @param a
	 */
	public void addUser(String n, String id, String tod, String cpn, String a, Turn t)
			throws UserAlreadyRegisteredException, BlankRequiredFieldException {
		
		List<String> requiredFields = Arrays.asList(n, id, tod );
		ArrayList<String> blankFields = requiredFields.stream()
										.filter(s -> s.isBlank())
										.collect(Collectors.toCollection(ArrayList::new));	

		if (!blankFields.isEmpty()) throw new BlankRequiredFieldException(blankFields);
		if (searchUser(id) != null) throw new UserAlreadyRegisteredException(id);
		users.add(new User(n, id, tod, cpn, a, t));
	}

	/**
	 * 
	 * @param id
	 */
	public User searchUser(String id) throws UserNotFoundException {
		return users.stream().filter(user -> user.getId().contentEquals(id)).findFirst().get();
	}

	/**
	 * 
	 * @param usr
	 */
	public void registerTurn(String id) throws UserAlreadyHasATurnException, UserNotFoundException {
		User usr = searchUser(id);
		if (usr.getTurn() != null)
			throw new UserAlreadyHasATurnException(usr);
		else {
			Turn turn = new Turn(generateNextTurnId(currentTurn));
			usr.setTurn(turn);
			turns.add(turn);
		}
	}

	/**
	 * 
	 * @return
	 */
	public String generateNextTurnId(Turn currentTurn) {
		String currTurn = currentTurn.getId();
		char letter = currTurn.charAt(0);
		int number = Integer.parseInt(currTurn.substring(1, currTurn.length()));
		int ASCIICurrLetter = (int) letter;
		int nextASCIILetter = (ASCIICurrLetter == 90) ? 65 : ASCIICurrLetter;
		int nextTurnNumber = (number < 99) ? number + 1 : number - 99;
		char nextTurnLetter = (number < 99) ? letter : (char) nextASCIILetter;
		String nextTurnId = String.valueOf(nextTurnLetter) + String.valueOf(nextTurnNumber);
		return nextTurnId;
	}

	public Turn searchTurn(String turnId)  throws UserNotFoundException {
		return turns.stream().filter(turn -> turn.getId().contentEquals(turnId)).findFirst().get();
	}
	
	/**
	 * 
	 * @param turn
	 */
	public void dispatchTurn(String turnId) {
		searchTurn(turnId).setState(Turn.ATTENDED);
	}

	public ArrayList<User> getUsers() {
		return this.users;
	}

	/**
	 * 
	 * @param users
	 */
	public void setUsers(ArrayList<User> users) {
		this.users = users;
	}

	public ArrayList<Turn> getTurns() {
		return this.turns;
	}

	/**
	 * 
	 * @param turns
	 */
	public void setTurns(ArrayList<Turn> turns) {
		this.turns = turns;
	}

	public Turn getCurrentTurn() {
		return currentTurn;
	}

	public void setCurrentTurn(Turn currentTurn) {
		this.currentTurn = currentTurn;
	}

}