package com.installment.manager.data.dao

import androidx.room.*
import com.installment.manager.data.model.Payment
import kotlinx.coroutines.flow.Flow

@Dao
interface PaymentDao {

    @Query("SELECT * FROM payments WHERE installmentId = :installmentId ORDER BY paymentNumber ASC")
    fun getPaymentsForInstallment(installmentId: Long): Flow<List<Payment>>

    @Query("SELECT * FROM payments WHERE installmentId = :installmentId ORDER BY paymentNumber ASC")
    suspend fun getPaymentsForInstallmentOnce(installmentId: Long): List<Payment>

    @Query("SELECT * FROM payments WHERE isPaid = 0 ORDER BY dueDateMillis ASC")
    fun getUpcomingPayments(): Flow<List<Payment>>

    @Query("SELECT * FROM payments WHERE isPaid = 0 AND dueDateMillis BETWEEN :startMillis AND :endMillis ORDER BY dueDateMillis ASC")
    fun getPaymentsDueInRange(startMillis: Long, endMillis: Long): Flow<List<Payment>>

    @Query("SELECT * FROM payments WHERE isPaid = 0 AND dueDateMillis < :currentMillis ORDER BY dueDateMillis ASC")
    fun getOverduePayments(currentMillis: Long): Flow<List<Payment>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayment(payment: Payment): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayments(payments: List<Payment>)

    @Update
    suspend fun updatePayment(payment: Payment)

    @Query("UPDATE payments SET isPaid = :isPaid, paidDateMillis = :paidDateMillis WHERE id = :id")
    suspend fun markPaymentPaid(id: Long, isPaid: Boolean, paidDateMillis: Long?)

    @Query("DELETE FROM payments WHERE installmentId = :installmentId")
    suspend fun deletePaymentsForInstallment(installmentId: Long)

    @Query("SELECT COUNT(*) FROM payments WHERE isPaid = 0 AND dueDateMillis < :currentMillis")
    fun getOverdueCount(currentMillis: Long): Flow<Int>

    @Query("SELECT * FROM payments WHERE isPaid = 0 AND dueDateMillis BETWEEN :startMillis AND :endMillis")
    suspend fun getPaymentsDueInRangeOnce(startMillis: Long, endMillis: Long): List<Payment>

    // For calendar view - get all payments in a month range
    @Query("SELECT * FROM payments WHERE dueDateMillis BETWEEN :startMillis AND :endMillis ORDER BY dueDateMillis ASC")
    fun getPaymentsInRange(startMillis: Long, endMillis: Long): Flow<List<Payment>>
}
