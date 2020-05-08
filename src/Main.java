import model.Artist;
import model.Datasource;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        Datasource datasource = new Datasource();

        if(!datasource.open()) {
            System.out.println("Can't open Datasource");
            return;
        }

        List<Artist> artists = datasource.queryArtist();
        if (artists == null) System.out.println("No artist");
        else for(Artist a : artists) System.out.println(a);

        datasource.close();
    }
}
