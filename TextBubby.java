package textbubby;



import java.io.BufferedWriter;
import java.io.FileWriter;

import java.io.IOException;

import java.util.ArrayList;

import java.util.Scanner;
/**
 *	
 *	This class is used to manipulate text in a file using command line interface (CLI)
 *	Users can add and delete some texts from file, display and clear all the text in the file and exit the program. 
 *	Users have to enter correct command format in order to perform the right command.
 *
 *	Some assumptions:
 *	When commands other than "display", "delete", "add", "clear", "exit" are typed by user, message is prompted to inform user type in other command
 *	When the index user typed in for deleting is greater than total number of text line in the file, a error message will be prompted (invalid command)
 *	A valid delete command should follow by a numeric number. e.g. "delete 2" is a valid command, but "delete" and "delete A" are invalid command
 *	TextBubby command line is not case sensitive, e.g. "DisPlay" is a valid command
 *	When exit command is typed by user, file close message is prompted and file is saved to the disk.This is to reduce the program complexity and runtime.
 *	
 *
 *
 *
 *
 * @author Weng Yuan
 * Tutorial Group: W10
 */




public class TextBubby {

	private ArrayList<String> myText = new ArrayList<String>();;

	private static final String COMMAND_DELETE = "delete";
	private static final String COMMAND_ADD = "add";
	private static final String COMMAND_DISPLAY = "display";
	private static final String COMMAND_CLEAR = "clear";
	private static final String COMMAND_EXIT = "exit";
	private static final String COMMAND_INVALID = "Invalid command";
	private static final String COMMAND_ENTER_PROMP = "Command: ";
	private static final String DELETE_INDEX_NO_EXIST = "text you want to delete is not existed";


	public TextBubby() {
	}

	/**
	 * Process method is to process all the command typed by user.
	 * 
	 */
	public void Process(String TextFileName) throws IOException {
		BufferedWriter output = openTextFile(TextFileName);

		Scanner sc = new Scanner(System.in);

		System.out.print(COMMAND_ENTER_PROMP);

		while (sc.hasNext()) {
			String LineCommand = sc.nextLine();
			Scanner LineScanner = new Scanner(LineCommand);

			String firstCharacter = LineScanner.next();

			if (firstCharacter.equalsIgnoreCase(COMMAND_ADD)) {
				addToTextfile(LineScanner, TextFileName);
			} else if (firstCharacter.equalsIgnoreCase(COMMAND_DISPLAY)) {
				displayTextFile(TextFileName);
			} else if (firstCharacter.equalsIgnoreCase(COMMAND_DELETE)) {
				deleteFromTextFile(LineScanner, TextFileName);
			} else if (firstCharacter.equalsIgnoreCase(COMMAND_CLEAR)) {
				clearTextFile(TextFileName);
			} else if (firstCharacter.equalsIgnoreCase(COMMAND_EXIT)) {
				exitTextFile(TextFileName, output);
				break;
			} else {
				System.out.println(COMMAND_INVALID);
			}

			LineScanner.close();

			System.out.print(COMMAND_ENTER_PROMP);
		}
		sc.close();
	}


	/**
	 * openTextFile method is to take in the text file name parameter and open this file.
	 * 
	 */
	private BufferedWriter openTextFile(String TextFileName) {

		BufferedWriter output = null;

		try {
			output = new BufferedWriter(new FileWriter(TextFileName));
		} catch(IOException e) {
			System.out.println("There was a problem: " + e + ". File cannot be opened");
		}

		welcomeMessage(TextFileName);

		return output;
	}



	private void welcomeMessage(String TextFileName) {
		System.out.println("Welcome to TextBubby. " + TextFileName + " is ready for use");
	}


	/**
	 * exitTextFile method is to save all the content to the file and close the file
	 * 
	 */
	private void exitTextFile(String TextFileName, BufferedWriter output) {	
		try {
			for (int i = 0; i < myText.size(); i++) {
				output.write(myText.get(i));

				if (i != myText.size() - 1) {
					output.newLine();
				}
			}

			output.close();
		} catch (IOException e) {
			System.out.println("There was a problem: " + e);
		}

		goodbyeMessage(TextFileName);
	}


	private void goodbyeMessage(String TextFileName) {
		System.out.println(TextFileName + " has been closed. Thank you!");
	}


	/**
	 * clearTextFile method is to clear all the content in ArrayList (file).
	 * 
	 */
	private void clearTextFile(String TextFileName) {

		myText = new ArrayList<String>();

		System.out.println("all content deleted from " + TextFileName);
	}


	/**
	 * deleteFromTextFile method is to delete a line text specified by user
	 * 
	 */
	private void deleteFromTextFile(Scanner LineScanner, String TextFileName) {

		// checking whether command only contains one delete character
		if(!LineScanner.hasNext()) {
			System.out.println(COMMAND_INVALID);
			return;
		}

		String nextChar = LineScanner.next();

		// a valid delete command should follow by numeric number
		if(!isNumeric(nextChar)) {
			System.out.println(COMMAND_INVALID);
			return;
		}

		// Index store in file start with 0. Therefore, index is going to delete is 1 smaller than specified by user
		int DeleteIndex = Integer.parseInt(nextChar)-1;

		if (DeleteIndex >= myText.size()) {
			System.out.println(DELETE_INDEX_NO_EXIST);
			return;
		}

		String DeleteText = myText.get(DeleteIndex);
		myText.remove(DeleteIndex);

		System.out.println("deleted from " + TextFileName + ": \"" + DeleteText+ "\"");

	}


	/**
	 * isNumeric method is to check whether the string is a numeric number or alphabet
	 * 
	 */
	public static boolean isNumeric(String str) {
		return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
	}


	/**
	 * displayTextFile method is to print out everything inside the ArrayList (file).
	 * 
	 */
	private void displayTextFile(String TextFileName) {

		if (myText.size() ==0) {
			System.out.println(TextFileName + " is empty");
		}

		for (int i = 0; i < myText.size(); i++) {
			System.out.println((i+1) + ". " + myText.get(i));
		}
	}


	/**
	 * addToTextFile method is to add a line text to ArrayList (file).
	 * 
	 */
	private void addToTextfile(Scanner LineScanner, String TextFileName) {

		String text = "";

		//This method is to remove the first space character in the remaining line of text.
		while (LineScanner.hasNext()) {
			if (text.length() == 0) {
				text += LineScanner.next();
			} else {
				text += " " + LineScanner.next();
			}
		}

		myText.add(text);

		System.out.println("added to "+ TextFileName + ": \"" + text+ "\"");
	}


	public static void main(String[] args) throws IOException {
		// do not alter this method
		TextBubby txtbubby = new TextBubby();
		String TextFileName = args[0];
		txtbubby.Process(TextFileName);
	}

}
