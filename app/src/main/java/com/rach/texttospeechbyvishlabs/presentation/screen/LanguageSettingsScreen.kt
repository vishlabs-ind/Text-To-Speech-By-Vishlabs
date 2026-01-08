package com.rach.texttospeechbyvishlabs.presentation.screen


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rach.texttospeechbyvishlabs.BackTopBar
import com.rach.texttospeechbyvishlabs.domain.model.languageOptions

@Composable
fun LanguageSettingsScreen(
    paddingValues: PaddingValues,
    selectedLanguageIndex: Int,
    onLanguageSelected: (Int) -> Unit,
    onBackClick: () -> Unit
) {
    Column(Modifier.fillMaxSize().padding(paddingValues)) {

        BackTopBar("Language Settings", onBackClick)

        LazyColumn(Modifier.padding(16.dp)) {
            itemsIndexed(languageOptions) { index, option ->
                ElevatedCard(
                    onClick = { onLanguageSelected(index) },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    Row(
                        Modifier.fillMaxWidth().padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(option.first)
                        RadioButton(
                            selected = index == selectedLanguageIndex,
                            onClick = { onLanguageSelected(index) }
                        )
                    }
                }
            }
        }
    }
}
