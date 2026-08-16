package com.installment.manager.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.installment.manager.data.model.DefaultCategories
import com.installment.manager.util.PersianDateUtil
import com.installment.manager.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddInstallmentScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var totalAmount by remember { mutableStateOf("") }
    var installmentAmount by remember { mutableStateOf("") }
    var totalCount by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("other") }
    var note by remember { mutableStateOf("") }
    var notificationsEnabled by remember { mutableStateOf(true) }
    var reminderDays by remember { mutableIntStateOf(3) }

    val today = remember { PersianDateUtil.today() }
    var startYear by remember { mutableIntStateOf(today.year) }
    var startMonth by remember { mutableIntStateOf(today.month) }
    var dayOfMonth by remember { mutableIntStateOf(today.day) }

    var showErrors by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("افزودن قسط جدید") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowForward, contentDescription = "بازگشت")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Title
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("عنوان قسط") },
                placeholder = { Text("مثال: وام خودرو") },
                modifier = Modifier.fillMaxWidth(),
                isError = showErrors && title.isBlank(),
                leadingIcon = { Icon(Icons.Filled.Title, contentDescription = null) },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            // Category Selection
            Text(
                "دسته‌بندی",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(DefaultCategories.list) { category ->
                    val isSelected = selectedCategory == category.name
                    val catColor = Color(category.color)

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { selectedCategory = category.name }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isSelected) catColor
                                    else catColor.copy(alpha = 0.15f)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                getCategoryIcon(category.name),
                                contentDescription = category.persianName,
                                tint = if (isSelected) Color.White else catColor,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            category.persianName,
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isSelected) catColor
                            else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }

            // Amount fields
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = totalAmount,
                    onValueChange = { totalAmount = it.filter { c -> c.isDigit() } },
                    label = { Text("مبلغ کل (ریال)") },
                    modifier = Modifier.weight(1f),
                    isError = showErrors && totalAmount.isBlank(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )
                OutlinedTextField(
                    value = installmentAmount,
                    onValueChange = { installmentAmount = it.filter { c -> c.isDigit() } },
                    label = { Text("مبلغ هر قسط (ریال)") },
                    modifier = Modifier.weight(1f),
                    isError = showErrors && installmentAmount.isBlank(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )
            }

            // Auto-calculate hint
            if (totalAmount.isNotBlank() && installmentAmount.isNotBlank()) {
                val total = totalAmount.toLongOrNull() ?: 0
                val each = installmentAmount.toLongOrNull() ?: 1
                if (each > 0) {
                    val suggested = ((total + each - 1) / each).toInt()
                    Text(
                        "تعداد پیشنهادی: ${PersianDateUtil.toPersianDigits(suggested)} قسط",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Total count
            OutlinedTextField(
                value = totalCount,
                onValueChange = { totalCount = it.filter { c -> c.isDigit() } },
                label = { Text("تعداد اقساط") },
                modifier = Modifier.fillMaxWidth(),
                isError = showErrors && totalCount.isBlank(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                leadingIcon = { Icon(Icons.Filled.Numbers, contentDescription = null) },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            // Start Date (Persian)
            Text(
                "تاریخ شروع (شمسی)",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Year
                    NumberPicker(
                        value = startYear,
                        onValueChange = { startYear = it },
                        range = today.year - 5..today.year + 10,
                        label = "سال"
                    )
                    Text("/", style = MaterialTheme.typography.headlineSmall)
                    // Month
                    NumberPicker(
                        value = startMonth,
                        onValueChange = { startMonth = it },
                        range = 1..12,
                        label = "ماه"
                    )
                    Text("/", style = MaterialTheme.typography.headlineSmall)
                    // Day
                    NumberPicker(
                        value = dayOfMonth,
                        onValueChange = { dayOfMonth = it },
                        range = 1..PersianDateUtil.getDaysInMonth(startYear, startMonth),
                        label = "روز"
                    )
                }

                // Show selected date in Persian
                Text(
                    PersianDateUtil.PersianDate(startYear, startMonth, dayOfMonth).format(),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }

            // Note
            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("یادداشت (اختیاری)") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Filled.Note, contentDescription = null) },
                maxLines = 3,
                shape = RoundedCornerShape(12.dp)
            )

            // Notification settings
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "یادآوری پرداخت",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Switch(
                            checked = notificationsEnabled,
                            onCheckedChange = { notificationsEnabled = it }
                        )
                    }

                    if (notificationsEnabled) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "${PersianDateUtil.toPersianDigits(reminderDays)} روز قبل از سررسید",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Slider(
                            value = reminderDays.toFloat(),
                            onValueChange = { reminderDays = it.toInt() },
                            valueRange = 1f..7f,
                            steps = 5
                        )
                    }
                }
            }

            // Submit button
            Button(
                onClick = {
                    showErrors = true
                    if (title.isNotBlank() && totalAmount.isNotBlank() &&
                        installmentAmount.isNotBlank() && totalCount.isNotBlank()
                    ) {
                        viewModel.addInstallment(
                            title = title,
                            totalAmount = totalAmount.toLong(),
                            installmentAmount = installmentAmount.toLong(),
                            totalCount = totalCount.toInt(),
                            category = selectedCategory,
                            dayOfMonth = dayOfMonth,
                            startYear = startYear,
                            startMonth = startMonth,
                            note = note,
                            notificationsEnabled = notificationsEnabled,
                            reminderDaysBefore = reminderDays
                        )
                        onBack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Filled.Save, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("ذخیره قسط", style = MaterialTheme.typography.titleMedium)
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun NumberPicker(
    value: Int,
    onValueChange: (Int) -> Unit,
    range: IntRange,
    label: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = { if (value > range.first) onValueChange(value - 1) },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(Icons.Filled.Remove, contentDescription = "کاهش", modifier = Modifier.size(16.dp))
            }
            Text(
                PersianDateUtil.toPersianDigits(value),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            IconButton(
                onClick = { if (value < range.last) onValueChange(value + 1) },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "افزایش", modifier = Modifier.size(16.dp))
            }
        }
    }
}
