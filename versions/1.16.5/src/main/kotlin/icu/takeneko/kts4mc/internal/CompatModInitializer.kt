package icu.takeneko.kts4mc.internal

import icu.takeneko.kts4mc.Mod
import icu.takeneko.kts4mc.compat.logging.LoggerCompat
import icu.takeneko.kts4mc.compat.text.Text
import net.fabricmc.api.ModInitializer

class CompatModInitializer : ModInitializer {
    override fun onInitialize() {
        LoggerCompat.init(Log4j2LoggerImpl())
        Text.init(TextImpl())
        Mod().onInitialize()
    }
}