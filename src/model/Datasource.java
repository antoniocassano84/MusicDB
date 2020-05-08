package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Datasource {

  public static final String DB_NAME = "music.db";
  public static final String CONNECTION_STRING = "jdbc:sqlite:C:\\Users\\anton\\IdeaProjects\\books\\Music\\src\\" +
                                                 DB_NAME;

  public static final String TABLE_ALBUMS = "albums";
  public static final String COLUMN_ALBUM_ID = "_id";
  public static final String COLUMN_ALBUM_NAME = "name";
  public static final String COLUMN_ALBUM_ARTIST = "artist";

  public static final String TABLE_ARTISTS = "artists";
  public static final String COLUMN_ARTIST_ID = "_id";
  public static final String COLUMN_ARTIST_NAME = "name";

  public static final String TABLE_SONGS = "songs";
  public static final String COLUMN_SONG_TRACK = "track";
  public static final String COLUMN_SONG_TITLE = "title";
  public static final String COLUMN_SONG_ALBUM = "album";

  private Connection conn;

  public boolean open() {
    try {
      conn = DriverManager.getConnection(CONNECTION_STRING);
      System.out.println("Connection established!");
      return true;
    } catch (SQLException e) {
      System.out.println("Couldn't connect to DB.");
      return false;
    }
  }

  public void close() {
    try {
      if(conn != null) conn.close();
    } catch(SQLException e) {
      System.out.println("Couldn't close connection " + e.getMessage());
    }
  }

  public List<Artist> queryArtist() {
    try (Statement statement = conn.createStatement();
         ResultSet results = statement.executeQuery("SELECT * FROM " + TABLE_ARTISTS)) {
      List<Artist> artists = new ArrayList<>();
      while (results.next())
        artists.add(new Artist(results.getInt(COLUMN_ARTIST_ID), results.getString(COLUMN_ARTIST_NAME)));
      return artists;
    } catch (SQLException e) { return null; }
  }

}