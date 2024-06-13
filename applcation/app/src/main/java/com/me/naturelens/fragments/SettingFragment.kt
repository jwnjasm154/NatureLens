package com.me.naturelens.fragments

import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.widget.ImageView
import androidx.cardview.widget.CardView
import com.me.naturelens.MainActivity
import com.me.naturelens.R
import com.me.naturelens.SplashActivity
import com.me.naturelens.TermsAndPrivacyPolicyActivity

class SettingFragment : Fragment() {
    private val animationDuration = 300
    private var mainActivity: MainActivity? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_setting, container, false)
        mainActivity = activity as? MainActivity
        val terms_and_privacy = view.findViewById<CardView>(R.id.terms_and_privacy)
        terms_and_privacy.setOnClickListener {
            val intent = Intent(view.context, TermsAndPrivacyPolicyActivity::class.java)
            view.context?.startActivity(intent)
        }
        var ischeck1 = false
        var ischeck2 = false
        val langPage = view.findViewById<CardView>(R.id.langPage)
        val themePage = view.findViewById<CardView>(R.id.ThemePage)

        val langPage_En = view.findViewById<CardView>(R.id.langPage_En)
        val langPage_Ar = view.findViewById<CardView>(R.id.langPage_Ar)
        val themePage_Dark = view.findViewById<CardView>(R.id.ThemePage_Dark)
        val themePage_Light = view.findViewById<CardView>(R.id.ThemePage_Light)

        langPage_Ar.setOnClickListener { setLanguage("ar") }
        langPage_En.setOnClickListener { setLanguage("en") }
        langPage.setOnClickListener {toggleViews(listOf(langPage_Ar,langPage_En, ))
            ischeck1=!ischeck1
            when(ischeck1) {

                true -> view?.findViewById<ImageView>(R.id.lang_icon_arrow)
                    ?.setImageResource(R.drawable.baseline_keyboard_arrow_up_24)
                false-> view?.findViewById<ImageView>(R.id.lang_icon_arrow)
                    ?.setImageResource(R.drawable.baseline_keyboard_arrow_down_24)
            }

        }
        themePage.setOnClickListener {toggleViews(listOf(themePage_Light,themePage_Dark, ))
            ischeck2=!ischeck2
            when(ischeck2) {

                true -> view?.findViewById<ImageView>(R.id.themes_icon_arrow)
                    ?.setImageResource(R.drawable.baseline_keyboard_arrow_up_24)
                false-> view?.findViewById<ImageView>(R.id.themes_icon_arrow)
                    ?.setImageResource(R.drawable.baseline_keyboard_arrow_down_24)
            }
        }
        themePage_Light.setOnClickListener { setTheme("green") }
        themePage_Dark.setOnClickListener { setTheme("red") }
        return view
    }

    private fun setLanguage(language: String) {
        mainActivity?.setLangApp(language)
        restartApp()
    }

    private fun setTheme(theme: String) {
        mainActivity?.setThemeApp(theme)
        restartApp()
    }

    private fun restartApp() {
        val intent = Intent (activity, SplashActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("EXIT", true)
        mainActivity?.startActivity(intent)
        Handler().postDelayed({mainActivity?.recreate()},100)

    }

    private fun toggleViews(views: List<View>) {
        val isVisible = views.all { it.visibility == View.GONE }

        animateViews(views, !isVisible)
    }

    private fun animateViews(views: List<View>, isVisible: Boolean) {
        val animator = ValueAnimator.ofInt(if (isVisible) 0 else views[0].height, if (isVisible) views[0].height else 0).apply {
            duration = animationDuration.toLong()
            interpolator = AccelerateInterpolator()
            addUpdateListener { animation ->
                val value = animation.animatedValue as Int
                views.forEachIndexed { index, view ->
                    view.translationY = value.toFloat()
                    view.alpha = if (isVisible) value.toFloat() / views[0].height.toFloat() else 1f - (value / views[0].height.toFloat())
                    view.visibility = if (!isVisible) View.VISIBLE else View.GONE
                }
            }
        }
        animator.start()
    }

}