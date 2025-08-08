package com.vinayak.semicolon.securefolderhidefiles.Other

import android.app.Activity
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.core.content.edit
import com.vinayak.semicolon.securefolderhidefiles.R

object SharePref {

    private const val PREF_NAME = "SecFolderDownloader"

    private const val KEY_AppShow = "SecFolderAppShow"
    private const val KEY_Rate = "SecFolderAppRate"
    private const val KEY_Password = "SecFolderPassword"
    private const val KEY_Question = "SecFolderQuestion"
    private const val KEY_Answer = "SecFolderAnswer"
    const val KEY_THEME = "theme_key"
    const val   THEME_LIGHT = "light"
    const val THEME_DARK = "dark"

    internal fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun setOnboarding(context: Context, accepted: Boolean) {
        getSharedPreferences(context).edit()
            .putBoolean(KEY_AppShow, accepted)
            .apply()
    }

    // Check if the terms are accepted
    fun isOnboarding(context: Context): Boolean {
        return getSharedPreferences(context).getBoolean(KEY_AppShow, false)
    }

    fun setAppProtection(context: Context, accepted: Boolean) {
        getSharedPreferences(context).edit() {
            putBoolean(KEY_Rate, accepted)
        }
    }

    // Check if the terms are accepted
    fun issetAppProtection(context: Context): Boolean {
        return getSharedPreferences(context).getBoolean(KEY_Rate, false)
    }

    // ✅ NEW: Save PIN
    fun setPin(context: Context, pin: String) {
        getSharedPreferences(context).edit().putString(KEY_Password, pin).apply()
    }

    // ✅ NEW: Get PIN
    fun getPin(context: Context): String? {
        return getSharedPreferences(context).getString(KEY_Password, null)
    }

    // ✅ SET Security Question & Answer
    fun setSecurityQuestion(context: Context, question: String, answer: String) {
        getSharedPreferences(context).edit()
            .putString(KEY_Question, question)
            .putString(KEY_Answer, answer)
            .apply()
    }

    // ✅ GET Security Question
    fun getSecurityQuestion(context: Context): String? {
        return getSharedPreferences(context).getString(KEY_Question, null)
    }

    // ✅ GET Security Answer
    fun getSecurityAnswer(context: Context): String? {
        return getSharedPreferences(context).getString(KEY_Answer, null)
    }

    fun saveTheme(context: Context, theme: String) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit().putString(KEY_THEME, theme).apply()
    }

    fun getSavedTheme(context: Context): String {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString(KEY_THEME, THEME_LIGHT) ?: THEME_LIGHT
    }

    fun applyTheme(activity: Activity) {
        when (getSavedTheme(activity)) {
            THEME_DARK -> activity.setTheme(R.style.Theme_MyApp_Dark)
            else -> activity.setTheme(R.style.Theme_MyApp_Light)
        }
    }
}



