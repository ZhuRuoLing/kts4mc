package icu.takeneko.kts4mc.mixins;

import com.mojang.brigadier.CommandDispatcher;
import icu.takeneko.kts4mc.command.ModCommandKt;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CommandManager.class)
public class CommandManagerMixin {
    @Shadow @Final private CommandDispatcher<ServerCommandSource> dispatcher;

    @Inject(method = "<init>", at = @At("RETURN"))
    void onCommandRegister(
            //#if MC < 11520
            //$$ boolean isDedicatedServer, CallbackInfo ci
            //#elseif MC < 11820
            //$$ CommandManager.RegistrationEnvironment environment, CallbackInfo ci
            //#else
            CommandManager.RegistrationEnvironment environment,
            net.minecraft.command.CommandRegistryAccess commandRegistryAccess,
            CallbackInfo ci
            //#endif
    ) {
        dispatcher.register(ModCommandKt.getCommand());
    }
}
