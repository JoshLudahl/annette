package com.softklass.annette.ui.screens

import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.TrendingDown
import androidx.compose.material.icons.automirrored.rounded.TrendingUp
import androidx.compose.material.icons.rounded.AccountBalance
import androidx.compose.material.icons.rounded.AccountBalanceWallet
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.NotificationsNone
import androidx.compose.material.icons.rounded.Payments
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.ShoppingBag
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softklass.annette.core.ui.currency.currencyFormatter
import com.softklass.annette.core.ui.currency.shortCurrencyFormatter
import com.softklass.annette.core.ui.design.EmptyState
import com.softklass.annette.core.ui.design.GradientHero
import com.softklass.annette.core.ui.design.GroupedSurface
import com.softklass.annette.core.ui.design.HeroPill
import com.softklass.annette.core.ui.design.InsightBanner
import com.softklass.annette.core.ui.design.MoneyStatCard
import com.softklass.annette.core.ui.design.PastelIconTile
import com.softklass.annette.core.ui.design.SectionHeader
import com.softklass.annette.core.ui.design.SoftPill
import com.softklass.annette.core.ui.design.StatusBarIcons
import com.softklass.annette.core.ui.design.TransactionRow
import com.softklass.annette.core.ui.design.categoryColors
import com.softklass.annette.core.ui.design.categoryIcon
import com.softklass.annette.feature.budget.data.model.BudgetItemType
import com.softklass.annette.ui.screens.viewmodels.NetWorthViewModel
import com.softklass.annette.ui.screens.viewmodels.TransactionPreview
import com.softklass.theme.ui.theme.AnnetteTheme
import com.softklass.theme.ui.theme.BrandTheme
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.abs

private enum class TransactionFilter { ALL, INCOME, EXPENSES }

@Composable
fun NetWorthScreen(
    modifier: Modifier = Modifier,
    viewModel: NetWorthViewModel,
    onNavigateToAssets: () -> Unit = {},
    onNavigateToLiabilities: () -> Unit = {},
    onNavigateToReport: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
) {
    val totalAssets by viewModel.totalAssets.collectAsState()
    val totalLiabilities by viewModel.totalLiabilities.collectAsState()
    val netWorth by viewModel.netWorth.collectAsState()
    val incomeTotal by viewModel.incomeTotal.collectAsState()
    val expenseTotal by viewModel.expenseTotal.collectAsState()
    val transactions by viewModel.recentTransactions.collectAsState()
    val netWorthDelta by viewModel.netWorthDelta.collectAsState()

    HomeContent(
        netWorth = netWorth,
        assetsTotal = totalAssets,
        liabilitiesTotal = totalLiabilities,
        incomeTotal = incomeTotal,
        expenseTotal = expenseTotal,
        transactions = transactions,
        netWorthDelta = netWorthDelta,
        modifier = modifier,
        onNavigateToAssets = onNavigateToAssets,
        onNavigateToLiabilities = onNavigateToLiabilities,
        onNavigateToReport = onNavigateToReport,
        onNavigateToSettings = onNavigateToSettings,
    )
}

@Composable
fun HomeContent(
    netWorth: Double,
    assetsTotal: Double,
    liabilitiesTotal: Double,
    incomeTotal: Double,
    expenseTotal: Double,
    transactions: List<TransactionPreview>,
    netWorthDelta: Double?,
    modifier: Modifier = Modifier,
    onNavigateToAssets: () -> Unit = {},
    onNavigateToLiabilities: () -> Unit = {},
    onNavigateToReport: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
) {
    val context = LocalContext.current
    var filter by remember { mutableStateOf(TransactionFilter.ALL) }
    val scrollState = rememberScrollState()
    val density = LocalDensity.current
    val statusBarHeightPx = WindowInsets.statusBars.getTop(density)
    var heroHeightPx by remember { mutableIntStateOf(0) }
    // Keep the icons white while the berry hero sits under the status bar, then flip to dark
    // once the light sheet scrolls beneath it.
    val darkStatusIcons = heroHeightPx > 0 && scrollState.value > heroHeightPx - statusBarHeightPx
    StatusBarIcons(darkIcons = darkStatusIcons)
    val statusScrimAlpha by animateFloatAsState(
        targetValue = if (darkStatusIcons) 1f else 0f,
        label = "statusScrim",
    )

    val visibleTransactions = remember(transactions, filter) {
        when (filter) {
            TransactionFilter.ALL -> transactions
            TransactionFilter.INCOME -> transactions.filter { it.type == BudgetItemType.INCOME }
            TransactionFilter.EXPENSES -> transactions.filter { it.type == BudgetItemType.EXPENSE }
        }
    }
    val visibleTotal = visibleTransactions.sumOf { it.amount }

    val monthLabel = remember {
        LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.US))
    }
    val todayLabel = remember {
        LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, d MMMM, yyyy", Locale.US))
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
        ) {
            GradientHero(modifier = Modifier.onSizeChanged { heroHeightPx = it.height }) {
                Column(
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(top = 10.dp, bottom = 46.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        AvatarBadge(onClick = onNavigateToSettings)
                        Spacer(Modifier.weight(1f))
                        SoftPill(
                            text = monthLabel,
                            containerColor = BrandTheme.colors.heroGlow,
                            contentColor = BrandTheme.colors.onHero,
                            trailingIcon = Icons.Rounded.KeyboardArrowDown,
                        )
                        Spacer(Modifier.weight(1f))
                        NotificationBell(
                            onClick = {
                                Toast.makeText(context, "You're all caught up", Toast.LENGTH_SHORT).show()
                            },
                        )
                    }

                    Spacer(Modifier.height(28.dp))

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = "Current Balance",
                            style = MaterialTheme.typography.bodySmall,
                            color = BrandTheme.colors.heroMuted,
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = currencyFormatter.format(netWorth),
                            style = MaterialTheme.typography.displaySmall.copy(fontSize = 40.sp),
                            color = BrandTheme.colors.onHero,
                            fontWeight = FontWeight.ExtraBold,
                            textAlign = TextAlign.Center,
                        )
                        Spacer(Modifier.height(12.dp))
                        val delta = netWorthDelta
                        HeroPill(
                            text = when {
                                delta == null -> "Assets minus liabilities"
                                delta >= 0 -> "+${shortCurrencyFormatter.format(abs(delta))} since last update"
                                else -> "-${shortCurrencyFormatter.format(abs(delta))} since last update"
                            },
                            leadingIcon = when {
                                delta == null -> null
                                delta >= 0 -> Icons.AutoMirrored.Rounded.TrendingUp
                                else -> Icons.AutoMirrored.Rounded.TrendingDown
                            },
                        )
                    }
                }
            }

            // The white sheet slides up over the berry hero, matching the reference composition.
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-24).dp),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                color = MaterialTheme.colorScheme.surface,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 22.dp),
                ) {
                    SectionHeader(
                        title = "Your Money",
                        actionLabel = "Details",
                        onAction = onNavigateToReport,
                        titleTrailing = {
                            Icon(
                                imageVector = Icons.Rounded.Info,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                modifier = Modifier
                                    .padding(start = 5.dp)
                                    .size(14.dp),
                            )
                        },
                    )
                    Spacer(Modifier.height(14.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        MoneyStatCard(
                            label = "Income",
                            value = currencyFormatter.format(incomeTotal),
                            icon = Icons.Rounded.Payments,
                            tint = BrandTheme.colors.positive,
                            container = BrandTheme.colors.positiveSoft,
                            info = true,
                            modifier = Modifier.weight(1f),
                            onClick = onNavigateToReport,
                        )
                        MoneyStatCard(
                            label = "Expenses",
                            value = currencyFormatter.format(expenseTotal),
                            icon = Icons.Rounded.ShoppingBag,
                            tint = BrandTheme.colors.negative,
                            container = BrandTheme.colors.negativeSoft,
                            info = true,
                            modifier = Modifier.weight(1f),
                            onClick = onNavigateToReport,
                        )
                    }
                    Spacer(Modifier.height(14.dp))
                    val surplus = incomeTotal - expenseTotal
                    InsightBanner(
                        title = when {
                            incomeTotal <= 0.0 && expenseTotal <= 0.0 ->
                                "Add income and expenses to unlock insights"
                            surplus < 0 -> "You're ${currencyFormatter.format(abs(surplus))} over your income"
                            else -> "You have ${currencyFormatter.format(surplus)} left to spend"
                        },
                        subtitle = when {
                            incomeTotal <= 0.0 && expenseTotal <= 0.0 -> null
                            surplus < 0 -> "Review this month's spending to get back on track"
                            else -> "Based on this month's recorded income and expenses"
                        },
                        actionLabel = "View",
                        onAction = onNavigateToReport,
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-10).dp)
                    .padding(horizontal = 20.dp),
            ) {
                SectionHeader(title = "Transactions")
                Spacer(Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterPill(
                        label = "All",
                        selected = filter == TransactionFilter.ALL,
                        onClick = { filter = TransactionFilter.ALL },
                    )
                    FilterPill(
                        label = "Income",
                        selected = filter == TransactionFilter.INCOME,
                        onClick = { filter = TransactionFilter.INCOME },
                    )
                    FilterPill(
                        label = "Expenses",
                        selected = filter == TransactionFilter.EXPENSES,
                        onClick = { filter = TransactionFilter.EXPENSES },
                    )
                }

                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = todayLabel,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = "Total ",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        text = currencyFormatter.format(visibleTotal),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
                Spacer(Modifier.height(10.dp))

                GroupedSurface {
                    if (visibleTransactions.isEmpty()) {
                        EmptyState(
                            icon = Icons.Rounded.Payments,
                            title = "No transactions yet",
                            message = "Use the + button to record income or an expense.",
                        )
                    } else {
                        visibleTransactions.forEachIndexed { index, transaction ->
                            val (tint, container) = categoryColors(transaction.category.ifBlank { transaction.name })
                            val isIncome = transaction.type == BudgetItemType.INCOME
                            TransactionRow(
                                title = transaction.name.ifBlank { "Untitled" },
                                subtitle = buildString {
                                    if (transaction.category.isNotBlank()) append(transaction.category)
                                    if (transaction.category.isNotBlank()) append(" · ")
                                    append(
                                        Instant.ofEpochMilli(transaction.date)
                                            .atZone(ZoneId.systemDefault())
                                            .toLocalDate()
                                            .format(DateTimeFormatter.ofPattern("MMM d", Locale.US)),
                                    )
                                },
                                amount = (if (isIncome) "+" else "-") + currencyFormatter.format(transaction.amount),
                                icon = categoryIcon(transaction.category.ifBlank { transaction.name }),
                                tint = tint,
                                container = container,
                                amountColor = if (isIncome) BrandTheme.colors.positive else BrandTheme.colors.negative,
                                trailingNote = if (isIncome) "Income" else "Expense",
                            )
                            if (index != visibleTransactions.lastIndex) {
                                HorizontalDivider(
                                    modifier = Modifier.padding(horizontal = 14.dp),
                                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(22.dp))

                SectionHeader(title = "Accounts")
                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    AccountCard(
                        label = "Assets",
                        amount = assetsTotal,
                        icon = Icons.Rounded.AccountBalance,
                        tint = BrandTheme.colors.positive,
                        container = BrandTheme.colors.positiveSoft,
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToAssets,
                    )
                    AccountCard(
                        label = "Liabilities",
                        amount = liabilitiesTotal,
                        icon = Icons.Rounded.AccountBalanceWallet,
                        tint = BrandTheme.colors.negative,
                        container = BrandTheme.colors.negativeSoft,
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToLiabilities,
                    )
                }

                Spacer(Modifier.height(120.dp))
            }
        }

    // Once the hero scrolls away, fade a subtle background scrim under the status bar so list
    // content is not sliced by the system icons.
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(with(density) { (statusBarHeightPx + 6).toDp() })
            .alpha(statusScrimAlpha)
            .background(
                Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.background.copy(alpha = 0.92f),
                        MaterialTheme.colorScheme.background.copy(alpha = 0f),
                    ),
                ),
            ),
    )
    }
}

@Composable
private fun AvatarBadge(onClick: () -> Unit) {
    Box(modifier = Modifier.clickable(onClick = onClick)) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.24f)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Rounded.Person,
                contentDescription = "Profile",
                tint = Color.White,
                modifier = Modifier.size(22.dp),
            )
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(16.dp)
                .clip(CircleShape)
                .background(Color.White),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Rounded.Check,
                contentDescription = null,
                tint = BrandTheme.colors.heroEnd,
                modifier = Modifier.size(10.dp),
            )
        }
    }
}

@Composable
private fun NotificationBell(onClick: () -> Unit) {
    Box(modifier = Modifier.clickable(onClick = onClick)) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.20f)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Rounded.NotificationsNone,
                contentDescription = "Notifications",
                tint = Color.White,
                modifier = Modifier.size(21.dp),
            )
        }
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 4.dp, end = 4.dp)
                .size(9.dp)
                .clip(CircleShape)
                .background(BrandTheme.colors.chart.coral),
        )
    }
}

@Composable
private fun FilterPill(label: String, selected: Boolean, onClick: () -> Unit) {
    SoftPill(
        text = label,
        containerColor = if (selected) {
            BrandTheme.colors.berry
        } else {
            MaterialTheme.colorScheme.surface
        },
        contentColor = if (selected) {
            Color.White
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        },
        onClick = onClick,
    )
}

@Composable
private fun AccountCard(
    label: String,
    amount: Double,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: Color,
    container: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 1.dp,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
        ),
        onClick = onClick,
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                PastelIconTile(
                    icon = icon,
                    tint = tint,
                    container = container,
                    size = 36.dp,
                    iconSize = 18.dp,
                    shape = RoundedCornerShape(11.dp),
                )
                Spacer(Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Rounded.ChevronRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp),
                )
            }
            Spacer(Modifier.height(12.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = currencyFormatter.format(amount),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
            )
        }
    }
}

@Preview(showBackground = true, heightDp = 1200)
@Composable
private fun HomeContentPreview() {
    AnnetteTheme {
        HomeContent(
            netWorth = 231_024.00,
            assetsTotal = 412_000.00,
            liabilitiesTotal = 180_976.00,
            incomeTotal = 4_875.12,
            expenseTotal = 8_145.78,
            transactions = listOf(
                TransactionPreview("Cash, EUR", "Cash", BudgetItemType.INCOME, 354.25, System.currentTimeMillis()),
                TransactionPreview("Cafes", "Food", BudgetItemType.EXPENSE, 12.49, System.currentTimeMillis() - 86_400_000),
                TransactionPreview("Rent", "Housing", BudgetItemType.EXPENSE, 1_850.00, System.currentTimeMillis() - 172_800_000),
            ),
            netWorthDelta = 784.0,
        )
    }
}
