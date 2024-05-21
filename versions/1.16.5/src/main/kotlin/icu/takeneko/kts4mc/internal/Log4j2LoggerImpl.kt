package icu.takeneko.kts4mc.internal

import icu.takeneko.kts4mc.compat.logging.Logger
import org.apache.logging.log4j.LogManager

class Log4j2LoggerImpl : Logger {
    private val logger = LogManager.getLogger("kts4mc")

    override fun info(info: String) {
        logger.info(info)
    }

    override fun error(info: String) {
        logger.error(info)
    }

    override fun warning(info: String) {
        logger.warn(info)
    }
}