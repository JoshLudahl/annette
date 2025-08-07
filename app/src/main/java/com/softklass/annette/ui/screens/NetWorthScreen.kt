package com.softklass.annette.ui.screens

import android.R.attr.textColor
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.softklass.annette.ui.screens.viewmodels.NetWorthViewModel
import com.softklass.annette.ui.theme.AnnetteTheme
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun PieChart(
    assets: Double,
    liabilities: Double,
    modifier: Modifier = Modifier,
    size: androidx.compose.ui.unit.Dp = 200.dp,
) {
    val total = assets + liabilities
    if (total <= 0) return
    
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
            size = Size(canvasSize, canvasSize)
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
    viewModel: NetWorthViewModel = hiltViewModel()
) {
    val totalAssets by viewModel.totalAssets.collectAsState()
    val totalLiabilities by viewModel.totalLiabilities.collectAsState()
    val netWorth by viewModel.netWorth.collectAsState()
    val currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US)
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
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
                modifier = Modifier.padding(24.dp),
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
                    text = "Total Assets - Total Liabilities",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
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
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Card(
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Assets",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(
                        text = currencyFormatter.format(totalAssets),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
            
            Card(
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Liabilities",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onError
                    )
                    Text(
                        text = currencyFormatter.format(totalLiabilities),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onError
                    )
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
fun NetWorthScreenPreview() {
    AnnetteTheme {
        NetWorthScreen()
    }
}
