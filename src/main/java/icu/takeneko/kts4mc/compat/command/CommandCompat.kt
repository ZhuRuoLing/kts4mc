package icu.takeneko.kts4mc.compat.command

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.minecraft.server.command.ServerCommandSource

object CommandCompat {
    private var dispatcher: CommandDispatcher<ServerCommandSource>? = null

    private val delayedCalls: MutableList<LiteralArgumentBuilder<ServerCommandSource>> = mutableListOf()

    fun register(command: LiteralArgumentBuilder<ServerCommandSource>) {
        if (dispatcher == null){
            delayedCalls += command
        }else{
            dispatcher!!.register(command)
        }
    }

    internal fun init(dispatcher: CommandDispatcher<ServerCommandSource>) {
        this.dispatcher = dispatcher
        delayedCalls.forEach {
            register(it)
        }
    }
}