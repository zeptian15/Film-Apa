package com.septian.filmapauiux.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Favourite(
    var id: String? = null,
    var title: String? = null,
    var description: String? = null,
    var language: String? = null,
    var vote: Double? = 0.0,
    var poster: String? = null,
    var background: String? = null,
    var type: String? = null
) : Parcelable