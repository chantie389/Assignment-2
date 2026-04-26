package com.example.a2imad

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.a2imad.ui.theme.A2IMADTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a2imad.Question

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
                A2IMADTheme{
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

// Handles which screen to show
@Composable
fun AppNavigation() {
    var currentScreen by remember { mutableStateOf("welcome") }
    var score by remember { mutableIntStateOf(0) }

    when (currentScreen) {
        "welcome" -> WelcomeScreen(onStartClick = { currentScreen = "quiz" })
        "quiz" -> QuizScreen(
            onQuizComplete = { finalScore ->
                score = finalScore
                currentScreen = "score"
            }
        )
        "score" -> ScoreScreen(
            score = score,
            totalQuestions = questions.size,
            onReviewClick = { currentScreen = "review" }
        )
        "review" -> ReviewScreen(onBackClick = { currentScreen = "welcome" })
    }
}

//  WELCOME SCREEN
@Composable
fun WelcomeScreen(onStartClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Life Hack or Urban Myth?",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Test your knowledge! Can you tell the difference between real life hacks and viral myths?",
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onStartClick) {
            Text("Start Quiz")
        }
    }
}

// QUIZ SCREEN
@Composable
fun QuizScreen(onQuizComplete: (Int) -> Unit) {
    var currentIndex by remember { mutableIntStateOf(0) }
    var score by remember { mutableIntStateOf(0) }
    var showFeedback by remember { mutableStateOf(false) }
    var isCorrect by remember { mutableStateOf(false) }

    val currentQuestion = questions[currentIndex]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Question ${currentIndex + 1}/${questions.size}",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = currentQuestion.statement,
            fontSize = 20.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))

        // Answer Buttons
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = {
                    isCorrect = currentQuestion.isHack
                    showFeedback = true
                    if (isCorrect) score++
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Hack (True)")
            }

            Button(
                onClick = {
                    isCorrect = !currentQuestion.isHack
                    showFeedback = true
                    if (isCorrect) score++
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Myth (False)")
            }
        }

        // Feedback
        if (showFeedback) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = if (isCorrect) "Correct! That's a real time saver!" else "Wrong! That's just an urban myth.",
                fontSize = 16.sp,
                color = if (isCorrect) androidx.compose.ui.graphics.Color.Green else androidx.compose.ui.graphics.Color.Red,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                if (currentIndex < questions.size - 1) {
                    currentIndex++
                    showFeedback = false
                } else {
                    onQuizComplete(score)
                }
            }) {
                Text("Next")
            }
        }
    }
}

// SCORE SCREEN
@Composable
fun ScoreScreen(score: Int, totalQuestions: Int, onReviewClick: () -> Unit) {
    val feedback = when {
        score >= totalQuestions * 0.8 -> "Master Hacker! You know your stuff!"
        score >= totalQuestions * 0.5 -> "Good job! Keep learning!"
        else -> " Stay Safe Online! Better luck next time."
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Quiz Complete!",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Your Score: $score / $totalQuestions",
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = feedback,
            fontSize = 18.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onReviewClick) {
            Text("Review Answers")
        }
    }
}

// REVIEW SCREEN (using LazyColumn)
@Composable
fun ReviewScreen(onBackClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "Review All Questions",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // LazyColumn for scrollable list
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(questions) { question ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(
                        text = question.statement,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Answer: ${if (question.isHack) "Hack" else "Myth"}",
                        color = if (question.isHack) androidx.compose.ui.graphics.Color.Green else androidx.compose.ui.graphics.Color.Red
                    )
                    Text(text = "Explanation: ${question.explanation}")
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onBackClick,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Back to Start")
        }
    }
}

// Sample Questions List
val questions = listOf(
    Question(
        statement = "Reading in dim light permanently damages your eyes.",
        isHack = false,
        explanation = "It causes temporary eye strain but no permanent damage."
    ),
    Question(
        statement = "Eating carrots improves your night vision.",
        isHack = false,
        explanation = "A myth spread during WWII to hide radar technology secrets."
    ),
    Question(
        statement = "Putting batteries in the fridge makes them last longer.",
        isHack = false,
        explanation = "Moisture can damage them. Store in a cool, dry place instead."
    ),
    Question(
        statement = "To remove a sticker easily, heat it with a hairdryer first.",
        isHack = true,
        explanation = "heat softens the adhesive, making it peel off cleanly."
    )
)
