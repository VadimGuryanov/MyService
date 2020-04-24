package kpfu.itis.myservice.common

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kpfu.itis.myservice.app.App

class HelperToastSnackbar {

    fun toast(context: Context?, message: String, longTime: Int) {
        Toast.makeText(context, message, longTime).show()
    }

    fun toast(context: Context?, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun snackbar(activity: AppCompatActivity, message: String, longTime: Int) {
        Snackbar.make(
            activity.findViewById(android.R.id.content),
            message,
            longTime)
            .show()

    }

    fun snackbar(activity: AppCompatActivity, message: String) {
        Snackbar.make(
            activity.findViewById(android.R.id.content),
            message,
            Snackbar.LENGTH_INDEFINITE)
            .show()

    }



}
