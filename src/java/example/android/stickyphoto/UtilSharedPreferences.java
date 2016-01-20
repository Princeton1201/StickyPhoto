package example.android.stickyphoto;

import java.lang.String;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.util.Log;

/**
 * SharedPreferences操作クラス.
 */
public class UtilSharedPreferences {
    public static String TAG = "UtilSharedPreferences";

    // プリファレンス名.
    public static String PREFERENCES_NAME = "preferences";
    // キー:登録件数.
    public static String KEY_ENTRY_NUM = "entry_num";
    // キー：登録タイトル.
    public static String KEY_TITLE = "title_";
    // キー：登録メモ.
    public static String KEY_MEMO = "memo_";
    // キー：登録画像URI.
    public static String KEY_IMAGE_URI = "image_uri_";

    SharedPreferences mPreference = null;
    Editor mEditor = null;

    /**
     * コンストラクタ(クラス生成時にコール).
     * @param context コンテキスト.
     */
    UtilSharedPreferences(final Context context) {
        mPreference = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        mEditor = mPreference.edit();
    }

    /**
     * 登録件数取得.
     * @return 登録件数.
     */
    public int getEntryNum() {
        if (mPreference == null) {
            Log.e(TAG, "getEntryNum(), mPreference is null");
            return 0;
        }

        int entryNum = mPreference.getInt(KEY_ENTRY_NUM, -1);
        if (entryNum == -1) {
            entryNum = 0;
            // 登録件数保存.
            mEditor.putInt(KEY_ENTRY_NUM, entryNum);
            mEditor.commit();
        }
        Log.d(TAG, "getEntryNum(), return entryNum = " + entryNum);
        return entryNum;
    }

    /**
     * タイトル取得.
     * @param itemNo 項目番号.
     * @return　タイトル.
     */
    public String getTitle(final int itemNo) {
        Log.d(TAG, "getTitle(), start itemNo = " + itemNo);

        if (mPreference == null) {
            Log.e(TAG, "getTitle(), mPreference is null");
            return null;
        }

        // タイトル取得.
        String key = createKey(KEY_TITLE, itemNo);
        String str = mPreference.getString(key, null);
        if (str == null) {
            Log.e(TAG, "getTitle(), getString is error, itemNo = " + itemNo);
        }

        Log.d(TAG, "getTitle(), return title = " + str);
        return str;
    }

    /**
     * メモ取得.
     * @param itemNo 項目番号.
     * @return　メモ.
     */
    public String getMemo(final int itemNo) {
        Log.d(TAG, "getMemo(), start itemNo = " + itemNo);

        if (mPreference == null) {
            Log.e(TAG, "getMemo(), mPreference is null");
            return null;
        }

        // メモ取得.
        String key = createKey(KEY_MEMO, itemNo);
        String str = mPreference.getString(key, null);
        if (str == null) {
            Log.e(TAG, "getTitle(), getString is error, itemNo = " + itemNo);
        }

        Log.d(TAG, "getTitle(), return title = " + str);
        return str;
    }

    /**
     * 登録件数保存.
     * @param entryNum 登録件数.
     */
    private void saveEntryNum(final int entryNum) {
        if (mEditor == null) {
            Log.e(TAG, "saveMemoItem(), mEditor is null");
            return;
        }

        // 登録件数保存.
        mEditor.putInt(KEY_ENTRY_NUM, entryNum);
        mEditor.commit();
    }

    /**
     * メモアイテム保存.
     * @param title　タイトル.
     * @param memo メモ.
     * @param imageUri 画像URI.
     */
    public void saveMemoItem(final String title, final String memo, final Uri imageUri) {
        // 現在の登録件数取得.
        int entryNum = getEntryNum();
        // 登録件数+1へ保存.
        entryNum++;

        saveMemoItem(title, memo, imageUri, entryNum);
    }

    /**
     * メモアイテム保存.
     * @param title　タイトル.
     * @param memo メモ.
     * @param imageUri 画像URI.
     * @param itemNo 項目番号.
     */
    private void saveMemoItem(final String title, final String memo, final Uri imageUri, final int itemNo) {
        Log.d(TAG, "saveMemoItem(), start title = " + title);
        Log.d(TAG, "saveMemoItem(), start memo = " + memo);
        Log.d(TAG, "saveMemoItem(), start imageUri = " + imageUri);
        Log.d(TAG, "saveMemoItem(), start itemNo = " + itemNo);

        if (mEditor == null) {
            Log.e(TAG, "saveMemoItem(), mEditor is null");
            return;
        }

        // タイトル保存.
        String keyTitle = createKey(KEY_TITLE, itemNo);
        mEditor.putString(keyTitle, title);
        mEditor.commit();

        // メモ保存.
        String keyMemo = createKey(KEY_MEMO, itemNo);
        mEditor.putString(keyMemo, memo);
        mEditor.commit();

        // 画像URI保存.

        // 登録件数保存.
        saveEntryNum(itemNo);
    }

    public void deleteMemoItem(final int itemNo) {
        // 現在の登録件数取得.
        int entryNum = getEntryNum();
        if (entryNum < itemNo) {
            Log.e(TAG, "deleteMemoItem(), itemNo is error, itemNo = " + itemNo);
            return;
        }

        // データ削除.
        for (int i = itemNo; i < entryNum; i++) {
            // 1つ後のデータを取得.
            String title = getTitle(i + 1);
            String memo = getMemo(i + 1);
            // 1つ後のデータを詰めて保存.
            saveMemoItem(title, memo, null, i);
        }

        // 登録件数保存.
        entryNum--;
        saveEntryNum(entryNum);
    }

    /**
     * キー取得
     * @param key キー種別.
     * @param itemNo 項目番号.
     * @return キー.
     */
    private String createKey(final String key, final int itemNo){
        StringBuffer buffer = new StringBuffer(key);
        buffer.append(String.valueOf(itemNo));
        return buffer.toString();
    }

    /**
     * 登録件数取得(widget用).
     * @param　コンテキスト.
     * @return 登録件数.
     */
    public static int getEntryNumForWidget(final Context context) {
        SharedPreferences preference = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);

        // 登録件数取得.
        int entryNum = preference.getInt(KEY_ENTRY_NUM, -1);

        Log.d(TAG, "getEntryNum(), return entryNum = " + entryNum);
        return entryNum;
    }

    /**
     * タイトル取得(widget用).
     * @param　コンテキスト.
     * @param itemNo 項目番号.
     * @return　タイトル.
     */
    public static String getTitleForWidget(final Context context, final int itemNo) {
        Log.d(TAG, "getTitle(), start itemNo = " + itemNo);

        SharedPreferences preference = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);

        // タイトル取得.
        StringBuffer buffer = new StringBuffer("KEY_TITLE");
        buffer.append(String.valueOf(itemNo));
        String key = buffer.toString();
        String str = preference.getString(key, null);
        if (str == null) {
            Log.e(TAG, "getTitle(), getString is error, itemNo = " + itemNo);
        }

        Log.d(TAG, "getTitle(), return title = " + str);
        return str;
    }

    /**
     * メモ取得(Widget用).
     * @param　コンテキスト.
     * @param itemNo 項目番号.
     * @return　メモ.
     */
    public static String getMemoForWidget(final Context context, final int itemNo) {
        Log.d(TAG, "getMemo(), start itemNo = " + itemNo);

        SharedPreferences preference = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);

        // メモ取得.
        StringBuffer buffer = new StringBuffer("KEY_MEMO");
        buffer.append(String.valueOf(itemNo));
        String key = buffer.toString();
        String str = preference.getString(key, null);
        if (str == null) {
            Log.e(TAG, "getTitle(), getString is error, itemNo = " + itemNo);
        }

        Log.d(TAG, "getTitle(), return title = " + str);
        return str;
    }
}