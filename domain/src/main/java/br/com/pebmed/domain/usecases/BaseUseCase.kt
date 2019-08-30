package br.com.pebmed.domain.usecases

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

abstract class BaseUseCase<SUCCESS, PARAMS> {

    abstract suspend fun run(params: PARAMS): SUCCESS

    open operator fun invoke(
        scope: CoroutineScope,
        params: PARAMS,
        onResult: (SUCCESS) -> Unit = {}
    ) {
        val backgroundJob = scope.async { run(params) }
        scope.launch { onResult(backgroundJob.await()) }
    }
}