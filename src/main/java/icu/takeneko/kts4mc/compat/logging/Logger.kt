package icu.takeneko.kts4mc.compat.logging

interface Logger {
    fun info(info: String)
    fun error(info: String)
    fun warning(info: String)
}