package com.vietbahnartranslate.model.data

data class Translation(
    val id: Int,
    val vietnameseSrc: String,
    val bahnaricTgt: String,
    val maleSpeech: String,
    val femaleSpeech: String
)
