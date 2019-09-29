package com.findparking.app.data.entities

data class BookmarkEntity (
    var _id: String?,
    var userId: String? = null,
    var bookId: String? = null,
    var chapterId: String? = null,
    var progress: Float
)