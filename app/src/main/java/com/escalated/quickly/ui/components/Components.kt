package com.escalated.quickly.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.escalated.quickly.data.Question
import com.escalated.quickly.ui.theme.*

@Composable
fun QuestionCard(
    question: Question,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = OrangePrimary.copy(alpha = 0.3f),
                spotColor = OrangePrimary.copy(alpha = 0.3f)
            ),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Question text
            Text(
                text = question.question,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                color = DarkText,
                modifier = Modifier.padding(vertical = 24.dp)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Scale indicator row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                ScaleEndpoint(
                    number = 1,
                    label = question.meaning1,
                    color = TealAccent,
                    isLeft = true
                )
                
                Text(
                    text = "From",
                    style = MaterialTheme.typography.labelMedium,
                    color = LightText,
                    modifier = Modifier.padding(bottom = 40.dp)
                )
                
                ScaleEndpoint(
                    number = 10,
                    label = question.meaning10,
                    color = OrangePrimary,
                    isLeft = false
                )
            }
        }
    }
}

@Composable
private fun ScaleEndpoint(
    number: Int,
    label: String,
    color: Color,
    isLeft: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.width(120.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Number badge
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(color),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = number.toString(),
                style = MaterialTheme.typography.headlineMedium,
                color = White,
                fontWeight = FontWeight.Bold
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Label
        Text(
            text = label.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = MediumText,
            textAlign = TextAlign.Center,
            maxLines = 2,
            lineHeight = 14.sp
        )
    }
}

@Composable
fun AnimatedNumberReveal(
    number: Int?,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(
        targetValue = if (number != null) 1f else 0.5f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = 300f),
        label = "scale"
    )
    
    val bgColor by animateColorAsState(
        targetValue = when {
            number == null -> OrangeLight
            number <= 3 -> TealAccent
            number <= 7 -> OrangePrimary
            else -> OrangeBurnt
        },
        label = "bgColor"
    )
    
    Box(
        modifier = modifier
            .size((160 * scale).dp)
            .clip(RoundedCornerShape(32.dp))
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(bgColor, bgColor.copy(alpha = 0.8f))
                )
            )
            .shadow(
                elevation = 24.dp,
                shape = RoundedCornerShape(32.dp),
                ambientColor = bgColor.copy(alpha = 0.5f),
                spotColor = bgColor.copy(alpha = 0.5f)
            ),
        contentAlignment = Alignment.Center
    ) {
        if (number != null) {
            Text(
                text = number.toString(),
                style = MaterialTheme.typography.displayLarge.copy(
                    fontSize = (72 * scale).sp
                ),
                color = White,
                fontWeight = FontWeight.Bold
            )
        } else {
            Text(
                text = "?",
                style = MaterialTheme.typography.displayLarge.copy(
                    fontSize = (72 * scale).sp
                ),
                color = White.copy(alpha = 0.7f),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun GradientButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: List<Color> = listOf(OrangePrimary, OrangeBurnt)
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(56.dp)
            .shadow(
                elevation = if (enabled) 8.dp else 0.dp,
                shape = RoundedCornerShape(28.dp),
                ambientColor = colors[0].copy(alpha = 0.4f),
                spotColor = colors[0].copy(alpha = 0.4f)
            ),
        enabled = enabled,
        shape = RoundedCornerShape(28.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = if (enabled) colors else listOf(
                            Color.Gray.copy(alpha = 0.5f),
                            Color.Gray.copy(alpha = 0.3f)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                color = White,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(56.dp),
        shape = RoundedCornerShape(28.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = OrangePrimary
        ),
        border = ButtonDefaults.outlinedButtonBorder.copy(
            width = 2.dp,
            brush = Brush.horizontalGradient(listOf(OrangePrimary, OrangeBurnt))
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun PlayerProgressIndicator(
    currentPlayer: Int,
    totalPlayers: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Player ${currentPlayer + 1} of $totalPlayers",
            style = MaterialTheme.typography.titleMedium,
            color = DarkText
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(totalPlayers) { index ->
                Box(
                    modifier = Modifier
                        .size(if (index == currentPlayer) 12.dp else 8.dp)
                        .clip(RoundedCornerShape(50))
                        .background(
                            when {
                                index < currentPlayer -> TealAccent
                                index == currentPlayer -> OrangePrimary
                                else -> OrangeLight.copy(alpha = 0.5f)
                            }
                        )
                )
            }
        }
    }
}
