package com.may.mongodbpractice

import com.may.mongodbpractice.document.Article
import com.may.mongodbpractice.document.Category
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.repository.MongoRepository

@SpringBootTest
class MongoRepositoryTest(
    @Autowired
    private val articleRepository: MongoRepository<Article, ObjectId>
) {
    @Test
    fun saveOneTest() {
        articleRepository.save(
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
    fun saveAllTest() {
        articleRepository.saveAll(
            listOf(
                Article(
                    articleName = "건강 아티클",
                    authorName = "Jenny",
                    category = Category.HEALTH,
                    score = 90,
                    views = 1000
                ),
                Article(
                    articleName = "가을 뮤트톤",
                    authorName = "Jenny",
                    category = Category.BEAUTY,
                    score = 93,
                    views = 1300
                ),
                Article(
                    articleName = "고양이 간식",
                    authorName = "Maong",
                    category = Category.PET,
                    score = 88,
                    views = 1000
                ),
                Article(
                    articleName = "꾹꾹이",
                    authorName = "Maong",
                    category = Category.PET,
                    score = 91,
                    views = 1100
                )
            )
        )
    }
}