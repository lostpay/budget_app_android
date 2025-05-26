package fcu.app.zhuanti;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ExpenseDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "budget.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "expenses";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_NOTE = "note";
    public static final String COLUMN_DATE = "date";

    public ExpenseDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_AMOUNT + " REAL," +
                COLUMN_NOTE + " TEXT," +
                COLUMN_DATE + " TEXT)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
