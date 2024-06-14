package com.me.naturelens


import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.util.Locale

abstract class BaseActivity : AppCompatActivity() {
    companion object {
        const val THEME_KEY = "theme"
        const val THEME_RED = "red"
        const val THEME_GREEN = "green"
        const val LANG_KEY = "lang"
        const val LANG_EN = "en"
        const val LANG_AR = "ar"
    }

    private var currentTheme: String = THEME_RED
    private var currentLang: String = LANG_EN

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = getSharedPreferences(THEME_KEY, Context.MODE_PRIVATE)
        currentTheme = sharedPreferences.getString(THEME_KEY, THEME_RED)!!

        when (currentTheme) {
            THEME_RED -> setTheme(R.style.MyDarkTheme)
            THEME_GREEN -> setTheme(R.style.MyLightTheme)
        }

        val sharedPreferences1 = getSharedPreferences(LANG_KEY, Context.MODE_PRIVATE)
        currentLang = sharedPreferences1.getString(LANG_KEY, LANG_EN)!!

        when (currentLang) {
            LANG_EN -> setLangApp(LANG_EN)
            LANG_AR -> setLangApp(LANG_AR)
        }

    }

    fun setThemeApp(theme: String) {
        when (theme) {
            THEME_RED -> setTheme(R.style.MyDarkTheme)
            THEME_GREEN -> setTheme(R.style.MyLightTheme)
        }

        currentTheme = theme

        val sharedPreferences = getSharedPreferences(THEME_KEY, Context.MODE_PRIVATE)
        with (sharedPreferences.edit()) {
            putString(THEME_KEY, theme)
            apply()
        }
    }

    fun setLangApp(lang: String) {
        val  resource: Resources = this.resources
        val  configuration: Configuration =resource.configuration
        val  local: Locale = Locale(lang)
        Locale.setDefault(local)
        configuration.setLocale(local)
        resource.updateConfiguration(configuration,resource.displayMetrics)

        currentTheme = lang

        val sharedPreferences = getSharedPreferences(LANG_KEY, Context.MODE_PRIVATE)
        with (sharedPreferences.edit()) {
            putString(LANG_KEY, lang)
            apply()
        }
    }

}