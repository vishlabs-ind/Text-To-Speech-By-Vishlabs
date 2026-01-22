package com.rach.texttospeechbyvishlabs.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rach.texttospeechbyvishlabs.domain.model.VoiceCategory
import com.rach.texttospeechbyvishlabs.domain.repository.TtsRepository
import com.rach.texttospeechbyvishlabs.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class TtsViewModel @Inject constructor(
    private val speakText: SpeakTextUseCase,
    private val stopSpeaking: StopSpeakingUseCase,
    private val changeLanguage: ChangeLanguageUseCase,
    private val changeVoice: ChangeVoiceCategoryUseCase,
    private val saveAudio: SaveAudioUseCase,
    private val speakParagraphsUseCase: SpeakParagraphsUseCase,
    private val repository: TtsRepository

) : ViewModel() {

    private val _speakingIndex = MutableStateFlow(-1)
    val speakingIndex: StateFlow<Int> = _speakingIndex

    fun speak(text: String) = speakText(text)

    fun stop() = stopSpeaking()

    fun setLanguage(locale: Locale) = changeLanguage(locale)

    fun setVoice(category: VoiceCategory) = changeVoice(category)

    fun save(text: String, name: String) = saveAudio(text, name)

    fun stopTts() {
        stopSpeaking()
    }

    fun saveToUri(text: String, uri: Uri) {
        repository.saveToUri(text, uri)
    }


    fun speakWithHighlight(text: String) {
        val lines = text.split("\n")

        val speakable = lines
            .mapIndexedNotNull { index, line ->
                if (line.trim().isNotEmpty()) index to line else null
            }

        if (speakable.isEmpty()) return

        val paragraphs = speakable.map { it.second }

        viewModelScope.launch {
            speakParagraphsUseCase(
                paragraphs = paragraphs,
                onIndexChange = { idx ->
                    _speakingIndex.value = idx
                },
                onFinished = {
                    _speakingIndex.value = -1
                }
            )
        }
    }

}
