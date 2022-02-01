package com.may.mongodbpractice

import com.may.mongodbpractice.document.Article
import com.may.mongodbpractice.document.Category
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoTemplate

@SpringBootTest
class MongoTemplateSaveTest(
    @Autowired
    private val mongoTemplate: MongoTemplate
) {
    @Test
    fun saveTest() {
        mongoTemplate.save(
            Article(
                articleName = "건강 아티클",
                authorName = "Jenny",
                category = Category.HEALTH,
                score = 90,
                views = 1000
            )
        )
    }

    @Test
    fun `save는 같은 _id를 가진 아티클을 저장하면 덮어씌운다`() {
        mongoTemplate.save(
            Article(
                id = ObjectId("61f90f5fd3d5c30dfe1a8f08"),
                articleName = "건강 아티클 2",
                authorName = "Jenny",
                category = Category.HEALTH,
                score = 90,
                views = 1000
            )
        )
    }

    @Test
    fun `insert는 같은 _id를 가진 아티클을 저장하면 예외가 발생한다`() {
        assertThrows<Exception> {
            mongoTemplate.insert(
                Article(
                    id = ObjectId("61f90f5fd3d5c30dfe1a8f08"),
                    articleName = "건강 아티클 3",
                    authorName = "Jenny",
                    category = Category.HEALTH,
                    score = 90,
                    views = 1000
                )
            )
        }
    }

    @Test
    fun insertAllTest() {
        mongoTemplate.insertAll(
            listOf(
                Article(
                    articleName = "마라탕 목이버섯 vs 잡채 목이버섯",
                    authorName = "Maong",
                    category = Category.FOOD,
                    score = 80,
                    views = 800
                ),
                Article(
                    articleName = "강아지 간식 추천 리스트",
                    authorName = "Maum",
                    category = Category.PET,
                    score = 98,
                    views = 500
                ),
                Article(
                    articleName = "임인년",
                    authorName = "Maong",
                    category = Category.JUST,
                    score = 70,
                    views = 500
                ),
                Article(
                    articleName = "마라탕 0.5단계",
                    authorName = "Jenny",
                    category = Category.FOOD,
                    score = 94,
                    views = 1800
                )
            )
        )
    }
}