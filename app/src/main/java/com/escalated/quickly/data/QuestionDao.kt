package com.escalated.quickly.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionDao {
    @Query("SELECT * FROM questions")
    fun getAllQuestions(): Flow<List<Question>>
    
    @Query("SELECT * FROM questions")
    suspend fun getAllQuestionsList(): List<Question>
    
    @Query("SELECT * FROM questions WHERE id = :id")
    suspend fun getQuestionById(id: Int): Question?
    
    @Query("SELECT * FROM questions WHERE isCustom = 1")
    fun getCustomQuestions(): Flow<List<Question>>
    
    @Query("SELECT COUNT(*) FROM questions")
    suspend fun getQuestionCount(): Int
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(question: Question): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<Question>)
    
    @Query("DELETE FROM questions WHERE id = :id")
    suspend fun deleteQuestion(id: Int)
    
    @Query("SELECT * FROM questions ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomQuestion(): Question?
}
