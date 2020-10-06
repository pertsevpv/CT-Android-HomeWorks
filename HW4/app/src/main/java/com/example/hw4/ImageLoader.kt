package com.example.hw4

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.AsyncTask
import android.util.Log
import android.view.View
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_main.*
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.net.URL
import java.sql.Array
import java.util.*
import kotlin.collections.ArrayList

class ImageLoader(val activity: MainActivity) :
    AsyncTask<String, Int, List<ImageCard>>() {

    override fun onPostExecute(result: List<ImageCard>?) {
        super.onPostExecute(result)
        if (!result.isNullOrEmpty()) {
            activity.addToList(result)
            activity.showList()
            //activity.showList(result)
        }
    }

    override fun doInBackground(vararg params: String?): List<ImageCard> {
        return getListFromResponse(URL(params[0]).openConnection().getInputStream().reader().readText())
    }

    private fun getListFromResponse(response: String?): List<ImageCard> {
        if (response == null || response.isBlank())
            return emptyList()
        val jsonArray = Gson().fromJson(response, JsonArray::class.java)
        val result: ArrayList<ImageCard> = ArrayList()
        for (i in 0 until jsonArray.size()) {
            val item = jsonArray[i].asJsonObject
            result.add(ImageCard().apply {
                if (item.has("description") && !item.get("description").isJsonNull) {
                    description = item.get("description").asString
                } else if (item.has("alt_description") && !item.get("alt_description").isJsonNull) {
                    description = item.get("alt_description").asString
                } else {
                    description = ""
                }

                val urls = item.get("urls").asJsonObject
                val smallUrl: String
                if (urls.has("thumb") && !urls.get("thumb").isJsonNull) {
                    smallUrl = urls.get("thumb").asString
                } else if (urls.has("small") && !urls.get("small").isJsonNull) {
                    smallUrl = urls.get("small").asString
                } else {
                    smallUrl = ""
                }
                previewBitmap = downloadPreview(smallUrl)

                /*if (urls.has("full") && !urls.get("full").isJsonNull) {
                    fullURL = urls.get("full").asString
                } else*/ if (urls.has("regular") && !urls.get("regular").isJsonNull) {
                fullURL = urls.get("regular").asString
            }
            })
        }
        return result
    }

    private fun downloadPreview(previewUrl: String): Bitmap {
        val img = BitmapFactory.decodeStream(URL(previewUrl).openConnection().getInputStream())
        val out = ByteArrayOutputStream()
        img.compress(Bitmap.CompressFormat.JPEG, 30, out)
        return BitmapFactory.decodeStream(ByteArrayInputStream(out.toByteArray()))
    }
}