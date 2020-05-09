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
  public static final int INDEX_ALBUM_ID = 1;
  public static final int INDEX_ALBUM_NAME = 2;
  public static final int INDEX_ALBUM_ARTIST = 3;

  public static final String TABLE_ARTISTS = "artists";
  public static final String COLUMN_ARTIST_ID = "_id";
  public static final String COLUMN_ARTIST_NAME = "name";
  public static final int INDEX_ARTIST_ID = 1;
  public static final int INDEX_ARTIST_NAME = 2;

  public static final String TABLE_SONGS = "songs";
  public static final String COLUMN_SONG_ID = "_id";
  public static final String COLUMN_SONG_TRACK = "track";
  public static final String COLUMN_SONG_TITLE = "title";
  public static final String COLUMN_SONG_ALBUM = "album";
  public static final int INDEX_SONG_ID = 1;
  public static final int INDEX_SONG_TRACK = 1;
  public static final int INDEX_SONG_TITLE = 2;
  public static final int INDEX_SONG_ALBUM = 3;

  public static final int ORDER_BY_NONE = 1;
  public static final int ORDER_BY_ASC = 2;
  public static final int ORDER_BY_DESC = 3;

  public static final String QUERY_ALBUMS_BY_ARTIST_START = "SELECT " + TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME +
                                                        " FROM " + TABLE_ALBUMS + " INNER JOIN " + TABLE_ARTISTS +
                                                        " ON " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ARTIST + " = " +
                                                        TABLE_ARTISTS + "." + COLUMN_ARTIST_ID +
                                                        " WHERE " + TABLE_ARTISTS + "." + COLUMN_ARTIST_NAME + " = \"";
  public static final String QUERY_ALBUMS_BY_ARTIST_SORT = " ORDER BY " + TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME +
                                                           " COLLATE NOCASE ";
//  SELECT artists.name, albums.name, songs.track, songs.title
//  FROM albums
//  INNER JOIN songs ON albums._id = songs.album
//  INNER JOIN artists ON albums.artist = artists._id
//  WHERE songs.title = 'Go Your Own Way'
//  ORDER BY artists.name, albums.name COLLATE NOCASE ASC;
  public static final String QUERY_ARTIST_FOR_SONG_START = "SELECT " +
                                                             TABLE_ARTISTS + "." + COLUMN_ARTIST_NAME + ", " +
                                                             TABLE_ALBUMS  + "." + COLUMN_ALBUM_NAME  + ", " +
                                                             TABLE_SONGS   + "." + COLUMN_SONG_TRACK  + ", " +
                                                             TABLE_SONGS   + "." + COLUMN_SONG_TITLE  +
                                                           " FROM " +
                                                             TABLE_ALBUMS +
                                                             " INNER JOIN " + TABLE_SONGS +
                                                             " ON " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ID + " = " +
                                                                      TABLE_SONGS  + "." + COLUMN_SONG_ALBUM +
                                                             " INNER JOIN " + TABLE_ARTISTS +
                                                             " ON " + TABLE_ALBUMS  + "." + COLUMN_ALBUM_ARTIST + " = " +
                                                                      TABLE_ARTISTS + "." + COLUMN_ARTIST_ID    +
                                                           " WHERE " +
                                                             TABLE_SONGS + "." + COLUMN_SONG_TITLE + " = \'";
  public static final String QUERY_ARTIST_FOR_SONG_SORT = " ORDER BY " +
                                                            TABLE_ARTISTS + "." + COLUMN_ARTIST_NAME + ", " +
                                                            TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME +
                                                          " COLLATE NOCASE ";

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

  public List<Artist> queryArtist(int sortOrder) {

    StringBuilder sb = new StringBuilder("SELECT * FROM ");
    sb.append(TABLE_ARTISTS);
    if(sortOrder != ORDER_BY_NONE) {
      sb.append(" ORDER BY ");
      sb.append(COLUMN_ARTIST_NAME);
      sb.append(" COLLATE NOCASE ");
      sb.append(sortOrder == ORDER_BY_DESC ? "DESC" : "ASC");
    }

    try (Statement statement = conn.createStatement();
         ResultSet results = statement.executeQuery(sb.toString())) {
      List<Artist> artists = new ArrayList<>();
      while (results.next())
        artists.add(new Artist(results.getInt(INDEX_ARTIST_ID), results.getString(INDEX_ARTIST_NAME)));
      return artists;
    } catch (SQLException e) { return null; }
  }

  public List<String> queryAlbumsForArtist(String artistName, int sortOrder) {
    StringBuilder sb = new StringBuilder(QUERY_ALBUMS_BY_ARTIST_START);
    sb.append(artistName);
    sb.append("\"");

    if(sortOrder != ORDER_BY_NONE) {
      sb.append(QUERY_ALBUMS_BY_ARTIST_SORT);
      sb.append(sortOrder == ORDER_BY_DESC ? "DESC" : "ASC");
      }

      System.out.println("SQL statement: " + sb.toString());

      try (Statement statement = conn.createStatement();
           ResultSet results = statement.executeQuery(sb.toString())) {
        List<String> albums = new ArrayList<>();
        while (results.next())
          albums.add(results.getString(1));
        return albums;
      } catch (SQLException e) { return null; }
  }

  public List<SongArtist> queryArtistsForSong(String songName, int sortOrder) {
    StringBuilder sb  = new StringBuilder(QUERY_ARTIST_FOR_SONG_START);
    sb.append(songName);
    sb.append("\'");

    if(sortOrder != ORDER_BY_NONE) {
      sb.append(QUERY_ARTIST_FOR_SONG_SORT);
      sb.append(sortOrder == ORDER_BY_DESC ? "DESC" : "ASC");
    }

    System.out.println("SQL statement: " + sb.toString());

    try (Statement statement = conn.createStatement();
         ResultSet results = statement.executeQuery(sb.toString())) {
      List<SongArtist> songArtist = new ArrayList<>();
      while (results.next())
        songArtist.add(new SongArtist(results.getString(1),
                                      results.getString(2),
                                      results.getInt(3)));
      return songArtist;
    } catch (SQLException e) { return null; }
  }

}
