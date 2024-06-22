package com.example.mbcw

import android.os.Bundle
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
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
                    ) {
                        val checkedState = remember { mutableStateOf(false) }

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState()),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.guessgame),
                                contentDescription = "homePageImage"
                            )
                            Switch(
                                checked = checkedState.value,
                                onCheckedChange = { checkedState.value = it }
                            )
                            Button(
                                onClick = {
                                    Intent(applicationContext, GuessTheCountry::class.java).also {
                                        startActivity(it)
                                    }
                                },
                                modifier = Modifier.width(200.dp)
                            ) {
                                Text(text = "Guess the country")
                            }
                            Button(
                                onClick = {
                                    Intent(applicationContext, GuessHints::class.java).also {
                                        startActivity(it)
                                    }
                                },
                                modifier = Modifier.width(200.dp)
                            ) {
                                Text(text = "Guess hints")
                            }
                            Button(
                                onClick = {
                                    Intent(applicationContext, GuessTheFlag::class.java).also {
                                        startActivity(it)
                                    }
                                },
                                modifier = Modifier.width(200.dp)
                            ) {
                                Text(text = "Guess the flag")
                            }
                            Button(
                                onClick = {
                                    Intent(applicationContext, AdvancedLevel::class.java).also {
                                        startActivity(it)
                                    }
                                },
                                modifier = Modifier.width(200.dp)
                            ) {
                                Text(text = "Advanced level")
                            }
                        }
                    }
                }
            }
        }
    }
}
