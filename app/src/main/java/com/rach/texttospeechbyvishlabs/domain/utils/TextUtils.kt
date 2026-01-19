package com.rach.texttospeechbyvishlabs.domain.utils

fun splitIntoParagraphs(text: String): List<String> {
    return text.split("\n")
        .map { it.trim() }
        .filter { it.isNotEmpty() }
}