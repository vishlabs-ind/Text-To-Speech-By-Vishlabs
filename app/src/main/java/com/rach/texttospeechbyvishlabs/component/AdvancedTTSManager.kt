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




