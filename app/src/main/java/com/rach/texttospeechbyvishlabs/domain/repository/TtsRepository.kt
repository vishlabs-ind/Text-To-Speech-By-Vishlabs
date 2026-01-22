package com.rach.texttospeechbyvishlabs.domain.repository

import android.net.Uri
import com.rach.texttospeechbyvishlabs.domain.model.VoiceCategory
import java.util.Locale

interface TtsRepository {
    fun speak(text: String)
    fun stop()
    fun setLanguage(locale: Locale)
    fun setVoiceCategory(category: VoiceCategory)
    fun saveToDownloads(text: String, fileName: String)
    fun speakWithCallback(text: String, onDone: () -> Unit)

    fun saveToUri(text: String, uri: Uri)

    fun speakParagraphs(
        paragraphs: List<String>,
        onIndexChange: (Int) -> Unit,
        onFinished: () -> Unit
    )


}