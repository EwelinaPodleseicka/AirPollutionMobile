package com.example.airpollution

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var displayTextView: TextView
    private lateinit var valueTextView: TextView
    private lateinit var counterTextView: TextView
    private lateinit var descriptionTextView: TextView
    // API Doc: https://api-docs.iqair.com/
    private val apiKey = "cb2f1e0e-98c6-4f15-81dd-b57eb3ffc781";
    private val apiUrl =
        "https://api.airvisual.com/v2/city?city=rzeszow&state=Subcarpathian%20Voivodeship&country=poland&key=$apiKey";

    var counter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        displayTextView = findViewById(R.id.displayTextView)
        valueTextView = findViewById(R.id.valueTextView)
        counterTextView = findViewById(R.id.counterTextView)
        descriptionTextView = findViewById(R.id.descriptionTextView)
        updateWeather()
        val button = findViewById<Button>(R.id.button)

        button.setOnClickListener {
            updateWeather()
        }
    }

    private fun updateWeather() {
        counter++
        val client = OkHttpClient();

        val request = Request.Builder() // budowanie requesta do wywołania API
            .url(apiUrl)
            .build()

        client.newCall(request).enqueue(object : Callback { //wywołanie API
            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string() ?: ""
                val json = JSONObject(responseBody)

                val aqi = json
                    .getJSONObject("data")
                    .getJSONObject("current")
                    .getJSONObject("pollution")
                    .getInt("aqius")

                var airQuality = ""
                if (aqi <= 50) {
                    airQuality = "GOOD"
                } else if (aqi <= 100) {
                    airQuality = "MODERATE"
                } else if (aqi <= 150) {
                    airQuality = "UNHEALTHY FOR SENSITIVE GROUPS"
                } else if (aqi <= 200) {
                    airQuality = "UNHEALTHY"
                } else if (aqi <= 300) {
                    airQuality = "VERY UNHEALTHY"
                } else {
                    airQuality = "HAZARDOUS"
                }

                runOnUiThread {
                    displayTextView.text = "Current air pollution in Rzeszow"
                    valueTextView.text = "$aqi"
                    counterTextView.text = "API Call counter: $counter"
                    descriptionTextView.text = airQuality
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                Log.d("ERROR","Request failed: ${e.message}")
            }
        })
    }
}