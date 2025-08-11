package com.softklass.annette.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star // Added for Budget
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
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.softklass.annette.feature.budget.navigation.budgetScreen
import com.softklass.annette.feature.budget.ui.BudgetScreen
import com.softklass.annette.ui.screens.AssetsScreen
import com.softklass.annette.ui.screens.ItemDetailScreen
import com.softklass.annette.ui.screens.LiabilitiesScreen
import com.softklass.annette.ui.screens.NetWorthScreen
import com.softklass.annette.ui.screens.SettingsScreen
import com.softklass.annette.ui.screens.viewmodels.AssetsViewModel
import com.softklass.annette.ui.screens.viewmodels.ItemDetailViewModel
import com.softklass.annette.ui.screens.viewmodels.LiabilitiesViewModel
import com.softklass.annette.ui.screens.viewmodels.NetWorthViewModel
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
sealed class Screen(val route: String, val title: String, @Contextual val icon: ImageVector) {
    @Serializable
    object NetWorth : Screen("net_worth", "Net Worth", Icons.Default.Home)
    @Serializable
    object Liabilities : Screen("liabilities", "Liabilities", Icons.Rounded.AddCard)
    @Serializable
    object Assets : Screen("assets", "Assets", Icons.Rounded.Savings)
    @Serializable
    object Budget : Screen("budget", "Budget", Icons.Filled.Star) // New Budget screen
    @Serializable
    object Settings : Screen("settings", "Settings", Icons.Default.Settings)
    @Serializable
    object ItemDetail : Screen("item_detail/{itemId}/{itemName}/{itemCategory}/{itemType}", "Item Detail", Icons.Default.Home) // This Icon might be a placeholder
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnnetteApp() {
    val navController = rememberNavController()
    val screens = listOf(
        Screen.NetWorth,
        Screen.Liabilities,
        Screen.Assets,
        Screen.Budget, // Added Budget to bottom nav items
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
                selected = currentDestination?.hierarchy?.any { it == screen } == true,
                onClick = {
                    navController.navigate(screen) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
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
        startDestination = Screen.NetWorth,
        modifier = modifier
    ) {
        budgetScreen(navController = navController)
        composable<Screen.NetWorth> {
            NetWorthScreen(viewModel = hiltViewModel<NetWorthViewModel>())
        }
        composable<Screen.Liabilities> {
            LiabilitiesScreen(
                viewModel = hiltViewModel<LiabilitiesViewModel>(),
                onNavigateToDetail = { itemId, itemName, itemCategory, itemType ->
                    navController.navigate("item_detail/$itemId/$itemName/$itemCategory/$itemType")
                }
            )
        }
        composable<Screen.Assets> {
            AssetsScreen(
                viewModel = hiltViewModel<AssetsViewModel>(),
                onNavigateToDetail = { itemId, itemName, itemCategory, itemType ->
                    navController.navigate("item_detail/$itemId/$itemName/$itemCategory/$itemType")
                }
            )
        }

        composable<Screen.Budget> { // Added route for BudgetScreen
            BudgetScreen()
        }

        composable<Screen.Settings> {
            SettingsScreen()
        }
        composable(
            route = Screen.ItemDetail.route,
            arguments = listOf(
                navArgument("itemId") { type = NavType.LongType },
                navArgument("itemName") { type = NavType.StringType },
                navArgument("itemCategory") { type = NavType.StringType },
                navArgument("itemType") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getLong("itemId") ?: 0L
            val itemName = backStackEntry.arguments?.getString("itemName") ?: ""
            val itemCategory = backStackEntry.arguments?.getString("itemCategory") ?: ""
            val itemType = backStackEntry.arguments?.getString("itemType") ?: ""
            
            ItemDetailScreen(
                itemId = itemId,
                itemName = itemName,
                itemCategory = itemCategory,
                itemType = itemType,
                onNavigateBack = { navController.popBackStack() },
                viewModel = hiltViewModel<ItemDetailViewModel>()
            )
        }
    }
}
