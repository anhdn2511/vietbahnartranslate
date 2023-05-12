package com.vietbahnartranslate.utils

import android.net.Uri

object DataUtils {
    var gender = false
    var speed = 1.0f
    var isSignedIn = false
    var displayName = ""
    var email = ""
    var photoURL: String = ""

    fun log(): String {
        return """
            displayName: $displayName,
            email: $email,
            photoURL: $photoURL
        """.trimIndent()
    }
}