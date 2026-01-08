package com.rach.texttospeechbyvishlabs.domain.usecase

import com.rach.texttospeechbyvishlabs.domain.repository.TtsRepository

class StopSpeakingUseCase(
    private val repo: TtsRepository
) {
    operator fun invoke() = repo.stop()
}