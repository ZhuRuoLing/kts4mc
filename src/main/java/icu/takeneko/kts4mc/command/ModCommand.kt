package icu.takeneko.kts4mc.command

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import icu.takeneko.kts4mc.scripting.ScriptManager
import net.minecraft.server.command.CommandManager.argument
import net.minecraft.server.command.CommandManager.literal
import net.minecraft.server.command.ServerCommandSource

private val activateCommand =
    literal("activate").then(
        argument("script", StringArgumentType.greedyString())
            .suggests { ctx, builder ->
                ScriptManager.allFiles().forEach { builder.suggest(it) }
                builder.buildFuture()
            }
            .executes {
                activate(it.getArgument("script", String::class.java))
                1
            }
    )

private val deactivateCommand =
    literal("deactivate").then(
        argument("script", StringArgumentType.greedyString())
            .suggests { ctx, builder ->
                ScriptManager.allFiles().forEach { builder.suggest(it) }
                builder.buildFuture()
            }
            .executes {
                deactivate(it.getArgument("script", String::class.java))
                1
            }
    )

private val freezeCommand =
    literal("freeze").then(
        literal("permanently").executes {
            freeze(true)
            1
        }
    ).then(
        literal("auto").then(
            literal("enable").executes {
                ScriptManager.configureAutoFreezing(true)
                1
            }
        ).then(
            literal("disable").executes {
                ScriptManager.configureAutoFreezing(true)
                1
            }
        )
    ).executes {
        freeze(false)
        1
    }

val command: LiteralArgumentBuilder<ServerCommandSource> =
    literal("kts4mc")
        .requires { it.hasPermissionLevel(1) }
        .then(
            activateCommand
        ).then(
            deactivateCommand
        ).then(
            freezeCommand
        )

fun activate(name: String) {
    ScriptManager.activate(name)
}

fun deactivate(name: String) {
    ScriptManager.deactivate(name)
}

fun freeze(permanent: Boolean) {
    ScriptManager.freeze(permanent)
}
