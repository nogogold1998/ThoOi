package com.sunasterisk.thooi.ui.placespicker

import android.Manifest
import androidx.annotation.RequiresPermission
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.places.api.net.PlacesClient
import com.sunasterisk.thooi.data.model.UserAddress
import com.sunasterisk.thooi.util.getCurrentPlaces
import kotlinx.coroutines.launch

class AddressViewModel(private val placesClient: PlacesClient) : ViewModel() {

    private val _places = MutableLiveData<List<UserAddress>>()
    val places: LiveData<List<UserAddress>> get() = _places

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_WIFI_STATE])
    fun findAddress() {
        viewModelScope.launch {
            placesClient.getCurrentPlaces().run {
                _places.value = map { it.place.run { UserAddress(id, name, address, latLng) } }
            }
        }
    }
}
