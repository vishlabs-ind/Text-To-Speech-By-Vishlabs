package com.rach.texttospeechbyvishlabs.domain.repository

import com.rach.texttospeechbyvishlabs.domain.model.VoiceCategory
import java.util.Locale

interface TtsRepository {
    fun speak(text: String)
    fun stop()
    fun setLanguage(locale: Locale)
    fun setVoiceCategory(category: VoiceCategory)
    fun saveToDownloads(text: String, fileName: String)
}