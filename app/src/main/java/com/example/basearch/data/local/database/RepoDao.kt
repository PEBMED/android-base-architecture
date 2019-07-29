package com.example.basearch.data.local.database

import androidx.room.*
import com.example.basearch.data.local.model.RepoCache

@Dao
interface RepoDao {
    @Query("SELECT * FROM Repo")
    suspend fun getAll(): List<RepoCache>

    @Query("SELECT * FROM Repo WHERE id = :id")
    suspend fun getFromId(id: Int): RepoCache?

    @Query("DELETE FROM Repo")
    suspend fun deleteAll()

    /**
     * Insert an object in the database.
     *
     * @param obj the object to be inserted.
     * @return The SQLite row id
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(obj: RepoCache): Long

    /**
     * Insert an array of objects in the database.
     *
     * @param obj the objects to be inserted.
     * @return The SQLite row ids
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(obj: List<RepoCache>): List<Long>

    /**
     * Update an object from the database.
     *
     * @param obj the object to be updated
     */
    @Update
    suspend fun update(obj: RepoCache)

    /**
     * Update an array of objects from the database.
     *
     * @param obj the object to be updated
     */
    @Update
    suspend fun update(obj: List<RepoCache>)

    /**
     * Delete an object from the database
     *
     * @param obj the object to be deleted
     */
    @Delete
    suspend fun delete(obj: RepoCache)

    @Transaction
    suspend fun upsert(obj: RepoCache) {
        val id = insert(obj)
        if (id == -1L) {
            update(obj)
        }
    }

    @Transaction
    suspend fun upsert(objList: List<RepoCache>) {
        val insertResult = insert(objList)
        val updateList = mutableListOf<RepoCache>()

        for (i in insertResult.indices) {
            if (insertResult[i] == -1L) {
                updateList.add(objList[i])
            }
        }

        if (updateList.isNotEmpty()) {
            update(updateList)
        }
    }
}