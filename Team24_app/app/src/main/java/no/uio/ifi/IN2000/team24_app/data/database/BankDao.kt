package no.uio.ifi.IN2000.team24_app.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BankDao{
    @Insert
    fun insertAll(vararg bank: Bank)

    @Delete
    fun delete(bank : Bank)

    @Query("SELECT * FROM Bank WHERE id = 0")
    fun get(): Flow<Bank>

    @Query("UPDATE Bank SET balance = balance - :sum WHERE id = 0")
    fun withdraw(sum : Int)

    @Query("UPDATE Bank SET balance = balance - :sum WHERE id = 0")
    fun deposit(sum : Int)
}