import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Frontend implements FrontendInterface{
  private String min = "min";
  private String max = "max";
  private String genre = "none";
  private Scanner scanner;
  private BackendInterface backend;

  public Frontend(Scanner in, BackendInterface backend){
    scanner = in;
    this.backend = backend;
  }

  /**
   * Repeated gives the user an opportunity to issue new commands until
   * they select Q to quit.
   */
  public void runCommandLoop() {
    // string to store user input
    String command = "";

    // keep printing the menu until user enters Q
    do {
      displayMainMenu();
      // update user command
      if (scanner.hasNext()) {
        command = scanner.nextLine();
      }
      switch(command){
        // command is R, call readFile
        case "R":
          readFile();
          break;

        // command is D call topFive
        case "D":
          topFive();
          break;

        // F, set the filter
        case "F":
          setFilter();
          break;

        // G, get songs by loudness
        case "G":
          getValues();
          break;
      }
    }
    while (!command.equals("Q"));
  }

  /**
   * Displays the menu of command options to the user.
   */
  public void displayMainMenu() {
    // main menu
    String menu = """
	    ~~~ Command Menu ~~~
	        [R]ead Data
	        [G]et Songs by Loudness dB [min - max]
	        [F]ilter By Genre (none)
	        [D]isplay Five Most Live
	        [Q]uit
	    Choose command:""";
    // replaces the range and genre when corresponding commands are inputted
    menu=menu.replace("min",min).replace("max",max).replace("none",genre);
    System.out.print(menu + " ");
  }

  /**
   * Provides text-based user interface and error handling for the
   * [R]ead Data command.
   */
  public void readFile() {
    // ask user to select a file
    System.out.print("Enter path to csv file to load: ");

    // stores user input
    String input = scanner.nextLine();

    // reads data in file, throws exception when file isn't found
    try{
      backend.readData(input);
      // tell user it succeeded
      System.out.println("Done reading file.");
    } catch (IOException e){
      System.out.println("Could not find file " + input);
    }
  }

  /**
   * Provides text-based user interface and error handling for the
   * [G]et Songs by Loudness command.
   */
  public void getValues() {
    // variables to store min and max
    int min = -1;
    int max = -1;

    // ask user to enter range they want to search by
    System.out.print("Enter range of values (MIN - MAX): ");

    // store use input
    String input = scanner.nextLine();
    String[] parts = input.split(" - "); // split min and max at -

    // store the input in variables, throw exception if the user input is not in number format
    try{
      min = Integer.parseInt(parts[0].strip());
      max = Integer.parseInt(parts[1].strip());

      // store min and max into private variables to change the menu
      this.min = parts[0].strip();
      this.max = parts[1].strip();

      // call backend method and store result
      List<String> result = backend.getRange(min, max);
      int size = result.size();

      // order the list into a message to print out
      String message = "";
      for (String s : result) {
        message += s + "\n";
      }

      // print the results for user
      System.out.println(size + " songs found between " + min + " - " + max + ":");
      System.out.println(message);

    } catch(NumberFormatException e){
      System.out.println("Min and Max are not in integer format." +
          "Please enter you values in this format: 10 - 20");
    }
  }

  /**
   * Provides text-based user interface and error handling for the
   * [F]ilter By Genre command.
   */
  public void setFilter() {
    // ask user for genre
    System.out.print("Enter genre: ");

    // store user input
    genre = scanner.nextLine();

    // use backend method to search for results
    List<String> result = backend.filterByGenre(genre);
    int size = result.size();

    if (size == 0) {
      genre = "none";
    }

    // order results into a String message
    String message = "";
    for (String s : result) {
      message += s + "\n";
    }

    // print out results for user to see
    System.out.println(size + " songs found between " + min + " - " + max
        + " in genre " + genre + ":");
    System.out.println(message);
  }

  /**
   * Provides text-based user interface and error handling for the
   * [D]isplay Five Most Live command.
   */
  public void topFive() {
    // empty list to store result
    List<String> result = new ArrayList<>();

    // call backend method to get top five
    // throw exception if user has not yet set the range for loudness
    try {
      result = backend.fiveMostLive();

      // order results into a String message
      String message = "";
      for (String s : result) {
        message += s + "\n";
      }

      // print out results for user to see
      System.out.println("Top five songs found between " + min + " - " + max
          + " in genre " + genre + ":");
      System.out.println(message);

    } catch(IllegalStateException e){
      System.out.println("[G]et Songs by Loudness command needs to be called before using this!");
    }
  }
}
