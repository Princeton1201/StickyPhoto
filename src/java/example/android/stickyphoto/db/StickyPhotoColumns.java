package example.android.stickyphoto.db;

import android.net.Uri;
import android.provider.BaseColumns;

public class StickyPhotoColumns implements BaseColumns {

    // Authority
    public static final String AUTHORITY = "sticky_photo";
    // Table
    public static final String TABLE = "data";

    // URI
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE);

    // タイトル
    public static final String TITLE = "title";
    // 本文
    public static final String BODY = "body";
    // 写真データパス
    public static final String PATH = "path";
}
