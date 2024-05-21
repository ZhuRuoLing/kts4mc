package icu.takeneko.kts4mc.compat.logging

object LoggerCompat {
    private lateinit var logger: Logger

    fun getLogger(): Logger {
        return logger
    }

    fun init(logger: Logger) {
        this.logger = logger
    }
}