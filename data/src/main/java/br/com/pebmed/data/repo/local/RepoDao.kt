package br.com.pebmed.data.repo.local

import androidx.room.*
import br.com.pebmed.data.repo.local.model.RepoEntityModel

@Dao
interface RepoDao {
    @Query("SELECT * FROM RepoModel")
    suspend fun getAll(): List<RepoEntityModel>

    @Query("SELECT * FROM RepoModel WHERE id = :id")
    suspend fun getFromId(id: Int): RepoEntityModel?

    @Query("DELETE FROM RepoModel")
    suspend fun deleteAll()

    /**
     * Insert an object in the database.
     *
     * @param obj the object to be inserted.
     * @return The SQLite row id
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(obj: RepoEntityModel): Long

    /**
     * Insert an array of objects in the database.
     *
     * @param obj the objects to be inserted.
     * @return The SQLite row ids
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(obj: List<RepoEntityModel>): List<Long>

    /**
     * Update an object from the database.
     *
     * @param obj the object to be updated
     */
    @Update
    suspend fun update(obj: RepoEntityModel)

    /**
     * Update an array of objects from the database.
     *
     * @param obj the object to be updated
     */
    @Update
    suspend fun update(obj: List<RepoEntityModel>)

    /**
     * Delete an object from the database
     *
     * @param obj the object to be deleted
     */
    @Delete
    suspend fun delete(obj: RepoEntityModel)

    @Transaction
    suspend fun upsert(obj: RepoEntityModel) {
        val id = insert(obj)
        if (id == -1L) {
            update(obj)
        }
    }

    @Transaction
    suspend fun upsert(objList: List<RepoEntityModel>) {
        val insertResult = insert(objList)
        val updateList = mutableListOf<RepoEntityModel>()

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