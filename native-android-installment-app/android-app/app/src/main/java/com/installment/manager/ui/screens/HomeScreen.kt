package com.installment.manager.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.installment.manager.data.model.DefaultCategories
import com.installment.manager.data.model.Installment
import com.installment.manager.util.PersianDateUtil
import com.installment.manager.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onAddClick: () -> Unit,
    onInstallmentClick: (Long) -> Unit,
    onCalendarClick: () -> Unit,
    onStatisticsClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val activeInstallments by viewModel.activeInstallments.collectAsState()
    val completedInstallments by viewModel.completedInstallments.collectAsState()
    val activeCount by viewModel.activeCount.collectAsState()
    val totalRemaining by viewModel.totalRemainingAmount.collectAsState()
    val totalPaid by viewModel.totalPaidAmount.collectAsState()
    val overdueCount by viewModel.overdueCount.collectAsState()
    val currencyUnit by viewModel.currencyUnit.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) }
    var showFilterMenu by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<String?>(null) }

    val today = remember { PersianDateUtil.today() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("مدیریت اقساط", style = MaterialTheme.typography.titleLarge)
                        Text(
                            today.format(),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onCalendarClick) {
                        Icon(Icons.Outlined.CalendarMonth, contentDescription = "تقویم")
                    }
                    IconButton(onClick = onStatisticsClick) {
                        Icon(Icons.Outlined.BarChart, contentDescription = "آمار")
                    }
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Outlined.Settings, contentDescription = "تنظیمات")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddClick,
                icon = { Icon(Icons.Filled.Add, contentDescription = null) },
                text = { Text("قسط جدید") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Summary Cards
            item {
                SummarySection(
                    activeCount = activeCount,
                    totalRemaining = totalRemaining,
                    totalPaid = totalPaid,
                    overdueCount = overdueCount,
                    currencyUnit = currencyUnit,
                    viewModel = viewModel
                )
            }

            // Tabs
            item {
                TabRow(
                    selectedTabIndex = selectedTab,
                    modifier = Modifier.clip(RoundedCornerShape(12.dp)),
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("فعال (${PersianDateUtil.toPersianDigits(activeInstallments.size)})") }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("تکمیل شده (${PersianDateUtil.toPersianDigits(completedInstallments.size)})") }
                    )
                }
            }

            // Category Filter
            item {
                CategoryFilterChips(
                    selectedCategory = selectedCategory,
                    onCategorySelected = { selectedCategory = it }
                )
            }

            // Installment List
            val displayList = when (selectedTab) {
                0 -> activeInstallments
                1 -> completedInstallments
                else -> activeInstallments
            }.let { list ->
                if (selectedCategory != null) {
                    list.filter { it.category == selectedCategory }
                } else {
                    list
                }
            }

            if (displayList.isEmpty()) {
                item {
                    EmptyState(selectedTab == 0)
                }
            }

            items(displayList, key = { it.id }) { installment ->
                InstallmentCard(
                    installment = installment,
                    currencyUnit = currencyUnit,
                    viewModel = viewModel,
                    onClick = { onInstallmentClick(installment.id) }
                )
            }

            // Bottom spacing for FAB
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun SummarySection(
    activeCount: Int,
    totalRemaining: Long,
    totalPaid: Long,
    overdueCount: Int,
    currencyUnit: String,
    viewModel: MainViewModel
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SummaryCard(
                modifier = Modifier.weight(1f),
                title = "اقساط فعال",
                value = PersianDateUtil.toPersianDigits(activeCount),
                icon = Icons.Filled.Receipt,
                color = MaterialTheme.colorScheme.primary
            )
            SummaryCard(
                modifier = Modifier.weight(1f),
                title = "معوقه",
                value = PersianDateUtil.toPersianDigits(overdueCount),
                icon = Icons.Filled.Warning,
                color = if (overdueCount > 0) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SummaryCard(
                modifier = Modifier.weight(1f),
                title = "مانده کل",
                value = viewModel.formatAmount(totalRemaining, currencyUnit),
                icon = Icons.Filled.AccountBalanceWallet,
                color = Color(0xFFFF9800),
                isSmallText = true
            )
            SummaryCard(
                modifier = Modifier.weight(1f),
                title = "پرداخت شده",
                value = viewModel.formatAmount(totalPaid, currencyUnit),
                icon = Icons.Filled.CheckCircle,
                color = Color(0xFF4CAF50),
                isSmallText = true
            )
        }
    }
}

@Composable
fun SummaryCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    isSmallText: Boolean = false
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                value,
                style = if (isSmallText) MaterialTheme.typography.bodyMedium
                else MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = color,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun CategoryFilterChips(
    selectedCategory: String?,
    onCategorySelected: (String?) -> Unit
) {
    val categories = DefaultCategories.list

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            selected = selectedCategory == null,
            onClick = { onCategorySelected(null) },
            label = { Text("همه") },
            leadingIcon = if (selectedCategory == null) {
                { Icon(Icons.Filled.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
            } else null
        )
    }

    // Scrollable category chips
    androidx.compose.foundation.lazy.LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories.size) { index ->
            val cat = categories[index]
            FilterChip(
                selected = selectedCategory == cat.name,
                onClick = {
                    onCategorySelected(if (selectedCategory == cat.name) null else cat.name)
                },
                label = { Text(cat.persianName) },
                leadingIcon = if (selectedCategory == cat.name) {
                    { Icon(Icons.Filled.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
                } else null
            )
        }
    }
}

@Composable
fun InstallmentCard(
    installment: Installment,
    currencyUnit: String,
    viewModel: MainViewModel,
    onClick: () -> Unit
) {
    val category = DefaultCategories.findByName(installment.category)
    val categoryColor = Color(category.color)
    val progress = if (installment.totalCount > 0) {
        installment.paidCount.toFloat() / installment.totalCount.toFloat()
    } else 0f

    val nextDueDate = remember(installment) {
        PersianDateUtil.addMonths(installment.startDateMillis, installment.paidCount)
    }
    val isOverdue = nextDueDate < System.currentTimeMillis() && !installment.isCompleted

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isOverdue)
                MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
            else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Category icon
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(categoryColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = getCategoryIcon(installment.category),
                        contentDescription = null,
                        tint = categoryColor,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        installment.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        category.persianName,
                        style = MaterialTheme.typography.bodySmall,
                        color = categoryColor
                    )
                }

                if (isOverdue) {
                    Icon(
                        Icons.Filled.Warning,
                        contentDescription = "معوقه",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(20.dp)
                    )
                }

                if (installment.isCompleted) {
                    Icon(
                        Icons.Filled.CheckCircle,
                        contentDescription = "تکمیل شده",
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Progress bar
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = if (installment.isCompleted) Color(0xFF4CAF50) else categoryColor,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "قسط ${PersianDateUtil.toPersianDigits(installment.paidCount)} از ${PersianDateUtil.toPersianDigits(installment.totalCount)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    "${PersianDateUtil.toPersianDigits((progress * 100).toInt())}٪",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = categoryColor
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "مبلغ هر قسط: ${viewModel.formatAmount(installment.installmentAmount, currencyUnit)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (!installment.isCompleted) {
                    val nextPersianDate = PersianDateUtil.fromMillis(nextDueDate)
                    Text(
                        "سررسید: ${nextPersianDate.format()}",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isOverdue) MaterialTheme.colorScheme.error
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyState(isActive: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            if (isActive) Icons.Outlined.Receipt else Icons.Outlined.CheckCircle,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.outline
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            if (isActive) "هنوز قسطی ثبت نشده" else "هنوز قسطی تکمیل نشده",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.outline
        )
        if (isActive) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "برای شروع، روی دکمه «قسط جدید» بزنید",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

fun getCategoryIcon(category: String): androidx.compose.ui.graphics.vector.ImageVector {
    return when (category) {
        "car" -> Icons.Filled.DirectionsCar
        "home" -> Icons.Filled.Home
        "phone" -> Icons.Filled.PhoneAndroid
        "appliance" -> Icons.Filled.Kitchen
        "education" -> Icons.Filled.School
        "medical" -> Icons.Filled.LocalHospital
        "personal" -> Icons.Filled.Person
        "bank" -> Icons.Filled.AccountBalance
        "shopping" -> Icons.Filled.ShoppingCart
        else -> Icons.Filled.MoreHoriz
    }
}
