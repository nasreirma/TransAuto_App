package com.amm.robotarmcontrol.Utilities

import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.constraintlayout.widget.ConstraintLayout
import com.amm.robotarmcontrol.R
import com.google.android.material.snackbar.Snackbar
import java.lang.Exception

object Utilities {
    fun showSnackBar(snackTitle: String?, act: Activity) {
        try {
            val view1 = act.findViewById<ConstraintLayout>(R.id.constraintlayoutMain)!!
            val snackbar: Snackbar = Snackbar.make(view1, snackTitle!!,  Snackbar.LENGTH_SHORT)
            val view: View = snackbar.view

            if (!act.isFinishing)
                snackbar.show()

            val txtv = view.findViewById(R.id.snackbar_text) as TextView
            txtv.gravity = Gravity.CENTER_HORIZONTAL
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}