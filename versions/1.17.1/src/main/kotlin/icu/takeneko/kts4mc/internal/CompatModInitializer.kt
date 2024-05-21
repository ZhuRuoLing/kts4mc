package icu.takeneko.kts4mc.internal

import icu.takeneko.kts4mc.Mod
import icu.takeneko.kts4mc.compat.logging.LoggerCompat
import net.fabricmc.api.ModInitializer

class CompatModInitializer : ModInitializer {
    private val modInstance = Mod()

    override fun onInitialize() {
        LoggerCompat.init(Slf4jLoggerImpl())
        modInstance.onInitialize()
    }
}