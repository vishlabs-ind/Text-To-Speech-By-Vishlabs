package com.rach.texttospeechbyvishlabs.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Card
import androidx.compose.material.TextButton
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rach.texttospeechbyvishlabs.BannerAdView

@Composable
fun HomeScreen(
    paddingValues: PaddingValues,
    text: String,
    onTextChange: (String) -> Unit,
    onPlayClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp)
            .semantics { contentDescription = "Text to Speech Screen" },
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
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp)
            ) {

                // Header row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Enter your text",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = "${wordCount}/2000",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = text,
                    onValueChange = { newText ->
                        val newWordCount = newText
                            .trim()
                            .split("\\s+".toRegex())
                            .filter { it.isNotEmpty() }
                            .size

                        if (newWordCount <= 2000) {
                            onTextChange(newText)
                        } else {
                            showDialog = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxSize(),
                    placeholder = {
                        Text("Please enter the text to read aloud.")
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    ),
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