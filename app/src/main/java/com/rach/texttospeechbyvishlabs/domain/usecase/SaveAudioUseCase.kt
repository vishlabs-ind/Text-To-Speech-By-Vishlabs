package com.rach.texttospeechbyvishlabs.domain.usecase

import com.rach.texttospeechbyvishlabs.domain.repository.TtsRepository

class SaveAudioUseCase(
    private val repo: TtsRepository
) {
    operator fun invoke(text: String, fileName: String) =
        repo.saveToDownloads(text, fileName)
}