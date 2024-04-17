package com.example.mbcw

import android.os.Bundle
import android.util.Log
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import java.util.Random
import com.example.mbcw.ui.theme.MBCWTheme
import com.example.mbcw.utils.readCountryDataFromAssets

class GuessTheFlag : ComponentActivity() {
    private lateinit var correctCountry: String
    private var isCorrect by mutableStateOf(false)
    private lateinit var displayedFlags: List<Pair<String, String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Load country flags data
        val countryDataList = readCountryDataFromAssets(baseContext, "updated_countries.json")
        val countryFlagsList = countryDataList.map { it.second }

        Log.e("DataList", "Data: $countryDataList")

        // Generate 3 unique random flags
        displayedFlags = generateRandomFlags(countryFlagsList, countryDataList)

        // Shuffle the displayed flags
        displayedFlags = displayedFlags.shuffled()

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
                            text = "Guess the country corresponding to the flag: ${correctCountry}",
                            modifier = Modifier.padding(vertical = 8.dp)
                        )

                        displayedFlags.forEach { (flagUrl, _) ->
                            ClickableImage(
                                flagUrl = flagUrl,
                                onClick = {
                                    Log.e("Flag", "Selected Flag: $flagUrl")
                                    onFlagClicked(flagUrl, countryDataList)
                                }
                            )
                        }

                        Button(
                            onClick = {
                                // Reset UI for next round
                                isCorrect = false
                                // Generate new set of flags
                                displayedFlags = generateRandomFlags(countryFlagsList, countryDataList)
                                // Shuffle the displayed flags
                                displayedFlags = displayedFlags.shuffled()
                            }
                        ) {
                            Text(text = "Next")
                        }

                        val message = if (isCorrect) "CORRECT!" else "WRONG! The correct country is: $correctCountry"
                        val color = if (isCorrect) Color.Green else Color.Red
                        Text(
                            text = message,
                            color = color,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }
            }
        }
    }

    private fun onFlagClicked(flagUrl: String, countryDataList: List<Pair<String, String>>) {
        var countryName = countryDataList.firstOrNull { it.second == "${flagUrl}.png" }?.first ?: "Unknown"

        if (countryName == correctCountry) {
            isCorrect = true
        } else {
            isCorrect = false
        }
    }

    private fun generateRandomFlags(countryFlagsList: List<String>, countryDataList: List<Pair<String, String>>): List<Pair<String, String>> {
        val randomFlags = mutableListOf<Pair<String, String>>()
        val random = Random()
        val maxIndex = countryFlagsList.size - 1

        // Ensure we have enough unique flags
        while (randomFlags.size < 3) {
            val randomIndex = random.nextInt(maxIndex)
            val randomFlag = countryFlagsList[randomIndex].substringBefore(".")

            if (!randomFlags.any { it.first == randomFlag }) {
                // Choose one of the flags as the correct flag for the user to guess
                val countryName = countryDataList.firstOrNull { it.second == "${randomFlag}.png" }?.first ?: "Unknown"
                correctCountry = countryName
                randomFlags.add(Pair(randomFlag, ""))
            }
        }

        return randomFlags
    }
}

@Composable
fun ClickableImage(
    flagUrl: String,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    Image(
        painter = painterResource(id = context.resources.getIdentifier(flagUrl, "drawable", context.packageName)),
        contentDescription = "Flag Image",
        modifier = Modifier
            .size(125.dp)
            .clickable(onClick = onClick)
    )
}