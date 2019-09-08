package br.com.pebmed.domain.usecases

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

abstract class BaseUseCase<RESULT, PARAMS> {

    abstract suspend fun run(params: PARAMS): RESULT

    open operator fun invoke(
        scope: CoroutineScope,
        params: PARAMS,
        onResult: (RESULT) -> Unit = {}
    ) {
        val backgroundJob = scope.async { run(params) }
        scope.launch { onResult(backgroundJob.await()) }
    }
}