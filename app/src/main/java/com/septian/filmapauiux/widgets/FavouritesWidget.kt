package com.septian.filmapauiux.widgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.net.toUri
import com.septian.filmapauiux.R

/**
 * Implementation of App Widget functionality.
 */
class FavouritesWidget : AppWidgetProvider() {

    companion object {
        private const val TOAST_ACTION = "com.septian.filmapauiux.TOAST_ACTION"
        private const val REFRESH_WIDGET = "com.septian.filmapauiux.REFRESH_WIDGET"
        const val EXTRA_ITEM = "com.septian.filmapauiux.EXTRA_ITEM"
        const val EXTRA_NAME = "com.septian.filmapauiux.EXTRA_NAME"
        const val EXTRA_FAVOURITE = "com.septian.filmapauiux.EXTRA_FAVOURITE"
    }

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val intent = Intent(context, FavouritesWidgetService::class.java)
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        intent.data = intent.toUri(Intent.URI_INTENT_SCHEME).toUri()
        val views = RemoteViews(
            context.packageName,
            R.layout.favorite_widget
        )
        views.setRemoteAdapter(R.id.stack_view, intent)
        views.setEmptyView(
            R.id.stack_view,
            R.id.empty_data
        )
        val toastIntent = Intent(context, FavouritesWidget::class.java)
        toastIntent.action =
            TOAST_ACTION
        toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        intent.data = intent.toUri(Intent.URI_INTENT_SCHEME).toUri()
        val toastPendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            toastIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        views.setPendingIntentTemplate(
            R.id.stack_view,
            getPendingSelfIntent(context, appWidgetId, REFRESH_WIDGET)
        )
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }


    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(
                context,
                appWidgetManager,
                appWidgetId
            )
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action != null) {
            if (intent.action == TOAST_ACTION) {
                val viewIndex = intent.getIntExtra(EXTRA_ITEM, 0)
                val title = intent.getStringExtra(EXTRA_NAME)
//                Toast.makeText(context, "Touched view $viewIndex $title", Toast.LENGTH_SHORT).show()
            } else if (intent.action == REFRESH_WIDGET) {
                val updateIntent = Intent(context, FavouritesWidgetService::class.java)
                updateIntent.data = updateIntent.toUri(Intent.URI_INTENT_SCHEME).toUri()
                val views = RemoteViews(context.packageName, R.layout.favorite_widget)
                views.setRemoteAdapter(R.id.stack_view, updateIntent)
                views.setEmptyView(R.id.stack_view, R.id.empty_data)
                Toast.makeText(context, "Refresh", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getPendingSelfIntent(
        context: Context,
        appWidgetId: Int,
        action: String
    ): PendingIntent {
        val intent = Intent(context, javaClass)
        intent.action = action
        return PendingIntent.getBroadcast(
            context,
            appWidgetId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }
}