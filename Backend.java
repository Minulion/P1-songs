import java.util.List;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;

/**
 * BackendPlaceholder - CS400 Project 1: iSongify
 *
 * This class doesn't really work.  The methods are hardcoded to output values
 * as placeholders throughout development.  It demonstrates the architecture
 * of the Backend class that will be implemented in a later week.
 */
public class BackendPlaceholder implements BackendInterface {

    public BackendPlaceholder(IterableSortedCollection<SongInterface> tree) {}

    /**
     * Loads data from the .csv file referenced by filename.
     * @param filename is the name of the csv file to load data from
     * @throws IOException when there is trouble finding/reading file
     */
    public void readData(String filename) throws IOException {
        Scanner sc = new Scanner(new File(filename));
        sc.nextLine(); // start from line 2
        String line;
        while (sc.hasNextLine()) {
            line = sc.nextLine();
            tree.insert(new Song(line));
        }
    }

    List<Song> current = new ArrayList<Song>(); //can be modified by either getRange or filterByGenre
    boolean getRangeCalled = false;
    boolean filterByGenreCalled = false;
    List<Integer> range = new ArrayList<Integer>();



    /**
     * Retrieves a list of song titles for songs that have a Loudness
     * within the specified range (sorted by Loudness in ascending order).  If 
     * a genre filter has been set using filterByGenre(), then only songs
     * in that genre should be included in the list that is returned by this 
     * method.
     *
     * Note that either this loudness range, or the resulting unfiltered list
     * of songs should be saved for later use by the other methods defined in 
     * this class.
     *
     * @param low is the minimum Loudness of songs in the returned list
     * @param hight is the maximum Loudness of songs in the returned list
     * @return List of titles for all songs in specified range 
     */
    public List<String> getRange(int low, int high) {
        getRangeCalled = true;
        List<String> titles = new ArrayList<String>();
        List<Song> songs = new ArrayList<Song>();
        if (filterByGenreCalled) {
            for (Song song : current) {
                if (song.getLoudness >= low && song.getLoudness <= high) {
                    titles.add(song.getTitle());
                    songs.add(song);
                }
            }
            current = songs;
            range = new ArrayList<Integer>();
            range.add(low);
            range.add(high);
            return titles;
        }
        for (Song song : tree) {
            if (song.getLoudness >= low && song.getLoudness <= high) {
                titles.add(song.getTitle());
                songs.add(song);
            }
        }
        current = songs;
        range = new ArrayList<Integer>();
        range.add(low);
        range.add(high);
	    return titles;
    }

    /**
     * Filters the list of songs returned by future calls of getRange() and 
     * fiveMostLive() to only include songs in one genre.  If getRange() was 
     * previously called, then this method will return a list of song titles
     * (sorted in ascending order by Loudness) that only includes songs in this
     * genre.  If getRange() was not previously called, then this method 
     * should return an empty list.
     *
     * Note that this genre threshold should be saved for later use by the
     * other methods defined in this class.
     *
     * @param genre is the genre of songs that should be returned
     * @return List of song titles, empty if getRange was not previously called
     */
    public List<String> filterByGenre(String genre) {
        filterByGenreCalled = true;
        List<String> titles = new ArrayList<String>();
        List<Song> songs = new ArrayList<Song>();
        if (!getRangeCalled) {
            return titles;
        }
        for (Song song : tree) {
            if (song.getGenres().contains(genre)) {
                songs.add(song);
                titles.add(song.getTitle());
            }
        }
        current = songs;
        return titles;
    }



    /**
     * This method makes use of the attribute range specified by the most
     * recent call to getRange().  If a genre filter has been set by
     * filterByGenre() then that will also be utilized by this method.
     * Of those songs that match these criteria, the five most live will be 
     * returned by this method as a List of Strings in increasing order of 
     * loudness.  Each string contains the liveness followed by a colon,
     * a space, and then the song's title.
     * If fewer than five such songs exist, display all of them.
     *
     * @return List of five most live song titles in order of loudness
     * @throws IllegalStateException when getRange() was not previously called.
     */
    public List<String> fiveMostLive() {
        if (!getRangeCalled) {
            throw new IllegalStateException();
        }
        this.getRange(range[0], range[1]);
        for (Song song : current) {

        }
	return java.util.Arrays.asList(new String[]{
		"8: Hey, Soul Sister",
		"52: Love The Way You Lie"
	    });	
    }    
}
