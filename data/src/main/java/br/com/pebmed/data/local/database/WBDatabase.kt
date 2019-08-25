package com.example.basearch.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.basearch.data.local.model.RepoCache

@Database(version = 1, entities = [RepoCache::class])
abstract class WBDatabase : RoomDatabase() {
    abstract fun repoDao(): RepoDao
}