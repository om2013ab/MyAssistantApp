package com.omarahmed.myassistant

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.omarahmed.myassistant.onBoarding.OnBoarding
import com.omarahmed.myassistant.utils.SharedPreference

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        Handler(Looper.getMainLooper()).postDelayed({
            handleStartActivity()
        },1000)
    }

    private fun handleStartActivity(){
        val sharedPreference = SharedPreference(this)
        val firstRun = sharedPreference.getBooleanValue("firstRun",true)
        if (firstRun){
            startActivity(Intent(this,OnBoarding::class.java))
            finish()
        } else {
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
        sharedPreference.putBooleanValue("firstRun",false)
    }
}