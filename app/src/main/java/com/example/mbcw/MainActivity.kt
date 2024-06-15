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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                    ) {

                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Guess Country",
                                fontSize = 35.sp,
                                fontStyle = FontStyle.Italic,
                                modifier = Modifier.padding(30.dp)
                            )
                            Button(onClick = { Intent(applicationContext, GuessTheCountry::class.java ).also { startActivity(it) } } ,modifier = Modifier.width(200.dp)) {
                                Text(text = "Guess the country")
                            }
                            Button(onClick = { Intent(applicationContext, GuessHints::class.java ).also { startActivity(it) } },modifier = Modifier.width(200.dp)) {
                                Text(text = "Guess hints")
                            }
                            Button(onClick = { Intent(applicationContext, GuessTheFlag::class.java).also { startActivity(it) } },modifier = Modifier.width(200.dp)) {
                                Text(text = "Guess the flag")
                            }
                            Button(onClick = { Intent(applicationContext, AdvancedLevel::class.java).also { startActivity(it) } },modifier = Modifier.width(200.dp)) {
                                Text(text = "Advanced level")
                            }
                        }
                    }
                }
            }
        }
    }
}