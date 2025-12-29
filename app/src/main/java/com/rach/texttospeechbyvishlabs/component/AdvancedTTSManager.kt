package com.rach.texttospeechbyvishlabs.component

import android.content.Context
import android.media.AudioAttributes
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import com.rach.texttospeechbyvishlabs.VoiceCategory
import java.io.File
import java.util.Locale

class AdvancedTTSManager(
    context: Context,
    private val onReady: (() -> Unit)? = null
) : TextToSpeech.OnInitListener {

    private val tts: TextToSpeech = TextToSpeech(context, this)

    var currentPitch = 1.0f
    var currentRate = 0.95f
    var currentLocale: Locale = Locale.US
    var currentCategory: VoiceCategory = VoiceCategory.NATURAL

    var isReady = false
        private set

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {

            tts.setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build()
            )

            setVoiceLanguage(Locale.US)
            applyVoiceCategory(VoiceCategory.NATURAL)

            isReady = true
            onReady?.invoke()
        }
    }


    fun applyVoiceCategory(category: VoiceCategory) {
        currentCategory = category

        when (category) {
            VoiceCategory.NATURAL -> {
                setPitch(1.0f)
                setSpeechRate(0.95f)
            }

            VoiceCategory.MALE -> {
                setPitch(0.9f)
                setSpeechRate(0.9f)
            }

            VoiceCategory.FEMALE -> {
                setPitch(1.2f)
                setSpeechRate(1.0f)
            }

            VoiceCategory.CHILD -> {
                setPitch(1.5f)
                setSpeechRate(1.1f)
            }

            VoiceCategory.ROBOT -> {
                setPitch(0.7f)
                setSpeechRate(0.75f)
            }
        }
    }


    fun setPitch(value: Float) {
        currentPitch = value.coerceIn(0.5f, 2.0f)
        tts.setPitch(currentPitch)
    }

    fun setSpeechRate(value: Float) {
        currentRate = value.coerceIn(0.5f, 2.0f)
        tts.setSpeechRate(currentRate)
    }


    fun setVoiceLanguage(locale: Locale) {
        currentLocale = locale

        val result = tts.setLanguage(locale)
        if (result == TextToSpeech.LANG_MISSING_DATA ||
            result == TextToSpeech.LANG_NOT_SUPPORTED
        ) {
            tts.language = Locale.US
        }

        selectBestVoice(locale)
    }

    private fun selectBestVoice(locale: Locale) {
        val voice = tts.voices?.firstOrNull {
            it.locale == locale &&
                    !it.isNetworkConnectionRequired &&
                    it.quality >= 400
        }
        voice?.let { tts.voice = it }
    }


    fun speak(text: String) {
        if (!isReady || text.isBlank()) return

        setVoiceLanguage(currentLocale)
        applyVoiceCategory(currentCategory)

        tts.speak(
            text,
            TextToSpeech.QUEUE_FLUSH,
            null,
            "tts_speak"
        )
    }


    fun saveToDownloads(
        text: String,
        fileName: String,
        onDone: () -> Unit
    ) {
        if (!isReady || text.isBlank()) return

        val dir = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DOWNLOADS
        )
        val file = File(dir, "$fileName.wav")

        tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onDone(utteranceId: String?) {
                Handler(Looper.getMainLooper()).post { onDone() }
            }

            override fun onStart(utteranceId: String?) {}
            override fun onError(utteranceId: String?) {}
        })

        val params = Bundle().apply {
            putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "save")
        }

        tts.synthesizeToFile(text, params, file, "save")
    }

    fun stop() = tts.stop()

    fun shutdown() {
        tts.stop()
        tts.shutdown()
    }
}
