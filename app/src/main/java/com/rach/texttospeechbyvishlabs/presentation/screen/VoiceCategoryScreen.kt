package com.rach.texttospeechbyvishlabs.presentation.screen


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rach.texttospeechbyvishlabs.BackTopBar
import com.rach.texttospeechbyvishlabs.domain.model.VoiceCategory

@Composable
fun VoiceCategoryScreen(
    paddingValues: PaddingValues,
    selectedCategoryName: String,
    onCategorySelected: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val selected = VoiceCategory.valueOf(selectedCategoryName)

    Column(Modifier.fillMaxSize().padding(paddingValues)) {

        BackTopBar("Voice Category", onBackClick)

        LazyColumn(Modifier.padding(16.dp)) {
            items(VoiceCategory.values()) { category ->
                ElevatedCard(
                    onClick = { onCategorySelected(category.name) },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    Row(
                        Modifier.fillMaxWidth().padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(category.name)
                        RadioButton(
                            selected = category == selected,
                            onClick = { onCategorySelected(category.name) }
                        )
                    }
                }
            }
        }
    }
}
