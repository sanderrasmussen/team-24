package no.uio.ifi.IN2000.team24_app.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao //methods for sql queries
interface BankDao{
    @Insert
    fun insertAll(vararg bank: Bank)

    @Delete
    fun delete(bank : Bank)

    @Query("SELECT * FROM Bank")
    fun get(): List<Bank>

    @Query("UPDATE Bank SET balance = balance - :sum ")
    fun withdraw(sum : Int)

    @Query("UPDATE Bank SET balance = balance + :sum ")
    fun deposit(sum : Int)
}