package com.example.androidapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * Simple activity for the example app.
 */
class MainActivity : AppCompatActivity() {

    @Suppress("UndocumentedPublicProperty")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
