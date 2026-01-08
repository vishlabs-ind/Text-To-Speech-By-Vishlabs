package com.rach.texttospeechbyvishlabs.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Card
import androidx.compose.material.TextButton
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.contentDescription
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

        BannerAdView()

        Card(modifier = Modifier.weight(1f)) {
            OutlinedTextField(
                value = text,
                onValueChange = { onTextChange(it) },
                label = { Text("Enter text") },
                modifier = Modifier.fillMaxSize().padding(10.dp)
            )
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