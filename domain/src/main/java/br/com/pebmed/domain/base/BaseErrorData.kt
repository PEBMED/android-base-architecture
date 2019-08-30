package br.com.pebmed.domain.base

data class BaseErrorData<ERROR_BODY>(
    val errorBody: ERROR_BODY? = null,
    val errorMessage: String? = null
)