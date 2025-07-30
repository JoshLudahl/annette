package com.softklass.annette.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.softklass.annette.data.database.AnnetteDatabase
import com.softklass.annette.data.database.dao.AssetDao
import com.softklass.annette.ui.screens.NetWorthScreen
import com.softklass.annette.ui.screens.LiabilitiesScreen
import com.softklass.annette.ui.screens.AssetsScreen
import com.softklass.annette.ui.screens.SettingsScreen
import com.softklass.annette.ui.screens.viewmodels.AssetsViewModel

class AssetsViewModelFactory(private val assetDao: AssetDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AssetsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AssetsViewModel(assetDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object NetWorth : Screen("net_worth", "Net Worth", Icons.Default.Home)
    object Liabilities : Screen("liabilities", "Liabilities", Icons.Default.Star)
    object Assets : Screen("assets", "Assets", Icons.Default.Favorite)
    object Settings : Screen("settings", "Settings", Icons.Default.Settings)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnnetteApp() {
    val navController = rememberNavController()
    val screens = listOf(
        Screen.NetWorth,
        Screen.Liabilities,
        Screen.Assets,
        Screen.Settings
    )

    Scaffold(
        bottomBar = {
            AnnetteBottomNavigation(
                navController = navController,
                screens = screens
            )
        }
    ) { innerPadding ->
        AnnetteNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun AnnetteBottomNavigation(
    navController: NavHostController,
    screens: List<Screen>
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {
        screens.forEach { screen ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = screen.title
                    )
                },
                label = { Text(screen.title) },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
fun AnnetteNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.NetWorth.route,
        modifier = modifier
    ) {
        composable(Screen.NetWorth.route) {
            NetWorthScreen()
        }
        composable(Screen.Liabilities.route) {
            LiabilitiesScreen()
        }
        composable(Screen.Assets.route) {
            val context = LocalContext.current
            val database = remember {
                Room.databaseBuilder(
                    context,
                    AnnetteDatabase::class.java,
                    AnnetteDatabase.DATABASE_NAME
                ).build()
            }
            val assetDao = remember { database.assetDao() }
            val factory = remember { AssetsViewModelFactory(assetDao) }
            val assetsViewModel: AssetsViewModel = viewModel(factory = factory)

            AssetsScreen(viewModel = assetsViewModel)
        }
        composable(Screen.Settings.route) {
            SettingsScreen()
        }
    }
}
