import java.util.Scanner;

public class Frontend implements FrontendInterface{
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
    String command = null;
    do {
      displayMainMenu();
      command = scanner.nextLine();
      switch(command){
        case "R":
          readFile();
          break;

        case "D":
          topFive();
          break;

        case "F":
          setFilter();
          break;

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
    String menu = """
	    ~~~ Command Menu ~~~
	        [R]ead Data
	        [G]et Songs by Loudness dB [min - max]
	        [F]ilter By Genre (none)
	        [D]isplay Five Most Live
	        [Q]uit
	    Choose command:""";
    System.out.print(menu + " ");
  }

  /**
   * Provides text-based user interface and error handling for the
   * [R]ead Data command.
   */
  public void readFile() {

  }

  /**
   * Provides text-based user interface and error handling for the
   * [G]et Songs by Loudness command.
   */
  public void getValues() {

  }

  /**
   * Provides text-based user interface and error handling for the
   * [F]ilter By Genre command.
   */
  public void setFilter() {

  }

  /**
   * Provides text-based user interface and error handling for the
   * [D]isplay Five Most Live command.
   */
  public void topFive() {

  }
}
