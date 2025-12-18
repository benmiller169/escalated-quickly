package com.escalated.quickly.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.escalated.quickly.data.Question
import com.escalated.quickly.ui.components.GradientButton
import com.escalated.quickly.ui.theme.*

@Composable
fun DiscussionScreen(
    question: Question?,
    playerCount: Int,
    onProceedToOrdering: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val emojiScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "emojiScale"
    )
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(OrangePrimary.copy(alpha = 0.1f), Cream, TealAccent.copy(alpha = 0.1f))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            
            // Header
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "🎉",
                    fontSize = 64.sp,
                    modifier = Modifier.scale(emojiScale)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Everyone's Ready!",
                    style = MaterialTheme.typography.headlineLarge,
                    color = OrangeBurnt,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                
                Text(
                    text = "All $playerCount players have their secret numbers",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MediumText,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            
            // Question reminder
            if (question != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "THE QUESTION",
                            style = MaterialTheme.typography.labelMedium,
                            color = OrangePrimary,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Text(
                            text = question.question,
                            style = MaterialTheme.typography.headlineSmall,
                            color = DarkText,
                            textAlign = TextAlign.Center
                        )
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        // Scale reminder
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(TealAccent),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "1",
                                        style = MaterialTheme.typography.titleLarge,
                                        color = White,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = question.meaning1,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MediumText,
                                    textAlign = TextAlign.Center
                                )
                            }
                            
                            Text(
                                text = "→",
                                style = MaterialTheme.typography.headlineMedium,
                                color = LightText,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                            
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(OrangePrimary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "10",
                                        style = MaterialTheme.typography.titleLarge,
                                        color = White,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = question.meaning10,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MediumText,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
            
            // Instructions and button
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 32.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = OrangeLight.copy(alpha = 0.3f))
                ) {
                    Text(
                        text = "💬 Discuss your answers with the group!\nThen guess who got which number.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = DarkText,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                GradientButton(
                    text = "Proceed to Ranking",
                    onClick = onProceedToOrdering,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
