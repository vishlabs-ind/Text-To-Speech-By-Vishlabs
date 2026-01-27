package com.rach.texttospeechbyvishlabs.data.repository

import android.net.Uri
import com.rach.texttospeechbyvishlabs.data.tts.AdvancedTTSManager
import com.rach.texttospeechbyvishlabs.domain.model.VoiceCategory
import com.rach.texttospeechbyvishlabs.domain.repository.TtsRepository
import java.util.Locale

class TtsRepositoryImpl(
    private val ttsManager: AdvancedTTSManager
) : TtsRepository {

    override fun speak(text: String) = ttsManager.speak(text)

    override fun stop() {
        ttsManager.stop()
    }

    override fun setLanguage(locale: Locale) =
        ttsManager.setVoiceLanguage(locale)

    override fun setVoiceCategory(category: VoiceCategory) =
        ttsManager.applyVoiceCategory(category)

    override fun saveToDownloads(text: String, fileName: String) =
        ttsManager.saveToDownloads(text, fileName)

    override fun speakWithCallback(text: String, onDone: () -> Unit) {
        ttsManager.speakWithCallback(text, onDone)
    }

    override fun saveToUri(text: String, uri: Uri) {
        ttsManager.saveToUri(text, uri) {}
    }


    override fun speakParagraphs(
        paragraphs: List<String>,
        startIndex : Int,
        onIndexChange: (Int) -> Unit,
        onFinished: () -> Unit
    ) {
        ttsManager.speakParagraphs(paragraphs, startIndex, onIndexChange, onFinished)
    }


}