package example.android.stickyphoto;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class StickyPhotoActivity extends Activity {
    // オプションメニュー項目ID.
    private static int MENU_ITEM_ALL_DELETE = 1;

    private UtilSharedPreferences mPreferences = null;
    private ListView mListView = null;
    private int mSelectPosition = 0;

    /*
     * (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // レイアウト設定.
        setContentView(R.layout.list_layout);

        // 新規作成ボタン.
        Button newButton = (Button)findViewById(R.id.new_item);
        newButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startEdit(Utils.INTENT_EXTRA_ITEM_NEW);
            }
        });

        //プレファレンス取得.
        Context context = getApplicationContext();
        mPreferences = new UtilSharedPreferences(context);

        // リスト画面取得.
        mListView = (ListView) findViewById(R.id.list);
        // リスト画面をスクロール時に背景色、画像が消えないよう設定.
        mListView.setScrollingCacheEnabled(false);
    }

    /*
     * (non-Javadoc)
     * @see android.app.Activity#onResume()
     */
    @Override
    public void onResume() {
        super.onResume();

        // リスト更新.
        updateList();

        // リスト項目のクリック時の動作.
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 詳細画面起動.
                startDetail(position + 1);
            }
        });

        // リスト項目のロングクリック時の動作.
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean  onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // タイトル取得.
                String title = mPreferences.getTitle(position + 1);

                // ダイアログ表示.
                /** TODO:. */
                String[] str_items = {"編集", "削除", "キャンセル"};
                AlertDialog.Builder builder = new AlertDialog.Builder(StickyPhotoActivity.this);
                builder.setTitle(title);
                builder.setItems(str_items, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which) {
                        switch(which) {
                        case 0:
                            // 編集画面起動.
                            startEdit(mSelectPosition);
                            break;
                        case 1:
                            // 選択データ削除.
                            mPreferences.deleteMemoItem(mSelectPosition);
                            // リスト更新.
                            updateList();
                            break;
                        default:
                            break;
                        }
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

                // 選択項目番号保持.
                mSelectPosition = position + 1;
                return true;
            }
        });
    }

    /*
     * (non-Javadoc)
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /** TODO:OptionsMenu. */
        menu.add(Menu.NONE, MENU_ITEM_ALL_DELETE, Menu.NONE, "全件削除");
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 詳細画面起動.
     * @param itemNo 項目番号.
     */
    private void startDetail(final int itemNo) {
        Intent intent = new Intent();
        intent.setClassName(Utils.PACKAGE_NAME, Utils.CLASS_NAME_DETAIL_ACTIVITY);
        intent.putExtra(Utils.INTENT_PARAM_ITEM_NO, itemNo);
        startActivity(intent);
    }

    /**
     * 編集画面起動.
     * @param itemNo 項目番号.
     */
    private void startEdit(final int itemNo) {
        Intent intent = new Intent();
        intent.setClassName(Utils.PACKAGE_NAME, Utils.CLASS_NAME_EDIT_ACTIVITY);
        intent.putExtra(Utils.INTENT_PARAM_ITEM_NO, itemNo);
        startActivity(intent);
    }

    /**
     * リスト更新.
     */
    private void updateList() {
        // プリファレンス読み出し.
        int entryNum = mPreferences.getEntryNum();

        // リストデータ作成.
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.list_item);
        String title = null;
        if (entryNum > 0) {
            // タイトルリスト作成.
            for (int i = 1; i <= entryNum; i++) {
                title = mPreferences.getTitle(i);
                if (title != null) {
                    arrayAdapter.add(title);
                }
            }
        }

        // リストデータ設定.
        mListView.setAdapter(arrayAdapter);
    }
}
