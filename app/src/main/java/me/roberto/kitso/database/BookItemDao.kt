package me.roberto.kitso.database

import androidx.room.Dao
import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import me.roberto.kitso.BookItem


@Dao
interface BookItemDao {


    @Insert(onConflict = REPLACE)
    fun save(item: BookItem)

    @Query("SELECT * FROM bookitem")
    fun load(): Maybe<List<BookItem>>



}
