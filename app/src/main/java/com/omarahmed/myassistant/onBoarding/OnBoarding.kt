package com.omarahmed.myassistant.onBoarding

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.viewpager2.widget.ViewPager2
import com.omarahmed.myassistant.MainActivity
import com.omarahmed.myassistant.R
import com.omarahmed.myassistant.utils.SharedPreference
import kotlinx.android.synthetic.main.activity_on_boarding.*

class OnBoarding : AppCompatActivity() {
    private val onBoardingAdapter = OnBoardingAdapter(
        listOf(
            OnBoardingData(
                "My Assistant",
                "Your assistant app through out your university journey",
                R.drawable.ic_assistant
            ),
            OnBoardingData(
                "Don't miss Anything",
                "Don't worry to miss anything, all your timetables, tests, and assignments are in one place",
                R.drawable.ic_do_not_miss
            ),
            OnBoardingData(
                "Get Notifications",
                "My assistant app will notify you with the upcoming assignment deadlines and test dates",
                R.drawable.ic_get_notification
            ),
            OnBoardingData(
                "Holidays",
                "Stay tuned with the upcoming holidays so that you can make your plan accordingly",
                R.drawable.ic_holidays
            )
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding)

        // to make status bar transparent
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        viewPager.adapter = onBoardingAdapter
        setUpIndicators()
        setCurrentIndicators(0)
        handleTheOnBoarding()

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicators(position)

                if (position == onBoardingAdapter.itemCount - 1 ){
                    btnGo.visibility = View.VISIBLE
                    val anim = AnimationUtils.loadAnimation(applicationContext,R.anim.btn_go_anim)
                    btnGo.startAnimation(anim)
                }
                else{
                    btnGo.visibility = View.GONE
                }
            }
        })
        btnGo.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }

    private fun handleTheOnBoarding() {
        val firstRun = SharedPreference(this).getBooleanValue("isFirstRun",true)
        if (!firstRun){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
        SharedPreference(this).putBooleanValue("isFirstRun",false)
    }

    private fun setUpIndicators() {
        val indicators = arrayOfNulls<ImageView>(onBoardingAdapter.itemCount) // indicators numbers
        val layoutParams: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        layoutParams.setMargins(8, 0, 8, 0)

        for (i in indicators.indices) {
            indicators[i] = ImageView(applicationContext)
            indicators[i].apply {
                this?.setImageDrawable(ContextCompat.getDrawable(applicationContext,R.drawable.indicators_inactive))
                this?.layoutParams = layoutParams
            }
            indicatorsContainer.addView(indicators[i])

        }

    }

    private fun setCurrentIndicators(index: Int) {
        val childCount = indicatorsContainer.childCount   // for indicators numbers
        for (i in 0 until childCount) {
            val imageView = indicatorsContainer[i] as ImageView

            if (i == index) {
                imageView.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.indicators_active))
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.indicators_inactive))
            }
        }
    }


}
