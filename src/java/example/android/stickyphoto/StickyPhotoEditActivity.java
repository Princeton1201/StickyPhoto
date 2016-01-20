package example.android.stickyphoto;

import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

/**
 *
 *
 */
public class StickyPhotoEditActivity extends Activity {
    private static final String TAG = "StickyPhotoEditActivity";

    private static final int REQUEST_GALLERY = 1;

    private UtilSharedPreferences mPreferences = null;
    private EditText mEditTitle = null;
    private EditText mEditMemo = null;
    private ImageView mEditPhoto = null;
    private Uri mEditPhotoUri = null;

    /*
     * (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        int itemNo = intent.getIntExtra(Utils.INTENT_PARAM_ITEM_NO, Utils.INTENT_EXTRA_ITEM_NEW);

        // プリファレンス保存.
        Context context = getApplicationContext();
        mPreferences = new UtilSharedPreferences(context);

        // Activityにレイアウト設定.
        setContentView(R.layout.edit_layout);

        // 入力テキスト.
        mEditTitle = (EditText) findViewById(R.id.title_edit);
        mEditMemo = (EditText) findViewById(R.id.memo_edit);
        if (itemNo != Utils.INTENT_EXTRA_ITEM_NEW) {
            mEditTitle.setText(mPreferences.getTitle(itemNo));
            mEditMemo.setText(mPreferences.getMemo(itemNo));
        }

        // 画像.
        mEditPhoto = (ImageView) findViewById(R.id.image_edit);
        mEditPhoto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
//                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_GALLERY);
            }
        });

        // キャンセルボタン動作.
        Button buttonCancel = (Button) findViewById(R.id.button_cancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Activity終了(前画面へ戻る).
                finish();
            }
        });

        // 完了ボタン動作.
        Button buttonDone = (Button) findViewById(R.id.button_done);
        buttonDone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // 入力文字列取得.
                SpannableStringBuilder bufTitle = (SpannableStringBuilder) mEditTitle.getText();
                String title = bufTitle.toString();
                SpannableStringBuilder bufMemo = (SpannableStringBuilder) mEditMemo.getText();
                String memo = bufMemo.toString();

                mPreferences.saveMemoItem(title, memo, mEditPhotoUri);

                // Avtivity終了(前画面へ戻る).
                finish();
            }
        });
    }

    /*
     * (non-Javadoc)
     * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult, start, resultCode = " + resultCode);
        if (resultCode != RESULT_OK) {
            Log.e(TAG, "onActivityResult, requestCode != RESULT_OK");
        }

        if (requestCode == REQUEST_GALLERY) {
            try {
                Uri uri = data.getData();
                InputStream in = getContentResolver().openInputStream(uri);
                Bitmap img = BitmapFactory.decodeStream(in);
                in.close();
                // 選択した画像を表示
                mEditPhoto.setImageBitmap(img);
                mEditPhotoUri = uri;
            } catch (Exception e){
                Log.d(TAG, "onActivityResult, Exception = " + e);
            }
        }
    }
}
