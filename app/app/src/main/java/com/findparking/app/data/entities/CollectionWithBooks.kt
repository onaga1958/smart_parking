package com.findparking.app.data.entities

data class CollectionWithBooks(
    val collection: CollectionEntity,
    val books: List<BookEntity>
)