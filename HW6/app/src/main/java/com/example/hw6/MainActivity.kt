package com.example.hw6

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val dur = 3000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        anim(load_text)
    }

    private fun anim(view: TextView) {
        ValueAnimator.ofArgb(Color.RED, Color.rgb(0xff, 0xcc, 0x00), Color.RED).apply {
            duration = 2000L
            repeatCount = ValueAnimator.INFINITE
            addUpdateListener {
                view.setTextColor(it.animatedValue as Int)
            }
        }.start()
    }
}