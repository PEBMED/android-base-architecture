package br.com.pebmed.domain.base

data class PaginationData(
        val nextPage: Int,
        val hasNextPage: Boolean
) {
    companion object {
        const val PAGE_START = 1
        const val PER_PAGE = 5
    }
}