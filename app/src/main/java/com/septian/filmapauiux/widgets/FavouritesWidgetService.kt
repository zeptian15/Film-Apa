package com.septian.filmapauiux.widgets

import android.content.Intent
import android.widget.RemoteViewsService

class FavouritesWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory =
        FavouritesViewsFactory(this.applicationContext)
}