package com.example.mbcw

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.example.mbcw.ui.theme.MBCWTheme
import com.example.mbcw.utils.readCountryDataFromAssets
import java.util.Random

class GuessTheCountry : ComponentActivity() {
    private var selectedCountry = ""
    private var correctCountry = ""
    private var isCorrect by mutableStateOf(false)
    private var showNextButton by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Load country flags data
        val countryDataList = readCountryDataFromAssets(baseContext, "updated_countries.json")
        val countryNamesList = countryDataList.map { it.first }
        val countryFlagsList = countryDataList.map { it.second }

        val random = Random()
        val randomIndex = random.nextInt(countryFlagsList.size)
        val randomCountryFlag = countryFlagsList[randomIndex].substringBefore(".")

        val flagResourceId = resources.getIdentifier(randomCountryFlag, "drawable", packageName)

        correctCountry = countryNamesList[randomIndex]

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
                        Text(text = "Guess The Country", fontSize = 20.sp)

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
                        Spacer(modifier = Modifier.height(16.dp))

                        Text(text = "Select the correct country:", fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(8.dp))

                        ContentView(countryNamesList) { selectedCountry ->
                            this@GuessTheCountry.selectedCountry = selectedCountry
                        }
                        Spacer(modifier = Modifier.height(16.dp))

                        if (showNextButton) {
                            Button(onClick = {
                                generateNewImageAndReset()
                            }) {
                                Text(text = "Next", fontSize = 18.sp)
                            }
                        } else {
                            Button(onClick = {
                                checkAnswer()
                            }) {
                                Text(text = "Submit", fontSize = 18.sp)
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))

                        DisplayResult(isCorrect, correctCountry, selectedCountry)
                    }
                }
            }
        }
    }

    private fun checkAnswer() {
        isCorrect = false

        if (selectedCountry == correctCountry) {
            isCorrect = true
            selectedCountry = ""
        }
    }

    private fun generateNewImageAndReset() {
        correctCountry = ""
        isCorrect = false
        showNextButton = false
    }

    private fun getResourceIdForRandomCountry(): Int {
        // Load country flags data
        val countryDataList = readCountryDataFromAssets(baseContext, "updated_countries.json")
        val countryNamesList = countryDataList.map { it.first }
        val countryFlagsList = countryDataList.map { it.second }

        val random = Random()
        val randomIndex = random.nextInt(countryFlagsList.size)
        correctCountry = countryNamesList[randomIndex]

        return resources.getIdentifier(countryFlagsList[randomIndex].substringBefore("."), "drawable", packageName)
    }

    @Composable
    fun DisplayResult(isCorrect: Boolean, correctCountry: String, selectedCountry: String) {

        if (selectedCountry.isNotEmpty()) {
            if (isCorrect) {
                Text(
                    text = "CORRECT!",
                    color = Color.Green,
                    fontSize = 18.sp
                )
            } else {
                Text(
                    text = "WRONG! The correct country is: $correctCountry",
                    color = Color.Red,
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Composable
fun ContentView(countryNamesList: List<String>, onItemSelected: (String) -> Unit) {
    var selectedIndex by rememberSaveable { mutableStateOf(0) }
    var buttonModifier = Modifier.width(300.dp)

    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // drop down list
        DropdownList(
            itemList = countryNamesList,
            selectedIndex = selectedIndex,
            modifier = buttonModifier,
            onItemClick = {
                selectedIndex = it
                // Update selected country when an item is selected
                onItemSelected(countryNamesList[it])
            }
        )

        // some other contents below the selection button and under the list
        Text(
            text = "You have chosen ${countryNamesList[selectedIndex]}",
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .background(Color.LightGray)
        )
    }
}


@Composable
fun DropdownList(itemList: List<String>, selectedIndex: Int, modifier: Modifier, onItemClick: (Int) -> Unit) {
    var showDropdown by rememberSaveable { mutableStateOf(true) }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {

        // button
        Box(
            modifier = modifier
                .clickable { showDropdown = true },
            contentAlignment = Alignment.Center
        ) {
            Text(text = itemList[selectedIndex], modifier = Modifier.padding(3.dp))
        }

        // dropdown list
        Box() {
            if (showDropdown) {
                Popup(
                    alignment = Alignment.TopCenter,
                    properties = PopupProperties(
                        excludeFromSystemGesture = true,
                    ),
                    // to dismiss on click outside
                    onDismissRequest = { showDropdown = false }
                ) {

                    Column(
                        modifier = modifier
                            .heightIn(max = 90.dp)
                            .verticalScroll(state = scrollState)
                            .border(width = 1.dp, color = Color.Gray),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {

                        itemList.onEachIndexed { index, item ->
                            if (index != 0) {
                                Divider(thickness = 1.dp, color = Color.LightGray)
                            }
                            Box(
                                modifier = Modifier
                                    .background(Color.Green)
                                    .fillMaxWidth()
                                    .clickable {
                                        onItemClick(index)
                                        showDropdown = !showDropdown
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = item,)
                            }
                        }

                    }
                }
            }
        }
    }
}
