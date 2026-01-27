package com.rach.texttospeechbyvishlabs.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rach.texttospeechbyvishlabs.domain.model.VoiceCategory
import com.rach.texttospeechbyvishlabs.domain.repository.TtsRepository
import com.rach.texttospeechbyvishlabs.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
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


    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying


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



    private var speakJob: Job? = null

    fun speakWithHighlight(text: String) {
        val lines = text.split("\n")
        val speakableMap = buildSpeakableLineMap(text)

        if (speakableMap.isEmpty()) return

        val paragraphs = speakableMap.map { lines[it] }

        speakJob?.cancel()
        _isPlaying.value = true

        val startIndex = _currentIndex.value.coerceIn(0, paragraphs.lastIndex)

        speakJob = viewModelScope.launch {
            speakParagraphsUseCase(
                paragraphs = paragraphs,
                startIndex = startIndex,   // 👈 this is key

                onIndexChange = { index ->
                    val realLineIndex = speakableMap[index]
                    _speakingIndex.value = realLineIndex
                    _currentIndex.value = index
                },

                onFinished = {
                    _isPlaying.value = false
                    _speakingIndex.value = -1
                    _currentIndex.value = 0
                }
            )
        }
    }







    private fun getSpeakableMap(text: String): List<Int> {
        return text.split("\n")
            .mapIndexedNotNull { index, line ->
                if (line.trim().isNotEmpty()) index else null
            }
    }

    private fun buildSpeakableLineMap(text: String): List<Int> {
        return text.split("\n")
            .mapIndexedNotNull { index, line ->
                if (line.isNotBlank()) index else null
            }
    }



    fun togglePlayPause(text: String) {
        if (_isPlaying.value) {
            speakJob?.cancel()     // ✅ critical
            stopSpeaking()
            _isPlaying.value = false
        } else {
            speakWithHighlight(text)
        }
    }



    fun playNext(text: String) {
        val max = getSpeakableMap(text).lastIndex
        _currentIndex.value = (_currentIndex.value + 1).coerceAtMost(max)

        speakJob?.cancel()
        stopSpeaking()
        speakWithHighlight(text)
    }

    fun playPrevious(text: String) {
        _currentIndex.value = (_currentIndex.value - 1).coerceAtLeast(0)

        speakJob?.cancel()
        stopSpeaking()
        speakWithHighlight(text)
    }







}
