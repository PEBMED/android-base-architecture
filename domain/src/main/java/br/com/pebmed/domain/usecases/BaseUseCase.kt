package com.example.basearch.domain.usecases

import br.com.pebmed.domain.base.ViewStateResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

abstract class BaseUseCase<SUCCESS, PARAMS> {

    abstract suspend fun run(params: PARAMS): ViewStateResource<SUCCESS>

    open operator fun invoke(
        scope: CoroutineScope,
        params: PARAMS,
        onResult: (ViewStateResource<SUCCESS>) -> Unit = {}
    ) {
        val backgroundJob = scope.async { run(params) }
        scope.launch { onResult(backgroundJob.await()) }
    }
}