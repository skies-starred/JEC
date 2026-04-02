package xyz.aerii.jec.annotations

/**
 * Marks a class to be automatically loaded on startup.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Load