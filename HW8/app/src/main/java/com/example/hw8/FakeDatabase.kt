package com.example.hw8

import androidx.room.Database
import androidx.room.InvalidationTracker
import androidx.room.RoomDatabase

@Database(entities = [Post::class], version = 1,exportSchema = true)
abstract class FakeDatabase : RoomDatabase() {
    abstract fun getFakeDao(): FakeDAO
}