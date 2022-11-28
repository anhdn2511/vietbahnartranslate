package com.vietbahnartranslate.utils

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.vietbahnartranslate.view.MainActivity

object KeyboardUtils {
    private val windowHeightMethod =  InputMethodManager::class.java.getMethod("getInputMethodWindowVisibleHeight")
    fun onKeyboardVisible(activity: MainActivity) {
        //activity.hideBottomNavigationView()
    }

    fun onKeyboardInvisible(view: View, activity: MainActivity) {
        val inputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        //activity.showBottomNavigationView()
    }

    fun isKeyboardVisible(imm: InputMethodManager) : Boolean {
        val keyboardHeight = windowHeightMethod.invoke(imm) as Int
        if (keyboardHeight > 0) return true
        return false
    }
}