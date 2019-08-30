package br.com.pebmed.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.pebmed.data.local.model.RepoCache

@Database(version = 1, entities = [RepoCache::class])
abstract class WBDatabase : RoomDatabase() {
    abstract fun repoDao(): RepoDao
}