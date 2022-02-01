package com.may.mongodbpractice

import com.may.mongodbpractice.document.Address
import com.may.mongodbpractice.document.Gender
import com.may.mongodbpractice.document.Student
import com.may.mongodbpractice.repository.StudentRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import java.math.BigDecimal
import java.time.LocalDateTime
import javax.annotation.PostConstruct
import org.springframework.boot.autoconfigure.security.SecurityProperties.User
import java.lang.Exception


@SpringBootApplication
class MongodbPracticeApplication

fun main(args: Array<String>) {
    runApplication<MongodbPracticeApplication>(*args)
}

@Bean
fun insertRunner(studentRepository: StudentRepository) = CommandLineRunner {
    println("hi")
    val address = Address(
        country = "England",
        city = "London",
        postCode = "NE9"
    )

    val student = Student(
        firstName = "Ayoung",
        lastName = "Moon",
        email = "ayoung@gmail.com",
        gender = Gender.FEMALE,
        address = address,
        favoriteSubjects = listOf("Computer Science"),
        totalSpentInBooks = BigDecimal.TEN,
        createdDateTime = LocalDateTime.now()
    )
    studentRepository.insert(student)
}