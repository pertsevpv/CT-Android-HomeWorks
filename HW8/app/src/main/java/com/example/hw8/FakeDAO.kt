package com.example.hw8

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FakeDAO {

    @Query("SELECT * FROM $TABLE_NAME")
    fun getAll(): List<Post>

    @Insert
    fun insertPost(post: Post)

    @Delete
    fun deletePost(post: Post)

    @Query("DELETE FROM $TABLE_NAME")
    fun deleteAll()
}