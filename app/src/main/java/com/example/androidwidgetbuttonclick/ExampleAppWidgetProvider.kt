package com.example.androidwidgetbuttonclick

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import kotlin.math.roundToInt

class ExampleAppWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        appWidgetIds.forEach { appWidgetId ->
            Log.e("TAG", "onUpdate $appWidgetId")
            val pendingIntent: PendingIntent = Intent(context, MainActivity::class.java)
                .let { intent ->
                    PendingIntent.getActivity(context, 0, intent, 0)
                }
            val pendingRefreshClickIntent: PendingIntent = Intent(context, javaClass).let {
                it.action = ACTION_REFRESH_CLICK
                it.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                return@let PendingIntent.getBroadcast(
                    context,
                    appWidgetId,
                    it,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            }

            val views = RemoteViews(
                context.packageName,
                R.layout.example_appwidget
            )
            views.setOnClickPendingIntent(R.id.button_refresh, pendingRefreshClickIntent)
            views.setOnClickPendingIntent(R.id.button_lauch_activity, pendingIntent)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        Log.i("TAG", "onReceive " + intent?.action)

        if (intent?.action == ACTION_REFRESH_CLICK) {
            val appWidgetId = intent.extras?.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID) ?: return
            Log.i("TAG", "onReceive appWidgetId $appWidgetId")

            val appWidgetManager = AppWidgetManager.getInstance(context)
            val views = RemoteViews(context!!.packageName, R.layout.example_appwidget)

            views.setTextViewText(R.id.text_data, "a " + (Math.random() * 9).roundToInt())
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    companion object {
        private const val ACTION_REFRESH_CLICK =
            "com.example.androidwidgetbuttonclick.action.ACTION_REFRESH_CLICK"
    }

}