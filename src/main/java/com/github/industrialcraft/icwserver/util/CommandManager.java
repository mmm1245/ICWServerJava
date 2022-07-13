package com.github.industrialcraft.icwserver.util;

import com.github.industrialcraft.icwserver.world.entity.Entity;
import com.github.industrialcraft.icwserver.world.entity.PlayerEntity;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.ArgumentCommandNode;

import static com.mojang.brigadier.arguments.IntegerArgumentType.getInteger;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static com.mojang.brigadier.builder.LiteralArgumentBuilder.literal;
import static com.mojang.brigadier.builder.RequiredArgumentBuilder.argument;

public class CommandManager {
    private CommandDispatcher<PlayerEntity> dispatcher;
    public CommandManager() {
        this.dispatcher = new CommandDispatcher<>();
        initCommands();
    }
    public void initCommands(){
        this.dispatcher.register(LiteralArgumentBuilder.<PlayerEntity>literal("getloc")
            .then(RequiredArgumentBuilder.<PlayerEntity,Integer>argument("entityId", integer()).executes(context -> {
                Entity entity = context.getSource().getServer().entityById(getInteger(context, "entityId"));
                if(entity != null)
                    context.getSource().sendChatMessage(String.format("%s has position x:%s y:%s w:%s", entity.id, entity.getLocation().x(), entity.getLocation().y(), entity.getLocation().world().getId()));
                else
                    context.getSource().sendChatMessage("Entity now found");
                return 1;
            })).executes(context -> {
                context.getSource().sendChatMessage(String.format("Your position is x:%s y:%s w:%s", context.getSource().getLocation().x(), context.getSource().getLocation().y(), context.getSource().getLocation().world().getId()));
                return 1;
            })
        );
    }
    public void execute(PlayerEntity player, String text){
        try {
            dispatcher.execute(text, player);
        } catch (CommandSyntaxException e) {
            player.sendChatMessage(e.getMessage());
        }
    }
}
