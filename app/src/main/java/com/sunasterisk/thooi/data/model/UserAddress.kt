package com.sunasterisk.thooi.data.model

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserAddress(
    val id: String?,
    val name: String?,
    val address: String?,
    val location: LatLng?
) : Parcelable
