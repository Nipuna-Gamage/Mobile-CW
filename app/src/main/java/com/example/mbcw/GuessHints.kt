package com.example.mbcw

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mbcw.ui.theme.MBCWTheme
import com.example.mbcw.utils.readCountryDataFromAssets
import java.util.Random

class GuessHints : ComponentActivity() {
    private var countryFlags = "randomIndex"
    private var correctCountry = ""
    private var displayedCountry = "randomIndex"
    private var remainingAttempts = 3
    private var isCorrect = false
    private var submittedGuess: String by mutableStateOf("")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val countryDataList = readCountryDataFromAssets(baseContext, "updated_countries.json")
        val countryNamesList = countryDataList.map { it.first }
        val countryFlagsList = countryDataList.map { it.second }

        val random = Random()
        val randomIndex = random.nextInt(countryFlagsList.size)
        val randomCountryFlag = countryFlagsList[randomIndex].substringBefore(".")

        correctCountry = countryNamesList[randomIndex]

        // Load country flags data
        loadCountryFlags()

        // Generate a random flag and initialize displayedCountry
        generateRandomFlag()

        setContent {
            MBCWTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Guess the country:",
                            fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = displayedCountry,
                            fontSize = 24.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        val flagResourceId = getResourceIdForRandomCountry()
                        if (flagResourceId != 0) {
                            Image(
                                painter = painterResource(id = flagResourceId),
                                contentDescription = "Flag Image",
                                modifier = Modifier.size(125.dp)
                            )
                        } else {
                            Log.e("FlagImage", "Resource not found for flag: $randomCountryFlag")
                        }
                        TextField(
                            value = submittedGuess,
                            onValueChange = { submittedGuess = it },
                            label = { Text("Enter a character") },
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { submitGuess() }
                        ) {
                            Text(text = "Submit")
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        if (isCorrect) {
                            Text(
                                text = "CORRECT!",
                                color = Color.Green,
                                fontSize = 20.sp
                            )
                        } else if (remainingAttempts == 0) {
                            Text(
                                text = "WRONG! The correct country is: $correctCountry",
                                color = Color.Red,
                                fontSize = 20.sp
                            )
                        }
                    }
                }
            }
        }
    }


    private fun loadCountryFlags(): Int {
        val countryDataList = readCountryDataFromAssets(baseContext, "updated_countries.json")
        val countryFlagsList = countryDataList.map { it.second }

        val random = Random()
        val randomIndex = random.nextInt(countryFlagsList.size)

        return resources.getIdentifier(countryFlagsList[randomIndex].substringBefore("."), "drawable", packageName)
    }

    private fun getResourceIdForRandomCountry(): Int {
        // Load country flags data
        val countryDataList = readCountryDataFromAssets(baseContext, "updated_countries.json")
        val countryFlagsList = countryDataList.map { it.second }

        val random = Random()
        val randomIndex = random.nextInt(countryFlagsList.size)

        return resources.getIdentifier(countryFlagsList[randomIndex].substringBefore("."), "drawable", packageName)
    }

    private fun generateRandomFlag() {
        displayedCountry = "-".repeat(correctCountry.length)
        isCorrect = false
        remainingAttempts = 3
    }

    private fun submitGuess() {
        if (submittedGuess.isEmpty()) return

        val guessedCharacter = submittedGuess[0]
        val stringBuilder = StringBuilder(displayedCountry)
        var found = false

        for (i in correctCountry.indices) {
            if (correctCountry[i].equals(guessedCharacter, ignoreCase = true)) {
                stringBuilder[i] = correctCountry[i]
                found = true
            }
        }

        if (!found) {
            remainingAttempts--
        }

        displayedCountry = stringBuilder.toString()
        submittedGuess = ""

        if (displayedCountry.equals(correctCountry, ignoreCase = true)) {
            isCorrect = true
        }
    }
}