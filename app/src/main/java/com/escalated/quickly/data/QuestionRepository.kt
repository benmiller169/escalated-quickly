package com.escalated.quickly.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class QuestionRepository(
    private val questionDao: QuestionDao,
    private val context: Context
) {
    val allQuestions: Flow<List<Question>> = questionDao.getAllQuestions()
    val customQuestions: Flow<List<Question>> = questionDao.getCustomQuestions()
    
    suspend fun initializeQuestionsIfNeeded() {
        withContext(Dispatchers.IO) {
            val count = questionDao.getQuestionCount()
            if (count == 0) {
                loadQuestionsFromAssets()
            }
        }
    }
    
    private suspend fun loadQuestionsFromAssets() {
        try {
            val jsonString = context.assets.open("questions.json")
                .bufferedReader()
                .use { it.readText() }
            
            val gson = Gson()
            val listType = object : TypeToken<List<QuestionJson>>() {}.type
            val questionsJson: List<QuestionJson> = gson.fromJson(jsonString, listType)
            
            val questions = questionsJson.map { it.toQuestion() }
            questionDao.insertQuestions(questions)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    suspend fun getAllQuestionsList(): List<Question> {
        return withContext(Dispatchers.IO) {
            questionDao.getAllQuestionsList()
        }
    }
    
    suspend fun getRandomQuestion(): Question? {
        return withContext(Dispatchers.IO) {
            questionDao.getRandomQuestion()
        }
    }
    
    suspend fun getRandomQuestionExcluding(excludeIds: Set<Int>): Question? {
        return withContext(Dispatchers.IO) {
            val allQuestions = questionDao.getAllQuestionsList()
            val availableQuestions = allQuestions.filter { it.id !in excludeIds }
            availableQuestions.randomOrNull()
        }
    }
    
    suspend fun addCustomQuestion(question: String, meaning1: String, meaning10: String): Long {
        return withContext(Dispatchers.IO) {
            val customQuestion = Question(
                question = question,
                meaning1 = meaning1,
                meaning10 = meaning10,
                isCustom = true
            )
            questionDao.insertQuestion(customQuestion)
        }
    }
    
    suspend fun deleteQuestion(id: Int) {
        withContext(Dispatchers.IO) {
            questionDao.deleteQuestion(id)
        }
    }
}
