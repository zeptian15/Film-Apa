package com.septian.filmapauiux.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(
    var title: String?,
    var release: String?,
    var description: String?,
    var poster: Int?,
    var background: Int?
) : Parcelable