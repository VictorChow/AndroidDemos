package demos.provider;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.victor.androiddemos.R;

import demos.activity.MainActivity;

/**
 * Created by Victor on 2017/3/13. (ง •̀_•́)ง
 */

public class WidgetAppWidgetProvider extends AppWidgetProvider {
    //requestId不重复
    private int requestId = 0;

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int appWidgetId : appWidgetIds) {
            Intent intent1 = new Intent("pers.victor.AndroidDemos.APPWIDGET_UPDATE").putExtra("icon", "1");
            Intent intent2 = new Intent("pers.victor.AndroidDemos.APPWIDGET_UPDATE").putExtra("icon", "2");
            Intent intent3 = new Intent(context, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntent1 = PendingIntent.getBroadcast(context, ++requestId, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
            PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context, ++requestId, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
            PendingIntent pendingIntent3 = PendingIntent.getActivity(context, ++requestId, intent3, PendingIntent.FLAG_UPDATE_CURRENT);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_demo_layout);
            views.setOnClickPendingIntent(R.id.tv_app_widget_1, pendingIntent1);
            views.setOnClickPendingIntent(R.id.tv_app_widget_2, pendingIntent2);
            views.setOnClickPendingIntent(R.id.iv_app_widget, pendingIntent3);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.contentEquals("pers.victor.AndroidDemos.APPWIDGET_UPDATE")) {
            String type = intent.getStringExtra("icon");
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_demo_layout);
            views.setImageViewResource(R.id.iv_app_widget, type.equals("1") ? R.drawable.iv_myself : R.drawable.ic_launcher);
            int[] ids = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, WidgetAppWidgetProvider.class));
            AppWidgetManager.getInstance(context).updateAppWidget(ids, views);
            return;
        }
        super.onReceive(context, intent);
    }
}
