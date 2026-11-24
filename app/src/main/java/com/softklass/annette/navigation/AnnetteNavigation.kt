package com.softklass.annette.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.BarChart
import androidx.compose.material.icons.rounded.Savings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.softklass.annette.feature.budget.data.model.BudgetItemType
import com.softklass.annette.feature.budget.ui.components.AddBudgetItemBottomSheet
import com.softklass.annette.feature.budget.ui.screens.BudgetViewModel
import com.softklass.annette.ui.screens.AssetsScreen
import com.softklass.annette.ui.screens.ItemDetailScreen
import com.softklass.annette.ui.screens.LiabilitiesScreen
import com.softklass.annette.ui.screens.NetWorthScreen
import com.softklass.annette.ui.screens.PlanScreen
import com.softklass.annette.ui.screens.ReportScreen
import com.softklass.annette.ui.screens.SettingsScreen
import com.softklass.annette.ui.screens.settings.PreferencesScreen
import com.softklass.annette.ui.screens.settings.PreferencesViewModel
import com.softklass.annette.ui.screens.viewmodels.AssetsViewModel
import com.softklass.annette.ui.screens.viewmodels.ItemDetailViewModel
import com.softklass.annette.ui.screens.viewmodels.LiabilitiesViewModel
import com.softklass.annette.ui.screens.viewmodels.NetWorthViewModel
import com.softklass.theme.ui.theme.BrandTheme
import kotlinx.coroutines.launch
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
sealed class Screen(val route: String, val title: String, @Contextual val icon: ImageVector) {
    @Serializable
    object Home : Screen("home", "Home", Icons.Filled.Home)

    @Serializable
    object Report : Screen("report", "Report", Icons.Rounded.BarChart)

    @Serializable
    object Plan : Screen("plan", "Plan", Icons.Rounded.Savings)

    @Serializable
    object Settings : Screen("settings", "Settings", Icons.Filled.Settings)

    @Serializable
    object Assets : Screen("assets", "Assets", Icons.Rounded.Savings)

    @Serializable
    object Liabilities : Screen("liabilities", "Liabilities", Icons.Rounded.Savings)

    @Serializable
    object ItemDetail : Screen(
        "item_detail/{itemId}/{itemName}/{itemCategory}/{itemType}",
        "Item Detail",
        Icons.Filled.Home,
    )

    @Serializable
    object Preference : Screen("preference", "Preference", Icons.Filled.Home)
}

private val tabScreens = listOf(Screen.Home, Screen.Report, Screen.Plan, Screen.Settings)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnnetteApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentTab = tabScreens.firstOrNull { tab ->
        currentDestination?.hierarchy?.any { it.route == tab::class.qualifiedName } == true
    }

    var showAddSheet by remember { mutableStateOf(false) }
    var addType by remember { mutableStateOf(BudgetItemType.EXPENSE) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val budgetViewModel: BudgetViewModel = hiltViewModel()

    fun navigateToTab(screen: Screen) {
        navController.navigate(screen) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0),
        bottomBar = {
            if (currentTab != null) {
                AnnetteBottomBar(
                    tabs = tabScreens,
                    currentTab = currentTab,
                    onTabSelected = { navigateToTab(it) },
                    onAddClick = {
                        addType = BudgetItemType.EXPENSE
                        showAddSheet = true
                        scope.launch { sheetState.show() }
                    },
                )
            }
        },
    ) { _ ->
        AnnetteNavHost(
            navController = navController,
            onOpenTab = { navigateToTab(it) },
        )

        if (showAddSheet) {
            AddBudgetItemBottomSheet(
                sheetState = sheetState,
                onDismissRequest = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion { showAddSheet = false }
                },
                initialType = addType,
                onAddBudgetItem = {
                    budgetViewModel.addBudgetItem(it)
                    scope.launch { sheetState.hide() }.invokeOnCompletion { showAddSheet = false }
                },
            )
        }
    }
}

@Composable
fun AnnetteBottomBar(
    tabs: List<Screen>,
    currentTab: Screen,
    onTabSelected: (Screen) -> Unit,
    onAddClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(start = 18.dp, end = 18.dp, bottom = 10.dp)
            .height(96.dp),
    ) {
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(70.dp),
            shape = RoundedCornerShape(28.dp),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 16.dp,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                tabs.forEach { tab ->
                    BottomBarItem(
                        tab = tab,
                        selected = tab == currentTab,
                        onClick = { onTabSelected(tab) },
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }

        Surface(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .size(58.dp),
            shape = CircleShape,
            color = BrandTheme.colors.berry,
            shadowElevation = 12.dp,
            onClick = onAddClick,
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "Add transaction",
                    tint = androidx.compose.ui.graphics.Color.White,
                    modifier = Modifier.size(26.dp),
                )
            }
        }
    }
}

@Composable
private fun BottomBarItem(
    tab: Screen,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val contentColor = if (selected) {
        MaterialTheme.colorScheme.onSurface
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }
    Column(
        modifier = modifier
            .fillMaxHeight()
            .clickable(onClick = onClick),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = tab.icon,
            contentDescription = tab.title,
            tint = contentColor,
            modifier = Modifier.size(22.dp),
        )
        Spacer(Modifier.height(3.dp))
        Text(
            text = tab.title,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            color = contentColor,
        )
    }
}

@Composable
fun AnnetteNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onOpenTab: (Screen) -> Unit = {},
) {

    NavHost(
        navController = navController,
        startDestination = Screen.Home,
        modifier = modifier,
    ) {
        composable<Screen.Home> {
            NetWorthScreen(
                viewModel = hiltViewModel<NetWorthViewModel>(),
                onNavigateToAssets = { navController.navigate(Screen.Assets) },
                onNavigateToLiabilities = { navController.navigate(Screen.Liabilities) },
                onNavigateToReport = { onOpenTab(Screen.Report) },
                onNavigateToSettings = { onOpenTab(Screen.Settings) },
            )
        }

        composable<Screen.Report> {
            ReportScreen(viewModel = hiltViewModel<BudgetViewModel>())
        }

        composable<Screen.Plan> {
            PlanScreen(
                viewModel = hiltViewModel<BudgetViewModel>(),
                onNavigateToSettings = { onOpenTab(Screen.Settings) },
                onNavigateToReport = { onOpenTab(Screen.Report) },
            )
        }

        composable<Screen.Settings> {
            SettingsScreen(
                onSettingsItemClick = { navController.navigate(Screen.Preference) },
            )
        }

        composable<Screen.Assets> {
            AssetsScreen(
                viewModel = hiltViewModel<AssetsViewModel>(),
                onNavigateBack = { navController.popBackStack() },
                onNavigateToDetail = { itemId, itemName, itemCategory, itemType ->
                    navController.navigate("item_detail/$itemId/$itemName/$itemCategory/$itemType")
                },
            )
        }

        composable<Screen.Liabilities> {
            LiabilitiesScreen(
                viewModel = hiltViewModel<LiabilitiesViewModel>(),
                onNavigateBack = { navController.popBackStack() },
                onNavigateToDetail = { itemId, itemName, itemCategory, itemType ->
                    navController.navigate("item_detail/$itemId/$itemName/$itemCategory/$itemType")
                },
            )
        }

        composable(
            route = Screen.ItemDetail.route,
            arguments = listOf(
                navArgument("itemId") { type = NavType.LongType },
                navArgument("itemName") { type = NavType.StringType },
                navArgument("itemCategory") { type = NavType.StringType },
                navArgument("itemType") { type = NavType.StringType },
            ),
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
                viewModel = hiltViewModel<ItemDetailViewModel>(),
            )
        }

        composable<Screen.Preference> {
            PreferencesScreen(
                viewModel = hiltViewModel<PreferencesViewModel>(),
                onNavigateBack = { navController.popBackStack() },
            )
        }
    }
}
