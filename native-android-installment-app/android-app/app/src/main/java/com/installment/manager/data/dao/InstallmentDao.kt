package com.installment.manager.data.dao

import androidx.room.*
import com.installment.manager.data.model.Installment
import kotlinx.coroutines.flow.Flow

@Dao
interface InstallmentDao {

    @Query("SELECT * FROM installments ORDER BY createdAt DESC")
    fun getAllInstallments(): Flow<List<Installment>>

    @Query("SELECT * FROM installments WHERE isCompleted = 0 ORDER BY createdAt DESC")
    fun getActiveInstallments(): Flow<List<Installment>>

    @Query("SELECT * FROM installments WHERE isCompleted = 1 ORDER BY createdAt DESC")
    fun getCompletedInstallments(): Flow<List<Installment>>

    @Query("SELECT * FROM installments WHERE category = :category ORDER BY createdAt DESC")
    fun getInstallmentsByCategory(category: String): Flow<List<Installment>>

    @Query("SELECT * FROM installments WHERE id = :id")
    fun getInstallmentById(id: Long): Flow<Installment?>

    @Query("SELECT * FROM installments WHERE id = :id")
    suspend fun getInstallmentByIdOnce(id: Long): Installment?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInstallment(installment: Installment): Long

    @Update
    suspend fun updateInstallment(installment: Installment)

    @Delete
    suspend fun deleteInstallment(installment: Installment)

    @Query("DELETE FROM installments WHERE id = :id")
    suspend fun deleteInstallmentById(id: Long)

    @Query("UPDATE installments SET paidCount = :paidCount, isCompleted = :isCompleted WHERE id = :id")
    suspend fun updatePaymentProgress(id: Long, paidCount: Int, isCompleted: Boolean)

    @Query("SELECT COUNT(*) FROM installments WHERE isCompleted = 0")
    fun getActiveCount(): Flow<Int>

    @Query("SELECT SUM(installmentAmount * (totalCount - paidCount)) FROM installments WHERE isCompleted = 0")
    fun getTotalRemainingAmount(): Flow<Long?>

    @Query("SELECT SUM(installmentAmount * paidCount) FROM installments")
    fun getTotalPaidAmount(): Flow<Long?>

    @Query("SELECT * FROM installments WHERE notificationsEnabled = 1 AND isCompleted = 0")
    suspend fun getInstallmentsWithNotifications(): List<Installment>
}
