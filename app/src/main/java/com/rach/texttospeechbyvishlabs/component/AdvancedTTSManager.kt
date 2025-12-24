package com.rach.texttospeechbyvishlabs.component

import android.content.Context
import android.media.AudioAttributes
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import java.io.File
import java.util.Locale

class AdvancedTTSManager(
    private val context: Context,
    private val onReady: (() -> Unit)? = null
) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = TextToSpeech(context, this)
    var isReady = false
        private set

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {

            // Natural audio output
            tts?.setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build()
            )

            // Default realistic voice
            setLanguageSafe(Locale.US)
            setBestVoice(Locale.US)
            setNaturalVoice()

            isReady = true
            onReady?.invoke()
        }
    }

    // -------------------- Language --------------------
    fun setLanguageSafe(locale: Locale) {
        val result = tts?.setLanguage(locale)
        if (result == TextToSpeech.LANG_MISSING_DATA ||
            result == TextToSpeech.LANG_NOT_SUPPORTED) {
            tts?.language = Locale.US
        }
    }

    fun setBestVoice(locale: Locale) {
        val voices = tts?.voices ?: return

        val bestVoice = voices.firstOrNull { voice ->
            voice.locale == locale &&
                    !voice.isNetworkConnectionRequired &&
                    voice.quality >= 400   // High quality threshold
        }

        bestVoice?.let {
            tts?.voice = it
        }
    }



    // -------------------- Natural sound --------------------
    fun setNaturalVoice() {
        tts?.setPitch(1.0f)        // Human pitch
        tts?.setSpeechRate(0.95f)  // Slightly slow = natural
    }

    // -------------------- Speak --------------------
    fun speak(text: String) {
        if (!isReady) return

        val formatted = text
            .replace(",", ", ")
            .replace(".", ". ")
            .replace("!", "! ")
            .replace("?", "? ")

        tts?.speak(
            formatted,
            TextToSpeech.QUEUE_FLUSH,
            null,
            "speak_realistic"
        )
    }

    // -------------------- Save as WAV --------------------
    fun saveToDownloads(
        text: String,
        fileName: String,
        onDone: () -> Unit
    ) {
        if (!isReady) return

        val downloadsDir = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DOWNLOADS
        )
        val file = File(downloadsDir, "$fileName.wav")

        tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onDone(utteranceId: String?) {
                Handler(Looper.getMainLooper()).post {
                    onDone()
                }
            }
            override fun onStart(utteranceId: String?) {}
            override fun onError(utteranceId: String?) {}
        })

        val params = Bundle().apply {
            putString(
                TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,
                "save_tts"
            )
        }

        tts?.synthesizeToFile(text, params, file, "save_tts")
    }

    // -------------------- Controls --------------------
    fun stop() {
        tts?.stop()
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
    }
}


/*
package com.rach.texttospeechbyvishlabs.component

import android.content.Context
import android.media.AudioAttributes
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import java.io.File
import java.util.Locale

class AdvancedTTSManager(private val context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = TextToSpeech(context, this)
    var isReady = false

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts?.setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_ACCESSIBILITY)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build()
            )
            isReady = true
        }
    }

    fun setLanguage(locale: Locale) {
        tts?.language = locale
    }

    fun setPitch(pitch: Float) {
        tts?.setPitch(pitch)
    }

    fun setSpeed(speed: Float) {
        tts?.setSpeechRate(speed)
    }

    fun speak(text: String) {
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "tts_id")
    }


    fun saveToDownloads(context: Context, text: String, fileName: String, onDone: () -> Unit) {
        val downloadsDir = android.os.Environment.getExternalStoragePublicDirectory(
            android.os.Environment.DIRECTORY_DOWNLOADS
        )
        val file = File(downloadsDir, "$fileName.wav")

        tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {

            override fun onDone(utteranceId: String?) {
                Handler(Looper.getMainLooper()).post {
                    onDone()   // ✅ now runs on UI thread
                }
            }

            override fun onStart(utteranceId: String?) {}
            override fun onError(utteranceId: String?) {}
        })

        val params = Bundle().apply {
            putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "save_tts_downloads")
        }

        tts?.synthesizeToFile(text, params, file, "save_tts_downloads")
    }

    fun stop() = tts?.stop()

    fun shutdown() = tts?.shutdown()
}

data class LanguageItem(val name: String, val locale: Locale)

val languages = listOf(
    LanguageItem("English", Locale.US),
    LanguageItem("Hindi", Locale("hi", "IN")),
    LanguageItem("French", Locale.FRANCE),
    LanguageItem("German", Locale.GERMANY)
)




*/
