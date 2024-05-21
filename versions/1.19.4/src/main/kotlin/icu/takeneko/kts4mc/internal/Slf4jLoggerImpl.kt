package icu.takeneko.kts4mc.internal

import icu.takeneko.kts4mc.compat.logging.Logger
import org.slf4j.LoggerFactory


class Slf4jLoggerImpl : Logger {
    private val logger = LoggerFactory.getLogger("kts4mc")

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