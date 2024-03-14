import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BackendDeveloperTests {

    /**
     * Tests whether or not readDate() throws an exception for an invalid file.
     */
    @Test
    public void testReadDataException() {
        IterableSortedCollection<SongInterface> tree = new IterableRedBlackTree<>(); 
	    BackendInterface backend = new Backend(tree);
        try {
            backend.readData("fakeName.file");
        } catch (IOException e) {
            //exception thrown for fake file
        }
    }

    /**
     * Tests if getRange() returns 0 songs when the input is invalid (min > max).
     */
    @Test
    public void testGetRangeBadInput() {
        IterableSortedCollection<SongInterface> tree = new IterableRedBlackTree<>(); 
	    BackendInterface backend = new Backend(tree);
        Assertions.assertTrue(backend.getRange(2,1).size() == 0);
    }

    /**
     * Tests if filterByGenre() returns 0 songs for an invalid genre.
     */
    @Test
    public void testFakeGenre() {
        IterableSortedCollection<SongInterface> tree = new IterableRedBlackTree<>(); 
	    BackendInterface backend = new Backend(tree);
        Assertions.assertTrue(backend.filterByGenre("fakepoop").size() == 0);
    }

    /**
     * Tests if fiveMostLive() throws an exception when getRange() is not yet called.
     */
    @Test
    public void testGetRangeNotCalled() {
        IterableSortedCollection<SongInterface> tree = new IterableRedBlackTree<>(); 
	    BackendInterface backend = new Backend(tree);
        try {
            backend.fiveMostLive();
            Assertions.fail("no exception thrown");
        } catch (IllegalStateException e) {
            //exception thrown when getRange() not previously called
        }
    }

    /**
     * Tests if fiveMostLive() always returns no more than 5 songs.
     */
    @Test
    public void testNoMoreThanFive() {
        IterableSortedCollection<SongInterface> tree = new IterableRedBlackTree<>(); 
	    BackendInterface backend = new Backend(tree);
        backend.getRange(-999,999);
        Assertions.assertTrue(backend.fiveMostLive().size() < 6);
    }

    /**
     * Tests to see if output of frontend matches backend when getRange() is called.
     * Also checks if method returns list of values (non-empty).
     */
    @Test 
    public void testIntegrationRange() {
        TextUITester tester = new TextUITester("R\nsongs.csv\nG\n80 - 90\nF\npop\nD\nQ\n");
        Scanner in = new Scanner(System.in);

        IterableSortedCollection<SongInterface> tree = new IterableRedBlackTree<>();
        BackendInterface back = new Backend(tree);
        Frontend front = new Frontend(in, back);

        front.runCommandLoop();
        String output = tester.checkOutput();

        List<String> result = back.getRange(50, 100);

        for (String s : result) {
            Assertions.assertTrue(output.contains(s));
        }
    }

    /**
     * Tests to see if output of frontend matches backend when filterByGenre() is called.
     * Also checks if method returns list of values (non-empty).
     */
    @Test 
    public void testIntegrationFilter() {
        TextUITester tester = new TextUITester("R\nsongs.csv\nG\n80 - 90\nF\npop\nD\nQ\n");
        Scanner in = new Scanner(System.in);

        IterableSortedCollection<SongInterface> tree = new IterableRedBlackTree<>();
        BackendInterface back = new Backend(tree);
        Frontend front = new Frontend(in, back);

        front.runCommandLoop();
        String output = tester.checkOutput();

        List<String> result = back.filterByGenre("pop");

        for (String s : result) {
            Assertions.assertTrue(output.contains(s));
        }
    }

    /**
     * 
     */
    @Test 
    public void testPartnerTopFive() {
        TextUITester tester = new TextUITester("");
        Scanner in = new Scanner(System.in);

        IterableSortedCollection<SongInterface> tree = new IterableRedBlackTree<>();
        BackendInterface back = new Backend(tree);
        Frontend front = new Frontend(in, back);

        front.topFive();
        String output = tester.checkOutput();

        Assertions.assertTrue(output.contains("[G]et Songs by Loudness command needs to be called before using this!"));
    }

    /**
     * 
     */
    @Test 
    public void testPartnerReadFile() {
        TextUITester tester = new TextUITester("");
        Scanner in = new Scanner(System.in);

        IterableSortedCollection<SongInterface> tree = new IterableRedBlackTree<>();
        BackendInterface back = new Backend(tree);
        Frontend front = new Frontend(in, back);

        front.readFile();
        String output = tester.checkOutput();

        Assertions.assertTrue(output.contains("Could not find file "));
    }


}