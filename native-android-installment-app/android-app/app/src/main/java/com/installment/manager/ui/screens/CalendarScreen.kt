package com.installment.manager.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.installment.manager.data.model.Payment
import com.installment.manager.util.PersianDateUtil
import com.installment.manager.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit,
    onInstallmentClick: (Long) -> Unit
) {
    val today = remember { PersianDateUtil.today() }
    var currentYear by remember { mutableIntStateOf(today.year) }
    var currentMonth by remember { mutableIntStateOf(today.month) }
    var selectedDay by remember { mutableStateOf<Int?>(null) }
    val currencyUnit by viewModel.currencyUnit.collectAsState()

    // Calculate month range in millis
    val monthStartMillis = remember(currentYear, currentMonth) {
        PersianDateUtil.toMillis(currentYear, currentMonth, 1)
    }
    val daysInMonth = remember(currentYear, currentMonth) {
        PersianDateUtil.getDaysInMonth(currentYear, currentMonth)
    }
    val monthEndMillis = remember(currentYear, currentMonth) {
        PersianDateUtil.toMillis(
            currentYear,
            currentMonth,
            daysInMonth
        ) + 86400000L // end of last day
    }

    val paymentsInMonth by viewModel.getPaymentsInRange(monthStartMillis, monthEndMillis)
        .collectAsState(initial = emptyList())

    // Map payments by day of month (Persian)
    val paymentsByDay = remember(paymentsInMonth) {
        paymentsInMonth.groupBy { payment ->
            PersianDateUtil.fromMillis(payment.dueDateMillis).day
        }
    }

    // First day of month - what day of week
    val firstDayOfWeek = remember(currentYear, currentMonth) {
        PersianDateUtil.getDayOfWeek(monthStartMillis)
    }

    val dayHeaders = listOf("ش", "ی", "د", "س", "چ", "پ", "ج")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("تقویم اقساط") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowForward, contentDescription = "بازگشت")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Month navigation
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        if (currentMonth > 1) {
                            currentMonth--
                        } else {
                            currentMonth = 12
                            currentYear--
                        }
                        selectedDay = null
                    }) {
                        Icon(Icons.Filled.ChevronRight, contentDescription = "ماه قبل")
                    }

                    Text(
                        "${PersianDateUtil.getMonthName(currentMonth)} ${PersianDateUtil.toPersianDigits(currentYear)}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    IconButton(onClick = {
                        if (currentMonth < 12) {
                            currentMonth++
                        } else {
                            currentMonth = 1
                            currentYear++
                        }
                        selectedDay = null
                    }) {
                        Icon(Icons.Filled.ChevronLeft, contentDescription = "ماه بعد")
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Day headers
            Row(modifier = Modifier.fillMaxWidth()) {
                dayHeaders.forEach { day ->
                    Text(
                        day,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Calendar grid
            val totalCells = firstDayOfWeek + daysInMonth
            val rows = (totalCells + 6) / 7

            for (row in 0 until rows) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    for (col in 0 until 7) {
                        val cellIndex = row * 7 + col
                        val dayNum = cellIndex - firstDayOfWeek + 1

                        if (dayNum in 1..daysInMonth) {
                            val hasPayments = paymentsByDay.containsKey(dayNum)
                            val hasUnpaid = paymentsByDay[dayNum]?.any { !it.isPaid } == true
                            val isToday = currentYear == today.year &&
                                    currentMonth == today.month && dayNum == today.day
                            val isSelected = selectedDay == dayNum

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .padding(2.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when {
                                            isSelected -> MaterialTheme.colorScheme.primary
                                            isToday -> MaterialTheme.colorScheme.primaryContainer
                                            else -> Color.Transparent
                                        }
                                    )
                                    .clickable { selectedDay = if (selectedDay == dayNum) null else dayNum },
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        PersianDateUtil.toPersianDigits(dayNum),
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = if (isToday || isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = when {
                                            isSelected -> MaterialTheme.colorScheme.onPrimary
                                            isToday -> MaterialTheme.colorScheme.onPrimaryContainer
                                            col == 6 -> MaterialTheme.colorScheme.error // Friday
                                            else -> MaterialTheme.colorScheme.onSurface
                                        }
                                    )
                                    if (hasPayments) {
                                        Box(
                                            modifier = Modifier
                                                .size(6.dp)
                                                .clip(CircleShape)
                                                .background(
                                                    if (hasUnpaid) MaterialTheme.colorScheme.error
                                                    else Color(0xFF4CAF50)
                                                )
                                        )
                                    }
                                }
                            }
                        } else {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Selected day payments
            val selectedPayments = if (selectedDay != null) {
                paymentsByDay[selectedDay] ?: emptyList()
            } else {
                paymentsInMonth
            }

            if (selectedPayments.isNotEmpty()) {
                Text(
                    if (selectedDay != null)
                        "اقساط ${PersianDateUtil.toPersianDigits(selectedDay!!)} ${PersianDateUtil.getMonthName(currentMonth)}"
                    else "تمام اقساط این ماه",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(selectedPayments) { payment ->
                        CalendarPaymentItem(
                            payment = payment,
                            viewModel = viewModel,
                            currencyUnit = currencyUnit,
                            onClick = { onInstallmentClick(payment.installmentId) }
                        )
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        if (selectedDay != null) "قسطی در این روز ندارید"
                        else "قسطی در این ماه ندارید",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }
}

@Composable
fun CalendarPaymentItem(
    payment: Payment,
    viewModel: MainViewModel,
    currencyUnit: String,
    onClick: () -> Unit
) {
    val persianDate = PersianDateUtil.fromMillis(payment.dueDateMillis)
    val isOverdue = !payment.isPaid && payment.dueDateMillis < System.currentTimeMillis()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                if (payment.isPaid) Icons.Filled.CheckCircle else Icons.Filled.Schedule,
                contentDescription = null,
                tint = when {
                    payment.isPaid -> Color(0xFF4CAF50)
                    isOverdue -> MaterialTheme.colorScheme.error
                    else -> MaterialTheme.colorScheme.primary
                },
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "قسط ${PersianDateUtil.toPersianDigits(payment.paymentNumber)}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    persianDate.format(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                viewModel.formatAmount(payment.amount, currencyUnit),
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = if (isOverdue) MaterialTheme.colorScheme.error
                else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
