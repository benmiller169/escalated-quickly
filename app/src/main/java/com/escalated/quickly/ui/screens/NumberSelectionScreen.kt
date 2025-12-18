package com.escalated.quickly.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.escalated.quickly.ui.components.AnimatedNumberReveal
import com.escalated.quickly.ui.components.GradientButton
import com.escalated.quickly.ui.components.PlayerProgressIndicator
import com.escalated.quickly.ui.theme.*

@Composable
fun NumberSelectionScreen(
    currentPlayerIndex: Int,
    totalPlayers: Int,
    revealedNumber: Int?,
    onRevealNumber: () -> Unit,
    onConfirmNumber: () -> Unit,
    onBackHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(OrangeBurnt.copy(alpha = 0.2f), Cream, OrangeLight.copy(alpha = 0.1f))
                )
            )
    ) {
        // Home button in top-left corner
        FloatingActionButton(
            onClick = onBackHome,
            modifier = Modifier
                .align(Alignment.TopStart)
                .statusBarsPadding()
                .padding(16.dp),
            containerColor = OrangeBurnt,
            contentColor = White,
            shape = CircleShape
        ) {
            Icon(
                Icons.Default.Home,
                contentDescription = "Go home"
            )
        }
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Progress indicator
            PlayerProgressIndicator(
                currentPlayer = currentPlayerIndex,
                totalPlayers = totalPlayers,
                modifier = Modifier.padding(top = 24.dp)
            )
            
            // Main content
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                if (revealedNumber == null) {
                    // "Give me a number" state
                    Text(
                        text = "🤫",
                        fontSize = 64.sp
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Text(
                        text = "Your turn!",
                        style = MaterialTheme.typography.headlineLarge,
                        color = DarkText,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Text(
                        text = "Keep your number secret",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MediumText,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(48.dp))
                    
                    GradientButton(
                        text = "Give Me a Number",
                        onClick = onRevealNumber,
                        modifier = Modifier
                            .scale(pulseScale)
                            .fillMaxWidth(0.8f)
                    )
                } else {
                    // Number revealed state
                    Text(
                        text = "Your secret number is...",
                        style = MaterialTheme.typography.titleLarge,
                        color = MediumText
                    )
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    AnimatedNumberReveal(number = revealedNumber)
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Text(
                        text = "Remember this number!",
                        style = MaterialTheme.typography.headlineSmall,
                        color = OrangeBurnt,
                        fontWeight = FontWeight.SemiBold
                    )
                    
                    Spacer(modifier = Modifier.height(48.dp))
                    
                    GradientButton(
                        text = "Got it! Pass the phone",
                        onClick = onConfirmNumber,
                        modifier = Modifier.fillMaxWidth(0.8f),
                        colors = listOf(TealAccent, TealDark)
                    )
                }
            }
            
            // Instruction at bottom
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = White.copy(alpha = 0.9f))
            ) {
                Text(
                    text = if (revealedNumber == null) 
                        "Tap the button to reveal your secret number" 
                    else 
                        "After confirming, pass the phone to the next player",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MediumText,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}
