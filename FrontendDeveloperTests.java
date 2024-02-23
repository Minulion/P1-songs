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
    // New TextUITester object for test
    TextUITester tester = new TextUITester("R\nsongs.csv\nQ\n");

    // calls helper method
    run();

    // stores output printed
    String output = tester.checkOutput();

    // checks the welcome message
    assertTrue(output.startsWith("Welcome to iSongify\n"));

    // checks the contents
    assertTrue(output.contains("Enter path to csv file to load:\n"));
    assertTrue(output.contains("Done reading file.\n"));

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
    // New TextUITester object for test
    TextUITester tester = new TextUITester("G\n80 - 90\nQ\n");

    // calls helper method
    run();

    // stores output printed
    String output = tester.checkOutput();

    // checks the welcome message
    assertTrue(output.startsWith("Welcome to iSongify\n"));

    // check content
    assertTrue(output.contains("Enter range of tempo values (MIN - MAX):\n"));
    assertTrue(output.contains("5 songs found between 80 - 90:"));
    assertTrue(output.contains("Hey, Soul Sister\nLove The Way You Lie\nTiK ToK\n"
        + "Bad Romance\nJust the Way You Are"));

    // checks menus
    assertNotEquals(output.indexOf("[R]ead Data\n"), output.lastIndexOf("[R]ead Data\n"));
    assertTrue(output.contains("[G]et Songs by Loudness dB [80 - 90]\n"));
    assertEquals(output.indexOf("G]et Songs by Loudness dB [80 - 90]\n"),
        output.lastIndexOf("G]et Songs by Loudness dB [80 - 90]\n"));

    // check that the program/method ends correctly
    assertTrue(output.endsWith("Thank you for using iSongify, Goodbye!\n"),
        "Command Q did not work as expected");
  }

  /**
   * Tests the frontend functionality of the setFilter method when the user inputs the command F
   */
  @Test
  void testFilter() {
    // New TextUITester object for test
    TextUITester tester = new TextUITester("F\nPop\nQ\n");

    // calls helper method
    run();

    // stores output printed
    String output = tester.checkOutput();

    // checks the welcome message
    assertTrue(output.startsWith("Welcome to iSongify\n"));

    // check content
    assertTrue(output.contains("Enter the genre to filter by:\n"));

    // check menu
    assertNotEquals(output.indexOf("[R]ead Data\n"), output.lastIndexOf("[R]ead Data\n"));

    // check that the program ends correctly
    assertTrue(output.endsWith("Thank you for using iSongify, Goodbye!\n"),
        "Command Q did not work as expected");
  }

  /**
   * Tests the frontend functionality of the topFive method when the user inputs the command D
   */
  @Test
  void testTopFive(){
    // New TextUITester object for test
    TextUITester tester = new TextUITester("D\nQ\n");

    // calls helper method
    run();

    // stores output printed
    String output = tester.checkOutput();

    // checks the welcome message
    assertTrue(output.startsWith("Welcome to iSongify\n"));

    // check content
    assertTrue(output.contains("Error: \n"));

    // check menu
    assertNotEquals(output.indexOf("[R]ead Data\n"), output.lastIndexOf("[R]ead Data\n"));

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

    // creates a tree and backend placeholder to instantiate a frontend object
    IterableSortedCollection<SongInterface> tree = new ISCPlaceholder<>();
    BackendInterface back = new BackendPlaceholder(tree);
    Frontend front = new Frontend(in, back);

    // called runCommandLoop method to check if the correct thing gets printed
    front.runCommandLoop();

    // goodbye message
    System.out.println("Thank you for using iSongify, Goodbye!");
  }
}
