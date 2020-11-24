package com.example.hw8

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add.*
import kotlin.math.absoluteValue
import kotlin.random.Random

class AddActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        post_button.setOnClickListener {
            if (!enter_title.text.isNullOrBlank() && !enter_body.text.isNullOrBlank()) {

                val newId: Int = Random.nextInt().absoluteValue
                val newTitle: String = enter_title.text.toString()
                val newBody: String = enter_body.text.toString()

                val newPost = Post(MY_USER_ID, newId, newTitle, newBody)
                setResult(
                    NEW_POST_RESULT_CODE,
                    Intent(this@AddActivity, MainActivity::class.java).apply {
                        putExtra(NEW_POST_KEY, newPost)
                    })
                finish()
            }
        }
    }
}