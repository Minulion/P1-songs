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
public class Backend implements BackendInterface {

    public Backend(IterableSortedCollection<SongInterface> tree) {}

    /**
     * Loads data from the .csv file referenced by filename.
     * @param filename is the name of the csv file to load data from
     * @throws IOException when there is trouble finding/reading file
     */
    public void readData(String filename) throws IOException {
        try {
            Scanner sc = new Scanner(new File(filename));
            sc.nextLine(); // start from line 2
        } catch(Exception e) {
            throw new IOException();
        }
        String line;
        while (sc.hasNextLine()) {
            line = sc.nextLine();
            tree.insert(new Song(line));
        }
    }

    List<Song> gotRange = new ArrayList<Song>(); //can be modified by either getRange or filterByGenre
    List<Song> filteredByGenre = new ArrayList<Song>();
    boolean getRangeCalled = false;
    boolean filterByGenreCalled = false;



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
            for (Song song : filteredByGenre) {
                if (song.getLoudness() >= low && song.getLoudness() <= high) {
                    titles.add(song.getTitle());
                    songs.add(song);
                }
            }
            gotRange = songs;
            return titles;
        }
        for (Song song : tree) {
            if (song.getLoudness() >= low && song.getLoudness() <= high) {
                titles.add(song.getTitle());
                songs.add(song);
            }
        }
        gotRange = songs;
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
        List<String> titles = new ArrayList<String>();
        List<Song> songs = new ArrayList<Song>();
        if (!getRangeCalled) {
            return titles;
        }
        filterByGenreCalled = true;
        for (Song song : tree) {
            if (song.getGenres().contains(genre)) {
                songs.add(song);
                titles.add(song.getTitle());
            }
        }
        filteredByGenre = songs;
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
        List<Song> liveSongs = gotRange; //sort list from getRange()
        List<String> fiveLive = new ArrayList<String>();
        for (int i = 0; i < liveSongs.size(); i++) {
            for (int j = 0; j < liveSongs.size() - 1 - i; j++) {
                if (liveSongs.get(j).getLiveness() < liveSongs.get(j+1).getLiveness()) { 
                    Song tmp = liveSongs.get(j+1);
                    liveSongs.set(j+1, liveSongs.get(j));
                    liveSongs.set(j,tmp);
                }
            }
        }
        for (int i = 0; i < 5; i++) {
            try {
                fiveLive.add(liveSongs.get(i).getLiveness() + ": " + liveSongs.get(i).getTitle());
            } catch(Exception e) {
                break;
            }
        }
	    return fiveLive;
    }    
}
