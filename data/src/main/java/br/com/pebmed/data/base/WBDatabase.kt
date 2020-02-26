package br.com.pebmed.data.base

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.pebmed.data.repo.local.model.RepoEntityModel
import br.com.pebmed.data.repo.local.RepoDao

@Database(version = 1, entities = [RepoEntityModel::class])
abstract class WBDatabase : RoomDatabase() {
    abstract fun repoDao(): RepoDao
}