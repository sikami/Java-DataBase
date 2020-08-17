import model.Artist;
import model.DataSource;
import model.SongArtist;

import javax.xml.crypto.Data;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        DataSource dataSource = new DataSource();
        if(!dataSource.open()) {
            System.out.println("Can't open datasource");
            return;
        }
        List<Artist> artistList = dataSource.queryArtist(DataSource.ORDER_BY_ASC);
        if(artistList == null) {
            System.out.println("No artist");
            return;
        }

        List<String> albumForArtist = dataSource.queryAlbumsForArtist("Pink Floyd", DataSource.ORDER_BY_DESC);
        for(String i : albumForArtist) {
            System.out.println(i);
        }

        List<SongArtist> result = dataSource.queryArtistForSong("She's On Fire", DataSource.ORDER_BY_ASC);
        if(result == null) {
            System.out.println("Couldn;t find the artist song ");
            return;
        }
        for(SongArtist i: result) {
            System.out.println(i.getArtistName() + ": " + i.getAlbumName() + ", track: " + i.getTrack());
        }

        dataSource.querySongMetaData();
        int count = dataSource.getCount(DataSource.TABLE_SONGS);
        System.out.println("songs is: " + count);

        dataSource.insertSong("Touch of Grey", "Grateful Dead", "in the dark", 1);
        dataSource.close();
    }
}
