package com.example.basearch.data.local.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Repo")
data class RepoCache(
    @NonNull
    @PrimaryKey(autoGenerate = false)
    val id: Int,

    @ColumnInfo(name = "is_private") val isPrivate: Boolean?,
    @ColumnInfo(name = "clone_url") val cloneUrl: String?,
    @ColumnInfo(name = "collaborators_url") val collaboratorsUrl: String?,
    @ColumnInfo(name = "comments_url") val commentsUrl: String?,
    @ColumnInfo(name = "commits_url") val commitsUrl: String?,
    @ColumnInfo(name = "contributors_url") val contributorsUrl: String?,
    @ColumnInfo(name = "created_at") val createdAt: String?,
    @ColumnInfo(name = "default_branch") val defaultBranch: String?,
    @ColumnInfo(name = "deployments_url") val deploymentsUrl: String?,
    @ColumnInfo(name = "downloads_url") val downloadsUrl: String?,
    @ColumnInfo(name = "events_url") val eventsUrl: String?,
    @ColumnInfo(name = "forks_count") val forksCount: Int?,
    @ColumnInfo(name = "forks_url") val forksUrl: String?,
    @ColumnInfo(name = "git_commits_url") val gitCommitsUrl: String?,
    @ColumnInfo(name = "git_refs_url") val gitRefsUrl: String?,
    @ColumnInfo(name = "git_tags_url") val gitTagsUrl: String?,
    @ColumnInfo(name = "git_url") val gitUrl: String?,
    @ColumnInfo(name = "has_downloads") val hasDownloads: Boolean?,
    @ColumnInfo(name = "has_issues") val hasIssues: Boolean?,
    @ColumnInfo(name = "has_pages") val hasPages: Boolean?,
    @ColumnInfo(name = "has_projects") val hasProjects: Boolean?,
    @ColumnInfo(name = "has_wiki") val hasWiki: Boolean?,
    @ColumnInfo(name = "hooks_url") val hooksUrl: String?,
    @ColumnInfo(name = "html_url") val htmlUrl: String?,
    @ColumnInfo(name = "issue_comment_url") val issueCommentUrl: String?,
    @ColumnInfo(name = "issue_events_url") val issueEventsUrl: String?,
    @ColumnInfo(name = "issues_url") val issuesUrl: String?,
    @ColumnInfo(name = "languages_url") val languagesUrl: String?,
    @ColumnInfo(name = "merges_url") val mergesUrl: String?,
    @ColumnInfo(name = "milestones_url") val milestonesUrl: String?,
    @ColumnInfo(name = "node_id") val nodeId: String?,
    @ColumnInfo(name = "notifications_url") val notificationsUrl: String?,
    @ColumnInfo(name = "open_issues") val openIssues: Int?,
    @ColumnInfo(name = "open_issues_count") val openIssuesCount: Int?,
    @Embedded(prefix = "test_") val owner: OwnerCache, //TODO change to relationship? https://developer.android.com/training/data-storage/room/relationships
    @ColumnInfo(name = "pulls_url") val pullsUrl: String?,
    @ColumnInfo(name = "pushed_at") val pushedAt: String?,
    @ColumnInfo(name = "releases_url") val releasesUrl: String?,
    @ColumnInfo(name = "teams_url") val teamsUrl: String?,
    @ColumnInfo(name = "trees_url") val treesUrl: String?,
    @ColumnInfo(name = "updated_at") val updatedAt: String?,
    @ColumnInfo(name = "watchers_count") val watchersCount: Int?,
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