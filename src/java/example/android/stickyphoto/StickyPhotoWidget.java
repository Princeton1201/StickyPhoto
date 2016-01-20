package example.android.stickyphoto;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * Widget表示.
 * Widget作成や更新された際の処理を行う.
 */
public class StickyPhotoWidget extends AppWidgetProvider
{
    private static final String TAG = "StickyPhotoWidget";

    /*
     * (non-Javadoc)
     * @see android.appwidget.AppWidgetProvider#onUpdate(android.content.Context, android.appwidget.AppWidgetManager, int[])
     *
     * Widget作成時や更新された場合に呼ばれる.
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetMgr, int[] id)
    {
        Log.d(TAG, "onUpdate, start");
    }

    /*
     * (non-Javadoc)
     * @see android.appwidget.AppWidgetProvider#onReceive(android.content.Context, android.content.Intent)
     *
     * 各種通知を受ける.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive, start, intent = " + intent);

        String ac = intent.getAction();
        if (ac.equals("notifyPostion")) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                int msg = bundle.getInt(Utils.INTENT_PARAM_ITEM_NO);
                setTextView(context, msg);
            }
        }
    }

    /**
     *
     * @param context
     * @param msg_st
     */
    void setTextView(Context context, int msg_st) {
        Log.d(TAG, "setTextView, start");
        String title = UtilSharedPreferences.getTitleForWidget(context, msg_st);
          String memo = UtilSharedPreferences.getMemoForWidget(context, msg_st);
          RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
          remoteViews.setTextViewText(R.id.item_title, title);
          remoteViews.setTextViewText(R.id.item_memo, memo);

          ComponentName thisWidget = new ComponentName(context, StickyPhotoWidget.class);
          AppWidgetManager manager = AppWidgetManager.getInstance(context);
          manager.updateAppWidget(thisWidget, remoteViews);
    }
}
