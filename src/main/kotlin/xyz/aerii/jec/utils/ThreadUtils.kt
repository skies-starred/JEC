@file:Suppress("Unused")

package xyz.aerii.jec.utils

import xyz.aerii.jec.JEC

@JvmName($$"safely$noParam")
inline fun safely(
    log: Boolean = false,
    fn: () -> Unit
) = safely<Exception>(log) { fn() }

@JvmName($$"safely$one")
inline fun <reified E : Throwable> safely(
    log: Boolean = false,
    fn: () -> Any?
) {
    try {
        fn()
    } catch (e: Throwable) {
        if (e !is E) throw e
        if (log) JEC.LOGGER.warn("Caught error while running method!", e)
    }
}

@JvmName($$"safely$two")
inline fun <reified E1 : Throwable, reified E2 : Throwable> safely(
    log: Boolean = false,
    fn: () -> Any?
) {
    try {
        fn()
    } catch (e: Throwable) {
        if (e !is E1 && e !is E2) throw e
        if (log) JEC.LOGGER.warn("Caught error while running method!", e)
    }
}

@JvmName($$"safely$three")
inline fun <reified E1 : Throwable, reified E2 : Throwable, reified E3 : Throwable> safely(
    log: Boolean = false,
    fn: () -> Any?
) {
    try {
        fn()
    } catch (e: Throwable) {
        if (e !is E1 && e !is E2 && e !is E3) throw e
        if (log) JEC.LOGGER.warn("Caught error while running method!", e)
    }
}