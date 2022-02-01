package com.may.mongodbpractice

import com.may.mongodbpractice.document.Article
import com.may.mongodbpractice.vo.ArticleWithScoreRate
import com.may.mongodbpractice.vo.AuthorScoreReport
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.aggregation.Aggregation.*
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators

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
        /*
            Jenny 640
            Maong 508
            Maum 98
         */
    }

    @Test
    fun `addFields로 새로운 field를 추가한다`() {
        val scoreRateExpression = ArithmeticOperators.valueOf("score").divideBy("views")
        val addFieldsOperation = addFields().addField("scoreRate").withValue(scoreRateExpression).build()
        val aggregation = newAggregation(addFieldsOperation)

        val result = mongoTemplate.aggregate(aggregation, Article::class.java, ArticleWithScoreRate::class.java)
        for (mappedResult in result.mappedResults) {
            println(mappedResult)
        }
    }

    @Test
    fun `원하는 필드로 sort를 한다`() {
        val sortOperation = sort(Sort.Direction.DESC, "authorName").and(Sort.Direction.ASC, "_id")
        val aggregation = newAggregation(sortOperation)

        val result = mongoTemplate.aggregate(aggregation, "article", Article::class.java)
        for (mappedResult in result.mappedResults) {
            println("${mappedResult.authorName} ${mappedResult.id}")
        }
        /*
            Maum 61f9145b8bb0e9658e6a3788
            Maong 61f9104fa7b3342df943ce61
            Maong 61f9104fa7b3342df943ce62
            Maong 61f91398b6c53846dcce61d0
            Maong 61f91398b6c53846dcce61d1
            Maong 61f9145b8bb0e9658e6a3787
            Maong 61f9145b8bb0e9658e6a3789
            Jenny 61f90f5fd3d5c30dfe1a8f08
            Jenny 61f9104fa7b3342df943ce5f
            Jenny 61f9104fa7b3342df943ce60
            Jenny 61f9114bcb65210e4b121306
            Jenny 61f91398b6c53846dcce61ce
            Jenny 61f91398b6c53846dcce61cf
            Jenny 61f9145b8bb0e9658e6a378a
         */
    }

    @Test
    fun `skip으로 원하는 양 이후의 도큐먼트를 가져온다`() {
        val sortOperation = sort(Sort.Direction.DESC, "authorName").and(Sort.Direction.ASC, "_id")
        val skipOperation = skip(7L)
        val aggregation = newAggregation(sortOperation, skipOperation)

        val result = mongoTemplate.aggregate(aggregation, "article", Article::class.java)
        for (mappedResult in result.mappedResults) {
            println("${mappedResult.authorName} ${mappedResult.id}")
        }
        /*
            Jenny 61f90f5fd3d5c30dfe1a8f08
            Jenny 61f9104fa7b3342df943ce5f
            Jenny 61f9104fa7b3342df943ce60
            Jenny 61f9114bcb65210e4b121306
            Jenny 61f91398b6c53846dcce61ce
            Jenny 61f91398b6c53846dcce61cf
            Jenny 61f9145b8bb0e9658e6a378a
         */
    }

    @Test
    fun `limit으로 원하는 양만큼의 도큐먼트를 가져온다`() {
        val sortOperation = sort(Sort.Direction.DESC, "authorName").and(Sort.Direction.ASC, "_id")
        val skipOperation = skip(7L)
        val limitOperation = limit(3L)
        val aggregation = newAggregation(sortOperation, skipOperation, limitOperation)

        val result = mongoTemplate.aggregate(aggregation, "article", Article::class.java)
        for (mappedResult in result.mappedResults) {
            println("${mappedResult.authorName} ${mappedResult.id}")
        }
        /*
            Jenny 61f90f5fd3d5c30dfe1a8f08
            Jenny 61f9104fa7b3342df943ce5f
            Jenny 61f9104fa7b3342df943ce60
         */
    }

    @Test
    fun `limit과 skip 순서로 도큐먼트를 가져온다`() {
        val sortOperation = sort(Sort.Direction.DESC, "authorName").and(Sort.Direction.ASC, "_id")
        val limitOperation = limit(7L)
        val skipOperation = skip(3L)
        val aggregation = newAggregation(sortOperation, limitOperation, skipOperation)

        val result = mongoTemplate.aggregate(aggregation, "article", Article::class.java)
        for (mappedResult in result.mappedResults) {
            println("${mappedResult.authorName} ${mappedResult.id}")
        }
        /*
            Maong 61f91398b6c53846dcce61d0
            Maong 61f91398b6c53846dcce61d1
            Maong 61f9145b8bb0e9658e6a3787
            Maong 61f9145b8bb0e9658e6a3789
         */
    }

    @Test
    fun `저자가 Jenny인 사용자를 찾는다(find)`() {
        val query = Query(Criteria.where("authorName").`is`("Jenny"))

        val result = mongoTemplate.find(query, Article::class.java)
        println(result.size)
    }
}