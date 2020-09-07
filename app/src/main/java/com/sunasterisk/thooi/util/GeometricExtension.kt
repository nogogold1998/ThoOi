package com.sunasterisk.thooi.util

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.GeoPoint

fun GeoPoint.toLatLng(): LatLng = LatLng(latitude, longitude)

fun LatLng.toGeoPoint(): GeoPoint = GeoPoint(latitude, longitude)
