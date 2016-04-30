package in.ac.iiitd.pcsma.chatclient.adapter;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import in.ac.iiitd.pcsma.chatclient.commons.Constants;

/**
 * Created by alan on 18/4/16.
 */
public class DBAdapter {

    private SQLiteDatabase db;
    private static final Pattern DIR_SEPORATOR = Pattern.compile("/");

    public SQLiteDatabase getDB() {
        return db;
    }

    public File getDatabaseDirectory() {
        File sdCard = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File directory = new File(sdCard.getAbsolutePath() + File.separator + Constants.APP_NAME);
        if (!directory.exists()) {
            Log.d(getClass().toString(), "Created directory " + directory.getAbsolutePath());
            directory.mkdir();
        }
        return directory;
    }

    public DBAdapter() {
        File dataBaseDirectory = getDatabaseDirectory();
        db = SQLiteDatabase.openDatabase(dataBaseDirectory.getAbsolutePath() + File.separator + Constants.APP_DB_NAME, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        db.execSQL("CREATE TABLE IF NOT EXISTS PrivateChat(friend_id TEXT, message TEXT);");
        db.execSQL("CREATE TABLE IF NOT EXISTS GroupChat(exchange_name TEXT, message TEXT);");
        db.execSQL("CREATE TABLE IF NOT EXISTS Exchanges(name TEXT, type TEXT);");
    }

    public void insertIntoPrivateChat(String friendId, String message) {
        System.out.println("Calling query insertIntoPrivateChat");
        db.execSQL("INSERT INTO PrivateChat VALUES(" + "'" + friendId + "'," + "'" + message + "');");
    }

    public void insertIntoGroupChat(String exchangeName, String message) {
        System.out.println("Calling query insertIntoGroupChat");
        db.execSQL("INSERT INTO GroupChat VALUES(" + "'" + exchangeName + "'," + "'" + message + "');");
    }

    public void insertIntoExchanges(String exchangeName, String type) {
        Cursor result = db.rawQuery("SELECT * FROM Exchanges WHERE name='" + exchangeName + "' " +
                "and type = '" + type + "';", null);
        if(result.getCount() == 0) {
            System.out.println("Calling query insertIntoExchanges");
            db.execSQL("INSERT INTO Exchanges VALUES(" + "'" + exchangeName + "'," + "'" + type + "');");
        }
    }

    public List<String> getAllChatsFromFriend(String friendId) {
        List<String> list = new ArrayList<>();
        Log.d(getClass().toString(), "Calling raw query getAllChatsFromFriend");
        Cursor result = db.rawQuery("SELECT * FROM PrivateChat WHERE friend_id='" + friendId + "';", null);
        while (result.moveToNext()) {
            String message = "";
            try {
                message = result.getString(result.getColumnIndex("message"));
                list.add(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public List<String> getAllChatsFromGroup(String exchangeName) {
        List<String> list = new ArrayList<>();
        Log.d(getClass().toString(), "Calling raw query getAllChatsFromGroup");
        Cursor result = db.rawQuery("SELECT * FROM GroupChat WHERE exchange_name='" + exchangeName + "';", null);
        while (result.moveToNext()) {
            String message = "";
            try {
                message = result.getString(result.getColumnIndex("message"));
                list.add(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public List<String> getAllExchanges() {
        List<String> list = new ArrayList<>();
        Log.d(getClass().toString(), "Calling raw query getAllExchanges");
        Cursor result = db.rawQuery("SELECT * FROM Exchanges;", null);
        while (result.moveToNext()) {
            String exchange = "";
            try {
                exchange = result.getString(result.getColumnIndex("name"));
                list.add(exchange);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public List<String> getFriendListForPrivateChats() {
        List<String> friends = new ArrayList<>();
        Log.d(getClass().toString(), "Calling raw query getFriendListForPrivateChats");
        Cursor result = db.rawQuery("SELECT * FROM PrivateChat;", null);
        while (result.moveToNext()) {
            String friendId = "";
            try {
                friendId = result.getString(result.getColumnIndex("friend_id"));
                if (!friends.contains(friendId)) {
                    friends.add(friendId);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return friends;
    }
}
