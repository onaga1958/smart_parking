package com.findparking.app.models

import java.util.*

data class User(
        var _id: String = "",
        var name: String? = "",
        var email: String = "",
        var about: String = "",
        var photo: String? = "",
        var registeredAt: Date? = null,
        var readCount: Int = 0,
        var emailVerified: Boolean = false,
        val readsCategories: ArrayList<String> = arrayListOf()
)

data class BookInCollectionEntity(
        var _id: String = "",
        var bookId: String?,
        var collectionId: String?,
        var order: Int = 0
)

data class ContentEntity(
        var language: String = "",
        var chapterEntities: ArrayList<ChapterEntity> = arrayListOf()
)

data class ChapterEntity(
        var _id: String,
        var title: String = "",
        var url: String = ""
)

data class Entity(
        var _id: String,
        var name: String = "",
        var image: String = "",
        var description: String = ""
)


data class StatusEntity(
        var _id: String = "",
        var bookId: String?,
        var userId: String?,
        var status: Int
) {
    companion object StatusType {
        const val CURRENT = 0
        const val FINISHED = 1
        const val FUTURE = 2
    }
}
