package uz.androidclub.tas_ixtube.managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import uz.androidclub.tas_ixtube.models.Video;
import uz.androidclub.tas_ixtube.utils.StringUtils;
import uz.androidclub.tas_ixtube.utils.TablesColumns;

/**
 * Created by yusufabd on 2/28/2017.
 */

public class DatabaseManager extends SQLiteOpenHelper implements TablesColumns {

    private SQLiteDatabase mDB;
    private static final String DB_NAME = "tas_ix_tube_db";
    private static final int DB_VERSION = 6;

    private final String CLEAR_TABLE_FAVORITE_VIDEO = "DELETE FROM " + TABLE_FAVORITE;

    private final String CLEAR_TABLE_SEARCH_HISTORY = "DELETE FROM " + TABLE_SEARCH_HISTORY;

    private final String CREATE_TABLE_FAVORITE_VIDEO = "CREATE TABLE " + TABLE_FAVORITE + "(" +
            COLUMN_FAVORITE_TABLE_ID + " integer primary key autoincrement, " +
            COLUMN_FAVORITE_ID + " text unique," +
            COLUMN_FAVORITE_TITLE + " text," +
            COLUMN_FAVORITE_OWNER + " text," +
            COLUMN_FAVORITE_LENGTH + " integer," +
            COLUMN_FAVORITE_VIEWS + " integer);";

    private final String CREATE_TABLE_SEARCH_HISTORY = "CREATE TABLE " + TABLE_SEARCH_HISTORY + "(" +
            COLUMN_SEARCH_HISTORY_TABLE_ID + " integer primary key autoincrement," +
            COLUMN_SEARCH_QUERY + " text unique);";

    public DatabaseManager(Context context){
        super(context, DB_NAME, null, DB_VERSION);
        mDB = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_FAVORITE_VIDEO);
        db.execSQL(CREATE_TABLE_SEARCH_HISTORY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion){
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITE);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SEARCH_HISTORY);
            onCreate(db);
        }
    }

    public void addToFavorite(Video obj){
        StringUtils.showLog(obj.getId() + " " + obj.getTitle());
        ContentValues values = new ContentValues();
        values.put(COLUMN_FAVORITE_ID, obj.getId());
        values.put(COLUMN_FAVORITE_TITLE, obj.getTitle());
        values.put(COLUMN_FAVORITE_OWNER, obj.getAuthor());
        values.put(COLUMN_FAVORITE_LENGTH, obj.getLength());
        values.put(COLUMN_FAVORITE_VIEWS, obj.getViews());
        mDB.insertWithOnConflict(TABLE_FAVORITE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void removeFromFavorite(Video obj){
        StringUtils.showLog(obj.getId() + " " + obj.getTitle());
        String id = "'" + obj.getId() + "'";
        mDB.delete(TABLE_FAVORITE, COLUMN_FAVORITE_ID.concat("=").concat(id), null);
    }

    public ArrayList<Video> getFavoriteList(){
        ArrayList<Video> list = new ArrayList<>();
        Cursor cursor = mDB.query(TABLE_FAVORITE, null, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()){
            do{
                Video video = new Video(cursor.getString(cursor.getColumnIndex(COLUMN_FAVORITE_TITLE)),
                                        cursor.getString(cursor.getColumnIndex(COLUMN_FAVORITE_OWNER)),
                                        " :" + cursor.getString(cursor.getColumnIndex(COLUMN_FAVORITE_VIEWS)),
                                        cursor.getString(cursor.getColumnIndex(COLUMN_FAVORITE_LENGTH)),
                                        cursor.getString(cursor.getColumnIndex(COLUMN_FAVORITE_ID)));
                list.add(video);
            }while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    public void clearFavoriteList(){
        mDB.execSQL(CLEAR_TABLE_FAVORITE_VIDEO);
    }

    public void addToSearchHistory(String query){
        ContentValues values = new ContentValues();
        values.put(COLUMN_SEARCH_QUERY, query);
        mDB.insertWithOnConflict(TABLE_SEARCH_HISTORY, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public ArrayList<String> getSearchHistory(){
        ArrayList<String> queries = new ArrayList<>();
        Cursor c = mDB.query(TABLE_SEARCH_HISTORY, null, null, null, null, null, null, "10");
        if (c != null && c.moveToFirst()){
            do{
               queries.add(c.getString(c.getColumnIndex(COLUMN_SEARCH_QUERY)));
            }while (c.moveToNext());
            c.close();
        }
        return queries;
    }

    public void clearSearchHistory(){
        mDB.execSQL(CLEAR_TABLE_SEARCH_HISTORY);
    }

}
