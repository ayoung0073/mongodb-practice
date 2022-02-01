package com.may.mongodbpractice.vo

import com.may.mongodbpractice.document.Category

data class ArticleWithScoreRate(
    val articleName: String,
    val authorName: String,
    val category: Category,
    val scoreRate: Double
)