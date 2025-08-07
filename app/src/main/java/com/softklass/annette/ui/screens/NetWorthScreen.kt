package com.softklass.annette.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.softklass.annette.currencyFormatter
import com.softklass.annette.ui.screens.viewmodels.NetWorthViewModel
import com.softklass.annette.ui.theme.AnnetteTheme

@Composable
fun PieChart(
    assets: Double,
    liabilities: Double,
    modifier: Modifier = Modifier,
    size: androidx.compose.ui.unit.Dp = 200.dp,
) {
    val total = assets + liabilities
    val assetsPercentage = (assets / total).toFloat()
    val liabilitiesPercentage = (liabilities / total).toFloat()
    val assetsColor = MaterialTheme.colorScheme.primary
    val liabilitiesColor = MaterialTheme.colorScheme.error

    Canvas(
        modifier = modifier.size(size)
    ) {
        val canvasSize = this.size.minDimension
        val radius = canvasSize / 2f
        val center = Offset(this.size.width / 2f, this.size.height / 2f)


        // Draw liabilities slice
        drawArc(
            color = liabilitiesColor,
            startAngle = -90f + (assetsPercentage * 360f),
            sweepAngle = liabilitiesPercentage * 360f,
            useCenter = true,
            topLeft = Offset(center.x - radius, center.y - radius),
            size = Size(canvasSize, canvasSize),
        )

        // Draw assets slice
        drawArc(
            color = assetsColor,
            startAngle = -90f,
            sweepAngle = assetsPercentage * 360f,
            useCenter = true,
            topLeft = Offset(center.x - radius, center.y - radius),
            size = Size(canvasSize, canvasSize),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NetWorthScreen(
    modifier: Modifier = Modifier,
    viewModel: NetWorthViewModel
) {
    val totalAssets by viewModel.totalAssets.collectAsState()
    val totalLiabilities by viewModel.totalLiabilities.collectAsState()
    val netWorth by viewModel.netWorth.collectAsState()

    NetWorthScreenContent(
        netWorth = netWorth,
        totalAssets = totalAssets,
        totalLiabilities = totalLiabilities,
        modifier = modifier
    )
}

@Composable
fun NetWorthScreenContent(
    netWorth: Double,
    totalAssets: Double,
    totalLiabilities: Double,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        NetWorthCardTitle(netWorth = netWorth)

        Spacer(modifier = Modifier.height(24.dp))

        DisplayPieChart(totalAssets = totalAssets, totalLiabilities = totalLiabilities)

        DisplayValueCards(totalAssets = totalAssets, totalLiabilities = totalLiabilities)
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
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Net Worth",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = currencyFormatter.format(netWorth),
                fontSize = 36.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Assets - Liabilities",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun DisplayPieChart(
    totalAssets: Double,
    totalLiabilities: Double
) {
    // Pie Chart Section
    if (totalAssets > 0 || totalLiabilities > 0) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Assets vs Liabilities",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(16.dp))

                PieChart(
                    assets = totalAssets,
                    liabilities = totalLiabilities,
                    size = 160.dp,
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Legend
                val primaryColor = MaterialTheme.colorScheme.primary
                val errorColor = MaterialTheme.colorScheme.error
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Canvas(modifier = Modifier.size(12.dp)) {
                            drawCircle(color = errorColor)
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Liabilities",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Canvas(modifier = Modifier.size(12.dp)) {
                            drawCircle(color = primaryColor)
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Assets",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun DisplayValueCards(
    totalAssets: Double,
    totalLiabilities: Double
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // Asset Value Card
        ValueCard(
            totalAssets = totalAssets,
            title = "Assets",
            cardContainerColor = MaterialTheme.colorScheme.primary,
            textColor = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.weight(1f),
        )

        // Liabilities Value Card
        ValueCard(
            totalAssets = totalLiabilities,
            title = "Liabilities",
            cardContainerColor = MaterialTheme.colorScheme.error,
            textColor = MaterialTheme.colorScheme.onError,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
fun ValueCard(
    totalAssets: Double,
    title: String,
    cardContainerColor: Color,
    textColor: Color,
    modifier: Modifier
) {
    Card(
        modifier = modifier
            .padding(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardContainerColor
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = textColor,
            )
            Text(
                text = currencyFormatter.format(totalAssets),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        }
    }
}


@PreviewLightDark
@Composable
fun DisplayPieChartPreview() {
    AnnetteTheme {
        DisplayPieChart(
            totalAssets = 1000.00,
            totalLiabilities = 500.00
        )
    }
}

@PreviewLightDark
@Composable
fun NetWorthCardTitlePreview() {
    AnnetteTheme {
        NetWorthCardTitle(23.00)
    }
}

@PreviewLightDark
@Composable
fun NetWorthScreenPreview() {
    AnnetteTheme {
        NetWorthScreen(viewModel = hiltViewModel())
    }
}
