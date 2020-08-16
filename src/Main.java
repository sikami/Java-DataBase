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

        for(Artist i : artistList) {
            System.out.println(i.getId() + ", Name: " + i.getName());
        }

        dataSource.close();
    }
}
