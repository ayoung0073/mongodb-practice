package com.may.mongodbpractice.repository

import com.may.mongodbpractice.document.Article
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface ArticleRepository : MongoRepository<Article, ObjectId>