package com.softklass.annette.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.rounded.AddCard
import androidx.compose.material.icons.rounded.Savings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.softklass.annette.ui.screens.AssetsScreen
import com.softklass.annette.ui.screens.LiabilitiesScreen
import com.softklass.annette.ui.screens.NetWorthScreen
import com.softklass.annette.ui.screens.SettingsScreen
import com.softklass.annette.ui.screens.viewmodels.AssetsViewModel
import com.softklass.annette.ui.screens.viewmodels.NetWorthViewModel

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object NetWorth : Screen("net_worth", "Net Worth", Icons.Default.Home)
    object Liabilities : Screen("liabilities", "Liabilities", Icons.Rounded.AddCard)
    object Assets : Screen("assets", "Assets", Icons.Rounded.Savings)
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
            NetWorthScreen(viewModel = hiltViewModel<NetWorthViewModel>())
        }
        composable(Screen.Liabilities.route) {
            LiabilitiesScreen()
        }
        composable(Screen.Assets.route) {
            AssetsScreen(viewModel = hiltViewModel<AssetsViewModel>())
        }

        composable(Screen.Settings.route) {
            SettingsScreen()
        }
    }
}
