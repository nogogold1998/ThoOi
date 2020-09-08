package com.sunasterisk.thooi.util

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.ACCESS_WIFI_STATE
import androidx.annotation.RequiresPermission
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place.Field.*
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.ktx.api.net.awaitFindAutocompletePredictions
import com.google.android.libraries.places.ktx.api.net.awaitFindCurrentPlace
import com.google.android.libraries.places.ktx.api.net.findAutocompletePredictionsRequest
import com.google.android.libraries.places.ktx.api.net.findCurrentPlaceRequest
import kotlinx.coroutines.delay

private const val COUNTRY = "VN"
private const val QUERY_DELAY = 300L
private val VN_SW = LatLng(8.1790665, 102.14441)
private val VN_NE = LatLng(23.393395, 114.3337595)

@RequiresPermission(allOf = [ACCESS_FINE_LOCATION, ACCESS_WIFI_STATE])
suspend fun PlacesClient.getCurrentPlaces() =
    awaitFindCurrentPlace(findCurrentPlaceRequest(listOf(NAME, ADDRESS, LAT_LNG))).placeLikelihoods

suspend fun PlacesClient.queryAutoComplete(query: String): MutableList<AutocompletePrediction> {
    delay(QUERY_DELAY)
    return awaitFindAutocompletePredictions(findAutocompletePredictionsRequest {
        setLocationBias(RectangularBounds.newInstance(VN_SW, VN_NE))
        setTypeFilter(TypeFilter.ADDRESS)
        setQuery(query)
        setCountries(COUNTRY)
    }).autocompletePredictions
}
