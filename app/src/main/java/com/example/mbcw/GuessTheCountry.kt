package com.example.mbcw

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.mbcw.ui.theme.MBCWTheme

var dropDownSelectedCountry = "" // user selected country from the drop down list
var imageDisplayId = ""  // country code of the displayed image
var guessTheCountryButtonCondition = mutableStateOf(false) // boolean condition for submit or next button
var guessTheCountryAnswerIsCorrect = mutableStateOf(false) // boolean value to store answers state
var guessTheCountryAlertDialogBox = mutableStateOf(false) // alert box open boolean condition
var oneTimeRunForGuessTheCountry = false // this variable is used to conform that if statement run only once

class GuessTheCountry : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        oneTimeRunForGuessTheCountry = intent.getBooleanExtra("methodRun", false) // getting the boolean value from the mainActivity
        guessTheCountryButtonCondition = mutableStateOf(false) // reverting
        guessTheCountryAnswerIsCorrect = mutableStateOf(false) // to the
        guessTheCountryAlertDialogBox = mutableStateOf(false) // default values

        super.onCreate(savedInstanceState)
        setContent {
            MBCWTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            // Additional UI elements can be added here
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            ImageDisplay()
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            dropDownList()
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            submitAndNextButton(this@GuessTheCountry)
                        }
                    }
                }
            }
        }
    }
}

// Display image to the user
@Composable
fun ImageDisplay() {
    val countryIdAndResId = Countrie().randomImage()
    val flagId by rememberSaveable { mutableStateOf(countryIdAndResId[0].toString()) }
    imageDisplayId = flagId // appending country key to the imageIdDisplay
    val flagResId = countryIdAndResId[1].toString().toInt()
    val randomImageId by rememberSaveable { mutableIntStateOf(flagResId) }

    Card(
        elevation = CardDefaults.cardElevation()
    ) {
        Image(
            painter = painterResource(id = randomImageId),
            contentDescription = "Image",
            modifier = Modifier
                .size(width = 346.dp, height = 250.dp)
                .padding(horizontal = 8.dp)
        )
    }
}

// Display selectable dropdown list to the user
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun dropDownList() {
    val countriesMap = Countrie()
        .countriesMap
        .entries
        .sortedBy { it.value }
        .associate { it.key to it.value }
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    var country by rememberSaveable { mutableStateOf("") }

    Box(
        contentAlignment = Alignment.Center
    ) {
        ExposedDropdownMenuBox(expanded = isExpanded, onExpandedChange = { isExpanded = it }) {
            TextField(
                enabled = false,
                value = country,
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    IconButton(
                        onClick = { isExpanded = true },
                        enabled = true
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = null
                        )
                    }
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = Modifier
                    .menuAnchor()
                    .clickable {
                        isExpanded = !isExpanded
                    }
            )
            ExposedDropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded = false }) {
                for (entry in countriesMap.entries) {
                    DropdownMenuItem(
                        text = { Text(countriesMap[entry.key].toString()) },
                        onClick = {
                            country = countriesMap[entry.key].toString()
                            isExpanded = false
                            dropDownSelectedCountry = country
                        }
                    )
                }
            }
        }
    }
}

// This function is used to provide user with submit button and next button
@Composable
fun submitAndNextButton(context: Context) {
    // These 3 variables are used to control the button flow and alertBox
    val statement by rememberSaveable { mutableStateOf(guessTheCountryButtonCondition) }
    val answerState by rememberSaveable { mutableStateOf(guessTheCountryAnswerIsCorrect) }
    val dialogBoxOpen by rememberSaveable { mutableStateOf(guessTheCountryAlertDialogBox) }

    if (!statement.value) { // Checking the condition for submit button
        Button(
            onClick = {
                answerState.value = checkAnswer()
                statement.value = true
                dialogBoxOpen.value = true
            }
        ) {
            Text(text = "Submit")
        }
    } else {
        Button(
            onClick = {
                (context as Activity).finish() // Trigger the onDestroy method and use intent to reload the same page
                val intent = Intent(context, GuessTheCountry::class.java)
                context.startActivity(intent)
            }
        ) {
            Text(text = "Next")
        }
    }
    if (dialogBoxOpen.value) {
        val country = Countrie().countriesMap
        country[imageDisplayId.uppercase()]?.let {
            AlertDialogBox(condition = answerState.value, it, onClose = { dialogBoxOpen.value = false })
        }
    }
}

// This alert dialog box is used in all 4 activity's to notify the user with answer correct or not
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialogBox(condition: Boolean, correctName: String, onClose: () -> Unit) {
    val color = if (condition) Color.Green else Color.Red
    val message = if (condition) "CORRECT!" else "WRONG!"
    val correctNameDisplay = if (condition) "" else correctName

    AlertDialog(
        onDismissRequest = { onClose() }
    ) {
        Box(
            modifier = Modifier
                .size(width = 200.dp, height = 150.dp)
                .background(Color.White)
                .clip(RoundedCornerShape(8.dp))
                .clickable { onClose() },
            contentAlignment = Alignment.Center
        ) {
            Column {
                Text(text = message, color = color, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = correctNameDisplay, color = Color.Blue, textAlign = TextAlign.Center)
            }
        }
    }
}

// This method checks the answer right or wrong and returns a boolean value
// This method is called inside the submitAndNextButton method
fun checkAnswer(): Boolean {
    val countryList = Countrie().countriesMap
    var answerState = false
    for ((key, value) in countryList) {
        if (value == dropDownSelectedCountry && key.lowercase() == imageDisplayId) {
            answerState = true
        }
    }
    return answerState
}
