package com.me.naturelens

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.me.naturelens.fragments.LiveFragment

class CameraActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        Handler(Looper.getMainLooper()).postDelayed({
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fl_wrapper2,LiveFragment())
                .commit()
        }, 500)

    }
}