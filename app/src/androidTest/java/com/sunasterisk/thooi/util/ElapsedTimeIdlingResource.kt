package com.sunasterisk.thooi.util

import androidx.test.espresso.IdlingPolicies
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import java.util.concurrent.TimeUnit

/**
 * Rewritten by Cong Vu Chi on 06/09/20 21:26.
 * Disclaimer: https://stackoverflow.com/questions/30155227/espresso-how-to-wait-for-some-time1-hour
 */
class ElapsedTimeIdlingResource(private val waitingTime: Long) : IdlingResource {
    private val startTime = System.currentTimeMillis()

    private val idlingCallbacks = mutableListOf<IdlingResource.ResourceCallback>()

    override fun getName() = "${ElapsedTimeIdlingResource::class.java.simpleName}:$waitingTime"

    override fun isIdleNow(): Boolean {
        val elapsed = System.currentTimeMillis() - startTime
        val idle = elapsed >= waitingTime
        if (idle) idlingCallbacks.forEach { it.onTransitionToIdle() }
        return idle
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback) {
        idlingCallbacks.add(callback)
    }
}

fun idleTestThenRun(waitingTimeMillis: Long = 10_000L, action: () -> Unit) {

    // Make sure Espresso does not time out
    IdlingPolicies.setMasterPolicyTimeout(waitingTimeMillis * 2, TimeUnit.MILLISECONDS)
    IdlingPolicies.setIdlingResourceTimeout(waitingTimeMillis * 2, TimeUnit.MILLISECONDS)

    // Now we wait
    val idlingResource: IdlingResource = ElapsedTimeIdlingResource(waitingTimeMillis)
    IdlingRegistry.getInstance().register(idlingResource)
    action()
    IdlingRegistry.getInstance().unregister(idlingResource)
}
