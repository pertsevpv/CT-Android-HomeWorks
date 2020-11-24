package com.example.hw8

import android.app.Application
import androidx.room.Room
import androidx.room.migration.Migration
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

class ThisApp : Application() {

    lateinit var fakeAPIService: FakeAPI
    lateinit var postDB: FakeDatabase

    companion object {
        const val BASE_URL = "https://jsonplaceholder.typicode.com"
        lateinit var instance: ThisApp
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        val mRetrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        fakeAPIService = mRetrofit.create()
        postDB = Room.databaseBuilder(applicationContext, FakeDatabase::class.java, DATABASE_NAME).allowMainThreadQueries().addMigrations()
            .build()
    }

}