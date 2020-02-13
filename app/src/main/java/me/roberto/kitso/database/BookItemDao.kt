package me.roberto.kitso.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import io.reactivex.Maybe
import me.roberto.kitso.model.BookItem


@Dao
interface BookItemDao {


    @Insert(onConflict = REPLACE)
    fun save(item: BookItem)

    @Query("SELECT * FROM bookitem")
    fun load(): Maybe<List<BookItem>>



}
