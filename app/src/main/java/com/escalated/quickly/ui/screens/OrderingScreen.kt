package com.escalated.quickly.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.escalated.quickly.data.Question
import com.escalated.quickly.ui.components.GradientButton
import com.escalated.quickly.ui.components.SecondaryButton
import com.escalated.quickly.ui.theme.*

@Composable
fun OrderingScreen(
    question: Question?,
    playerCount: Int,
    onPlayNewGame: () -> Unit,
    onBackHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    val playerNames = remember { mutableStateListOf<String>().apply { 
        repeat(playerCount) { add("") }
    } }
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Cream, White, OrangeLight.copy(alpha = 0.1f))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            
            // Header
            Text(
                text = "🏆",
                style = MaterialTheme.typography.displayMedium
            )
            
            Text(
                text = "Rank the Players!",
                style = MaterialTheme.typography.headlineLarge,
                color = OrangeBurnt,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Question reminder
            if (question != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = question.question,
                            style = MaterialTheme.typography.bodyLarge,
                            color = DarkText,
                            textAlign = TextAlign.Center
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "1 = ${question.meaning1}",
                                style = MaterialTheme.typography.labelSmall,
                                color = TealAccent
                            )
                            Text(
                                text = "10 = ${question.meaning10}",
                                style = MaterialTheme.typography.labelSmall,
                                color = OrangePrimary
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Scale header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "← ${question?.meaning1 ?: "1"}",
                    style = MaterialTheme.typography.labelMedium,
                    color = TealAccent,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "${question?.meaning10 ?: "10"} →",
                    style = MaterialTheme.typography.labelMedium,
                    color = OrangePrimary,
                    fontWeight = FontWeight.SemiBold
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Player name inputs
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    playerNames.forEachIndexed { index, name ->
                        val progress = (index + 1).toFloat() / playerCount
                        val indicatorColor = lerp(TealAccent, OrangePrimary, progress)
                        
                        RankInputRow(
                            rank = index + 1,
                            name = name,
                            onNameChange = { playerNames[index] = it },
                            indicatorColor = indicatorColor,
                            isLast = index == playerCount - 1,
                            onNext = { 
                                if (index < playerCount - 1) {
                                    focusManager.moveFocus(FocusDirection.Down)
                                } else {
                                    focusManager.clearFocus()
                                }
                            }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Action buttons
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                GradientButton(
                    text = "Play New Game",
                    onClick = onPlayNewGame,
                    modifier = Modifier.fillMaxWidth()
                )
                
                SecondaryButton(
                    text = "Back to Home",
                    onClick = onBackHome,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun RankInputRow(
    rank: Int,
    name: String,
    onNameChange: (String) -> Unit,
    indicatorColor: androidx.compose.ui.graphics.Color,
    isLast: Boolean,
    onNext: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Rank number
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(indicatorColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = rank.toString(),
                style = MaterialTheme.typography.titleMedium,
                color = White,
                fontWeight = FontWeight.Bold
            )
        }
        
        // Name input
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            placeholder = { Text("Player name") },
            modifier = Modifier.weight(1f),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = indicatorColor,
                cursorColor = indicatorColor
            ),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = if (isLast) ImeAction.Done else ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { onNext() },
                onDone = { onNext() }
            )
        )
    }
}

// Color interpolation helper
private fun lerp(start: androidx.compose.ui.graphics.Color, end: androidx.compose.ui.graphics.Color, fraction: Float): androidx.compose.ui.graphics.Color {
    return androidx.compose.ui.graphics.Color(
        red = start.red + (end.red - start.red) * fraction,
        green = start.green + (end.green - start.green) * fraction,
        blue = start.blue + (end.blue - start.blue) * fraction,
        alpha = start.alpha + (end.alpha - start.alpha) * fraction
    )
}
