package com.escalated.quickly.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "questions")
data class Question(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val question: String,
    val meaning1: String,
    val meaning10: String,
    val isCustom: Boolean = false
)

/**
 * Data class for parsing questions from JSON assets file
 */
data class QuestionJson(
    val id: Int,
    val question: String,
    val `1`: String,
    val `10`: String
) {
    fun toQuestion(): Question = Question(
        id = id,
        question = question,
        meaning1 = `1`,
        meaning10 = `10`,
        isCustom = false
    )
}
