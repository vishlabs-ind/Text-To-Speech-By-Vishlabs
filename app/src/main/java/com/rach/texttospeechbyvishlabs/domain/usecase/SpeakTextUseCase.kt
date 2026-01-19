package com.rach.texttospeechbyvishlabs.domain.usecase

import com.rach.texttospeechbyvishlabs.domain.repository.TtsRepository

class SpeakTextUseCase(
    private val repo: TtsRepository
) {
    operator fun invoke(text: String) = repo.speak(text)

    fun speakWithCallback(text: String, onDone: () -> Unit) {
        repo.speakWithCallback(text, onDone)
    }

}
