package com.example.mbcw.utils

import android.content.Context
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader

fun readCountryDataFromAssets(context: Context, path: String): List<Pair<String, String>> {
    val countryData = mutableListOf<Pair<String, String>>()

    try {
        val file = context.assets.open(path)
        val bufferedReader = BufferedReader(InputStreamReader(file))
        val stringBuilder = StringBuilder()
        bufferedReader.useLines { lines ->
            lines.forEach {
                stringBuilder.append(it)
            }
        }
        val jsonString = stringBuilder.toString()

        // Parse JSON string to extract country names and flags
        val json = JSONObject(jsonString)
        json.keys().forEach { key ->
            val countryObject = json.getJSONObject(key)
            val countryName = countryObject.getString("name")
            val countryFlag = countryObject.getString("flag")
            countryData.add(Pair(countryName, countryFlag))
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return countryData
}