package fcu.app.zhuanti;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ExpenseDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "budget.db";
    public static final int DATABASE_VERSION = 3;

    public static final String TABLE_NAME = "expenses";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_NOTE = "note";
    public static final String COLUMN_TYPE = "type"; // income / expense
    public static final String COLUMN_CATEGORY = "category";

    public static final String CATEGORY_TABLE = "categories"; // 支出分類
    public static final String INCOME_CATEGORY_TABLE = "income_categories"; // 收入分類
    public static final String COLUMN_CATEGORY_NAME = "name";

    public ExpenseDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_AMOUNT + " REAL, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_NOTE + " TEXT, " +
                COLUMN_TYPE + " TEXT, " +
                COLUMN_CATEGORY + " TEXT)");

        db.execSQL("CREATE TABLE " + CATEGORY_TABLE + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CATEGORY_NAME + " TEXT UNIQUE)");

        db.execSQL("CREATE TABLE " + INCOME_CATEGORY_TABLE + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CATEGORY_NAME + " TEXT UNIQUE)");

        db.execSQL("INSERT INTO " + CATEGORY_TABLE + " (name) VALUES ('飲食'), ('交通'), ('娛樂')");
        db.execSQL("INSERT INTO " + INCOME_CATEGORY_TABLE + " (name) VALUES ('薪水'), ('獎金'), ('股息'), ('其他')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CATEGORY_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + INCOME_CATEGORY_TABLE);
        onCreate(db);
    }
}
