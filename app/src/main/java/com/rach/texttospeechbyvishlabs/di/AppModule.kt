package com.rach.texttospeechbyvishlabs.di

import android.content.Context
import com.rach.texttospeechbyvishlabs.data.repository.TtsRepositoryImpl
import com.rach.texttospeechbyvishlabs.data.tts.AdvancedTTSManager
import com.rach.texttospeechbyvishlabs.domain.repository.TtsRepository
import com.rach.texttospeechbyvishlabs.domain.usecase.ChangeLanguageUseCase
import com.rach.texttospeechbyvishlabs.domain.usecase.ChangeVoiceCategoryUseCase
import com.rach.texttospeechbyvishlabs.domain.usecase.SaveAudioUseCase
import com.rach.texttospeechbyvishlabs.domain.usecase.SpeakParagraphsUseCase
import com.rach.texttospeechbyvishlabs.domain.usecase.SpeakTextUseCase
import com.rach.texttospeechbyvishlabs.domain.usecase.StopSpeakingUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTtsManager(
        @ApplicationContext context: Context
    ): AdvancedTTSManager {
        return AdvancedTTSManager(context)
    }

    @Provides
    @Singleton
    fun provideTtsRepository(
        manager: AdvancedTTSManager
    ): TtsRepository {
        return TtsRepositoryImpl(manager)
    }

    @Provides fun provideSpeakText(repo: TtsRepository) = SpeakTextUseCase(repo)
    @Provides fun provideStop(repo: TtsRepository) = StopSpeakingUseCase(repo)
    @Provides fun provideLang(repo: TtsRepository) = ChangeLanguageUseCase(repo)
    @Provides fun provideVoice(repo: TtsRepository) = ChangeVoiceCategoryUseCase(repo)
    @Provides fun provideSave(repo: TtsRepository) = SaveAudioUseCase(repo)
    @Provides fun provideParagraph(repo: TtsRepository) = SpeakParagraphsUseCase(repo)
}
