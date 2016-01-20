package example.android.stickyphoto.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class StickyPhotoHelper extends SQLiteOpenHelper {
    // DB名とバージョン
    private static final String DB_NAME = "StickyPhoto";
    private static final int DB_VERSION = 1;

    // コンストラクタ
    public StickyPhotoHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // DB初回アクセス時にコールされ、テーブルを作成する
        sqLiteDatabase.execSQL("CREATE TABLE data (" +
                               "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                               "title TEXT," +
                               "body TEXT," +
                               "path TEXT" +
                               ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        // DBに変更があったときに呼ばれる
    }
}
