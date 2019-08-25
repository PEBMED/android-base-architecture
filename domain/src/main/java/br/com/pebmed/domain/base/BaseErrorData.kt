package com.example.basearch.data.remote

data class BaseErrorData<ERROR_BODY>(
    val errorBody: ERROR_BODY? = null,
    val errorMessage: String? = null
)