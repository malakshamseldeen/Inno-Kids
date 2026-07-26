package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.sp
import com.example.ui.screens.AIPlaygroundScreen
import com.example.ui.screens.ChallengesScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.InnoChatScreen
import com.example.ui.screens.LearnScreen
import com.example.ui.screens.ParentDashboardScreen
import com.example.ui.screens.ProfileRewardsScreen
import com.example.ui.theme.CyanElectric
import com.example.ui.theme.InnoKidsTheme
import com.example.ui.theme.SpaceBackground
import com.example.ui.theme.SpaceCardBg
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.InnoKidsViewModel

data class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
)

class MainActivity : ComponentActivity() {

    private val viewModel: InnoKidsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            InnoKidsTheme {
                InnoKidsApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun InnoKidsApp(viewModel: InnoKidsViewModel) {
    var currentRoute by remember { mutableStateOf("home") }

    val navItems = listOf(
        BottomNavItem("home", "Home", Icons.Default.Home),
        BottomNavItem("learn", "Learn", Icons.Default.Psychology),
        BottomNavItem("inno_chat", "Ask Inno", Icons.Default.SmartToy),
        BottomNavItem("challenges", "Play", Icons.Default.Extension),
        BottomNavItem("playground", "Studio", Icons.Default.Brush),
        BottomNavItem("profile", "Rewards", Icons.Default.Face),
        BottomNavItem("parent", "Parent", Icons.Default.Lock)
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = SpaceBackground,
        bottomBar = {
            NavigationBar(
                containerColor = SpaceCardBg,
                contentColor = CyanElectric
            ) {
                navItems.forEach { item ->
                    val isSelected = currentRoute == item.route
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { currentRoute = item.route },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title,
                                tint = if (isSelected) CyanElectric else TextSecondary
                            )
                        },
                        label = {
                            Text(
                                text = item.title,
                                color = if (isSelected) CyanElectric else TextSecondary,
                                fontSize = 10.sp
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Color(0xFF1E293B)
                        ),
                        modifier = Modifier.testTag("nav_item_${item.route}")
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentRoute) {
                "home" -> HomeScreen(
                    viewModel = viewModel,
                    onNavigateToTab = { target -> currentRoute = target },
                    onOpenLesson = { lesson ->
                        viewModel.selectLesson(lesson)
                        currentRoute = "learn"
                    }
                )
                "learn" -> LearnScreen(
                    viewModel = viewModel,
                    onLessonOpened = { lesson -> viewModel.selectLesson(lesson) }
                )
                "inno_chat" -> InnoChatScreen(viewModel = viewModel)
                "challenges" -> ChallengesScreen(viewModel = viewModel)
                "playground" -> AIPlaygroundScreen(viewModel = viewModel)
                "profile" -> ProfileRewardsScreen(viewModel = viewModel)
                "parent" -> ParentDashboardScreen(viewModel = viewModel)
            }
        }
    }
}
