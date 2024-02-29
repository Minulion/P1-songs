import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Scanner;

public class FrontendDeveloperTests {
  /**
   * Tests that the main menu displays correctly and entering the command Q successfully closes
   * the app with the corresponding messages
   */
  @Test
  void testMenu() {
    // New TextUITester object for test
    TextUITester tester = new TextUITester("Q\n");

    // calls helper method
    run();

    // stores the output printed
    String output = tester.checkOutput();

    // checks if output starts with the welcome message
    assertTrue(output.startsWith("Welcome to iSongify\n"), "Welcome message incorrect");

    // checks the contents
    assertTrue(output.contains("[R]ead Data\n"));
    assertTrue(output.contains("[D]isplay Five Most Live\n"));
    assertTrue(output.contains("[F]ilter By Genre (none)\n"));
    assertTrue(output.contains("[G]et Songs by Loudness dB [min - max]\n"));
    assertTrue(output.contains("[Q]uit\n"));

    // check that the menu is only printed once
    assertEquals(output.indexOf("[R]ead Data\n"), output.lastIndexOf("[R]ead Data\n"));

    // check the goodbye message gets printed (method ended)
    assertTrue(output.endsWith("Thank you for using iSongify, Goodbye!\n"),
        "Command Q did not work as expected");
  }

  /**
   * Tests the frontend functionality of the readFile method when the user inputs the command R
   */
  @Test
  void testReadFile() {
    // Case 1: file is found
    // New TextUITester object for test
    TextUITester tester = new TextUITester("R\nsongs.csv\nQ\n");

    // calls helper method
    run();

    // stores output printed
    String output = tester.checkOutput();

    // checks the welcome message
    assertTrue(output.startsWith("Welcome to iSongify\n"));

    // checks the contents
    assertTrue(output.contains("Enter path to csv file to load: "));
    assertTrue(output.contains("Done reading file.\n"));

    // check that the menu gets printed more than once
    assertNotEquals(output.indexOf("[R]ead Data\n"), output.lastIndexOf("[R]ead Data\n"));

    // check that the program/method ends correctly
    assertTrue(output.endsWith("Thank you for using iSongify, Goodbye!\n"),
        "Command Q did not work as expected");

    // ---------------------------------------------------------------------------------------
    // Case 2: file not found
    // New TextUITester object for test
    tester = new TextUITester("R\nsomefile.csv\nQ\n");

    // calls helper method
    run();

    // stores output printed
    output = tester.checkOutput();

    // checks the welcome message
    assertTrue(output.startsWith("Welcome to iSongify\n"));

    // checks the contents
    assertTrue(output.contains("Enter path to csv file to load: "));
    assertTrue(output.contains("Could not find file somefile.csv\n"));

    // check that the menu gets printed more than once
    assertNotEquals(output.indexOf("[R]ead Data\n"), output.lastIndexOf("[R]ead Data\n"));

    // check that the program/method ends correctly
    assertTrue(output.endsWith("Thank you for using iSongify, Goodbye!\n"),
        "Command Q did not work as expected");
  }

  /**
   * Tests the frontend functionality of the getValues method when the user inputs the command G
   */
  @Test
  void testGetValues() {
    // Case 1: valid values
    // New TextUITester object for test
    TextUITester tester = new TextUITester("G\n80 - 90\nQ\n");

    // calls helper method
    run();

    // stores output printed
    String output = tester.checkOutput();

    // checks the welcome message
    assertTrue(output.startsWith("Welcome to iSongify\n"));

    // check content
    assertTrue(output.contains("Enter range of values (MIN - MAX): "));
    assertTrue(output.contains("5 songs found between 80 - 90:\n"));
    assertTrue(output.contains("""
        Hey, Soul Sister
        Love The Way You Lie
        TiK ToK
        Bad Romance
        Just the Way You Are
        """));

    // checks menus
    assertNotEquals(output.indexOf("[R]ead Data\n"), output.lastIndexOf("[R]ead Data\n"));
    assertTrue(output.contains("[G]et Songs by Loudness dB [80 - 90]\n"));
    assertEquals(output.indexOf("G]et Songs by Loudness dB [80 - 90]\n"),
        output.lastIndexOf("G]et Songs by Loudness dB [80 - 90]\n"));

    // check that the program/method ends correctly
    assertTrue(output.endsWith("Thank you for using iSongify, Goodbye!\n"),
        "Command Q did not work as expected");

    // ----------------------------------------------------------------------------------------
    // Case 2: invalid values
    // New TextUITester object for test
    tester = new TextUITester("G\neight - 90\nQ\n");

    // calls helper method
    run();

    // stores output printed
    output = tester.checkOutput();

    // checks the welcome message
    assertTrue(output.startsWith("Welcome to iSongify\n"));

    // check content
    assertTrue(output.contains("Enter range of values (MIN - MAX): "));
    assertTrue(output.contains("Min and Max are not in integer format." +
        "Please enter you values in this format: 10 - 20"));

    // check that the program/method ends correctly
    assertTrue(output.endsWith("Thank you for using iSongify, Goodbye!\n"),
        "Command Q did not work as expected");
  }

  /**
   * Tests the frontend functionality of the setFilter method when the user inputs the command F
   */
  @Test
  void testFilter() {
    // Case 1: get range not called, empty list (0 songs found)
    // New TextUITester object for test
    TextUITester tester = new TextUITester("F\npop\nQ\n");

    // calls helper method
    run();

    // stores output printed
    String output = tester.checkOutput();

    // checks the welcome message
    assertTrue(output.startsWith("Welcome to iSongify\n"));

    // check content
    assertTrue(output.contains("Enter genre: "));
    assertTrue(output.contains("0 songs found between min - max in genre pop:\n"));

    // check menu
    assertNotEquals(output.indexOf("[R]ead Data\n"), output.lastIndexOf("[R]ead Data\n"));
    assertTrue(output.contains("[F]ilter By Genre (pop)\n"));
    assertEquals(output.indexOf("[F]ilter By Genre (pop)"),
        output.lastIndexOf("[F]ilter By Genre (pop)"));

    // check that the program ends correctly
    assertTrue(output.endsWith("Thank you for using iSongify, Goodbye!\n"),
        "Command Q did not work as expected");

    // ----------------------------------------------------------------------------------------
    // Case 2: get range gets called, function as expected
    // New TextUITester object for test
    tester = new TextUITester("G\n80 - 90\nF\npop\nQ\n");

    // calls helper method
    run();

    // stores output printed
    output = tester.checkOutput();

    // checks the welcome message
    assertTrue(output.startsWith("Welcome to iSongify\n"));

    // check content
    assertTrue(output.contains("Enter genre: "));
    assertTrue(output.contains("2 songs found between 80 - 90 in genre pop:\n"));
    assertTrue(output.contains("Hey, Soul Sister\nLove The Way You Lie\n"));

    // check menu
    assertTrue(output.contains("[F]ilter By Genre (pop)\n"));
    assertEquals(output.indexOf("[F]ilter By Genre (pop)"),
        output.lastIndexOf("[F]ilter By Genre (pop)"));

    // check that the program/method ends correctly
    assertTrue(output.endsWith("Thank you for using iSongify, Goodbye!\n"),
        "Command Q did not work as expected");
  }

  /**
   * Tests the frontend functionality of the topFive method when the user inputs the command D
   */
  @Test
  void testTopFive(){
    // Case 1: get range not called, give error message
    // New TextUITester object for test
    TextUITester tester = new TextUITester("D\nQ\n");

    // calls helper method
    run();

    // stores output printed
    String output = tester.checkOutput();

    // checks the welcome message
    assertTrue(output.startsWith("Welcome to iSongify\n"));

    // check content
    assertTrue(output.contains("[G]et Songs by Loudness command needs " +
        "to be called before using this!"));

    // check menu
    assertNotEquals(output.indexOf("[R]ead Data\n"), output.lastIndexOf("[R]ead Data\n"));

    // check that the program ends correctly
    assertTrue(output.endsWith("Thank you for using iSongify, Goodbye!\n"),
        "Command Q did not work as expected");

    // ----------------------------------------------------------------------------------------
    // Case 2: get range called
    // New TextUITester object for test
    tester = new TextUITester("G\n80 - 90\nF\npop\nD\nQ\n");

    // calls helper method
    run();

    // stores output printed
    output = tester.checkOutput();

    // checks the welcome message
    assertTrue(output.startsWith("Welcome to iSongify\n"));

    // check content
    assertTrue(output.contains("Top five songs found between 80 - 90 in genre pop:"));
    assertTrue(output.contains("8: Hey, Soul Sister\n52: Love The Way You Lie\n"));

    // check that the program ends correctly
    assertTrue(output.endsWith("Thank you for using iSongify, Goodbye!\n"),
        "Command Q did not work as expected");
  }

  /**
   * Helper method that creates a scanner and tests each specific functionality
   * It prints out a welcome message, then creates a frontend object to use the runCommandLoop
   * method
   */
  public static void run() {
    // new scanner
    Scanner in = new Scanner(System.in);

    // welcome message
    System.out.println("Welcome to iSongify");
    System.out.println("===================");

    // creates a tree and backend placeholder to instantiate a frontend object
    IterableSortedCollection<SongInterface> tree = new ISCPlaceholder<>();
    BackendInterface back = new BackendPlaceholder(tree);
    Frontend front = new Frontend(in, back);

    // called runCommandLoop method to check if the correct thing gets printed
    front.runCommandLoop();

    // goodbye message
    System.out.println();
    System.out.println("===================");
    System.out.println("Thank you for using iSongify, Goodbye!");
  }
}
