package com.andrew.photoweatherapp.presentation

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.andrew.photoweatherapp.R

class MainActivity : AppCompatActivity() {

    private val lostConnectivityTextView by lazy { findViewById<TextView>(R.id.lost_connectivity_textView) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ConnectivityListener(this).observe(this) {
            lostConnectivityTextView.visibility = if (it == false) View.VISIBLE else View.GONE
        }
    }
}