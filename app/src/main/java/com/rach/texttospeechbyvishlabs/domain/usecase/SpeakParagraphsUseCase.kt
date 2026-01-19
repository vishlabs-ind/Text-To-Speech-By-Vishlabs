package com.rach.texttospeechbyvishlabs.domain.usecase

import com.rach.texttospeechbyvishlabs.domain.repository.TtsRepository

class SpeakParagraphsUseCase(
    private val repository: TtsRepository
) {
    operator fun invoke(
        paragraphs: List<String>,
        onIndexChange: (Int) -> Unit,
        onFinished: () -> Unit
    ) {
        repository.speakParagraphs(paragraphs, onIndexChange, onFinished)
    }
}
