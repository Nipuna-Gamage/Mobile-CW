package com.example.mbcw

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mbcw.ui.theme.MBCWTheme
import kotlin.random.Random

val countries = listOf("Andorra", "United Arab Emirates", "Afghanistan","Antigua and Barbuda","Anguilla","Albania","Armenia","Angola","Antarctica","Argentina",)
val flags = listOf(R.drawable.ad, R.drawable.ae, R.drawable.af, R.drawable.ag, R.drawable.ai, R.drawable.al, R.drawable.am, R.drawable.ao, R.drawable.aq, R.drawable.ar)

class GuessTheCountry : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MBCWTheme {
                GuessCountryScreen()

            }
        }
    }
}

@Composable
fun GuessCountryScreen() {
    var selectedCountry by remember { mutableStateOf("") }
    var flagIndex by remember { mutableStateOf(Random.nextInt(flags.size)) }
    var showMessage by remember { mutableStateOf(false) }
    var isCorrect by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (showMessage) {
            if (isCorrect) {
                Text(
                    text = "CORRECT!",
                    color = Color.Green
                )
            } else {
                Text(
                    text = "WRONG!",
                    color = Color.Red
                )
            }
            Text(
                text = "Correct country: ${countries[flagIndex]}",
                color = Color.Blue
            )

        } else {
            Image(
                painter = painterResource(id = flags[flagIndex]),
                contentDescription = "Flag Image",
                modifier = Modifier.size(200.dp)
            )
            Column {
                countries.forEach { country ->
                    Text(
                        text = country,
                        modifier = Modifier.clickable {
                            selectedCountry = country
                        }
                    )
                }
                Button(onClick = {
                    showMessage = true
                    isCorrect = selectedCountry == countries[flagIndex]
                }) {
                    Text("Submit")
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewGuessTheCountryScreen() {
    GuessCountryScreen()
}