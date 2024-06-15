package com.example.mbcw

import android.os.Bundle
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mbcw.ui.theme.MBCWTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MBCWTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {

                        Column(
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Button(onClick = { Intent(applicationContext, GuessTheCountry::class.java ).also { startActivity(it) } } ) {
                                Text(text = "Guess the country")
                            }
                            Button(onClick = { Intent(applicationContext, GuessHints::class.java ).also { startActivity(it) } }) {
                                Text(text = "Guess hints")
                            }
                            Button(onClick = { Intent(applicationContext, GuessTheFlag::class.java).also { startActivity(it) } }) {
                                Text(text = "Guess the flag")
                            }
                            Button(onClick = { Intent(applicationContext, AdvancedLevel::class.java).also { startActivity(it) } }) {
                                Text(text = "Advanced level")
                            }
                        }
                    }
                }
            }
        }
    }
}