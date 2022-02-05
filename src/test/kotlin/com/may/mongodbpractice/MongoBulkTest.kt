package com.may.mongodbpractice

import com.may.mongodbpractice.document.Article
import com.may.mongodbpractice.document.Category
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.BulkOperations
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update

@SpringBootTest
class MongoBulkTest(
    @Autowired
    private val mongoTemplate: MongoTemplate
) {
    @Test
    fun `bulk로 upsert 실행`() {
        val bulkOps =
            mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, Article::class.java)
        val article = Article(
            id = ObjectId("61f90f5fd3d5c30dfe1a8f09"),
            articleName = "insert article",
            authorName = "애용",
            category = Category.BEAUTY,
            score = 90,
            views = 1000
        )
        val articleList = List(100000) { article }

        articleList.forEach {
            val update = Update()
                .set("articleName", it.articleName)
                .set("authorName", it.authorName)
                .set("category", it.category)
                .set("views", it.views)
            bulkOps.upsert(
                Query(
                    Criteria("_id").`is`(it.id)
                ),
                update.currentDate("updated_at")
            )
        }
        try {
            val startTime = System.currentTimeMillis()
            bulkOps.execute()
            val endTime = System.currentTimeMillis()
            println("time : ${endTime - startTime}") // 10000 : 1991, 100000 : 8482
        } catch (e: Exception) {
            throw RuntimeException("fail to update businessAccountTargetGroupList")
        }
    }

    @Test
    fun `bulk 없이 upsert 실행`() {
        val article = Article(
            id = ObjectId("61f90f5fd3d5c30dfe1a8f09"),
            articleName = "insert article",
            authorName = "애용",
            category = Category.BEAUTY,
            score = 90,
            views = 1000
        )
        val articleList = List(100000) { article }
        val startTime = System.currentTimeMillis()
        articleList.forEach {
            val update = Update()
                .set("articleName", it.articleName)
                .set("authorName", it.authorName)
                .set("category", it.category)
                .set("views", it.views)
                .currentDate("updated_at")
            mongoTemplate.upsert(
                Query(
                    Criteria("_id").`is`(it.id)
                ),
                update,
                Article::class.java
            )
        }
        val endTime = System.currentTimeMillis()
        println("time : ${endTime - startTime}") // 10000 : 5589, 100000 : 27129
    }
}