package com.example.inostudiocase.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.inostudiocase.MovieDao
import com.example.inostudiocase.data.room.MovieEntity

@Database(entities = [MovieEntity::class], version = 1, exportSchema = false)
abstract class MovieDatabase : RoomDatabase() {
    abstract val movieDao: MovieDao
}