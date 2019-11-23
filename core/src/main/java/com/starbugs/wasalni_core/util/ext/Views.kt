package com.starbugs.wasalni_core.util.ext

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}

fun FloatingActionButton.enableFab() {
    isEnabled = true
    alpha = 1f
}


fun FloatingActionButton.disableFab() {
    isEnabled = false
    alpha = 0.5f
}