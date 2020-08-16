package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataSource {
    public static final String DB_NAME = "music.db";
    public static final String CONNECTION_STRING = "jdbc:sqlite:/home/listya/Documents/Belajar/Java/Databases/" + DB_NAME;
    public static final String TABLE_ALBUMS = "albums";
    public static final String COLUMN_ALBUMS_ID = "_id";
    public static final String COLUMN_ALBUM_NAME = "name";
    public static final String COLUMN_ALBUMS_ARTIST = "artist";

    public static final String ARTIST_ID = "_id";
    public static final String TABLE_ARTIST = "artists";
    public static final String ARTIST_NAME = "name";

    public static final String TABLE_SONGS = "songs";
    public static final String SONGS_ID = "_id";
    public static final String SONGS_TRACK = "track";
    public static final String SONGS_TITLE = "title";
    public static final String SONGS_ALBUM = "album";

    private Connection conn;

    public boolean open() {
        try {
            conn = DriverManager.getConnection(CONNECTION_STRING);
            return true;
        } catch (SQLException e) {
            System.out.println("Can't connect to database");
            return false;
        }
    }

    public boolean close() {
        try {
            if (conn != null) {
                conn.close();
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Can't close database");
            return false;
        }
    }

    public List<Artist> queryArtist() {
        try (Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM " + TABLE_ARTIST + " ORDER BY " + ARTIST_NAME)) {

            List<Artist> artists = new ArrayList<>();
            while (resultSet.next()) {
                Artist artist = new Artist();
                artist.setId(resultSet.getInt(ARTIST_ID));
                artist.setName(resultSet.getString(ARTIST_NAME));
                artists.add(artist);
            }

            return artists;

        } catch (SQLException e) {
            System.out.println("Query failed " + e.getMessage());
            return null;
        }
    }
}

