package fcu.app.zhuanti;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class GoalsDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "goals.db";
    private static final int DB_VERSION = 1;

    public static final String TABLE_NAME = "Goals";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_CURRENT_AMOUNT = "currentAmount";
    public static final String COLUMN_TARGET_AMOUNT = "targetAmount";

    public GoalsDBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT NOT NULL, " +
                COLUMN_CURRENT_AMOUNT + " REAL NOT NULL, " +
                COLUMN_TARGET_AMOUNT + " REAL NOT NULL" +
                ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}

