package com.example.basuapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PinLockActivity : AppCompatActivity() {

    private val prefsName = "basu_pin_prefs"
    private val pinKey = "user_pin"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pin_lock)

        val prefs = getSharedPreferences(prefsName, MODE_PRIVATE)
        val savedPin = prefs.getString(pinKey, null)

        val titleText = findViewById<TextView>(R.id.pinTitle)
        val pinInput = findViewById<EditText>(R.id.pinInput)
        val submitButton = findViewById<Button>(R.id.pinSubmitButton)
        val errorText = findViewById<TextView>(R.id.pinError)

        val isSettingUp = savedPin == null
        titleText.text = if (isSettingUp) "Set a PIN (min 4 digits)" else "Enter PIN"

        submitButton.setOnClickListener {
            val entered = pinInput.text.toString()

            if (entered.length < 4) {
                errorText.text = "PIN must be at least 4 digits"
                return@setOnClickListener
            }

            if (isSettingUp) {
                prefs.edit().putString(pinKey, entered).apply()
                openMain()
            } else {
                if (entered == savedPin) {
                    openMain()
                } else {
                    errorText.text = "Incorrect PIN"
                    pinInput.text.clear()
                }
            }
        }
    }

    private fun openMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
