import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.io.IOException;

public class BackendDeveloperTests {

    /**
     * Tests whether or not readDate() throws an exception for an invalid file.
     */
    @Test
    public void testReadDataException() {
        IterableSortedCollection<SongInterface> tree = new ISCPlaceholder<>(); 
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
        IterableSortedCollection<SongInterface> tree = new ISCPlaceholder<>(); 
	    BackendInterface backend = new Backend(tree);
        Assertions.assertTrue(backend.getRange(2,1).size() == 0);
    }

    /**
     * Tests if filterByGenre() returns 0 songs for an invalid genre.
     */
    @Test
    public void testFakeGenre() {
        IterableSortedCollection<SongInterface> tree = new ISCPlaceholder<>(); 
	    BackendInterface backend = new Backend(tree);
        Assertions.assertTrue(backend.filterByGenre("fakepoop").size() == 0);
    }

    /**
     * Tests if fiveMostLive() throws an exception when getRange() is not yet called.
     */
    @Test
    public void testGetRangeNotCalled() {
        IterableSortedCollection<SongInterface> tree = new ISCPlaceholder<>(); 
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
        IterableSortedCollection<SongInterface> tree = new ISCPlaceholder<>(); 
	    BackendInterface backend = new Backend(tree);
        backend.getRange(-999,999);
        Assertions.assertTrue(backend.fiveMostLive().size() < 6);
    }
}