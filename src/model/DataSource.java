package model;

import javax.swing.plaf.nimbus.State;
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
    public static final int INDEX_ALBUM_ID = 1;
    public static final int INDEX_ALBUM_NAME = 2;
    public static final int INDEX_ALBUM_ARTIST = 3;

    public static final String ARTIST_ID = "_id";
    public static final String TABLE_ARTIST = "artists";
    public static final String ARTIST_NAME = "name";
    public static final int INDEX_ARTIST_ID = 1;
    public static final int INDEX_ARTIST_NAME = 2;

    public static final String TABLE_SONGS = "songs";
    public static final String SONGS_ID = "_id";
    public static final String SONGS_TRACK = "track";
    public static final String SONGS_TITLE = "title";
    public static final String SONGS_ALBUM = "album";
    public static final int INDEX_SONGS_ID = 1;
    public static final int INDEX_SONGS_TRACK = 2;
    public static final int INDEX_SONGS_TITLE = 3;
    public static final int INDEX_SONGS_ALBUM = 4;

    public static final int ORDER_BY_NONE = 1;
    public static final int ORDER_BY_ASC = 2;
    public static final int ORDER_BY_DESC = 3;

    public static final String QUERY_ARTIST_FOR_SONG_START = "SELECT " + TABLE_ARTIST + "." + ARTIST_NAME + ", " +
            TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME + ", " + TABLE_SONGS + "." + SONGS_TRACK + " FROM " +
            TABLE_SONGS + " INNER JOIN " + TABLE_ALBUMS + " ON " + TABLE_SONGS + "." + SONGS_ALBUM + " = " +
            TABLE_ALBUMS + "." + COLUMN_ALBUMS_ID + " INNER JOIN " + TABLE_ARTIST + " ON " + TABLE_ALBUMS +
            "." + COLUMN_ALBUMS_ARTIST + " = " + TABLE_ARTIST + "." + ARTIST_ID + " WHERE " + TABLE_SONGS + "." +
            SONGS_TITLE + " = \"";

    public static final String QUERY_ARTIST_FOR_SONG_SORT = " ORDER BY " + COLUMN_ALBUMS_ARTIST + "." + ARTIST_NAME +
            ", " + TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME + " COLLATE NOCASE ";


    public static final String QUERY_ALBUM_BY_ARTIST_START = "SELECT " + TABLE_ALBUMS + '.' + COLUMN_ALBUM_NAME + " FROM " +
            TABLE_ALBUMS + " INNER JOIN " + TABLE_ARTIST + " ON " + TABLE_ALBUMS + "." + COLUMN_ALBUMS_ARTIST +
            " = " + TABLE_ARTIST + "." + ARTIST_ID + " WHERE " + TABLE_ARTIST + "." + ARTIST_NAME + " = \"";

    public static final String QUERY_ALBUMS_BY_ARTIST_SORT = " ORDER BY " + TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME + " COLLATE NOCASE ";

    public static final String QUERY_ARTIST = "SELECT * FROM " + TABLE_ARTIST;

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

    public List<Artist> queryArtist(int sortOrder) {
        StringBuilder stringBuilder = new StringBuilder(QUERY_ARTIST);

        if(sortOrder != ORDER_BY_NONE) {
            stringBuilder.append(" ORDER BY ");
            stringBuilder.append(ARTIST_NAME);
            stringBuilder.append(" COLLATE NOCASE ");
            if(sortOrder == ORDER_BY_DESC) {
                stringBuilder.append("DESC");
            } else {
                stringBuilder.append("ASC");
            }
        }


        try (Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(stringBuilder.toString())) {

            List<Artist> artists = new ArrayList<>();
            while (resultSet.next()) {
                Artist artist = new Artist();
                artist.setId(resultSet.getInt(INDEX_ARTIST_ID));
                artist.setName(resultSet.getString(INDEX_ARTIST_NAME));
                artists.add(artist);
            }

            return artists;

        } catch (SQLException e) {
            System.out.println("Query failed " + e.getMessage());
            return null;
        }
    }

    public List<String> queryAlbumsForArtist(String artistName, int sortOrder) {
        StringBuilder stringBuilder = new StringBuilder(QUERY_ALBUM_BY_ARTIST_START);
        stringBuilder.append(artistName);
        stringBuilder.append("\"");

        if(sortOrder != ORDER_BY_NONE) {
            stringBuilder.append(QUERY_ALBUMS_BY_ARTIST_SORT);
            if(sortOrder == ORDER_BY_DESC) {
                stringBuilder.append("DESC");
            } else {
                stringBuilder.append("ASC");
            }
        }

        System.out.println("SQL statement: " + stringBuilder.toString());

        try(Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery(stringBuilder.toString())){
            List<String> albums = new ArrayList<>();
            while(resultSet.next()) {
                albums.add(resultSet.getString(1));
            }
            return albums;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }

    }

    public List<SongArtist> queryArtistForSong(String songName, int sortOrder) {
        StringBuilder sb = new StringBuilder(QUERY_ARTIST_FOR_SONG_START);
        sb.append(songName);
        sb.append("\"");

        if(sortOrder != ORDER_BY_NONE) {
            sb.append(QUERY_ALBUMS_BY_ARTIST_SORT);
            if(sortOrder == ORDER_BY_DESC) {
                sb.append("DESC");
            } else {
                sb.append("ASC");
            }
        }

        System.out.println("Sequal statement: " + sb.toString());

        try(Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery(sb.toString())) {
            List<SongArtist> result = new ArrayList<>();

            while(resultSet.next()) {
                SongArtist songArtist = new SongArtist();
                songArtist.setArtistName(resultSet.getString(1));
                songArtist.setAlbumName(resultSet.getString(2));
                songArtist.setTrack(resultSet.getInt(3));
                result.add(songArtist);
            }
            return result;
        } catch(SQLException e) {
            System.out.println("Query failed: " + e. getMessage());
            return null;
        }
    }

    public void querySongMetaData() {
        String sql = "SELECT * FROM " + TABLE_SONGS;
        try(Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery(sql)) {
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int numColumn = resultSetMetaData.getColumnCount();
            for (int i = 1; i < numColumn; i++) {
                System.out.format("Column %d in the songs table is names %s \n", i, resultSetMetaData.getColumnName(i));
            }

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
        }
    }
}

