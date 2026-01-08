package com.rach.texttospeechbyvishlabs

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.rach.texttospeechbyvishlabs.presentation.component.BottomNavBar
import com.rach.texttospeechbyvishlabs.data.repository.TtsRepositoryImpl
import com.rach.texttospeechbyvishlabs.data.tts.AdvancedTTSManager
import com.rach.texttospeechbyvishlabs.domain.usecase.ChangeLanguageUseCase
import com.rach.texttospeechbyvishlabs.domain.usecase.ChangeVoiceCategoryUseCase
import com.rach.texttospeechbyvishlabs.domain.usecase.SaveAudioUseCase
import com.rach.texttospeechbyvishlabs.domain.usecase.SpeakTextUseCase
import com.rach.texttospeechbyvishlabs.domain.usecase.StopSpeakingUseCase
import com.rach.texttospeechbyvishlabs.presentation.viewmodel.TtsViewModel
import com.rach.texttospeechbyvishlabs.domain.model.VoiceCategory
import com.rach.texttospeechbyvishlabs.domain.model.languageOptions
import com.rach.texttospeechbyvishlabs.presentation.screen.AboutUsScreen
import com.rach.texttospeechbyvishlabs.presentation.screen.HomeScreen
import com.rach.texttospeechbyvishlabs.presentation.screen.LanguageSettingsScreen
import com.rach.texttospeechbyvishlabs.presentation.screen.PrivacyPolicyScreen
import com.rach.texttospeechbyvishlabs.presentation.screen.SettingsScreen
import com.rach.texttospeechbyvishlabs.presentation.screen.VoiceCategoryScreen
import com.rach.texttospeechbyvishlabs.ui.theme.HabitChangeTheme
import kotlinx.coroutines.launch

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
    val repo = remember { TtsRepositoryImpl(ttsManager) }

    val viewModel = remember {
        TtsViewModel(
            SpeakTextUseCase(repo),
            StopSpeakingUseCase(repo),
            ChangeLanguageUseCase(repo),
            ChangeVoiceCategoryUseCase(repo),
            SaveAudioUseCase(repo)
        )
    }

    var selectedIndex by remember { mutableStateOf(0) }
    var text by remember { mutableStateOf("") }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var currentScreen by remember { mutableStateOf(DrawerScreen.HOME) }
    var backPressedTime by remember { mutableStateOf(0L) }

    var selectedLanguageIndex by rememberSaveable { mutableStateOf(0) }
    var selectedCategoryName by rememberSaveable { mutableStateOf(VoiceCategory.NATURAL.name) }


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
                                currentScreen = DrawerScreen.SETTINGS
                            }
                        }
                    }
                )
            }
        ) { paddingValues ->
            when (currentScreen) {
                DrawerScreen.HOME -> HomeScreen(
                    paddingValues = paddingValues,
                    onPlayClick = {viewModel.speak(text)},
                    text = text,
                    onTextChange = { text = it }
                )
                DrawerScreen.LANGUAGE_SETTINGS -> LanguageSettingsScreen(
                    paddingValues = paddingValues,
                    selectedLanguageIndex = selectedLanguageIndex,
                    onLanguageSelected = { index ->
                        selectedLanguageIndex = index
                        viewModel.setLanguage(languageOptions[index].second)
                    },
                    onBackClick = { currentScreen = DrawerScreen.SETTINGS }
                )


                DrawerScreen.VOICE_CATEGORY -> VoiceCategoryScreen(
                    paddingValues = paddingValues,
                    selectedCategoryName = selectedCategoryName,
                    onCategorySelected = { name ->
                        selectedCategoryName = name
                        viewModel.setVoice(VoiceCategory.valueOf(name))
                    },
                    onBackClick = { currentScreen = DrawerScreen.SETTINGS }
                )

                DrawerScreen.ABOUT_US -> AboutUsScreen(paddingValues = paddingValues, onBackClick = {
                    currentScreen = DrawerScreen.SETTINGS})
                DrawerScreen.PRIVACY_POLICY -> PrivacyPolicyScreen(paddingValues = paddingValues, onBackClick = {currentScreen = DrawerScreen.SETTINGS})
                //  DrawerScreen.PROFILE -> ProfileScreen(paddingValues = paddingValues)
                DrawerScreen.LOGOUT -> {
                    LaunchedEffect(Unit) {
                       // Toast.makeText(context, "Logged out successfully", Toast.LENGTH_SHORT).show()
                        currentScreen = DrawerScreen.HOME
                    }
                }

                DrawerScreen.SETTINGS -> SettingsScreen(
                    paddingValues = paddingValues,
                    onBackClick = { currentScreen = DrawerScreen.HOME },
                    onNavigateToLanguage = { currentScreen = DrawerScreen.LANGUAGE_SETTINGS },
                    onNavigateToVoice = { currentScreen = DrawerScreen.VOICE_CATEGORY },
                    onNavigateToAbout = { currentScreen = DrawerScreen.ABOUT_US },
                    onNavigateToPrivacy = { currentScreen = DrawerScreen.PRIVACY_POLICY }
                )

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

    SETTINGS("Settings", Icons.Default.Settings),
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
fun BannerAdView() {
    AndroidView(
        modifier = Modifier
            .fillMaxWidth(),
        factory = { context ->
            AdView(context).apply {
                setAdSize(AdSize.LARGE_BANNER)
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


