package com.escalated.quickly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.escalated.quickly.ui.screens.*
import com.escalated.quickly.ui.theme.ThatEscalatedQuicklyTheme
import com.escalated.quickly.viewmodel.GamePhase
import com.escalated.quickly.viewmodel.GameViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ThatEscalatedQuicklyTheme {
                EscalatedQuicklyApp()
            }
        }
    }
}

@Composable
fun EscalatedQuicklyApp(
    viewModel: GameViewModel = viewModel()
) {
    val gameState by viewModel.gameState.collectAsState()
    
    // Show add question dialog if needed
    if (gameState.showAddQuestionDialog) {
        AddQuestionDialog(
            onDismiss = { viewModel.hideAddQuestionDialog() },
            onConfirm = { question, meaning1, meaning10 ->
                viewModel.addCustomQuestion(question, meaning1, meaning10)
            }
        )
    }
    
    // Main content based on game phase
    when (gameState.phase) {
        GamePhase.HOME -> {
            HomeScreen(
                playerCount = gameState.playerCount,
                onPlayerCountChange = { viewModel.setPlayerCount(it) },
                onStartGame = { viewModel.startGame() },
                modifier = Modifier.fillMaxSize()
            )
        }
        
        GamePhase.QUESTION -> {
            QuestionScreen(
                question = gameState.currentQuestion,
                onSkipQuestion = { viewModel.skipQuestion() },
                onAddQuestion = { viewModel.showAddQuestionDialog() },
                onStartRound = { viewModel.startNumberSelection() },
                onBackHome = { viewModel.backToHome() },
                isLoading = gameState.isLoading,
                modifier = Modifier.fillMaxSize()
            )
        }
        
        GamePhase.NUMBER_SELECTION -> {
            NumberSelectionScreen(
                currentPlayerIndex = gameState.currentPlayerIndex,
                totalPlayers = gameState.playerCount,
                revealedNumber = gameState.revealedNumber,
                onRevealNumber = { viewModel.revealNumber() },
                onConfirmNumber = { viewModel.confirmNumber() },
                onBackHome = { viewModel.backToHome() },
                modifier = Modifier.fillMaxSize()
            )
        }
        
        GamePhase.DISCUSSION -> {
            DiscussionScreen(
                question = gameState.currentQuestion,
                playerCount = gameState.playerCount,
                onProceedToOrdering = { viewModel.proceedToOrdering() },
                onBackHome = { viewModel.backToHome() },
                modifier = Modifier.fillMaxSize()
            )
        }
        
        GamePhase.ORDERING -> {
            OrderingScreen(
                question = gameState.currentQuestion,
                playerCount = gameState.playerCount,
                onPlayNewGame = { viewModel.playNewGame() },
                onBackHome = { viewModel.backToHome() },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
