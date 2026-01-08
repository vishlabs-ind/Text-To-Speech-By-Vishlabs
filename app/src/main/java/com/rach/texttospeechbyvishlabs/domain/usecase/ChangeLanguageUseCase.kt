package com.rach.texttospeechbyvishlabs.domain.usecase

import com.rach.texttospeechbyvishlabs.domain.repository.TtsRepository
import java.util.Locale

class ChangeLanguageUseCase(
    private val repo: TtsRepository
) {
    operator fun invoke(locale: Locale) = repo.setLanguage(locale)
}