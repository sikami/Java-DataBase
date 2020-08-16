import model.Artist;
import model.DataSource;

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

        List<String> albumForArtist = dataSource.queryAlbumsForArtist("Iron Maiden", DataSource.ORDER_BY_ASC);
        for(String i : albumForArtist) {
            System.out.println(i);
        }

        dataSource.close();
    }
}
