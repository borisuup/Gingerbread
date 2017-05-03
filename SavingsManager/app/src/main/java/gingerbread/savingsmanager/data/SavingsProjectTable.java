package gingerbread.savingsmanager.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Boris Wang on 4/17/2017.
 */

public class SavingsProjectTable extends SQLiteOpenHelper
        implements BaseColumns {

    public static final String TABLE_NAME = "savings_project";
    public static final String COLUMN_NAME_BANK_NAME = "bank_name";
    public static final String COLUMN_NAME_START_DATE = "start_date";
    public static final String COLUMN_NAME_END_DATE = "end_date";
    public static final String COLUMN_NAME_AMOUNT = "amount";
    public static final String COLUMN_NAME_YIELD = "yield";
    public static final String COLUMN_NAME_INTEREST = "interest";

    public static final String DISPLAY_BANK_NAME = "display_bank_name";
    public static final String DISPLAY_START_DATE = "display_start_date";
    public static final String DISPLAY_END_DATE = "display_end_date";
    public static final String DISPLAY_AMOUNT = "display_amount";
    public static final String DISPLAY_YIELD = "display_yield";
    public static final String DISPLAY_INTEREST = "display_interest";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_BANK_NAME + " TEXT," +
                    COLUMN_NAME_START_DATE + " TIMESTAMP," +
                    COLUMN_NAME_END_DATE + " TIMESTAMP," +
                    COLUMN_NAME_AMOUNT + " FLOAT," +
                    COLUMN_NAME_YIELD + " FLOAT," +
                    COLUMN_NAME_INTEREST + " FLOAT)";
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME;


    public SavingsProjectTable(Context context, String database){
        super(context, database, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

}
