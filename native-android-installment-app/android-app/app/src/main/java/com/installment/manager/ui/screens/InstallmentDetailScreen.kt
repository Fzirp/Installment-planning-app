package com.installment.manager.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import com.installment.manager.data.model.DefaultCategories
import com.installment.manager.data.model.Installment
import com.installment.manager.data.model.Payment
import com.installment.manager.util.PersianDateUtil
import com.installment.manager.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InstallmentDetailScreen(
    viewModel: MainViewModel,
    installmentId: Long,
    onBack: () -> Unit
) {
    val installment by viewModel.getInstallmentById(installmentId).collectAsState(initial = null)
    val payments by viewModel.getPaymentsForInstallment(installmentId).collectAsState(initial = emptyList())
    val currencyUnit by viewModel.currencyUnit.collectAsState()

    var showDeleteDialog by remember { mutableStateOf(false) }

    val inst = installment

    if (inst == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val category = DefaultCategories.findByName(inst.category)
    val categoryColor = Color(category.color)
    val progress = if (inst.totalCount > 0) inst.paidCount.toFloat() / inst.totalCount.toFloat() else 0f

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("حذف قسط") },
            text = { Text("آیا از حذف «${inst.title}» مطمئن هستید؟ تمام اطلاعات پرداخت‌ها نیز حذف خواهد شد.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteInstallment(inst)
                        showDeleteDialog = false
                        onBack()
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("حذف")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("انصراف")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(inst.title) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowForward, contentDescription = "بازگشت")
                    }
                },
                actions = {
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(
                            Icons.Filled.Delete,
                            contentDescription = "حذف",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
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
            // Header Card
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = categoryColor.copy(alpha = 0.1f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .background(categoryColor.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    getCategoryIcon(inst.category),
                                    contentDescription = null,
                                    tint = categoryColor,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(
                                    inst.title,
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    category.persianName,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = categoryColor
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Progress
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(12.dp)
                                .clip(RoundedCornerShape(6.dp)),
                            color = if (inst.isCompleted) Color(0xFF4CAF50) else categoryColor,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "قسط ${PersianDateUtil.toPersianDigits(inst.paidCount)} از ${PersianDateUtil.toPersianDigits(inst.totalCount)} پرداخت شده",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                "${PersianDateUtil.toPersianDigits((progress * 100).toInt())}٪",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = categoryColor
                            )
                        }
                    }
                }
            }

            // Info Cards
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    InfoCard(
                        modifier = Modifier.weight(1f),
                        title = "مبلغ کل",
                        value = viewModel.formatAmount(inst.totalAmount, currencyUnit),
                        icon = Icons.Outlined.Payments
                    )
                    InfoCard(
                        modifier = Modifier.weight(1f),
                        title = "مبلغ هر قسط",
                        value = viewModel.formatAmount(inst.installmentAmount, currencyUnit),
                        icon = Icons.Outlined.Receipt
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val remaining = inst.installmentAmount * (inst.totalCount - inst.paidCount)
                    val paid = inst.installmentAmount * inst.paidCount
                    InfoCard(
                        modifier = Modifier.weight(1f),
                        title = "پرداخت شده",
                        value = viewModel.formatAmount(paid, currencyUnit),
                        icon = Icons.Outlined.CheckCircle,
                        valueColor = Color(0xFF4CAF50)
                    )
                    InfoCard(
                        modifier = Modifier.weight(1f),
                        title = "مانده",
                        value = viewModel.formatAmount(remaining, currencyUnit),
                        icon = Icons.Outlined.Schedule,
                        valueColor = Color(0xFFFF9800)
                    )
                }
            }

            // Start date & note
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Outlined.CalendarMonth, contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "تاریخ شروع: ${PersianDateUtil.fromMillis(inst.startDateMillis).format()}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        if (inst.note.isNotBlank()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Outlined.Note, contentDescription = null,
                                    modifier = Modifier.size(20.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(inst.note, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
            }

            // Payments header
            item {
                Text(
                    "لیست اقساط",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // Payment items
            items(payments, key = { it.id }) { payment ->
                PaymentItem(
                    payment = payment,
                    installment = inst,
                    currencyUnit = currencyUnit,
                    viewModel = viewModel,
                    categoryColor = categoryColor
                )
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}

@Composable
fun InfoCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    valueColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(4.dp))
            Text(title, style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(
                value,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = valueColor
            )
        }
    }
}

@Composable
fun PaymentItem(
    payment: Payment,
    installment: Installment,
    currencyUnit: String,
    viewModel: MainViewModel,
    categoryColor: Color
) {
    val isOverdue = !payment.isPaid && payment.dueDateMillis < System.currentTimeMillis()
    val persianDate = PersianDateUtil.fromMillis(payment.dueDateMillis)

    val backgroundColor by animateColorAsState(
        targetValue = when {
            payment.isPaid -> Color(0xFF4CAF50).copy(alpha = 0.08f)
            isOverdue -> MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
            else -> MaterialTheme.colorScheme.surface
        },
        label = "bg"
    )

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Payment number
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        when {
                            payment.isPaid -> Color(0xFF4CAF50).copy(alpha = 0.2f)
                            isOverdue -> MaterialTheme.colorScheme.error.copy(alpha = 0.2f)
                            else -> categoryColor.copy(alpha = 0.15f)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (payment.isPaid) {
                    Icon(
                        Icons.Filled.Check,
                        contentDescription = null,
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text(
                        PersianDateUtil.toPersianDigits(payment.paymentNumber),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (isOverdue) MaterialTheme.colorScheme.error else categoryColor
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "قسط ${PersianDateUtil.toPersianDigits(payment.paymentNumber)}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    "سررسید: ${persianDate.format()}",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isOverdue) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    viewModel.formatAmount(payment.amount, currencyUnit),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (payment.isPaid && payment.paidDateMillis != null) {
                    Text(
                        "پرداخت: ${PersianDateUtil.fromMillis(payment.paidDateMillis).format()}",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF4CAF50)
                    )
                }
            }

            // Toggle paid button
            IconButton(
                onClick = { viewModel.togglePaymentPaid(payment, installment) }
            ) {
                Icon(
                    if (payment.isPaid) Icons.Filled.CheckCircle else Icons.Outlined.RadioButtonUnchecked,
                    contentDescription = if (payment.isPaid) "پرداخت شده" else "پرداخت نشده",
                    tint = if (payment.isPaid) Color(0xFF4CAF50)
                    else if (isOverdue) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.outline,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}
