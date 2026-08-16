package com.installment.manager.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "installments")
data class Installment(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val totalAmount: Long,          // Total loan/purchase amount in Rials
    val installmentAmount: Long,    // Each installment amount
    val totalCount: Int,            // Total number of installments
    val paidCount: Int = 0,         // Number of paid installments
    val category: String,           // Category name
    val dayOfMonth: Int,            // Day of month for payment
    val startDateMillis: Long,      // Start date in millis
    val note: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val isCompleted: Boolean = false,
    val notificationsEnabled: Boolean = true,
    val reminderDaysBefore: Int = 3  // Days before due date to remind
)
