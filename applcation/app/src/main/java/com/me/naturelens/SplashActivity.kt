package com.me.naturelens

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()
        hideStatusBar()

        val isExitIntent = intent.extras?.getBoolean("EXIT", false) ?: false
        val mainIntent = Intent(this, MainActivity::class.java)

        if (isExitIntent) {
            startActivity(mainIntent)
            finishAffinity()
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(mainIntent)
                finishAffinity()
            }, 3000)
        }
    }

    private fun hideStatusBar() {
        WindowCompat.getInsetsController(window,window.decorView).apply {
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            hide(WindowInsetsCompat.Type.statusBars())
        }
    }
}