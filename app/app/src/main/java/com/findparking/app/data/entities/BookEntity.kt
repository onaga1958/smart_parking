package com.findparking.app.data.entities

import com.findparking.app.models.Entity
import com.findparking.app.models.ChapterEntity
import com.findparking.app.models.ContentEntity
import java.util.ArrayList

data class BookEntity(
    var _id: String,
    var title: String = "",
    var image: String = "",
    var description: String = "",
    var authorId: String? = null, //id of Author
    var entity: Entity? = null,
    var languages: List<String> = listOf(), //[en, ua]
    var contents: ArrayList<ContentEntity> = arrayListOf(),
    var chapterEntities: ArrayList<ChapterEntity> = arrayListOf(),
    var categories: ArrayList<String> = arrayListOf(),
    var tags: ArrayList<String> = arrayListOf(),
    var readCount: Int = 0
)