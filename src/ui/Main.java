package ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
		
		// Text art created at: https://fsymbols.com/text-art/

		String welcomeTo = 	"░██╗░░░░░░░██╗███████╗██╗░░░░░░█████╗░░█████╗░███╗░░░███╗███████╗  ████████╗░█████╗░██╗\n"+
							"░██║░░██╗░░██║██╔════╝██║░░░░░██╔══██╗██╔══██╗████╗░████║██╔════╝  ╚══██╔══╝██╔══██╗╚═╝\n"+
							"░╚██╗████╗██╔╝█████╗░░██║░░░░░██║░░╚═╝██║░░██║██╔████╔██║█████╗░░  ░░░██║░░░██║░░██║░░░\n"+
							"░░████╔═████║░██╔══╝░░██║░░░░░██║░░██╗██║░░██║██║╚██╔╝██║██╔══╝░░  ░░░██║░░░██║░░██║░░░\n"+
							"░░╚██╔╝░╚██╔╝░███████╗███████╗╚█████╔╝╚█████╔╝██║░╚═╝░██║███████╗  ░░░██║░░░╚█████╔╝██╗\n"+
							"░░░╚═╝░░░╚═╝░░╚══════╝╚══════╝░╚════╝░░╚════╝░╚═╝░░░░░╚═╝╚══════╝  ░░░╚═╝░░░░╚════╝░╚═╝\n";

		String TurnsApp = "████████╗██╗░░░██╗██████╗░███╗░░██╗░██████╗  ░█████╗░██████╗░██████╗░\n"+
						  "╚══██╔══╝██║░░░██║██╔══██╗████╗░██║██╔════╝  ██╔══██╗██╔══██╗██╔══██╗\n"+
						  "░░░██║░░░██║░░░██║██████╔╝██╔██╗██║╚█████╗░  ███████║██████╔╝██████╔╝\n"+
						  "░░░██║░░░██║░░░██║██╔══██╗██║╚████║░╚═══██╗  ██╔══██║██╔═══╝░██╔═══╝░\n"+
						  "░░░██║░░░╚██████╔╝██║░░██║██║░╚███║██████╔╝  ██║░░██║██║░░░░░██║░░░░░\n"+
						  "░░░╚═╝░░░░╚═════╝░╚═╝░░╚═╝╚═╝░░╚══╝╚═════╝░  ╚═╝░░╚═╝╚═╝░░░░░╚═╝░░░░░\n";
				
				String by = "█▄▄ █▄█ ▀   \n"+
							"█▄█ ░█░ ▄   \n";
		
		String authority = "█▀ █▀▀ █▄▄ ▄▀█ █▀ ▀█▀ █ ▄▀█ █▄░█   █▀▀ ▄▀█ █▀█ █▀▀ █ ▄▀█   ▄▀█ █▀▀ █▀█ █▀ ▀█▀ ▄▀█\n"+
						   "▄█ ██▄ █▄█ █▀█ ▄█ ░█░ █ █▀█ █░▀█   █▄█ █▀█ █▀▄ █▄▄ █ █▀█   █▀█ █▄▄ █▄█ ▄█ ░█░ █▀█\n";
		
		int indentationLevels = 5;
				
		println(String.join("\n", 
					Arrays.asList(welcomeTo.split("\n")).stream()
					.map(s -> String.join("", Collections.nCopies(4, "\t")) + s)
					.collect( Collectors.toCollection(ArrayList::new))
		));

		println(String.join("\n", 
				Arrays.asList(TurnsApp.split("\n")).stream()
				.map(s -> String.join("", Collections.nCopies(indentationLevels, "\t")) + s)
				.collect( Collectors.toCollection(ArrayList::new))
		));

		println("\n");
		
		println(String.join("\n", 
				Arrays.asList(by.split("\n")).stream()
				.map(s -> String.join("", Collections.nCopies(6+3, "\t")) + s)
				.collect( Collectors.toCollection(ArrayList::new))
		));
		
		println("\n");
		
		println(String.join("\n", 
				Arrays.asList(authority.split("\n")).stream()
				.map(s -> String.join("", Collections.nCopies(2+2, "\t")) + s)
				.collect( Collectors.toCollection(ArrayList::new))
		));
		
		int election = 0;
		long start;
		long end;
		manager.generateRandomSurnames(100).forEach(System.out::println);
		String OPCIONES = "\n\t1).Add user\n\t2).Register turn\n\t3).Attend\n\t4).Exit";
		while (election != 4) {
			start = System.currentTimeMillis();
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
				print("\tThe next available turn is " + manager.generateNextTurnId(manager.lastTurn.getId())
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
					println("\tCurrent turn is " +  manager.getCurrentTurn().getId());
					println("\tChooose: ");
					println("\t\t1). Attend current turn.");
					println("\t\t2). Attend another turn");
					println("\t\t3). Go back to menu");
					print("\tAnswer [1-2]: ");
					
					int attentionMethodRes = Integer.parseInt(sc.readLine());
					Turn turnToBeAttended = null;
					
					switch (attentionMethodRes) {
					case 1:
						turnToBeAttended = manager.getCurrentTurn();
						break;
					case 2:
						manager.consultNextTurnToBeAttended().getId();
						print("\t\t\tType the id of Turn that you want to attend: ");
						String idSearched = sc.readLine();
						turnToBeAttended = manager.searchTurn(idSearched);
						break;
					case 3:
						break;
					default:
						println("Invalid choice");
						break;
					}
					if(attentionMethodRes!=3) {
					print("\t\t\tChoose the attention state:\n" + 
						    "\t\t\t" + "\t1) Dispatch as: Attended\n"+ 
						    "\t\t\t" + "\t2) Dispatch as: User not present\n"+
						    "\t\t\t" + "\t3) Go back\n"+ 
						    "\t\t\tAnswer[1-3]: ");
					int res = Integer.parseInt(sc.readLine());
						switch (res) {
						case 1:
							manager.dispatchTurn(turnToBeAttended, Turn.ATTENDED);
							break;
						case 2:
							manager.dispatchTurn(turnToBeAttended, Turn.USER_NOT_PRESENT);
							break;
						case 3:
							election=3;
							break;
						default:
							println("Invalid choice");
							break;
						}
						println(SUCCESS_OP);
					}
				} catch(NoSuchElementException | NumberFormatException e) {
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
			end = System.currentTimeMillis();
			long opDuration = end - start;
			System.out.println("Time it took: (millseg)" + opDuration);
			String opTime = TimeUnit.MILLISECONDS.toHours(opDuration) + " " + TimeUnit.MILLISECONDS.toMinutes(opDuration) + " " + TimeUnit.MILLISECONDS.toSeconds(opDuration);
			System.out.println(opTime);
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