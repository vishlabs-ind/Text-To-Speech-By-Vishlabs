package com.rach.texttospeechbyvishlabs

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Card
import androidx.compose.material.TextButton
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.rach.texttospeechbyvishlabs.component.AdvancedTTSManager
import com.rach.texttospeechbyvishlabs.component.BottomNavBar
import com.rach.texttospeechbyvishlabs.component.CustomTopAppBar
import com.rach.texttospeechbyvishlabs.ui.theme.HabitChangeTheme
import java.util.Locale

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HabitChangeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AdvancedTTSScreen()
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdvancedTTSScreen() {
    val context = LocalContext.current
    val ttsManager = remember { AdvancedTTSManager(context) }
    var selectedIndex by remember { mutableStateOf(0) }
    var text by remember { mutableStateOf("") }
    DisposableEffect(Unit) {
        onDispose { ttsManager.shutdown() }
    }
    var showSettings by remember { mutableStateOf(false) }


    Scaffold(
        topBar = {
            CustomTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = if (showSettings) "Settings" else "Home",
                onSettingsClick = { showSettings = true },
                onNavigationIconClick={showSettings = false }
            )
        },

        floatingActionButtonPosition = FabPosition.Center,

        bottomBar = {
            BottomNavBar(
                selectedItem = selectedIndex,
                onItemSelected = { index ->
                    selectedIndex = index
                    when (index) {
                        0 -> {}
                        1 -> {
                            ttsManager.stop()
                        }

                        2 -> {
                            ttsManager.saveToDownloads(
                                text = text,
                                fileName = "tts_${System.currentTimeMillis()}"
                            ) {
                                Toast.makeText(
                                    context,
                                    "Saved in Download folder",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        3 -> {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    "Check out this app:\nhttps://play.google.com/store/apps/details?id=${context.packageName}"
                                )
                            }
                            context.startActivity(
                                Intent.createChooser(shareIntent, "Share app via")
                            )
                        }

                        4 -> {
                            val intent = Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", context.packageName, null)
                            )
                            context.startActivity(intent)
                        }
                    }
                }
            )
        }


    ) { paddingValues ->
        if (showSettings) {
            TTSSettingsScreen(
                ttsManager = ttsManager,
                onBack = { showSettings = false }
            )
        } else {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .semantics { contentDescription = "Text to Speech Screen" },
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                var text by remember { mutableStateOf("") }
                var showDialog by remember { mutableStateOf(false) }

                val wordCount = text
                    .trim()
                    .split("\\s+".toRegex())
                    .filter { it.isNotEmpty() }
                    .size

                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    OutlinedTextField(
                        value = text,
                        onValueChange = { newText ->
                            val newWordCount = newText
                                .trim()
                                .split("\\s+".toRegex())
                                .filter { it.isNotEmpty() }
                                .size

                            if (newWordCount <= 2000) {
                                text = newText
                            } else {
                                showDialog = true
                            }
                        },
                        label = { Text("Enter text") },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp)
                    )
                }


                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        title = { Text("Subscription Required") },
                        text = {
                            Text("You have exceeded 2000 words. Please take a subscription to continue.")
                        },
                        confirmButton = {
                            TextButton(onClick = {
                                showDialog = false
                                // Navigate to subscription screen
                            }) {
                                Text("Subscribe")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showDialog = false }) {
                                Text("Cancel")
                            }
                        }
                    )
                }


                Button(
                    onClick = { ttsManager.speak(text) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Play 🔊")
                }
            }
        }


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TTSSettingsScreen(
    ttsManager: AdvancedTTSManager,
    onBack: () -> Unit
) {
    var selectedLanguage by remember { mutableStateOf(languageOptions.first()) }
    var selectedCategory by remember { mutableStateOf(VoiceCategory.NATURAL) }

    var languageExpanded by remember { mutableStateOf(false) }
    var categoryExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = "Voice Settings",
                onSettingsClick = {} // not needed here
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {


            Text("Language", style = MaterialTheme.typography.titleMedium)

            ExposedDropdownMenuBox(
                expanded = languageExpanded,
                onExpandedChange = { languageExpanded = !languageExpanded }
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    readOnly = true,
                    value = selectedLanguage.first,
                    onValueChange = {},
                    label = { Text("Select Language") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = languageExpanded)
                    }
                )

                ExposedDropdownMenu(
                    expanded = languageExpanded,
                    onDismissRequest = { languageExpanded = false }
                ) {
                    languageOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option.first) },
                            onClick = {
                                selectedLanguage = option
                                languageExpanded = false
                                ttsManager.setVoiceLanguage(option.second)
                            }
                        )
                    }
                }
            }


            Text("Voice Category", style = MaterialTheme.typography.titleMedium)

            ExposedDropdownMenuBox(
                expanded = categoryExpanded,
                onExpandedChange = { categoryExpanded = !categoryExpanded }
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    readOnly = true,
                    value = selectedCategory.name,
                    onValueChange = {},
                    label = { Text("Select Voice Category") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded)
                    }
                )

                ExposedDropdownMenu(
                    expanded = categoryExpanded,
                    onDismissRequest = { categoryExpanded = false }
                ) {
                    VoiceCategory.values().forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category.name) },
                            onClick = {
                                selectedCategory = category
                                categoryExpanded = false
                                ttsManager.applyVoiceCategory(category)
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Back")
            }
        }
    }
}


val languageOptions = listOf(
    "English (US)" to Locale.US,
    "English (UK)" to Locale.UK,
    "Hindi" to Locale("hi", "IN"),
    "French" to Locale.FRANCE,
    "German" to Locale.GERMANY,
    "Spanish" to Locale("es", "ES")
)
enum class VoiceCategory {
    NATURAL,
    MALE,
    FEMALE,
    CHILD,
    ROBOT
}







