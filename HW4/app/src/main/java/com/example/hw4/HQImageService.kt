package com.example.hw4

import android.app.IntentService
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory

import java.io.ByteArrayOutputStream
import java.net.URL

const val RESPONSE_KEY = "RESPONSE_Key"
const val BYTE_ARRAY_KEY = "BYTE_ARRAY_KEY"

class HQImageService : IntentService("ImageLoader") {
    init {
        setIntentRedelivery(true)
    }

    override fun onHandleIntent(intent: Intent?) {
        val url = intent?.getStringExtra(FULLURL_KEY)
        val hqImage = BitmapFactory.decodeStream(URL(url).openConnection().getInputStream())
        sendBroadcast(Intent().apply {
            action = RESPONSE_KEY
            addCategory(Intent.CATEGORY_DEFAULT)
            putExtra(
                BYTE_ARRAY_KEY,
                ByteArrayOutputStream().apply {
                    hqImage.compress(Bitmap.CompressFormat.JPEG, 70, this)
                }.toByteArray()
            )
        })
    }

}