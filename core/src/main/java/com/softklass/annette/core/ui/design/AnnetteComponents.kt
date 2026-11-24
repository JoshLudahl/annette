package com.softklass.annette.core.ui.design

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForwardIos
import androidx.compose.material.icons.automirrored.rounded.TrendingDown
import androidx.compose.material.icons.automirrored.rounded.TrendingUp
import androidx.compose.material.icons.rounded.AccountBalance
import androidx.compose.material.icons.rounded.AttachMoney
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.CreditCard
import androidx.compose.material.icons.rounded.DirectionsBus
import androidx.compose.material.icons.rounded.Flight
import androidx.compose.material.icons.rounded.FoodBank
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.LocalHospital
import androidx.compose.material.icons.rounded.MonetizationOn
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.Payments
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material.icons.rounded.School
import androidx.compose.material.icons.rounded.ShoppingBag
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material.icons.rounded.VolunteerActivism
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softklass.theme.ui.theme.BrandTheme
import kotlin.math.abs

/* ---------------------------------------------------------------------------------------------
 * Surfaces
 * ------------------------------------------------------------------------------------------- */

/** White, softly bordered container used for grouped lists and section cards. */
@Composable
fun GroupedSurface(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.surface,
    shape: RoundedCornerShape = RoundedCornerShape(24.dp),
    content: @Composable ColumnScope.() -> Unit,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = shape,
        color = color,
        shadowElevation = 0.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.55f)),
    ) {
        Column(content = content)
    }
}

/* ---------------------------------------------------------------------------------------------
 * Pills & small controls
 * ------------------------------------------------------------------------------------------- */

@Composable
fun SoftPill(
    text: String,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    textStyle: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.labelMedium,
    onClick: (() -> Unit)? = null,
) {
    val shape = RoundedCornerShape(percent = 50)
    Row(
        modifier = modifier
            .clip(shape)
            .background(containerColor)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(horizontal = 12.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        if (leadingIcon != null) {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(14.dp),
            )
        }
        Text(
            text = text,
            style = textStyle,
            color = contentColor,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        if (trailingIcon != null) {
            Icon(
                imageVector = trailingIcon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(12.dp),
            )
        }
    }
}

/** Translucent pill used on the berry hero. */
@Composable
fun HeroPill(
    text: String,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    onClick: (() -> Unit)? = null,
) {
    SoftPill(
        text = text,
        modifier = modifier,
        containerColor = BrandTheme.colors.heroGlow,
        contentColor = BrandTheme.colors.onHero,
        leadingIcon = leadingIcon,
        textStyle = MaterialTheme.typography.labelMedium,
        onClick = onClick,
    )
}

@Composable
fun PastelIconTile(
    icon: ImageVector,
    tint: Color,
    container: Color,
    modifier: Modifier = Modifier,
    size: Dp = 44.dp,
    iconSize: Dp = 22.dp,
    shape: RoundedCornerShape = RoundedCornerShape(14.dp),
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(shape)
            .background(container),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(iconSize),
        )
    }
}

@Composable
fun CircleIconButton(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    contentDescription: String? = null,
    size: Dp = 42.dp,
    iconSize: Dp = 18.dp,
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(containerColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = contentColor,
            modifier = Modifier.size(iconSize),
        )
    }
}

/* ---------------------------------------------------------------------------------------------
 * Section header
 * ------------------------------------------------------------------------------------------- */

@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
    titleTrailing: (@Composable RowScope.() -> Unit)? = null,
    style: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.titleLarge,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            style = style,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold,
        )
        if (titleTrailing != null) {
            titleTrailing()
        }
        Spacer(Modifier.weight(1f))
        if (actionLabel != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(percent = 50))
                    .clickable(enabled = onAction != null) { onAction?.invoke() }
                    .padding(horizontal = 4.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Text(
                    text = actionLabel,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                if (onAction != null) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowForwardIos,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(10.dp),
                    )
                }
            }
        }
    }
}

/* ---------------------------------------------------------------------------------------------
 * Money cards
 * ------------------------------------------------------------------------------------------- */

@Composable
fun MoneyStatCard(
    label: String,
    value: String,
    icon: ImageVector,
    tint: Color,
    container: Color,
    modifier: Modifier = Modifier,
    info: Boolean = false,
    onClick: () -> Unit = {},
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(22.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 1.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f)),
        onClick = onClick,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            PastelIconTile(
                icon = icon,
                tint = tint,
                container = container,
                size = 40.dp,
                iconSize = 20.dp,
                shape = RoundedCornerShape(12.dp),
            )
            Spacer(Modifier.height(22.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                if (info) {
                    Spacer(Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Rounded.Info,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.65f),
                        modifier = Modifier.size(12.dp),
                    )
                }
            }
            Spacer(Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
fun AmountWithDelta(
    amount: String,
    delta: String?,
    positive: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.End) {
        Text(
            text = amount,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
        )
        if (delta != null) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (positive) Icons.AutoMirrored.Rounded.TrendingUp
                    else Icons.AutoMirrored.Rounded.TrendingDown,
                    contentDescription = null,
                    tint = if (positive) BrandTheme.colors.positive else BrandTheme.colors.negative,
                    modifier = Modifier.size(12.dp),
                )
                Text(
                    text = delta,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (positive) BrandTheme.colors.positive else BrandTheme.colors.negative,
                )
            }
        }
    }
}

/* ---------------------------------------------------------------------------------------------
 * Insight banner
 * ------------------------------------------------------------------------------------------- */

@Composable
fun InsightBanner(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    actionLabel: String = "Get Pro",
    onAction: (() -> Unit)? = null,
) {
    val colors = BrandTheme.colors
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = colors.insightBackground,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Rounded.AutoAwesome,
                contentDescription = null,
                tint = colors.onInsight,
                modifier = Modifier.size(18.dp),
            )
            Spacer(Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.onInsight,
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.labelSmall,
                        color = colors.insightMuted,
                    )
                }
            }
            if (onAction != null) {
                Spacer(Modifier.width(8.dp))
                Surface(
                    shape = RoundedCornerShape(percent = 50),
                    color = if (colors.onInsight == Color.White) Color.White else colors.onInsight,
                    onClick = onAction,
                ) {
                    Text(
                        text = actionLabel,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = colors.insightBackground,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                    )
                }
            }
        }
    }
}

/* ---------------------------------------------------------------------------------------------
 * Segmented control
 * ------------------------------------------------------------------------------------------- */

@Composable
fun <T> SegmentedToggle(
    options: List<T>,
    selected: T,
    onSelect: (T) -> Unit,
    label: (T) -> String,
    modifier: Modifier = Modifier,
) {
    val isDarkScheme = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val selectedContainer = if (isDarkScheme) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surface
    }
    val selectedContent = if (isDarkScheme) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSurface
    }
    val selectedColor by animateColorAsState(
        targetValue = selectedContainer,
        label = "segmentColor",
    )
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(percent = 50))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        options.forEach { option ->
            val isSelected = option == selected
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(percent = 50))
                    .background(if (isSelected) selectedColor else Color.Transparent)
                    .clickable { onSelect(option) }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = label(option),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = if (isSelected) selectedContent
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

/* ---------------------------------------------------------------------------------------------
 * Progress
 * ------------------------------------------------------------------------------------------- */

@Composable
fun GoalProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = BrandTheme.colors.berry,
    trackColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    height: Dp = 10.dp,
    dashedRemainder: Boolean = true,
) {
    val safeProgress = progress.coerceIn(0f, 1f)
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(height),
    ) {
        val corner = CornerRadius(size.height / 2f, size.height / 2f)
        drawRoundRect(
            color = trackColor,
            topLeft = Offset(0f, 0f),
            size = Size(size.width, size.height),
            cornerRadius = corner,
        )
        if (safeProgress > 0f) {
            drawRoundRect(
                color = color,
                topLeft = Offset(0f, 0f),
                size = Size(size.width * safeProgress, size.height),
                cornerRadius = corner,
            )
        }
        if (dashedRemainder && safeProgress < 0.98f) {
            val dash = PathEffect.dashPathEffect(
                floatArrayOf(size.height * 0.9f, size.height * 1.1f),
            )
            drawLine(
                color = color.copy(alpha = 0.45f),
                start = Offset(size.width * safeProgress + size.height * 0.4f, size.height / 2f),
                end = Offset(size.width - size.height * 0.5f, size.height / 2f),
                strokeWidth = size.height * 0.75f,
                cap = StrokeCap.Round,
                pathEffect = dash,
            )
        }
    }
}

@Composable
fun RingProgress(
    progress: Float,
    color: Color,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    strokeWidth: Dp = 4.dp,
    trackColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    content: @Composable () -> Unit = {},
) {
    Box(modifier = modifier.size(size), contentAlignment = Alignment.Center) {
        Canvas(Modifier.fillMaxSize()) {
            val stroke = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            val inset = strokeWidth.toPx() / 2f
            drawArc(
                color = trackColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = Offset(inset, inset),
                size = Size(this.size.width - inset * 2, this.size.height - inset * 2),
                style = stroke,
            )
            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = 360f * progress.coerceIn(0f, 1f),
                useCenter = false,
                topLeft = Offset(inset, inset),
                size = Size(this.size.width - inset * 2, this.size.height - inset * 2),
                style = stroke,
            )
        }
        content()
    }
}

/* ---------------------------------------------------------------------------------------------
 * Hero
 * ------------------------------------------------------------------------------------------- */

@Composable
fun GradientHero(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = BrandTheme.colors
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(colors.heroStart, colors.heroEnd),
                    start = Offset.Zero,
                    end = Offset.Infinite,
                ),
            ),
    ) {
        Canvas(Modifier.matchParentSize()) {
            drawCircle(
                color = colors.heroGlow,
                radius = size.width * 0.42f,
                center = Offset(size.width * 0.92f, -size.height * 0.12f),
            )
            drawCircle(
                color = colors.heroGlow.copy(alpha = colors.heroGlow.alpha * 0.6f),
                radius = size.width * 0.30f,
                center = Offset(size.width * 0.08f, size.height * 0.92f),
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            content = content,
        )
    }
}

/* ---------------------------------------------------------------------------------------------
 * Transactions
 * ------------------------------------------------------------------------------------------- */

@Composable
fun TransactionRow(
    title: String,
    subtitle: String,
    amount: String,
    icon: ImageVector,
    tint: Color,
    container: Color,
    modifier: Modifier = Modifier,
    amountColor: Color = MaterialTheme.colorScheme.onSurface,
    trailingNote: String? = null,
    onClick: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PastelIconTile(
            icon = icon,
            tint = tint,
            container = container,
            size = 44.dp,
            iconSize = 20.dp,
            shape = RoundedCornerShape(13.dp),
        )
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Spacer(Modifier.width(8.dp))
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = amount,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = amountColor,
            )
            if (trailingNote != null) {
                Text(
                    text = trailingNote,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

/* ---------------------------------------------------------------------------------------------
 * Top bar & empty state
 * ------------------------------------------------------------------------------------------- */

@Composable
fun AnnettePageHeader(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    onBack: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    StatusBarIcons()
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (onBack != null) {
            CircleIconButton(
                icon = Icons.AutoMirrored.Rounded.ArrowBack,
                onClick = onBack,
                contentDescription = "Back",
                containerColor = MaterialTheme.colorScheme.surface,
            )
            Spacer(Modifier.width(12.dp))
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        actions()
    }
}

@Composable
fun EmptyState(
    icon: ImageVector,
    title: String,
    message: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp, horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(BrandTheme.colors.berrySoft),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = BrandTheme.colors.berry,
                modifier = Modifier.size(28.dp),
            )
        }
        Spacer(Modifier.height(14.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}

/* ---------------------------------------------------------------------------------------------
 * Category visuals
 * ------------------------------------------------------------------------------------------- */

private val categoryIconMap: Map<String, ImageVector> = mapOf(
    "food" to Icons.Rounded.Restaurant,
    "restaurant" to Icons.Rounded.Restaurant,
    "dining" to Icons.Rounded.Restaurant,
    "groceries" to Icons.Rounded.FoodBank,
    "supermarket" to Icons.Rounded.FoodBank,
    "transport" to Icons.Rounded.DirectionsBus,
    "transportation" to Icons.Rounded.DirectionsBus,
    "travel" to Icons.Rounded.Flight,
    "vacation" to Icons.Rounded.Flight,
    "shopping" to Icons.Rounded.ShoppingBag,
    "clothing" to Icons.Rounded.ShoppingCart,
    "utilities" to Icons.Rounded.Home,
    "home" to Icons.Rounded.Home,
    "housing" to Icons.Rounded.Home,
    "real estate" to Icons.Rounded.Home,
    "property" to Icons.Rounded.Home,
    "rent" to Icons.Rounded.Home,
    "mortgage" to Icons.Rounded.Home,
    "health" to Icons.Rounded.LocalHospital,
    "medical" to Icons.Rounded.LocalHospital,
    "education" to Icons.Rounded.School,
    "investments" to Icons.Rounded.AccountBalance,
    "investment" to Icons.Rounded.AccountBalance,
    "salary" to Icons.Rounded.Payments,
    "income" to Icons.Rounded.Payments,
    "rental income" to Icons.Rounded.Payments,
    "paycheck" to Icons.Rounded.Payments,
    "cash" to Icons.Rounded.MonetizationOn,
    "card" to Icons.Rounded.CreditCard,
    "credit" to Icons.Rounded.CreditCard,
    "entertainment" to Icons.Rounded.Movie,
    "charity" to Icons.Rounded.VolunteerActivism,
    "loan" to Icons.Rounded.AttachMoney,
)

fun categoryIcon(category: String): ImageVector =
    categoryIconMap[category.lowercase()] ?: Icons.Rounded.ShoppingBag

private fun paletteIndexFor(category: String): Int {
    val known = listOf(
        "food", "groceries", "transport", "travel", "shopping", "utilities",
        "health", "education", "investments", "salary", "entertainment", "charity",
    )
    val index = known.indexOf(category.lowercase())
    return if (index >= 0) index % 7 else abs(category.lowercase().hashCode()) % 7
}

/** Pastel tint + ink pair for a category, derived from the brand chart palette. */
@Composable
fun categoryColors(category: String): Pair<Color, Color> {
    val palette = BrandTheme.colors.chart
    val color = palette.all[paletteIndexFor(category)]
    val isDark = MaterialTheme.colorScheme.surface.luminance() < 0.5f
    val container = color.copy(alpha = if (isDark) 0.22f else 0.14f)
    return color to container
}

/** Explicit chart colour for a category, used to keep legend/rows and slices in sync. */
@Composable
fun categoryChartColor(category: String, index: Int): Color {
    val palette = BrandTheme.colors.chart.all
    return palette[index % palette.size]
}
