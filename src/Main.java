import model.Artist;
import model.Datasource;
import model.SongArtist;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        Datasource datasource = new Datasource();

        if(!datasource.open()) {
            System.out.println("Can't open Datasource");
            return;
        }

        List<Artist> artists = datasource.queryArtist(Datasource.ORDER_BY_ASC);
        if (artists == null) System.out.println("No artist");
        else for(Artist a : artists) System.out.println(a);

        System.out.println();

        List<String> albumsIronMaiden = datasource.queryAlbumsForArtist("Iron Maiden",
                                                                         Datasource.ORDER_BY_ASC);
        if (albumsIronMaiden == null) System.out.println("No album");
        else for(String a : albumsIronMaiden) System.out.println(a);

        System.out.println();

        List<SongArtist> songAlbum = datasource.queryArtistsForSong("Go Your Own Way",
                                                                      Datasource.ORDER_BY_ASC);
        if (songAlbum == null) System.out.println("No info");
        else for(SongArtist a : songAlbum) System.out.println(a);

        datasource.close();
    }
}
