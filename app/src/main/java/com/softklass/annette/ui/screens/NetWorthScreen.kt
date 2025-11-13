package com.softklass.annette.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountBalance
import androidx.compose.material.icons.rounded.AccountBalanceWallet
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softklass.annette.currencyFormatter
import com.softklass.annette.ui.components.CallToActionCard
import com.softklass.annette.ui.components.ValueCard
import com.softklass.annette.ui.screens.viewmodels.NetWorthViewModel
import com.softklass.annette.ui.theme.ExtendedTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NetWorthScreen(
    modifier: Modifier = Modifier,
    viewModel: NetWorthViewModel
) {
    val netWorth by viewModel.netWorth.collectAsState()
    val incomeTotal by viewModel.incomeTotal.collectAsState()
    val expenseTotal by viewModel.expenseTotal.collectAsState()

    Scaffold { innerPadding ->
        innerPadding
        NetWorthScreenContent(
            netWorth = netWorth,
            incomeTotal = incomeTotal,
            expenseTotal = expenseTotal,
            modifier = modifier
        )
    }

}

@Composable
fun NetWorthScreenContent(
    netWorth: Double,
    incomeTotal: Double,
    expenseTotal: Double,
    modifier: Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        NetWorthCardTitle(netWorth = netWorth)

        Spacer(modifier = Modifier.height(4.dp))

        CallToActionCard(
            title = "Budget for this month",
            subtitle = "Cash Available",
            containerColor = ExtendedTheme.colors.cta.colorContainer,
            contentColor = ExtendedTheme.colors.cta.onColor
        )

        Spacer(modifier = Modifier.height(20.dp))

        CallToActionCard(
            title = "Create a Saving goal",
            subtitle = "This is a placeholder for your saving goals. You can create a saving goal here.",
            showText = false,
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        )

        Spacer(modifier = Modifier.height(16.dp))


        Text(
            text = "Cash",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .padding(start = 16.dp)
                .fillMaxWidth()
                .align(Alignment.Start),
            textAlign = TextAlign.Start
        )

        Spacer(modifier = Modifier.height(8.dp))

        DisplayIncomeExpenseCards(income = incomeTotal, expense = expenseTotal)
    }
}

@Composable
fun NetWorthCardTitle(
    netWorth: Double
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = ExtendedTheme.colors.blackboard.colorContainer
        ),
        shape = RoundedCornerShape(24.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Net worth balance",
                fontSize = 15.sp,
                fontWeight = FontWeight.Thin,
                color = ExtendedTheme.colors.blackboard.onColorContainer.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = currencyFormatter.format(netWorth),
                fontSize = 34.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Assets - Liabilities",
                fontSize = 14.sp,
                color = Color.White,
            )
        }
    }
}

@Composable
fun DisplayIncomeExpenseCards(
    income: Double,
    expense: Double
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ValueCard(
            totalAssets = income,
            title = "Income",
            textColor = ExtendedTheme.colors.blackboard.color,
            modifier = Modifier.weight(1f),
            icon = Icons.Rounded.AccountBalance,
            iconBackgroundColor = ExtendedTheme.colors.asset.colorContainer,
            onCardClick = { /* Handle income card click */ }
        )
        ValueCard(
            totalAssets = expense,
            title = "Expense",
            textColor = ExtendedTheme.colors.blackboard.color,
            modifier = Modifier.weight(1f),
            icon = Icons.Rounded.AccountBalanceWallet,
            iconBackgroundColor = ExtendedTheme.colors.liability.colorContainer,
            onCardClick = { /* Handle expense card click */ }
        )
    }
}
