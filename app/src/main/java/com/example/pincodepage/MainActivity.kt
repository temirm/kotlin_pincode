package com.example.pincodepage

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    private val PIN_KEY = "PIN_KEY"
    private val PIN_COLOR_KEY = "PIN_COLOR_KEY"

    private val numberButtons = mutableListOf<Button>()
    private lateinit var btnDelete: Button
    private lateinit var btnOk: Button
    private lateinit var pinCodeTextView: TextView

    private var pinCodeStringBuilder: StringBuilder = java.lang.StringBuilder(4)
    private var pinColor: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        setClickListeners()
        resetPinColor()
        restoreFromSavedInstanceState(savedInstanceState)
        showPinCodeText()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(PIN_KEY, pinCodeStringBuilder.toString())
        outState.putInt(PIN_COLOR_KEY, pinColor)
    }

    private fun initViews() {
        for (i in 0..9) {
            val btn: Button =
                findViewById(resources.getIdentifier("btn_${i}", "id", packageName))
            numberButtons.add(btn)
        }

        btnDelete = findViewById(R.id.btn_delete)
        btnOk = findViewById(R.id.btn_ok)

        pinCodeTextView = findViewById(R.id.pin_code)
    }

    private fun showPinCodeText() {
        if (pinCodeStringBuilder.isEmpty()) {
            pinCodeTextView.text = resources.getString(R.string.hint_text)
            return
        }
        pinCodeTextView.text = pinCodeStringBuilder.toString()
        pinCodeTextView.setTextColor(pinColor)
    }

    private fun setClickListeners() {
        numberButtons.forEachIndexed { index, button ->
            button.setOnClickListener {
                if (pinCodeStringBuilder.length >= 4) {
                    return@setOnClickListener
                }
                pinCodeStringBuilder.append(index)
                resetPinColor()
                showPinCodeText()
            }
        }

        btnDelete.setOnClickListener {
            pinCodeStringBuilder.deleteLast()
            resetPinColor()
            showPinCodeText()
        }

        btnOk.setOnClickListener {
            if (isCorrectPin()) {
                pinColor = getColor(R.color.button_primary)
                Toast.makeText(
                    this,
                    resources.getString(R.string.success_toast),
                    Toast.LENGTH_SHORT).show()
            } else {
                pinColor = getColor(R.color.error_text)
            }
            showPinCodeText()
        }
    }

    private fun resetPinColor() {
        pinColor = getColor(R.color.hint_text)
    }

    private fun restoreFromSavedInstanceState(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            pinCodeStringBuilder.append(savedInstanceState.getString(PIN_KEY))
            pinColor = savedInstanceState.getInt(PIN_COLOR_KEY)
        }
    }

    private fun isCorrectPin(): Boolean {
        return pinCodeStringBuilder.toString() == resources.getString(R.string.correct_pass)
    }
}