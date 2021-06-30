package no4mat.no4mat.agenda;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BDLocal extends SQLiteOpenHelper {
    private static final String DB_NAME = "agenda.sqlite";
    private static final int DB_VERSION = 1;
    private static final String LIST_AGENDA = "CREATE TABLE list_agenda(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "name TEXT," +
            "lastName TEXT," +
            "phone TEXT," +
            "category," +
            "date TEXT," +
            "time TEXT" +
            ")";

    public BDLocal(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(LIST_AGENDA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
