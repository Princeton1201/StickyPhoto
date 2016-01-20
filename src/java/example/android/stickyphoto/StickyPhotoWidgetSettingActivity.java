package example.android.stickyphoto;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

/**
 * Widget設定画面Activity.
 * Widget作成前に呼ばれる.
 */
public class StickyPhotoWidgetSettingActivity extends Activity {
    // ログ出力用タグ.
    private static final String TAG = "StickyPhotoWidgetSettingActivity";

    // preferences.
    private UtilSharedPreferences mPreferences = null;
    // リスト画面.
    private ListView mListView = null;
    // 作成するWidgetID.
    private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    /*
     * (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     *
     * Activity起動時に呼ばれる.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 起動時のIntentに作成するWidgetのIDが渡ってくるので保持してく.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras == null) {
            // Widget作成失敗.
            Log.d(TAG, "onCreate, intent extras is null");
            setResult(RESULT_CANCELED);
            finish();
        }

        // WidgetID取得.
        mAppWidgetId = extras.getInt( AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            // WidgetID不正.
            Log.d(TAG, "onCreate, appwidgetid is invalid");
            setResult(RESULT_CANCELED);
            finish();
        }

        // 画面のレイアウト設定.
        setContentView(R.layout.list_layout);

        // 新規作成ボタンは不要なので非表示.
        Button newButton = (Button)findViewById(R.id.new_item);
        newButton.setVisibility(View.GONE);

        // preferences取得.
        Context context = getApplicationContext();
        mPreferences = new UtilSharedPreferences(context);

        // リスト画面取得.
        mListView = (ListView) findViewById(R.id.list);
    }

    /*
     * (non-Javadoc)
     * @see android.app.Activity#onResume()
     *
     * Activity起動時、再起動時に、onCreateの後で呼ばれる.
     */
    @Override
    public void onResume() {
        super.onResume();

        // 画面に表示するリスト更新.
        updateList();

        // リスト項目のクリック時の動作を設定.
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /*
             * (non-Javadoc)
             * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
             *
             * リストの項目を選択すると呼ばれる.
             */
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick, position = " + position);

                // 選択された項目番号をWidgetへ通知.
                Bundle bundle = new Bundle();
                bundle.putInt(Utils.INTENT_PARAM_ITEM_NO, position + 1);
                Intent intent = new Intent("notifyPostion");
                intent.putExtras(bundle);
                sendBroadcast(intent);

                // Widget作成正常終了.
                Intent resultValue = new Intent();
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                setResult(RESULT_OK, resultValue);
                finish();
            }
        });
    }

    /**
     * リスト更新処理.
     * preferencesからメモ情報を取得し、リストに設定する.
     */
    private void updateList() {
        // preferencesからメモ登録件数取得.
        int entryNum = mPreferences.getEntryNum();

        // リストデータ作成.
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.list_item);
        String title = null;
        if (entryNum > 0) {
            // プレファレンスからタイトルを取得し、配列へ格納.
            for (int i = 1; i <= entryNum; i++) {
                title = mPreferences.getTitle(i);
                if (title != null) {
                    arrayAdapter.add(title);
                }
            }
        }

        // 配列をリストへ設定.
        mListView.setAdapter(arrayAdapter);
    }
}
