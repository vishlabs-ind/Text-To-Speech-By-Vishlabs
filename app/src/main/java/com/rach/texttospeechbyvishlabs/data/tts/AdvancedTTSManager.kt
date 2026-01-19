package com.rach.texttospeechbyvishlabs.data.tts

import android.content.Context
import android.media.AudioAttributes
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import com.rach.texttospeechbyvishlabs.domain.model.VoiceCategory
import java.io.File
import java.util.Locale

class AdvancedTTSManager(
    private val context: Context,
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
                setPitch(0.75f)
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
        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            tts.language = Locale.US
        }
    }

    fun speak(text: String) {
        if (!isReady || text.isBlank()) return

        val params = Bundle().apply {
            putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "simple_speak")
        }

        tts.speak(text, TextToSpeech.QUEUE_FLUSH, params, "simple_speak")
    }

    fun speakParagraphs(
        paragraphs: List<String>,
        onIndexChange: (Int) -> Unit,
        onFinished: () -> Unit
    ) {
        if (!isReady || paragraphs.isEmpty()) return

        var index = 0

        fun speakNext() {
            if (index >= paragraphs.size) {
                Handler(Looper.getMainLooper()).post {
                    onFinished()
                }
                return
            }

            val utteranceId = "para_$index"

            val params = Bundle().apply {
                putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utteranceId)
            }

            tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(id: String?) {
                    if (id == utteranceId) {
                        Handler(Looper.getMainLooper()).post {
                            onIndexChange(index)
                        }
                    }
                }

                override fun onDone(id: String?) {
                    if (id == utteranceId) {
                        index++
                        speakNext()
                    }
                }

                override fun onError(id: String?) {}
            })

            tts.speak(
                paragraphs[index],
                TextToSpeech.QUEUE_FLUSH,
                params,
                utteranceId
            )
        }

        speakNext()
    }

    fun saveToUri(
        text: String,
        uri: Uri,
        onDone: () -> Unit
    ) {
        if (!isReady || text.isBlank()) return

        val tempFile = File(context.cacheDir, "tts_temp.wav")

        val params = Bundle().apply {
            putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "save_temp")
        }

        tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onDone(id: String?) {
                if (id == "save_temp") {
                    try {
                        val input = tempFile.inputStream()
                        val output = context.contentResolver.openOutputStream(uri)

                        input.copyTo(output!!)
                        input.close()
                        output.close()

                        Handler(Looper.getMainLooper()).post { onDone() }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onStart(id: String?) {}
            override fun onError(id: String?) {}
        })

        tts.synthesizeToFile(text, params, tempFile, "save_temp")
    }

    fun saveToDownloads(
        text: String,
        fileName: String
    ) {
        if (!isReady || text.isBlank()) return

        val downloadsDir = context.getExternalFilesDir(null)
        val file = File(downloadsDir, "$fileName.wav")

        val utteranceId = "save_download_${System.currentTimeMillis()}"

        val params = Bundle().apply {
            putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utteranceId)
        }

        tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onDone(id: String?) {
                if (id == utteranceId) {
                    Handler(Looper.getMainLooper()).post {
                    }
                }
            }

            override fun onStart(id: String?) {}
            override fun onError(id: String?) {}
        })

        tts.synthesizeToFile(text, params, file, utteranceId)
    }

    fun speakWithCallback(
        text: String,
        onDone: () -> Unit
    ) {
        if (!isReady || text.isBlank()) return

        val utteranceId = "callback_${System.currentTimeMillis()}"

        val params = Bundle().apply {
            putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utteranceId)
        }

        tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onDone(id: String?) {
                if (id == utteranceId) {
                    Handler(Looper.getMainLooper()).post {
                        onDone()
                    }
                }
            }

            override fun onStart(id: String?) {}
            override fun onError(id: String?) {}
        })

        tts.speak(text, TextToSpeech.QUEUE_FLUSH, params, utteranceId)
    }

    fun stop() = tts.stop()

    fun shutdown() {
        tts.stop()
        tts.shutdown()
    }
}
