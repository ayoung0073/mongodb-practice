package com.may.mongodbpractice

import com.may.mongodbpractice.document.Article
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.aggregation.Aggregation.*

@SpringBootTest
class MongoAggregateTest(
    @Autowired
    private val mongoTemplate: MongoTemplate
) {
    @Test
    fun `저자가 Jenny인 아티클을 찾는다(aggregate)`() {
        val matchOperation = match(
            Criteria.where("authorName").`is`("Jenny")
        )

        val aggregation = newAggregation(
            matchOperation
        )

        val result = mongoTemplate.aggregate(aggregation, "article", Article::class.java)
        println(result.mappedResults.size)
    }

    @Test
    fun `저자를 그룹화하여 해당 저자의 아티클들의 총점을 구한다`() {
        val groupOperation = group("authorName").sum("score").`as`("totalScore")

        val projectOperation = project()
            .and("_id").`as`("authorName")
            .and("totalScore").`as`("totalScore")

        val aggregation = newAggregation(
            groupOperation,
            projectOperation
        )

        val result = mongoTemplate.aggregate(aggregation, Article::class.java, AuthorScoreReport::class.java)

        for (mappedResult in result.mappedResults) {
            println("${mappedResult.authorName} ${mappedResult.totalScore}")
        }
    }

    @Test
    fun `저자가 Jenny인 사용자를 찾는다(find)`() {
        val query = Query(Criteria.where("authorName").`is`("Jenny"))

        val result = mongoTemplate.find(query, Article::class.java)
        println(result.size)
    }
}