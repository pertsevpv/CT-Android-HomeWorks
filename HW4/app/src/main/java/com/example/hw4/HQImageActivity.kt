package com.example.hw4

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.PersistableBundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.hq_image.*

const val FULLURL_KEY = "FULLURL_KEY"
const val DESCRIPTION_KEY = "DESCRIPTION_KEY"

class HQImageActivity : AppCompatActivity() {

    var broadcastReceiver: BroadcastReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.hq_image)

        val hqUrl = intent.getStringExtra(FULLURL_KEY)
        startService(Intent(this, HQImageService::class.java).apply {
            putExtra(FULLURL_KEY, hqUrl)
        })

        hq_description.text = intent.getStringExtra(DESCRIPTION_KEY)
        hq_description.movementMethod = ScrollingMovementMethod()

        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val byteArray = intent?.getByteArrayExtra(BYTE_ARRAY_KEY)
                if (byteArray != null) {
                    val byteImg = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                    hq_image.setImageBitmap(byteImg)
                }
            }
        }
        registerReceiver(
            broadcastReceiver,
            IntentFilter(RESPONSE_KEY).apply {
                addCategory(Intent.CATEGORY_DEFAULT)
            }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadcastReceiver)
    }

}

