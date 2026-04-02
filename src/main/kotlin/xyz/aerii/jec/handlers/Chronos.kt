@file:Suppress("UNUSED")

package xyz.aerii.jec.handlers

import kotlinx.atomicfu.atomic
import xyz.aerii.jec.annotations.Load
import xyz.aerii.jec.utils.safely
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit
import kotlin.time.Duration

@Load
object Chronos {
    object Time : TimeScheduler()

    interface Task {
        val active: Boolean
        fun cancel()
    }

    abstract class TimeScheduler internal constructor() {
        private val executor = Executors.newScheduledThreadPool(2) { Thread(it, "Chronos-Time").apply { isDaemon = true } }
        private val tasks = ConcurrentHashMap<String, Pair<ScheduledFuture<*>, TaskImpl>>()
        private var nextId = atomic(0L)

        infix fun after(duration: Duration) = TimeScheduleBuilder(duration)

        infix fun every(duration: Duration) = TimeRepeatBuilder(duration)

        private fun scheduleOnce(delay: Duration, action: () -> Unit): Task {
            val id = "time_once_${nextId.incrementAndGet()}"
            val task = TaskImpl { tasks.remove(id)?.first?.cancel(false) }

            val t = executor.schedule(
                {
                    safely { action() }
                    tasks.remove(id)
                },
                delay.inWholeMilliseconds,
                TimeUnit.MILLISECONDS
            )

            tasks[id] = t to task
            return task
        }

        private fun scheduleRepeating(interval: Duration, initialDelay: Duration, action: () -> Unit): Task {
            val id = "time_recurring_${nextId.incrementAndGet()}"
            val task = TaskImpl { tasks.remove(id)?.first?.cancel(false) }

            val future = executor.scheduleAtFixedRate(
                { safely { action() } },
                initialDelay.inWholeMilliseconds,
                interval.inWholeMilliseconds,
                TimeUnit.MILLISECONDS
            )

            tasks[id] = future to task
            return task
        }

        inner class TimeScheduleBuilder(private val delay: Duration) {
            infix fun then(action: () -> Unit) = scheduleOnce(delay, action)
        }

        inner class TimeRepeatBuilder(private val interval: Duration) {
            private var startDelay = interval

            infix fun after(duration: Duration) = apply { startDelay = duration }

            infix fun repeat(action: () -> Unit) = scheduleRepeating(interval, startDelay, action)
        }
    }

    private class TaskImpl(private val onCancel: () -> Unit) : Task {
        private val cancelled = atomic(false)

        override val active get() = !cancelled.value

        override fun cancel() = if (cancelled.compareAndSet(expect = false, update = true)) onCancel() else {}
    }
}