import okhttp3.*
import org.json.JSONObject //bilblioteka zewnetrzna x2
import java.io.IOException

fun main(args: Array<String>) {
    // API Doc: https://api-docs.iqair.com/
    val apiKey = "cb2f1e0e-98c6-4f15-81dd-b57eb3ffc781";
    val apiUrl =
        "https://api.airvisual.com/v2/city?city=rzeszow&state=Subcarpathian%20Voivodeship&country=poland&key=$apiKey";
    val client = OkHttpClient();

    val request = Request.Builder()
        .url(apiUrl)
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onResponse(call: Call, response: Response) {
            val responseBody = response.body?.string()
            val json = JSONObject(responseBody)

            val aqi = json
                .getJSONObject("data")
                .getJSONObject("current")
                .getJSONObject("pollution")
                .getInt("aqius")

            println("Current air pollution in Rzeszow: $aqi")

            // based on https://www.airnow.gov/aqi/aqi-basics
            if (aqi <= 50) {
                println("Good - " + "Air quality is satisfactory, and air pollution poses little or no risk.")
            } else if (aqi <= 100) {
                println(
                    "Moderate - " + "Air quality is acceptable. However, there may be a risk for some people, " +
                            "particularly those who are unusually sensitive to air pollution."
                )


            }

            // Validate response on: https://www.iqair.com/poland/subcarpathian-voivodeship/rzeszow
        }

        override fun onFailure(call: Call, e: IOException) {
            println("Request failed: ${e.message}")
        }
    })
}
