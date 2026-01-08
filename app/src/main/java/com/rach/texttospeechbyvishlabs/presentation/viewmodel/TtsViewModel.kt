package com.rach.texttospeechbyvishlabs.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.rach.texttospeechbyvishlabs.domain.model.VoiceCategory
import com.rach.texttospeechbyvishlabs.domain.usecase.*

import java.util.Locale

class TtsViewModel(
    private val speakText: SpeakTextUseCase,
    private val stopSpeaking: StopSpeakingUseCase,
    private val changeLanguage: ChangeLanguageUseCase,
    private val changeVoice: ChangeVoiceCategoryUseCase,
    private val saveAudio: SaveAudioUseCase
) : ViewModel() {

    fun speak(text: String) = speakText(text)

    fun stop() = stopSpeaking()

    fun setLanguage(locale: Locale) = changeLanguage(locale)

    fun setVoice(category: VoiceCategory) = changeVoice(category)

    fun save(text: String, name: String) =
        saveAudio(text, name)
}