package com.example.mbcw

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mbcw.ui.theme.MBCWTheme

class AdvancedLevel : ComponentActivity() {
    private var incorrectAttempts = 0
    private var score by mutableStateOf(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MBCWTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Guess the country names:",
                            modifier = Modifier.padding(vertical = 8.dp)
                        )

                        Button(
                            onClick = {
                                submitGuess()
                            }
                        ) {
                            Text(text = "Submit")
                        }

                        Text(
                            text = "Score: $score",
                            modifier = Modifier
                                .align(Alignment.End)
                                .padding(top = 8.dp)
                        )
                    }
                }
            }
        }
    }

    private fun submitGuess() {
        var isCorrect = true

        if (isCorrect) {
            score += 3 - incorrectAttempts
            showResult("CORRECT!", Color.Green)
        } else {
            incorrectAttempts++
            if (incorrectAttempts >= 3) {
                showResult("WRONG!", Color.Red)
                displayCorrectAnswers()
            } else {
                showResult("WRONG!", Color.Red)
            }
        }
    }
    private fun showResult(message: String, color: Color) {
    }

    private fun displayCorrectAnswers() {
    }
}