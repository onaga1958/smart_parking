package com.findparking.app.data.entities

import java.util.ArrayList

data class CollectionEntity(
    var _id: String,
    var title: String = "",
    var image: String = "",
    var description: String = "",
    var userId: String? = null,
    var categories: ArrayList<String> = arrayListOf(),
    var tags: ArrayList<String> = arrayListOf()
)