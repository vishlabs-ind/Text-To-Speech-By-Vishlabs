package com.rach.texttospeechbyvishlabs.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.TextButton
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rach.texttospeechbyvishlabs.BannerAdView
import androidx.compose.ui.text.input.TextFieldValue


@Composable
fun HomeScreen(
    paddingValues: PaddingValues,
    text: String,
    onTextChange: (String) -> Unit,
    speakingIndex: Int,
    onPlayClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        var showDialog by remember { mutableStateOf(false) }

        val wordCount = remember(text) {
            text.trim()
                .split("\\s+".toRegex())
                .filter { it.isNotEmpty() }
                .size
        }

        BannerAdView()

        Card(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Enter your text", fontWeight = FontWeight.SemiBold)
                    Text("$wordCount / 2000", color = MaterialTheme.colorScheme.primary)
                }

                Spacer(modifier = Modifier.height(8.dp))


                var fieldValue by remember {
                    mutableStateOf(TextFieldValue(text))
                }

                LaunchedEffect(text) {
                    if (text != fieldValue.text) {
                        fieldValue = fieldValue.copy(text = text)
                    }
                }

                val lines = text.split("\n")

                val speakableLineIndexes = remember(text) {
                    lines.mapIndexedNotNull { index, line ->
                        if (line.trim().isNotEmpty()) index else null
                    }
                }

                val highlightColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)

                val annotatedText = remember(text, speakingIndex) {
                    buildAnnotatedString {
                        val highlightedLine =
                            if (speakingIndex in speakableLineIndexes.indices)
                                speakableLineIndexes[speakingIndex]
                            else -1

                        lines.forEachIndexed { lineIndex, line ->
                            if (lineIndex == highlightedLine) {
                                withStyle(SpanStyle(background = highlightColor)) {
                                    append(line)
                                }
                            } else {
                                append(line)
                            }
                            if (lineIndex != lines.lastIndex) append("\n")
                        }
                    }
                }


                LaunchedEffect(annotatedText) {
                    fieldValue = fieldValue.copy(
                        annotatedString = annotatedText,
                        selection = fieldValue.selection
                    )
                }

                OutlinedTextField(
                    value = fieldValue,
                    onValueChange = { newValue ->
                        fieldValue = newValue
                        onTextChange(newValue.text)
                    },
                    modifier = Modifier.fillMaxSize(),
                    placeholder = { Text("Please enter the text to read aloud.") },
                    shape = RoundedCornerShape(16.dp),
                    maxLines = Int.MAX_VALUE
                )


            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Subscription Required") },
                text = { Text("You exceeded 2000 words") },
                confirmButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("OK")
                    }
                }
            )
        }

        Button(
            onClick = onPlayClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Play 🔊")
        }
    }
}
