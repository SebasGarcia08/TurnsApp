package ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import CustomExceptions.BannedUserException;
import CustomExceptions.BlankRequiredFieldException;
import CustomExceptions.UserAlreadyHasATurnException;
import CustomExceptions.UserAlreadyRegisteredException;
import CustomExceptions.UserNotFoundException;
import CustomExceptions.InvalidInputException;
import CustomExceptions.TurnsLimitExceededException;
import model.*;

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
		final String PROGRAM_PATH = "data/PROGRAM.dat";
		
		TurnsManager manager = new TurnsManager();
		BufferedReader sc = new BufferedReader(new InputStreamReader(System.in));

		// Deserialize manager obj
		File serializedFile = new File(PROGRAM_PATH); 
		if(serializedFile.exists()) {
			System.out.print("Restore system state? [y = YES /<Any> = NO]: ");
			String res = sc.readLine();
			if(res.equals("y")) {
				try {
					System.out.println("Loading app...");
					FileInputStream file = new FileInputStream(serializedFile);
					ObjectInputStream in = new ObjectInputStream( file );
					manager = (TurnsManager) in.readObject();
					in.close();
					file.close();
					
					System.out.print("Update system datetime to the current or leave it as it was saved? [u = UPDATE/ <Any>: NOT UPDATE]?: ");
					if( sc.readLine().equals("u") ) {
						manager.updateDateTimeBySystem();
					}
				}catch(IOException e) {
					System.out.println(e.getMessage());
				} catch (ClassNotFoundException e) {
					System.out.println("An error ocurred");
				}		
			}		
		}
				
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
		int exit = 11;
		String OPCIONES = "\n\t1).Add user " + 
						  "\n\t2).Add turn type" +
						  "\n\t3).Register turn"+
						  "\n\t4).Attend all pending turns until current datetime"+
						  "\n\t5).Update date"+
						  "\n\t6).Show time"+
						  "\n\t7).Summarize pending turns to be attended"+
						  "\n\t8). Generate random users" +
						  "\n\t9).Generate random turns by days" +
						  "\n\t10). Reports"+
						  "\n\t"+ exit +").Exit";
		while (election != exit && (start = System.currentTimeMillis()) > 0 ) {
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
				try {
					System.out.println("\n[TURN TYPE REGISTRATION]");
					System.out.print("\tName: ");
					String ttname = sc.readLine();
					System.out.print("\tDuration (minutes): ");
					float ttduration = Float.parseFloat(sc.readLine());
					manager.registerTurnType(ttname, ttduration);
				} catch(Exception e) {
					System.out.println(e.getMessage());
				}
			break;
			case 3:
				println("\n[TURN REGISTRATION]");
					print("\tWrite the id of the user: ");
					String id_ = sc.readLine();
					int idxOfTurnType = 0;
					if(manager.getTurnTypes().isEmpty()) {
						System.out.println("\tThere is no turn types available. Create one.");
						break;
					} else {
						System.out.println("Select the turnType");
						int count=1;
						for(TurnType x : manager.getTurnTypes()) {
							System.out.println("\t\t[" + count + "]" + " " + x.toString());
							count++;
						}
						System.out.print("\tAnswer: ");
						try {
							idxOfTurnType = Integer.parseInt(sc.readLine()) - 1;
							if( idxOfTurnType < 0 || idxOfTurnType > manager.getTurnTypes().size()) {
								throw new NumberFormatException();
							}
						} catch(NumberFormatException e) {
							System.out.println("Invalid input");
							break;
						}
					}
					try {
						// Before registering, update the datetime
						manager.updateDateTimeByMillis(System.currentTimeMillis() - start);
						start = System.currentTimeMillis();
						manager.registerTurn(id_, idxOfTurnType);						
						println(SUCCESS_OP);
					} catch (UserAlreadyHasATurnException | UserNotFoundException | BannedUserException e) {
						println(e.getMessage());
						println(FAILED_OP);
					}
			break;
			case 4:
				try {
					// Before registering, update the datetime
					manager.updateDateTimeByMillis(System.currentTimeMillis() - start);
					start = System.currentTimeMillis();
					manager.attendAllTurnsUpToTheCurrentDateTime();
				} catch(NoSuchElementException e) {
					println(e.getMessage());
					println(FAILED_OP);
				}
			break;
			case 5:
				print("\tChoose: \n\t[1] Synchronize datetime with this computer.\n\t[2] Update datetime manually\n\t[Any] Go back\n\t[Answer]: ");
				try {
					String res = sc.readLine();
					switch(res) {
						case "1": 
							manager.updateDateTimeBySystem();
							start = System.currentTimeMillis();
							break;
						case "2":
							print("\t\tWrite the datetime you will update the system (with format YYYY-MM-DD hh:mm:ss). E,g. 2025-12-31 23:59:55: ");
							String strDate = sc.readLine();
							manager.updateDateTimeManually(strDate);
							break;
						default:
							break;
					}
				}catch(Exception e) {
					println(e.getMessage());
				}
				break;
			case 6:
				manager.updateDateTimeByMillis(System.currentTimeMillis() - start);
				start = System.currentTimeMillis();
				System.out.println("\tCURRENT SYSTEM TIME " + manager.sendDateTime());
				break;
			case 7:
				 System.out.println(manager.sendTurnsQueue());
				break;
			case 8:
				try {
					System.out.print("Type the number of users to be generated: ");	
					System.out.println("Now there are " + manager.generateRandomUsers(Integer.parseInt(sc.readLine())) + " users." );
				}catch(NumberFormatException e) {
					break;
				}
				break;
			case 9:
				try {
					System.out.print("\t\tType the number of days: ");
					int numDays = Integer.parseInt(sc.readLine());
					System.out.print("\t\tType the number of turns per days: ");
					int numTurnsPerDay = Integer.parseInt(sc.readLine());
					manager.registerTurnsPerDay(numDays, numTurnsPerDay);
					System.out.println(SUCCESS_OP);
				} catch (TurnsLimitExceededException | NoSuchElementException e) {
					System.out.println(e.getMessage());
					System.out.println(FAILED_OP);
				}catch( NumberFormatException e ) {
					break;
				}
			break;
			case 10:
				System.out.println("\tDo you want to visualize the report in console or export it as a CSV file in /reports folder?");
				System.out.print("\t\tAnswer [C: Console / E: Export as CSV / <Any>: Return to menu]: ");
				String visMode = sc.readLine().toUpperCase();
				if( !visMode.equals("C") && !visMode.equals("E")) break;
				System.out.print("\tGenerate report about:" + 
								 "\n\t\t1). All turns requested by user"+
								 "\n\t\t2). All users that requested some turn"+
								 "\n\t\t<Any> Go back to menu"+
								 "\n\tAnswer [1/2]: "
								);
				String kindOfReport = sc.readLine();
				String resSorting = "";
				String userId = "";
				String turnId = "";
				switch (kindOfReport) {
				case "1":
					System.out.print("\t\t Type the id of the user: ");
					userId = sc.readLine();
					System.out.print("\t\t\tSort by: \n\t\t\tD).Turn ending datetime\n\t\t\tZ). Without criteria\n\t\tAnswer: ");
					resSorting = sc.readLine().toUpperCase();
					break;
				case "2":
					System.out.print("\t\t Type the id of turn: ");
					turnId = sc.readLine();
					System.out.print("\t\t\tSort by: \n\t\t\tA). Number of absences\n\t\t\tN). Name and surnames\n\t\t\tZ). Without criteria\n\t\tAnswer: ");
					resSorting = sc.readLine().toUpperCase();
					break;
				default:
					break;
				} 
				boolean firstValidCase = (kindOfReport.equals("1") && (resSorting.equals("D") || resSorting.equals("Z")));
				boolean secondValidCase = (kindOfReport.equals("2") && ( resSorting.equals("A") || resSorting.equals("N") || resSorting.equals("Z") ));
				if( firstValidCase ) {
					try {
						List<String> titles = Arrays.asList("TURN_ID","TURN_STATE");
						ArrayList<Turn> turns = manager.getAllTurnsRequestedByUser(userId);
						if(resSorting.equals("D"))
							turns = manager.getSortedTurnsByEndingDateTime(turns);
						if( visMode.equals("C") ) {
								System.out.println( String.join("\t", titles) );
								turns.forEach( x -> System.out.println(x.getId() + "\t" + x.getState()));							
						}else {
							System.out.print("Type the report name: ");
							String filename = sc.readLine();
							manager.generateCSVReportForTurns(turns, titles, filename);
						}
					} catch(NoSuchElementException e) {
						System.out.println(e.getMessage());
						System.out.println(FAILED_OP);
					}
				} else if ( secondValidCase ) {
					try {
						List<String> titles = Arrays.asList("NAMES","SURNAMES","NUMBER_OF_ABSENCES");
						ArrayList<User>  users = manager.getAllUserThatHadTurn(turnId);
						if( resSorting.equals("A") )
							users = manager.getSortedUsersByNumberOfAbsences(users);
						if(resSorting.equals("N"))
							users = manager.getSortedUsersByNameAndSurname(users);
						if( visMode.equals("C") ) {
							System.out.println( String.join("\t", titles) );
							users.forEach( x -> System.out.println(x.getId() + "\t" + x.getNames() + "\t" + x.getSurnames() + "\t" + x.numberOfAbsences));
						}else {
							System.out.print("Type the report name: ");
							String filename = sc.readLine();
							manager.generateCSVReportForUsers(users, titles, filename);
						}
					}catch(NoSuchElementException e) {
						System.out.println(e.getMessage());
						System.out.println(FAILED_OP);
					}
				}
				break;
			case 11:
				System.out.println("[EXIT]");
				System.out.print("Choose:\n\t" + 
										"[1] Save changes\n\t"+ 
										"[2] Exit without saving changes\n\t"+
								 "Answer: [1/2]: "
								);
				String key = sc.readLine();
				switch (key) {
				case "1":
					System.out.println("\tSaving system state...");
					try {
						FileOutputStream file = new FileOutputStream(new File( PROGRAM_PATH )); 
						ObjectOutputStream out = new ObjectOutputStream(file);
						
						out.writeObject(manager);
						out.close();
						file.close();
						System.out.println("\tSaved successfully");
					}catch( IOException e ) {
						System.out.println(e.getMessage());
					}
				break;
				case "2":
					System.out.print("\tARE YOU SURE? [y = Yes / <Any> = No]: ");
					String confirm = sc.readLine();
					if(confirm.equals("y")) {
						println("\tGoodbye!");
						sc.close();						
					}
					else { election = 0; }
				break;
				default:
					System.out.println("Invalid choice");
					election = 0;
					break;
				}
			break;
			default:
				println("Invalid choice.");
			break;
			}
			end = System.currentTimeMillis();
			long opDurationMenu = end - start;
			System.out.println("Time it took: (ms) " + opDurationMenu);
			manager.updateDateTimeByMillis(System.currentTimeMillis() - start);
			System.out.println("Actual time: " + manager.sendDateTime());
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