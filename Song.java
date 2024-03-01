public class Song extends Comparable<Song> implements SongInterface {

    public Song(String data) { //constructor for Song class
        List<String> words = new ArrayList<String>();
        String word;
        int qCount = 0;
        boolean doubleQ = false;
        for (int i = 0; i < data.length(); i++) {
            if (i == data.length - 1) { //if last char in string
                word.add(data.charAt(i));
                words.add(word);
                break;
            }
            if (data.charAt(i) == '"') {
                if (doubleQ) { //two quotes in a row
                    word.add(data.charAt(i));
                    doubleQ = false;
                } else {
                    doubleQ = true;
                }
                qCount++;
                continue;
            }
            if (qCount%2 == 0) { // if even num of quotations
                if (data.charAt(i) == ',') {
                    words.add(word);
                } else {
                    word.add(data.charAt(i));
                }
            } else { // odd num of quotations
                word.add(data.charAt(i));
            }
            doubleQ = false;
        }
        // if (words.size() != 9) {
        //     throw new IOException();
        // }

        title = words[0];
        artist = words[1];
        genres = words[2];
        year = (int)words[3];
        BPM = (int)words[4];
        energy = (int)words[5];
        danceability = (int)words[6];
        loudness = (int)words[7];
        liveness = (int)words[8];
    }

    private String title;
    private String artist;
    private String genres;
    private int year;
    private int BPM;
    private int energy;
    private int danceability;
    private int loudness;
    private int liveness;

    public String getTitle() {
        return title;
    } // returns this song's title

    public String getArtist() {
        return artist;
    } // returns this song's artist

    public String getGenres() {
        return genres;
    } // returns string containing each of this song's genres

    public int getYear() {
        return year;
    } // returns this song's year in the Billboard
    
    public int getBPM() {
        return BPM;
    } // returns this song's speed/tempo in beats per minute
    
    public int getEnergy() {
        return energy;
    } // returns this song's energy rating 
    
    public int getDanceability() {
        return danceability;
    } // returns this song's danceability rating
    
    public int getLoudness() {
        return loudness;
    } // returns this song's loudness in dB
    
    public int getLiveness() {
        return liveness;
    } // returns this song's liveness rating
    
}