package br.com.pebmed.domain.base

/**
 * @Descrição: PaginationData armazena as informações de paginação de determinado conteudo e também
 * possui as diretrizes de primeira página e limite de conteúdos por página.
 */
data class PaginationData(
        val nextPage: Int,
        val hasNextPage: Boolean
) {
    companion object {
        const val PAGE_START = 1
        const val PER_PAGE = 5
    }
}