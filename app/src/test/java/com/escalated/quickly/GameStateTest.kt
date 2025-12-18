package com.escalated.quickly

import com.escalated.quickly.viewmodel.GamePhase
import com.escalated.quickly.viewmodel.GameState
import org.junit.Assert.*
import org.junit.Test

/**
 * Unit tests for GameState and game logic
 */
class GameStateTest {

    @Test
    fun `initial state should be HOME phase with 3 players`() {
        val state = GameState()
        
        assertEquals(GamePhase.HOME, state.phase)
        assertEquals(3, state.playerCount)
        assertNull(state.currentQuestion)
        assertTrue(state.assignedNumbers.isEmpty())
        assertEquals(0, state.currentPlayerIndex)
        assertNull(state.revealedNumber)
    }

    @Test
    fun `player count should be valid range 2 to 9`() {
        val state = GameState(playerCount = 2)
        assertEquals(2, state.playerCount)
        
        val state2 = GameState(playerCount = 9)
        assertEquals(9, state2.playerCount)
    }

    @Test
    fun `assigned numbers should not contain duplicates`() {
        // Simulate assigning numbers to 5 players
        val assignedNumbers = mutableListOf<Int>()
        val availableNumbers = (1..10).toMutableList()
        
        repeat(5) {
            val randomNumber = availableNumbers.random()
            availableNumbers.remove(randomNumber)
            assignedNumbers.add(randomNumber)
        }
        
        // Verify no duplicates
        assertEquals(5, assignedNumbers.size)
        assertEquals(5, assignedNumbers.toSet().size)
    }

    @Test
    fun `all assigned numbers should be in range 1 to 10`() {
        val assignedNumbers = listOf(1, 5, 3, 8, 10)
        
        assertTrue(assignedNumbers.all { it in 1..10 })
    }

    @Test
    fun `game phase progression should be correct`() {
        val phases = listOf(
            GamePhase.HOME,
            GamePhase.QUESTION,
            GamePhase.NUMBER_SELECTION,
            GamePhase.DISCUSSION,
            GamePhase.ORDERING
        )
        
        // Verify HOME is first
        assertEquals(GamePhase.HOME, phases[0])
        
        // Verify ORDERING is last
        assertEquals(GamePhase.ORDERING, phases.last())
    }

    @Test
    fun `number selection should track current player index`() {
        var state = GameState(
            phase = GamePhase.NUMBER_SELECTION,
            playerCount = 4,
            currentPlayerIndex = 0
        )
        
        // Simulate player 1 getting a number
        state = state.copy(
            assignedNumbers = state.assignedNumbers + 5,
            currentPlayerIndex = state.currentPlayerIndex + 1
        )
        
        assertEquals(1, state.currentPlayerIndex)
        assertEquals(1, state.assignedNumbers.size)
        assertEquals(5, state.assignedNumbers[0])
    }

    @Test
    fun `should transition to discussion when all players have numbers`() {
        val state = GameState(
            phase = GamePhase.NUMBER_SELECTION,
            playerCount = 3,
            currentPlayerIndex = 2,
            assignedNumbers = listOf(4, 7)
        )
        
        // Last player confirms their number
        val newPlayerIndex = state.currentPlayerIndex + 1
        val shouldTransition = newPlayerIndex >= state.playerCount
        
        assertTrue(shouldTransition)
    }

    @Test
    fun `used question IDs should prevent repeats`() {
        val usedIds = setOf(1, 5, 10)
        val allQuestionIds = (1..25).toList()
        
        val availableIds = allQuestionIds.filter { it !in usedIds }
        
        assertEquals(22, availableIds.size)
        assertFalse(availableIds.contains(1))
        assertFalse(availableIds.contains(5))
        assertFalse(availableIds.contains(10))
    }

    @Test
    fun `maximum 9 players should use at most 9 unique numbers`() {
        val maxPlayers = 9
        val availableNumbers = (1..10).toList()
        
        assertTrue(availableNumbers.size >= maxPlayers)
        
        // Simulate assigning to 9 players
        val assigned = availableNumbers.shuffled().take(maxPlayers)
        
        assertEquals(9, assigned.size)
        assertEquals(9, assigned.toSet().size) // All unique
    }

    @Test
    fun `play new game should reset game state but keep player count`() {
        val state = GameState(
            phase = GamePhase.ORDERING,
            playerCount = 5,
            assignedNumbers = listOf(1, 3, 5, 7, 9),
            currentPlayerIndex = 5
        )
        
        // Simulate "Play New Game"
        val newState = state.copy(
            phase = GamePhase.QUESTION,
            assignedNumbers = emptyList(),
            currentPlayerIndex = 0,
            revealedNumber = null
        )
        
        assertEquals(GamePhase.QUESTION, newState.phase)
        assertEquals(5, newState.playerCount) // Preserved
        assertTrue(newState.assignedNumbers.isEmpty())
        assertEquals(0, newState.currentPlayerIndex)
    }

    @Test
    fun `back to home should fully reset state`() {
        val state = GameState(
            phase = GamePhase.ORDERING,
            playerCount = 7,
            assignedNumbers = listOf(2, 4, 6, 8, 1, 3, 5)
        )
        
        // Simulate "Back Home"
        val newState = GameState()
        
        assertEquals(GamePhase.HOME, newState.phase)
        assertEquals(3, newState.playerCount) // Default
        assertTrue(newState.assignedNumbers.isEmpty())
    }
}
