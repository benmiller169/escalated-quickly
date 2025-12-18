package com.escalated.quickly.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.escalated.quickly.data.AppDatabase
import com.escalated.quickly.data.Question
import com.escalated.quickly.data.QuestionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class GamePhase {
    HOME,
    QUESTION,
    NUMBER_SELECTION,
    DISCUSSION,
    ORDERING
}

data class GameState(
    val phase: GamePhase = GamePhase.HOME,
    val playerCount: Int = 3,
    val currentQuestion: Question? = null,
    val assignedNumbers: List<Int> = emptyList(),
    val currentPlayerIndex: Int = 0,
    val revealedNumber: Int? = null,
    val usedQuestionIds: Set<Int> = emptySet(),
    val isLoading: Boolean = false,
    val showAddQuestionDialog: Boolean = false
)

class GameViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: QuestionRepository
    
    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()
    
    init {
        val database = AppDatabase.getDatabase(application)
        repository = QuestionRepository(database.questionDao(), application)
        
        viewModelScope.launch {
            repository.initializeQuestionsIfNeeded()
        }
    }
    
    fun setPlayerCount(count: Int) {
        if (count in 2..9) {
            _gameState.value = _gameState.value.copy(playerCount = count)
        }
    }
    
    fun startGame() {
        viewModelScope.launch {
            _gameState.value = _gameState.value.copy(isLoading = true)
            val question = repository.getRandomQuestion()
            _gameState.value = _gameState.value.copy(
                phase = GamePhase.QUESTION,
                currentQuestion = question,
                assignedNumbers = emptyList(),
                currentPlayerIndex = 0,
                revealedNumber = null,
                usedQuestionIds = question?.let { setOf(it.id) } ?: emptySet(),
                isLoading = false
            )
        }
    }
    
    fun skipQuestion() {
        viewModelScope.launch {
            _gameState.value = _gameState.value.copy(isLoading = true)
            val currentState = _gameState.value
            val question = repository.getRandomQuestionExcluding(currentState.usedQuestionIds)
            if (question != null) {
                _gameState.value = currentState.copy(
                    currentQuestion = question,
                    usedQuestionIds = currentState.usedQuestionIds + question.id,
                    isLoading = false
                )
            } else {
                // All questions used, get any random one
                val anyQuestion = repository.getRandomQuestion()
                _gameState.value = currentState.copy(
                    currentQuestion = anyQuestion,
                    isLoading = false
                )
            }
        }
    }
    
    fun useCustomQuestion(question: Question) {
        _gameState.value = _gameState.value.copy(
            currentQuestion = question,
            usedQuestionIds = _gameState.value.usedQuestionIds + question.id,
            showAddQuestionDialog = false
        )
    }
    
    fun startNumberSelection() {
        _gameState.value = _gameState.value.copy(
            phase = GamePhase.NUMBER_SELECTION,
            assignedNumbers = emptyList(),
            currentPlayerIndex = 0,
            revealedNumber = null
        )
    }
    
    fun revealNumber() {
        val currentState = _gameState.value
        val availableNumbers = (1..10).filter { it !in currentState.assignedNumbers }
        if (availableNumbers.isNotEmpty()) {
            val randomNumber = availableNumbers.random()
            _gameState.value = currentState.copy(revealedNumber = randomNumber)
        }
    }
    
    fun confirmNumber() {
        val currentState = _gameState.value
        val revealedNumber = currentState.revealedNumber ?: return
        
        val newAssignedNumbers = currentState.assignedNumbers + revealedNumber
        val newPlayerIndex = currentState.currentPlayerIndex + 1
        
        if (newPlayerIndex >= currentState.playerCount) {
            // All players have numbers, move to discussion
            _gameState.value = currentState.copy(
                phase = GamePhase.DISCUSSION,
                assignedNumbers = newAssignedNumbers,
                currentPlayerIndex = newPlayerIndex,
                revealedNumber = null
            )
        } else {
            _gameState.value = currentState.copy(
                assignedNumbers = newAssignedNumbers,
                currentPlayerIndex = newPlayerIndex,
                revealedNumber = null
            )
        }
    }
    
    fun proceedToOrdering() {
        _gameState.value = _gameState.value.copy(phase = GamePhase.ORDERING)
    }
    
    fun playNewGame() {
        viewModelScope.launch {
            _gameState.value = _gameState.value.copy(isLoading = true)
            val question = repository.getRandomQuestionExcluding(_gameState.value.usedQuestionIds)
                ?: repository.getRandomQuestion()
            _gameState.value = _gameState.value.copy(
                phase = GamePhase.QUESTION,
                currentQuestion = question,
                assignedNumbers = emptyList(),
                currentPlayerIndex = 0,
                revealedNumber = null,
                usedQuestionIds = question?.let { _gameState.value.usedQuestionIds + it.id } 
                    ?: _gameState.value.usedQuestionIds,
                isLoading = false
            )
        }
    }
    
    fun backToHome() {
        _gameState.value = GameState()
    }
    
    fun showAddQuestionDialog() {
        _gameState.value = _gameState.value.copy(showAddQuestionDialog = true)
    }
    
    fun hideAddQuestionDialog() {
        _gameState.value = _gameState.value.copy(showAddQuestionDialog = false)
    }
    
    fun addCustomQuestion(questionText: String, meaning1: String, meaning10: String) {
        viewModelScope.launch {
            val newId = repository.addCustomQuestion(questionText, meaning1, meaning10)
            val newQuestion = Question(
                id = newId.toInt(),
                question = questionText,
                meaning1 = meaning1,
                meaning10 = meaning10,
                isCustom = true
            )
            _gameState.value = _gameState.value.copy(
                currentQuestion = newQuestion,
                showAddQuestionDialog = false
            )
        }
    }
}
