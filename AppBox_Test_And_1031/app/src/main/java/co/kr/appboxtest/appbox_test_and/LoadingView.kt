package co.kr.appboxtest.appbox_test_and

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class LoadingView(context: Context)  {

    private val dialog: Dialog
    private val handler = Handler(Looper.getMainLooper())
    private var dotCount = 0
    private lateinit var loadingText: TextView

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.loading_layout, null)
        loadingText = view.findViewById(R.id.loadingText)

        dialog = AlertDialog.Builder(context)
            .setView(view)
            .setCancelable(false)
            .create()

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        loadingText.setBackgroundColor(Color.TRANSPARENT)
    }

    fun show() {
        dialog.show()
        startDotAnimation()
    }

    fun dismiss() {
        stopDotAnimation()
        dialog.dismiss()
    }

    private fun startDotAnimation() {
        handler.post(object : Runnable {
            override fun run() {
                dotCount = (dotCount + 1) % 4
                val dots = ".".repeat(dotCount)
                loadingText.text = "Loading$dots"
                handler.postDelayed(this, 500)
            }
        })
    }

    private fun stopDotAnimation() {
        handler.removeCallbacksAndMessages(null)
    }

    
}