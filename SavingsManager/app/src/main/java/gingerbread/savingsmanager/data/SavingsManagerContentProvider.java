package gingerbread.savingsmanager.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by Boris Wang on 4/17/2017.
 */

public class SavingsManagerContentProvider extends ContentProvider {

    public static final String AUTHORITY = "gingerbread.savingsmanager.provider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + SavingsProjectTable.TABLE_NAME);
    private static final String DATABASE_NAME = "SavingsManager.db";

    private SavingsProjectTable savingsProjectTable;
    private SQLiteDatabase database;

    @Override
    public boolean onCreate() {
        savingsProjectTable = new SavingsProjectTable(getContext(), DATABASE_NAME);
        database = savingsProjectTable.getWritableDatabase();
        return (database == null) ? false : true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return database.query(SavingsProjectTable.TABLE_NAME, projection,selection, selectionArgs, null, null, sortOrder);
    }

    @Override
    public String getType( Uri uri) {
        return null;
    }

    @Override
    public Uri insert( Uri uri, ContentValues values) {
        long id = database.insert(SavingsProjectTable.TABLE_NAME, null, values);
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }

    @Override
    public int delete( Uri uri, String selection, String[] selectionArgs) {
        return database.delete(SavingsProjectTable.TABLE_NAME,selection,selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return database.update(SavingsProjectTable.TABLE_NAME, values, selection, selectionArgs);
    }
}
