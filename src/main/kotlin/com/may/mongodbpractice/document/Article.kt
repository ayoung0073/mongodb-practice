package com.may.mongodbpractice.document

import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document

@Document(value = "article")
class Article(
    var id: ObjectId ?= null,
    val articleName: String,
    val authorName: String,
    val category: Category,
    val score: Int,
    val views: Long
)

enum class Category {
    BEAUTY, FOOD, HEALTH, BOOK, IT, PET, JUST
}