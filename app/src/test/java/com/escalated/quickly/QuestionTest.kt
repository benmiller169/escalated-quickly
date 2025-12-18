package com.escalated.quickly

import com.escalated.quickly.data.Question
import com.escalated.quickly.data.QuestionJson
import org.junit.Assert.*
import org.junit.Test

/**
 * Unit tests for Question data models
 */
class QuestionTest {

    @Test
    fun `QuestionJson toQuestion conversion should work correctly`() {
        val json = QuestionJson(
            id = 1,
            question = "Test question?",
            `1` = "Least likely",
            `10` = "Most likely"
        )
        
        val question = json.toQuestion()
        
        assertEquals(1, question.id)
        assertEquals("Test question?", question.question)
        assertEquals("Least likely", question.meaning1)
        assertEquals("Most likely", question.meaning10)
        assertFalse(question.isCustom)
    }

    @Test
    fun `custom question should have isCustom flag set`() {
        val customQuestion = Question(
            id = 100,
            question = "My custom question",
            meaning1 = "Low",
            meaning10 = "High",
            isCustom = true
        )
        
        assertTrue(customQuestion.isCustom)
    }

    @Test
    fun `bundled question should have isCustom false by default`() {
        val question = Question(
            id = 1,
            question = "Bundled question",
            meaning1 = "One",
            meaning10 = "Ten"
        )
        
        assertFalse(question.isCustom)
    }

    @Test
    fun `question should contain all required fields`() {
        val question = Question(
            id = 5,
            question = "Sample question text",
            meaning1 = "Meaning for 1",
            meaning10 = "Meaning for 10",
            isCustom = false
        )
        
        assertNotNull(question.id)
        assertNotNull(question.question)
        assertNotNull(question.meaning1)
        assertNotNull(question.meaning10)
    }

    @Test
    fun `random question selection from list should work`() {
        val questions = listOf(
            Question(1, "Q1", "1", "10"),
            Question(2, "Q2", "1", "10"),
            Question(3, "Q3", "1", "10")
        )
        
        val randomQuestion = questions.randomOrNull()
        
        assertNotNull(randomQuestion)
        assertTrue(questions.contains(randomQuestion))
    }

    @Test
    fun `excluding used questions should return remaining`() {
        val allQuestions = listOf(
            Question(1, "Q1", "1", "10"),
            Question(2, "Q2", "1", "10"),
            Question(3, "Q3", "1", "10"),
            Question(4, "Q4", "1", "10"),
            Question(5, "Q5", "1", "10")
        )
        
        val usedIds = setOf(1, 3)
        val availableQuestions = allQuestions.filter { it.id !in usedIds }
        
        assertEquals(3, availableQuestions.size)
        assertTrue(availableQuestions.none { it.id == 1 })
        assertTrue(availableQuestions.none { it.id == 3 })
    }

    @Test
    fun `empty question list should return null for random`() {
        val emptyList = emptyList<Question>()
        
        val result = emptyList.randomOrNull()
        
        assertNull(result)
    }
}
