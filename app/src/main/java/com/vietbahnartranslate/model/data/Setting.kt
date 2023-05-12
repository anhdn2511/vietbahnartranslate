package com.vietbahnartranslate.model.data

import android.net.Uri

data class Setting (
    var gender: Boolean = false, // default: male

    var speed: Float = 1.0f,

    var isSignedIn: Boolean = false,

    var displayName: String = "",

    var email: String = "",

    var photoURL: String = ""
)
