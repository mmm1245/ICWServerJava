package com.github.industrialcraft.icwserver.util;

import com.github.industrialcraft.icwserver.GameServer;
import com.github.industrialcraft.icwserver.net.messages.ChatMessage;
import com.github.industrialcraft.icwserver.world.World;
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
import static com.mojang.brigadier.arguments.StringArgumentType.string;
import static com.mojang.brigadier.arguments.StringArgumentType.getString;
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
                    context.getSource().sendChatMessage("Entity not found");
                return 1;
            })).executes(context -> {
                context.getSource().sendChatMessage(String.format("Your position is x:%s y:%s w:%s", context.getSource().getLocation().x(), context.getSource().getLocation().y(), context.getSource().getLocation().world().getId()));
                return 1;
            })
        );
        this.dispatcher.register(LiteralArgumentBuilder.<PlayerEntity>literal("playerid")
                .then(RequiredArgumentBuilder.<PlayerEntity,String>argument("name", string()).executes(context -> {
                    PlayerEntity player = context.getSource().getServer().playerByName(getString(context, "name"));
                    if(player != null)
                        context.getSource().sendChatMessage(String.format("%s has id %s", player.getConnection().profile.name(), player.id));
                    else
                        context.getSource().sendChatMessage("Player not found");
                    return 1;
                })).executes(context -> {
                    context.getSource().sendChatMessage(String.format("Your id is %s", context.getSource().id));
                    return 1;
                })
        );
        this.dispatcher.register(LiteralArgumentBuilder.<PlayerEntity>literal("tp")
                .then(RequiredArgumentBuilder.<PlayerEntity,Integer>argument("id", integer())
                .then(RequiredArgumentBuilder.<PlayerEntity,Integer>argument("x", integer())
                .then(RequiredArgumentBuilder.<PlayerEntity,Integer>argument("y", integer())
                .then(RequiredArgumentBuilder.<PlayerEntity,Integer>argument("w", integer())
                .executes(context -> {
                    Entity entity = context.getSource().getServer().entityById(getInteger(context, "id"));
                    if(entity == null){
                        context.getSource().sendChatMessage("Entity not found");
                        return 1;
                    }
                    World world = context.getSource().getServer().worldById(getInteger(context, "w"));
                    if(world == null){
                        context.getSource().sendChatMessage("World not found");
                        return 1;
                    }
                    entity.teleport(new Location(getInteger(context, "x"), getInteger(context, "y"), world));
                    return 1;
                })))))
        );
        this.dispatcher.register(LiteralArgumentBuilder.<PlayerEntity>literal("tick")
                .then(LiteralArgumentBuilder.<PlayerEntity>literal("freeze").executes(context -> {
                    context.getSource().getServer().getTickManager().toggleFreeze();
                    if(context.getSource().getServer().getTickManager().isFrozen()){
                        context.getSource().getServer().getWSServer().broadcast(new ChatMessage("Game frozen"));
                    } else {
                        context.getSource().getServer().getWSServer().broadcast(new ChatMessage("Game unfrozen"));
                    }
                    return 1;
                }))
                .then(LiteralArgumentBuilder.<PlayerEntity>literal("warp")
                .then(RequiredArgumentBuilder.<PlayerEntity,Integer>argument("time", integer()).executes(context -> {
                    context.getSource().getServer().getTickManager().setWarpTime(getInteger(context, "time"));
                    context.getSource().getServer().getWSServer().broadcast(new ChatMessage(String.format("Tick warping %s ticks", getInteger(context, "time"))));
                    return 1;
                })))
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
