package ui;

import model.*;
import java.util.Scanner;

import CustomExceptions.BlankRequiredFieldException;
import CustomExceptions.UserAlreadyHasATurnException;
import CustomExceptions.UserAlreadyRegisteredException;
import CustomExceptions.UserNotFoundException;

public class Main {
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		TurnsManager manager = new TurnsManager();
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);

		int election = 0;
		String OPCIONES = "\n1). Add user.\n2). Register turn.\n3). Atender";
		while (election != 4) {
			print("======== MENU ========\n" + OPCIONES + "\nAnswer: ");
			election = sc.nextInt();
			switch (election) {
			case 1:
				println("Please fill-in the following fields (required ones are marked with *)");

				println("Name *: ");
				String n = sc.nextLine();

				println("Type of document *: ");
				String tod = sc.nextLine();

				println("Document number *: ");
				String id = sc.nextLine();

				println("Cellphone number *: ");
				String cpn = sc.nextLine();

				println("Address *: ");
				String a = sc.nextLine();

				try {
					manager.addUser(n, id, tod, cpn, a, null);
				} catch (UserAlreadyRegisteredException | BlankRequiredFieldException e) {
					println(e.getMessage());
				}
				break;

			case 2:
				print("The next available turn is " + manager.generateNextTurnId(manager.currentTurn)
						+ ". Do you want to assign it to some user? [y/n]: ");
				String y_n = String.valueOf(sc.nextLine().charAt(0));
				if (y_n.equalsIgnoreCase("n")) { println("Ok, returning to menu..."); } 
				else if (y_n.equalsIgnoreCase("y")) {
					print("Write the id of the user: ");
					String id_ = sc.next();
					try {
						manager.registerTurn(id_);
					} catch (UserAlreadyHasATurnException | UserNotFoundException e) {
						println(e.getMessage());
					}
				} else
					println("Invalid choice. Possible answers are 'y' (yes) or 'n' (no)");
				break;
			case 3:
				println("Current turn: ");
				break;
			case 4:
				println("Goodbye!");
				break;
			default:
				println("Invalid choice.");
				break;
			}
		}
	}

	public static void print(String s) {
		System.out.print(s);
	}

	public static void println(String s) {
		System.out.println(s);
	}

}