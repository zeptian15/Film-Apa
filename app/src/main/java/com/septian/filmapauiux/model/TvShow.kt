package com.septian.filmapauiux.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TvShow(
    var title: String? = null,
    var language: String? = null,
    var vote: Double? = 0.0,
    var description: String? = null,
    var poster: String? = null,
    var background: String? = null
) : Parcelable