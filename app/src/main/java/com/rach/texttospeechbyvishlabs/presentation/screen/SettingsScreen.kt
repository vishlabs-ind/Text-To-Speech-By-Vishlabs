package com.rach.texttospeechbyvishlabs.presentation.screen


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.rach.texttospeechbyvishlabs.BackTopBar

@Composable
fun SettingsScreen(
    paddingValues: PaddingValues,
    onBackClick: () -> Unit,
    onNavigateToLanguage: () -> Unit,
    onNavigateToVoice: () -> Unit,
    onNavigateToAbout: () -> Unit,
    onNavigateToPrivacy: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {

        BackTopBar("Settings", onBackClick)

        LazyColumn(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                SettingsItemCard(Icons.Outlined.Translate, "Language Settings", onNavigateToLanguage)
            }
            item {
                SettingsItemCard(Icons.Outlined.VolumeUp, "Voice Category", onNavigateToVoice)
            }
            item {
                SettingsItemCard(Icons.Outlined.Info, "About", onNavigateToAbout)
            }
            item {
                SettingsItemCard(Icons.Outlined.PrivacyTip, "Privacy Policy", onNavigateToPrivacy)
            }
        }
    }
}


@Composable
fun SettingsItemCard(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit
) {
    ElevatedCard(
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )

            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
