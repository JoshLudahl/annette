package com.softklass.annette.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softklass.annette.R
import com.softklass.annette.core.ui.OverViewDivider
import com.softklass.annette.core.ui.composables.DisplayIncomeExpenseCards
import com.softklass.annette.core.ui.currency.currencyFormatter
import com.softklass.annette.ui.components.CallToActionCard
import com.softklass.annette.ui.screens.viewmodels.NetWorthViewModel
import com.softklass.theme.ui.theme.ExtendedTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NetWorthScreen(
    modifier: Modifier = Modifier,
    viewModel: NetWorthViewModel
) {
    val totalAssets by viewModel.totalAssets.collectAsState()
    val totalLiabilities by viewModel.totalLiabilities.collectAsState()

    val netWorth by viewModel.netWorth.collectAsState()
    val incomeTotal by viewModel.incomeTotal.collectAsState()
    val expenseTotal by viewModel.expenseTotal.collectAsState()

    Scaffold { innerPadding ->
        innerPadding
        NetWorthScreenContent(
            netWorth = netWorth,
            assetsTotal = totalAssets,
            liabilitiesTotal = totalLiabilities,
            incomeTotal = incomeTotal,
            expenseTotal = expenseTotal,
            modifier = modifier
        )
    }

}

@Composable
fun NetWorthScreenContent(
    netWorth: Double,
    assetsTotal: Double,
    liabilitiesTotal: Double,
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
        NetWorthCardTitle(
            netWorth = netWorth,
            assetsTotal = assetsTotal,
            liabilitiesTotal = liabilitiesTotal
        )

        Spacer(modifier = Modifier.height(4.dp))

        CallToActionCard(
            title = "Budget for this month",
            subtitle = "Cash Available",
            containerColor = ExtendedTheme.colors.cta.colorContainer,
            contentColor = ExtendedTheme.colors.cta.onColor,
            monthlyBudget = currencyFormatter.format(incomeTotal - expenseTotal),
        )

        Spacer(modifier = Modifier.height(20.dp))

        val context = LocalContext.current
        CallToActionCard(
            title = "Create a Saving goal",
            subtitle = "Set some savings goals for reaching your financial goals. (coming soon)",
            showText = false,
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            onClick = {
                Toast.makeText(
                    context,
                    "Coming soon",
                    Toast.LENGTH_SHORT
                ).show()
            },
            monthlyBudget = "0.00"
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
    netWorth: Double,
    assetsTotal: Double,
    liabilitiesTotal: Double
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
        Box(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 16.dp)
                    .size(120.dp),
                alpha = 0.8f,
                colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(
                    MaterialTheme.colorScheme.surface
                )
            )
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
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                OverViewDivider(
                    data = listOf("Assets", "Liabilities"),
                    values = { label ->
                        when (label) {
                            "Assets" -> assetsTotal.toFloat()
                            "Liabilities" -> liabilitiesTotal.toFloat()
                            else -> 0f
                        }
                    },
                    colors = { label ->
                        when (label) {
                            "Assets" -> ExtendedTheme.colors.asset.color // bright accent for assets
                            "Liabilities" -> ExtendedTheme.colors.liability.color
                            else -> Color.White
                        }
                    }
                )
            }
        }
    }
}