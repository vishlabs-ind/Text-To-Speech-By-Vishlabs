package com.rach.texttospeechbyvishlabs.domain.usecase

import com.rach.texttospeechbyvishlabs.domain.model.VoiceCategory
import com.rach.texttospeechbyvishlabs.domain.repository.TtsRepository

class ChangeVoiceCategoryUseCase(
    private val repo: TtsRepository
) {
    operator fun invoke(category: VoiceCategory) =
        repo.setVoiceCategory(category)
}