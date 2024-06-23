package com.example.mbcw

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mbcw.ui.theme.MBCWTheme

class GuessHints : ComponentActivity() {
    private var selectedChar: Char = ' '
    private var outputName = mutableStateOf("")
    private var countryNameCharList = mutableListOf<Char>()
    private var guessHintImageResId = mutableIntStateOf(0)
    private var textFieldTxt = mutableStateOf("")
    private var incorrectLettersEntered = mutableIntStateOf(0)
    private var guessHintAnswerCondition = mutableStateOf(false)
    private var guessHintButtonCondition = mutableStateOf(false)
    private var guessHintAlertDialogBox = mutableStateOf(false)
    private var countryName = mutableStateOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initOutputNameAndImageResId()

        setContent {
            MBCWTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CountryImageForHint()
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            DisplayText()
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        //Text(text = countryName.value)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            SingleCharacterInputField()
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Button(this@GuessHints)
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun CountryImageForHint() {
        val randomImageId by rememberSaveable { mutableStateOf(guessHintImageResId) }
        Card(
            elevation = CardDefaults.cardElevation(),
            shape = RoundedCornerShape(16.dp), // Set the shape of the card (rounded corners)
        ) {
            Image(
                painter = painterResource(id = randomImageId.intValue),
                contentDescription = "Image",
                modifier = Modifier
                    .size(width = 346.dp, height = 250.dp)
                    .padding(horizontal = 8.dp)
            )
        }
    }

    @Composable
    fun DisplayText() {
        var output by rememberSaveable { mutableStateOf(outputName) }
        Text(text = output.value, color = Color.Black, fontSize = 20.sp)
    }

    @Composable
    fun SingleCharacterInputField() {
        var text by rememberSaveable{ mutableStateOf(textFieldTxt) }

        Column {
            OutlinedTextField(
                value = text.value,
                onValueChange = { newValue ->
                    if (newValue.length <= 1) {
                        text.value = newValue
                        selectedChar = newValue.lowercase().toCharArray()[0]
                    }
                },
                label = {Text("Enter a letter")},
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                ),
                singleLine = true,
                maxLines = 1
            )
        }
    }

    @Composable
    fun Button(context: Context){
        val statement by rememberSaveable { mutableStateOf(guessHintButtonCondition) }
        val incorrectLettersEnteredTimes by rememberSaveable{ mutableStateOf(incorrectLettersEntered) }
        val dialogBoxOpen by rememberSaveable{ mutableStateOf(guessHintAlertDialogBox) }
        val answersCondition by rememberSaveable{ mutableStateOf(guessHintAnswerCondition) }
        
        if(!statement.value){
            Button(
                onClick = {
                    listHandler()
                    if(countryNameCharList.all{it == '*'}){
                        answersCondition.value = true
                        statement.value = true
                        dialogBoxOpen.value = true
                    }else if(incorrectLettersEnteredTimes.intValue == 3) {
                        statement.value = true
                        dialogBoxOpen.value = true
                    }
                }
            ) {
                Text(text = "Submit")
            }
        }else{
            Button(
                onClick = {(context as Activity).finish()
                    val intent = Intent(context, GuessHints::class.java)
                    context.startActivity(intent)
                }
            ) {
                Text(text = "Next")
            }
        }
        if(dialogBoxOpen.value){
            if(statement.value) {
                AlertDialogBox(answersCondition.value, countryName.value, onClose = { dialogBoxOpen.value = false })
            }
        }
    }

    fun listHandler() {
        if(countryNameCharList.contains(selectedChar)) {
            val index = countryNameCharList.indexOf(selectedChar)
            val outputName2 = outputName.value.substring(0, index) + selectedChar.uppercase() + outputName.value.substring(index + 1)
            countryNameCharList[index] = '*'
            outputName.value = outputName2
            Log.d("text1", outputName.toString())
            textFieldTxt.value = ""
        }else{
            textFieldTxt.value = ""
            incorrectLettersEntered.intValue ++
        }
    }

    fun initOutputNameAndImageResId(){
        val countryIdAndResId = Countrie().randomImage()
        val flagId = countryIdAndResId[0].toString()
        val flagResId = countryIdAndResId[1].toString().toInt()
        val countriesMap = Countrie().countriesMap
        var countryNameDashLine  = ""
        for(i in countriesMap.keys){
            if(i.lowercase() == flagId){
                for(char in countriesMap.getValue(i).lowercase().toCharArray()){
                    countryNameCharList.add(char)
                    countryNameDashLine += "_"
                }
                countryName.value = countriesMap.getValue(i)
            }
        }
        guessHintImageResId.intValue = flagResId
        outputName.value = countryNameDashLine
    }
}