/*
 * This file is part of the KotlinScript4Minecraft project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024  Fallen_Breath and contributors
 *
 * KotlinScript4Minecraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * KotlinScript4Minecraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with KotlinScript4Minecraft.  If not, see <https://www.gnu.org/licenses/>.
 */

package icu.takeneko.kts4mc.mixins;

import com.mojang.brigadier.CommandDispatcher;
import icu.takeneko.kts4mc.compat.command.CommandCompat;
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
        CommandCompat.INSTANCE.init$kts4mc(dispatcher);
    }
}
