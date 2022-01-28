package com.example.inostudiocase.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.inostudiocase.data.room.ActorsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ActorsDao {
    @Query("SELECT * from actors")
    fun getAll(): Flow<List<ActorsEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActor(actors: ActorsEntity)

    @Query("DELETE FROM actors WHERE id = :id")
    suspend fun deleteActor(id: Int)
}