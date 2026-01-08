package com.rach.texttospeechbyvishlabs.data.repository

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
        ttsManager.saveToDownloads(text, fileName) {}
}