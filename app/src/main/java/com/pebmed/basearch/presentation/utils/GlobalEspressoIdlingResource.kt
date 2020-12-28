package com.pebmed.basearch.presentation.utils

import androidx.test.espresso.idling.CountingIdlingResource

object GlobalEspressoIdlingResource {
    private const val RESOURCE = "GLOBAL"

    @JvmField val countingIdlingResource = CountingIdlingResource(RESOURCE)

    fun increment() {
        countingIdlingResource.increment()
    }

    fun decrement() {
        if (!countingIdlingResource.isIdleNow) {
            countingIdlingResource.decrement()
        }
    }
}