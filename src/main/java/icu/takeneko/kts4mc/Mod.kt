package icu.takeneko.kts4mc

import icu.takeneko.kts4mc.compat.logging.LoggerCompat
import icu.takeneko.kts4mc.file.Data
import net.fabricmc.api.ModInitializer

class Mod: ModInitializer {
    val logger = LoggerCompat.getLogger()

    override fun onInitialize() {
        Data.init()
    }
}