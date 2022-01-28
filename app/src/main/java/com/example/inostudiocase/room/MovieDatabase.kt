package com.example.inostudiocase.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.inostudiocase.MovieDao
import com.example.inostudiocase.data.room.ActorsEntity
import com.example.inostudiocase.data.room.MovieEntity

@Database(entities = [MovieEntity::class, ActorsEntity::class], version = 1, exportSchema = false)
abstract class MovieDatabase : RoomDatabase() {
    abstract val movieDao: MovieDao
    abstract val actorsDao: ActorsDao
}