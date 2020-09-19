package com.sunasterisk.thooi.data.source.remote.dto

data class FirestoreCategory(val title: String, val images: List<String>, val span: String) {
    constructor() : this("", emptyList(), "")
}
