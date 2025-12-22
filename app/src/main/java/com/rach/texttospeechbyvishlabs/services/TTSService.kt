package com.rach.texttospeechbyvishlabs.services

import android.app.Service
import android.content.Intent
import android.speech.tts.TextToSpeech
import java.util.Locale

class TTSService : Service() {

    private lateinit var tts: TextToSpeech

    override fun onCreate() {
        super.onCreate()
        tts = TextToSpeech(this) {
            tts.language = Locale.US
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val text = intent?.getStringExtra("text") ?: ""
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        tts.shutdown()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?) = null
}
