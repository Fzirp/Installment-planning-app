package com.installment.manager.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "payments",
    foreignKeys = [
        ForeignKey(
            entity = Installment::class,
            parentColumns = ["id"],
            childColumns = ["installmentId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("installmentId")]
)
data class Payment(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val installmentId: Long,
    val paymentNumber: Int,         // Which installment number (1, 2, 3...)
    val amount: Long,
    val dueDateMillis: Long,        // When it's due
    val paidDateMillis: Long? = null, // When it was actually paid
    val isPaid: Boolean = false,
    val note: String = ""
)
