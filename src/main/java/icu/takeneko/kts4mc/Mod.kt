package icu.takeneko.kts4mc

import icu.takeneko.kts4mc.command.command
import icu.takeneko.kts4mc.compat.command.CommandCompat
import icu.takeneko.kts4mc.compat.logging.LoggerCompat
import icu.takeneko.kts4mc.file.Data
import icu.takeneko.kts4mc.scripting.ScriptManager
import net.fabricmc.api.ModInitializer

class Mod: ModInitializer {
    val logger = LoggerCompat.getLogger()

    override fun onInitialize() {
        Data.init()
        ScriptManager.init()
        CommandCompat.register(command)
    }
}