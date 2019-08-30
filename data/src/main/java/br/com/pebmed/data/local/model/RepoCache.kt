package br.com.pebmed.data.local.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.pebmed.data.local.model.OwnerCache

@Entity(tableName = "Repo")
data class RepoCache(
    @NonNull
    @PrimaryKey(autoGenerate = false)
    val id: Int,

    @ColumnInfo(name = "forks_count") val forksCount: Int?,
    @ColumnInfo(name = "forks_url") val forksUrl: String?,
    @ColumnInfo(name = "open_issues") val openIssues: Int?,
    @ColumnInfo(name = "open_issues_count") val openIssuesCount: Int?,
    @Embedded(prefix = "test_") val owner: OwnerCache, //TODO change to relationship? https://developer.android.com/training/data-storage/room/relationships
    @ColumnInfo(name = "pushed_at") val pushedAt: String?,
    @ColumnInfo(name = "updated_at") val updatedAt: String?,
    @ColumnInfo(name = "watchers_count") val watchersCount: Int?,
    @ColumnInfo(name = "stargazers_count") val stargazersCount: Int?,
    @ColumnInfo(name = "is_synchronized") val isSynchronized: Boolean?,
    @ColumnInfo(name = "last_sync_date") val lastSyncDate: String?,
    val description: String?,
    val disabled: Boolean?,
    val fork: Boolean?,
    val forks: Int?,
    val fullName: String?,
    val homepage: String?,
    val language: String?,
    val name: String?,
    val score: Double?,
    val size: Int?,
    val url: String?,
    val watchers: Int?
)