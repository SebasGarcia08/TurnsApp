package ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.NoSuchElementException;

import model.*;

import CustomExceptions.BlankRequiredFieldException;
import CustomExceptions.UserAlreadyHasATurnException;
import CustomExceptions.UserAlreadyRegisteredException;
import CustomExceptions.UserNotFoundException;
import CustomExceptions.InvalidInputException;;

/**
 * Main class for TurnsApp
 * @author Sebastián García Acosta
 *
 */
public class Main {
	/**
	 * Main method.
	 * @param args, String[].
	 */
	public static void main(String[] args) throws IOException {
		final String SUCCESS_OP = "[SUCCESSFUL OPERATION]";
		final String FAILED_OP = "[FAILED OPERATION]";
		TurnsManager manager = new TurnsManager();
		BufferedReader sc = new BufferedReader(new InputStreamReader(System.in));

		int election = 0;
		String OPCIONES = "\n\t1).Add user\n\t2).Register turn\n\t3).Attend\n\t4).Exit";
		while (election != 4) {
			print("\n======== MENU ========" + OPCIONES + "\nAnswer [1-4]: ");
			try {
				election = Integer.parseInt(sc.readLine());
			} catch(NumberFormatException e) {
				election = -1;
			}
			switch (election) {
			case 1:
					println("\n[USER SIGNUP]");
					String n, s, tod, id, cpn, a;
					println("Please fill-in the following fields (required ones are marked with *)");

					print("\tName *: ");
					n = sc.readLine();
					
					print("\tSurnames *: ");
					s = sc.readLine();

					println("\tType of document *:");
					
					for(int i = 0; i < User.TYPES_OF_DOCUMENTS.length; i++)
						println("\t" + "\t" + (i+1) + "). " + User.TYPES_OF_DOCUMENTS[i]);
					print("\tAnswer [1-5]: ");
					try {
						tod = User.TYPES_OF_DOCUMENTS[Integer.parseInt(sc.readLine())-1];
					}catch(IndexOutOfBoundsException | NumberFormatException e) {
						println("Invalid choice");
						break;
					}

					print("\tDocument number *: ");
					id = sc.readLine();

					print("\tCellphone number: ");
					cpn = sc.readLine();

					print("\tAddress: ");
					a = sc.readLine();
					try {
						manager.addUser(n, s, id, tod, cpn, a, null);
						println(SUCCESS_OP);
					} catch(UserAlreadyRegisteredException | BlankRequiredFieldException | InvalidInputException e) {
						println(e.getMessage());
						println(FAILED_OP);
						break;
					}
				break;

			case 2:
				println("\n[TURN REGISTRATION]");
				print("\tThe next available turn is " + manager.generateNextTurnId(manager.lastTurn)
						+ ". Do you want to assign it to some user? [y/n]: ");
				String y_n = "";
				try { y_n = String.valueOf(sc.readLine().charAt(0)); }
				catch(StringIndexOutOfBoundsException e) { println("Invalid choice"); break; }
				if (y_n.equalsIgnoreCase("n")) { println("Ok, returning to menu..."); } 
				else if (y_n.equalsIgnoreCase("y")) {
					print("\tWrite the id of the user: ");
					String id_ = sc.readLine();
					try {
						manager.registerTurn(id_);
						println(SUCCESS_OP);
					} catch (UserAlreadyHasATurnException | UserNotFoundException e) {
						println(e.getMessage());
						println(FAILED_OP);
					}
				} else
					println("Invalid choice. Possible answers are 'y' (yes) or 'n' (no)");
				break;
			case 3:
				try {
					println("\n[ATTEND TURN]");
					print("\tThe current turn is " + manager.getCurrentTurn().getId());
					print(". Choose what to do with it:\n" + 
						    "\t" + "\t1) Dispatch as: Attend\n"+ 
						    "\t" + "\t2) Dispatch as: User not present\n"+
						    "\t" + "\t3) Go back to main menu\n"+ 
						    "\tAnswer[1-3]: ");
					int res = Integer.parseInt(sc.readLine());
						switch (res) {
						case 1:
							manager.dispatchTurn(Turn.ATTENDED);
							break;
						case 2:
							manager.dispatchTurn(Turn.USER_NOT_PRESENT);
							break;
						case 3:	
							break;
						default:
							println("Invalid choice");
							break;
						}
						println(SUCCESS_OP);
				} catch(NoSuchElementException e) {
					println(e.getMessage());
					println(FAILED_OP);
				}
				break;
			case 4:
				println("Goodbye!");
				sc.close();
				break;
			default:
				println("Invalid choice.");
				break;
			}
		}
	}

	/**
	 * Wrapper method for System.out.print. 
	 * The only purpose of this method is to avoid typing System.out.print all the time.
	 * @param s, String to be printed.
	 */
	public static void print(String s) {
		System.out.print(s);
	}
	
	/**
	 * Wrapper method for System.out.println. 
	 * The only purpose of this method is to avoid typing System.out.println all the time.
	 * @param s, String to be printed.
	 */
	public static void println(String s) {
		System.out.println(s);
	}

}