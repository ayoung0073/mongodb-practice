package com.may.mongodbpractice

import com.may.mongodbpractice.document.Article
import com.may.mongodbpractice.document.Category
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update

@SpringBootTest
class MongoUpsertTest(
    @Autowired
    private val mongoTemplate: MongoTemplate
) {
    @Test
    fun `존재하지 않는 도큐먼트를 upsert하면 insert를 한다`() {
        val article = Article(
            id = ObjectId("61f90f5fd3d5c30dfe1a8f30"),
            articleName = "insert article",
            authorName = "애용",
            category = Category.BEAUTY,
            score = 90,
            views = 1000
        )
        val update = Update()
            .set("articleName", article.articleName)
            .set("authorName", article.authorName)
            .set("category", article.category)
            .set("views", article.views)
        mongoTemplate.upsert(
            Query(
                Criteria("_id").`is`(article.id)
            ),
            update.currentDate("updated_at"),
            Article::class.java
        )
    }

    @Test
    fun `존재하는 도큐먼트일 때, 새로운 필드가 있으면 추가만 하고 나머지 필드는 그대로다`() {
        val articleList = listOf(
            Article(
                id = ObjectId("61f90f5fd3d5c30dfe1a8f30"),
                articleName = "article1",
                authorName = "애용",
                category = Category.BEAUTY,
                score = 90,
                views = 1000
            ),
            Article(
                id = ObjectId("61f90f5fd3d5c30dfe1a8f32"),
                articleName = "insert article",
                authorName = "애용",
                category = Category.BEAUTY,
                score = 90,
                views = 1000
            )
        )
        articleList.forEach { article ->
            val update = Update()
                .set("newField", "new")
                .currentDate("updated_at")
            mongoTemplate.upsert(
                Query(
                    Criteria("_id").`is`(article.id)
                ),
                update,
                Article::class.java
            )
        }
    }

    @Test
    fun `존재하는 도큐먼트일 때, 기존 필드를 수정하면 해당 필드가 수정되고, 새로운 필드가 추가되면 필드가 추가된다`() {
        val article = Article(
            id = ObjectId("61f90f5fd3d5c30dfe1a8f30"),
            articleName = "insert article",
            authorName = "애용",
            category = Category.BEAUTY,
            score = 90,
            views = 1000
        )
        val update = Update()
            .set("newField2", "new2")
            .set("newField", "newnew")
            .currentDate("updated_at")

        mongoTemplate.upsert(
            Query(
                Criteria("_id").`is`(article.id)
            ),
            update,
            Article::class.java
        )
    }
}
