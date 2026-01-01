package com.rach.texttospeechbyvishlabs

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Card
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.rach.texttospeechbyvishlabs.component.AdvancedTTSManager
import com.rach.texttospeechbyvishlabs.component.BottomNavBar
import com.rach.texttospeechbyvishlabs.ui.theme.HabitChangeTheme
import kotlinx.coroutines.launch
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

@Composable
fun AdvancedTTSScreen() {
    val context = LocalContext.current
    val ttsManager = remember { AdvancedTTSManager(context) }
    var selectedIndex by remember { mutableStateOf(0) }
    var text by remember { mutableStateOf("") }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var currentScreen by remember { mutableStateOf(DrawerScreen.HOME) }
    var backPressedTime by remember { mutableStateOf(0L) }
    BackHandler {
        if (currentScreen != DrawerScreen.HOME) {

            currentScreen = DrawerScreen.HOME
        } else {
            val currentTime = System.currentTimeMillis()
            if (currentTime - backPressedTime < 2000) {
                (context as? AppCompatActivity)?.finish()
            } else {
                backPressedTime = currentTime
                Toast.makeText(
                    context,
                    "Press back again to exit",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    DisposableEffect(Unit) {
        onDispose {
            ttsManager.shutdown()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                currentScreen = currentScreen,
                onScreenSelected = { screen ->
                    currentScreen = screen
                    scope.launch { drawerState.close() }
                },
                onClose = {
                    scope.launch { drawerState.close() }
                }
            )
        },
        gesturesEnabled = drawerState.isOpen
    ) {
        Scaffold(
            topBar = {
            },
            floatingActionButtonPosition = FabPosition.Center,
            bottomBar = {
                BottomNavBar(
                    selectedItem = selectedIndex,
                    onItemSelected = { index ->
                        selectedIndex = index
                        when (index) {
                            0 -> {
                                currentScreen = DrawerScreen.HOME
                            }
                            1 -> {
                                ttsManager.stop()
                                text = ""
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
                                scope.launch {
                                    if (drawerState.isOpen) {
                                        drawerState.close()
                                    } else {
                                        drawerState.open()
                                    }
                                }
                            }
                        }
                    }
                )
            }
        ) { paddingValues ->
            when (currentScreen) {
                DrawerScreen.HOME -> HomeScreen(
                    paddingValues = paddingValues,
                    ttsManager = ttsManager,
                    text = text,
                    onTextChange = { text = it }
                )
                DrawerScreen.LANGUAGE_SETTINGS -> LanguageSettingsScreen(
                    paddingValues = paddingValues,
                    ttsManager = ttsManager,
                    onBackClick = { currentScreen = DrawerScreen.HOME }
                )
                DrawerScreen.VOICE_CATEGORY -> VoiceCategoryScreen(
                    paddingValues = paddingValues,
                    ttsManager = ttsManager,
                    onBackClick = { currentScreen = DrawerScreen.HOME }
                )
                DrawerScreen.ABOUT_US -> AboutUsScreen(paddingValues = paddingValues, onBackClick = {
                    currentScreen = DrawerScreen.HOME})
                DrawerScreen.PRIVACY_POLICY -> PrivacyPolicyScreen(paddingValues = paddingValues, onBackClick = {currentScreen = DrawerScreen.HOME})
                //  DrawerScreen.PROFILE -> ProfileScreen(paddingValues = paddingValues)
                DrawerScreen.LOGOUT -> {
                    LaunchedEffect(Unit) {
                       // Toast.makeText(context, "Logged out successfully", Toast.LENGTH_SHORT).show()
                        currentScreen = DrawerScreen.HOME
                    }
                }
            }
        }
    }
}

enum class DrawerScreen(val title: String, val icon: ImageVector) {
    HOME("Home", Icons.Default.Home),
    LANGUAGE_SETTINGS("Language Settings", Icons.Default.DateRange),
    VOICE_CATEGORY("Voice Category", Icons.Default.Settings),
    ABOUT_US("About Us", Icons.Default.Info),
    PRIVACY_POLICY("Privacy Policy", Icons.Default.Lock),
    //  PROFILE("Profile", Icons.Default.Person),
    LOGOUT("Home", Icons.Default.Home)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerContent(
    currentScreen: DrawerScreen,
    onScreenSelected: (DrawerScreen) -> Unit,
    onClose: () -> Unit
) {
    ModalDrawerSheet(
        modifier = Modifier.width(280.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp)
        ) {

            Spacer(modifier = Modifier.height(16.dp))
            Divider()

            // Menu Items
            DrawerScreen.values().forEach { screen ->
                if (screen != DrawerScreen.HOME) {
                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                imageVector = screen.icon,
                                contentDescription = screen.title
                            )
                        },
                        label = { Text(screen.title) },
                        selected = currentScreen == screen,
                        onClick = {
                            if (screen == DrawerScreen.LOGOUT) {
                                onScreenSelected(screen)
                            } else {
                                onScreenSelected(screen)
                            }
                        },
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun HomeScreen(
    paddingValues: PaddingValues,
    ttsManager: AdvancedTTSManager,
    text: String,
    onTextChange: (String) -> Unit
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

        val wordCount = text
            .trim()
            .split("\\s+".toRegex())
            .filter { it.isNotEmpty() }
            .size
        BannerAdView()
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
                        onTextChange(newText)
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
                text = { Text("You have exceeded 2000 words. Please take a subscription to continue.") },
                confirmButton = {
                    TextButton(onClick = {
                        showDialog = false
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageSettingsScreen(
    paddingValues: PaddingValues,
    ttsManager: AdvancedTTSManager,
    onBackClick: () -> Unit
) {
    var selectedLanguage by remember { mutableStateOf(languageOptions.first()) }
    var languageExpanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {

        BackTopBar(
            title = "Language Settings",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
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
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoiceCategoryScreen(
    paddingValues: PaddingValues,
    ttsManager: AdvancedTTSManager,
    onBackClick: () -> Unit
) {
    var selectedCategory by remember { mutableStateOf(VoiceCategory.NATURAL) }
    var categoryExpanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {

        BackTopBar(
            title = "Voice Category",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
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
        }
    }
}


@Composable
fun AboutUsScreen(
    paddingValues: PaddingValues,
    onBackClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {

        BackTopBar(
            title = "About Us",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Text to Speech by Vish Labs",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Version 1.0.0")
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "We are dedicated to providing the best text-to-speech experience with high-quality voices and advanced features."
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Contact Us",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Email: support@vishlabs.com")
                    Text("Website: www.vishlabs.com")
                }
            }
        }
    }
}


@Composable
fun PrivacyPolicyScreen(
    paddingValues: PaddingValues,
    onBackClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {

        BackTopBar(
            title = "Privacy Policy",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Data Collection",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "We collect minimal data necessary to provide our services. Your text inputs are processed locally."
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Data Usage",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "We do not sell or share your personal information with third parties."
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Your Rights",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "You have the right to access, modify, or delete your data at any time."
                    )
                }
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
@Composable
fun BannerAdView() {
    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp), // IMPORTANT
        factory = { context ->
            AdView(context).apply {
                setAdSize(AdSize.BANNER)
                adUnitId = "ca-app-pub-3940256099942544/6300978111"
                loadAd(AdRequest.Builder().build())
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackTopBar(
    title: String,
    onBackClick: () -> Unit
) {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
        }
    )
}

