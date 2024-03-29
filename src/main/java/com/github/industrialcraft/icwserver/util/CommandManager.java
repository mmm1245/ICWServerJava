package com.github.industrialcraft.icwserver.util;

import com.github.industrialcraft.icwserver.inventory.Item;
import com.github.industrialcraft.icwserver.net.messages.ChatMessage;
import com.github.industrialcraft.icwserver.script.*;
import com.github.industrialcraft.icwserver.world.World;
import com.github.industrialcraft.icwserver.world.entity.Entity;
import com.github.industrialcraft.icwserver.world.entity.PlayerEntity;
import com.github.industrialcraft.icwserver.world.entity.StatusEffect;
import com.github.industrialcraft.icwserver.world.entity.js.EntityFromJS;
import com.github.industrialcraft.inventorysystem.ItemStack;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import org.openjdk.nashorn.api.scripting.ScriptObjectMirror;

import javax.script.ScriptException;

import java.util.function.BiFunction;
import java.util.function.Function;

import static com.mojang.brigadier.arguments.FloatArgumentType.floatArg;
import static com.mojang.brigadier.arguments.FloatArgumentType.getFloat;
import static com.mojang.brigadier.arguments.IntegerArgumentType.getInteger;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static com.mojang.brigadier.arguments.StringArgumentType.*;
import static com.mojang.brigadier.builder.RequiredArgumentBuilder.argument;

public class CommandManager {
    private CommandDispatcher<JSPlayer> dispatcher;
    public CommandManager() {
        this.dispatcher = new CommandDispatcher<>();
        initCommands();
    }
    private void initCommands(){
        this.dispatcher.register(LiteralArgumentBuilder.<JSPlayer>literal("getloc")
            .then(RequiredArgumentBuilder.<JSPlayer,Integer>argument("entityId", integer()).executes(context -> {
                Entity entity = context.getSource().getInternal().getServer().entityById(getInteger(context, "entityId"));
                if(entity != null)
                    context.getSource().sendChatMessage(String.format("%s has position x:%s y:%s w:%s", entity.id, entity.getLocation().x(), entity.getLocation().y(), entity.getLocation().world().getId()));
                else
                    context.getSource().sendChatMessage("Entity not found");
                return 1;
            })).executes(context -> {
                context.getSource().sendChatMessage(String.format("Your position is x:%s y:%s w:%s", context.getSource().location().x(), context.getSource().location().y(), context.getSource().location().world().getInternal().getId()));
                return 1;
            })
        );
        this.dispatcher.register(LiteralArgumentBuilder.<JSPlayer>literal("playerid")
                .then(RequiredArgumentBuilder.<JSPlayer,String>argument("name", string()).executes(context -> {
                    PlayerEntity player = context.getSource().getInternal().getServer().playerByName(getString(context, "name"));
                    if(player != null)
                        context.getSource().sendChatMessage(String.format("%s has id %s", player.getConnection().profile.name(), player.id));
                    else
                        context.getSource().sendChatMessage("Player not found");
                    return 1;
                })).executes(context -> {
                    context.getSource().sendChatMessage(String.format("Your id is %s", context.getSource().getInternal().id));
                    return 1;
                })
        );
        this.dispatcher.register(LiteralArgumentBuilder.<JSPlayer>literal("tp")
                .then(RequiredArgumentBuilder.<JSPlayer,Integer>argument("id", integer())
                .then(RequiredArgumentBuilder.<JSPlayer,Integer>argument("x", integer())
                .then(RequiredArgumentBuilder.<JSPlayer,Integer>argument("y", integer())
                .then(RequiredArgumentBuilder.<JSPlayer,Integer>argument("w", integer())
                .executes(context -> {
                    Entity entity = context.getSource().getInternal().getServer().entityById(getInteger(context, "id"));
                    if(entity == null){
                        context.getSource().sendChatMessage("Entity not found");
                        return 1;
                    }
                    World world = context.getSource().getInternal().getServer().worldById(getInteger(context, "w"));
                    if(world == null){
                        context.getSource().sendChatMessage("World not found");
                        return 1;
                    }
                    entity.teleport(new Location(getInteger(context, "x"), getInteger(context, "y"), world));
                    return 1;
                })))))
        );
        this.dispatcher.register(LiteralArgumentBuilder.<JSPlayer>literal("tick")
                .then(LiteralArgumentBuilder.<JSPlayer>literal("freeze").executes(context -> {
                    context.getSource().getInternal().getServer().getTickManager().toggleFreeze();
                    if(context.getSource().getInternal().getServer().getTickManager().isFrozen()){
                        context.getSource().getInternal().getServer().getWSServer().broadcast(new ChatMessage("Game frozen"));
                    } else {
                        context.getSource().getInternal().getServer().getWSServer().broadcast(new ChatMessage("Game unfrozen"));
                    }
                    return 1;
                }))
                .then(LiteralArgumentBuilder.<JSPlayer>literal("warp")
                .then(RequiredArgumentBuilder.<JSPlayer,Integer>argument("time", integer()).executes(context -> {
                    context.getSource().getInternal().getServer().getTickManager().setWarpTime(getInteger(context, "time"));
                    context.getSource().getInternal().getServer().getWSServer().broadcast(new ChatMessage(String.format("Tick warping %s ticks", getInteger(context, "time"))));
                    return 1;
                })))
        );
        this.dispatcher.register(LiteralArgumentBuilder.<JSPlayer>literal("kill")
                .then(RequiredArgumentBuilder.<JSPlayer,Integer>argument("id", integer()).executes(context -> {
                    Entity entity = context.getSource().getInternal().getServer().entityById(getInteger(context, "id"));
                    if(entity == null){
                        context.getSource().sendChatMessage("Entity not found");
                    } else {
                        entity.kill();
                        context.getSource().sendChatMessage("Entity killed");
                    }
                    return 1;
                }))
        );
        this.dispatcher.register(LiteralArgumentBuilder.<JSPlayer>literal("createworld")
            .then(RequiredArgumentBuilder.<JSPlayer,String>argument("orientation", string()).executes(context -> {
                EWorldOrientation worldOrientation = EWorldOrientation.byName(getString(context, "orientation"));
                if(worldOrientation == null){
                    context.getSource().sendChatMessage("Orientation not valid");
                    return 1;
                }
                World world = context.getSource().getInternal().getServer().createWorld(worldOrientation);
                context.getSource().sendChatMessage(String.format("World created with id %s", world.getId()));
                return 1;
        })));
        this.dispatcher.register(LiteralArgumentBuilder.<JSPlayer>literal("killworld")
            .then(RequiredArgumentBuilder.<JSPlayer,Integer>argument("id", integer()).executes(context -> {
                World world = context.getSource().getInternal().getServer().worldById(getInteger(context, "id"));
                if(world == null){
                    context.getSource().sendChatMessage("World not found");
                } else {
                    if(world.remove()) {
                        context.getSource().sendChatMessage(String.format("World with id %s removed", world.getId()));
                    } else {
                        context.getSource().sendChatMessage("Unable to remove world(lobbies cannot be removed)");
                    }
                }
                return 1;
        })));
        this.dispatcher.register(LiteralArgumentBuilder.<JSPlayer>literal("spawn")
                .then(RequiredArgumentBuilder.<JSPlayer,Integer>argument("x", integer())
                .then(RequiredArgumentBuilder.<JSPlayer,Integer>argument("y", integer())
                .then(RequiredArgumentBuilder.<JSPlayer,Integer>argument("w", integer())
                .then(RequiredArgumentBuilder.<JSPlayer,String>argument("type", word())
                .then(RequiredArgumentBuilder.<JSPlayer,String>argument("data", greedyString())
                .executes(context -> {
                    ScriptingManager scriptingManager = context.getSource().getInternal().getServer().getScriptingManager();
                    JSEntityData entityData = scriptingManager.entityRegistry.getEntities().get(getString(context, "type"));
                    if(entityData == null){
                        context.getSource().sendChatMessage("Entity type not found");
                        return 1;
                    }
                    World world = context.getSource().getInternal().getServer().worldById(getInteger(context, "w"));
                    if(world == null){
                        context.getSource().sendChatMessage("World not found");
                        return 1;
                    }
                    try {
                        Object data = scriptingManager.getEngine().eval("(" + getString(context, "data") + ")");
                        entityData.spawn(new JSLocation(getInteger(context, "x"), getInteger(context, "y"), world), data);
                    } catch (ScriptException e) {
                        context.getSource().sendChatMessage("Invalid data for entity spawn: " + e.getMessage());
                    }
                    return 1;
            }))))))
        );
        this.dispatcher.register(LiteralArgumentBuilder.<JSPlayer>literal("effect")
            .then(RequiredArgumentBuilder.<JSPlayer,Integer>argument("id", integer())
            .then(RequiredArgumentBuilder.<JSPlayer,String>argument("type", string())
            .then(RequiredArgumentBuilder.<JSPlayer,Integer>argument("time", integer())
            .then(RequiredArgumentBuilder.<JSPlayer,String>argument("data", greedyString()).executes(context -> {
                Entity entity = context.getSource().getInternal().getServer().entityById(getInteger(context, "id"));
                ScriptingManager scriptingManager = context.getSource().getInternal().getServer().getScriptingManager();
                if(entity == null){
                    context.getSource().sendChatMessage("Entity not found");
                    return 1;
                }
                JSStatusEffectData statusEffectData = scriptingManager.statusEffectRegistry.getStatusEffects().get(getString(context, "type"));
                if(statusEffectData == null){
                    context.getSource().sendChatMessage("Status effect not found");
                    return 1;
                }
                StatusEffect statusEffect = entity.addStatusEffect(statusEffectData, getInteger(context, "time"));
                try {
                    statusEffect.data = scriptingManager.getEngine().eval("(" + getString(context, "data") + ")");
                } catch (ScriptException e) {
                    context.getSource().sendChatMessage("Status effect data manipulation failed: " + e.getMessage());
                }
                return 1;
        }))))));
        this.dispatcher.register(LiteralArgumentBuilder.<JSPlayer>literal("soundeffect")
                .then(RequiredArgumentBuilder.<JSPlayer,String>argument("id", string())
                .then(RequiredArgumentBuilder.<JSPlayer,Integer>argument("x", integer())
                .then(RequiredArgumentBuilder.<JSPlayer,Integer>argument("y", integer())
                .then(RequiredArgumentBuilder.<JSPlayer,Integer>argument("w", integer()).executes(context -> {
                    ScriptingManager scriptingManager = context.getSource().getInternal().getServer().getScriptingManager();
                    JSSoundEffectRegistry.SoundEffect soundEffect = scriptingManager.soundEffectRegistry.getSoundEffects().get(getString(context, "id"));
                    if(soundEffect == null){
                        context.getSource().sendChatMessage("Sound effect not found");
                        return 1;
                    }
                    World world = context.getSource().getInternal().getServer().worldById(getInteger(context, "w"));
                    if(world == null){
                        context.getSource().sendChatMessage("World not found");
                        return 1;
                    }
                    world.playSoundEffect(soundEffect, getInteger(context, "x"), getInteger(context, "y"));
                    return 1;
                }))))));
        this.dispatcher.register(LiteralArgumentBuilder.<JSPlayer>literal("ride")
                .then(RequiredArgumentBuilder.<JSPlayer,Integer>argument("ridden", integer())
                .then(RequiredArgumentBuilder.<JSPlayer,Integer>argument("passenger", integer()).executes(context -> {
                    Entity ridden = context.getSource().getInternal().getServer().entityById(getInteger(context, "ridden"));
                    if(ridden == null){
                        context.getSource().sendChatMessage("Ridden entity not found");
                        return 1;
                    }
                    Entity passenger = context.getSource().getInternal().getServer().entityById(getInteger(context, "passenger"));
                    if(passenger == null){
                        context.getSource().sendChatMessage("Passenger entity not found");
                        return 1;
                    }
                    if(!ridden.trySetPassenger(passenger)){
                        context.getSource().sendChatMessage("Couldnt make passenger ride");
                        return 1;
                    }
                    return 1;
                }))));
        this.dispatcher.register(LiteralArgumentBuilder.<JSPlayer>literal("getdata")
                .then(RequiredArgumentBuilder.<JSPlayer,Integer>argument("id", integer()).executes(context -> {
                    Entity entity = context.getSource().getInternal().getServer().entityById(getInteger(context, "id"));
                    ScriptingManager scriptingManager = context.getSource().getInternal().getServer().getScriptingManager();
                    if(entity == null) {
                        context.getSource().sendChatMessage("Entity not found");
                        return 1;
                    }
                    context.getSource().sendChatMessage(scriptingManager.tryJsonToString(entity.data));
                    return 1;
        })));
        this.dispatcher.register(LiteralArgumentBuilder.<JSPlayer>literal("data")
            .then(RequiredArgumentBuilder.<JSPlayer,Integer>argument("id", integer())
            .then(RequiredArgumentBuilder.<JSPlayer,String>argument("data", greedyString()).executes(context -> {
                Entity entity = context.getSource().getInternal().getServer().entityById(getInteger(context, "id"));
                ScriptingManager scriptingManager = context.getSource().getInternal().getServer().getScriptingManager();
                if(entity != null) {
                    if(entity instanceof EntityFromJS entityFromJS) {
                        try {
                            Object data = scriptingManager.getEngine().eval("(" + getString(context, "data") + ")");
                            if(!entityFromJS.modData(data)){
                                context.getSource().sendChatMessage("Couldnt modify entity");
                            }
                        } catch (ScriptException e) {
                            context.getSource().sendChatMessage("Invalid data for entity modification: " + e.getMessage());
                        }
                    } else {
                        context.getSource().sendChatMessage("You can only manipulate data belonging to EntityFromJS");
                    }
                } else {
                    context.getSource().sendChatMessage("Entity not found");
                }
                return 1;
        }))));
        this.dispatcher.register(LiteralArgumentBuilder.<JSPlayer>literal("clone")
            .then(RequiredArgumentBuilder.<JSPlayer,Integer>argument("id", integer())
            .then(RequiredArgumentBuilder.<JSPlayer,Integer>argument("x", integer())
            .then(RequiredArgumentBuilder.<JSPlayer,Integer>argument("y", integer())
            .then(RequiredArgumentBuilder.<JSPlayer,Integer>argument("w", integer())
            .executes(context -> {
                Entity entity = context.getSource().getInternal().getServer().entityById(getInteger(context, "id"));
                if(entity == null){
                    context.getSource().sendChatMessage("Entity not found");
                    return 1;
                }
                World world = context.getSource().getInternal().getServer().worldById(getInteger(context, "w"));
                if(world == null){
                    context.getSource().sendChatMessage("World not found");
                    return 1;
                }
                try {
                    entity.clone(new Location(getInteger(context, "x"), getInteger(context, "y"), world));
                } catch (Exception e){
                    context.getSource().sendChatMessage("Couldnt clone: " + e.getMessage());
                }
                return 1;
            })))))
        );
        this.dispatcher.register(LiteralArgumentBuilder.<JSPlayer>literal("knockback")
                .then(RequiredArgumentBuilder.<JSPlayer,Integer>argument("id", integer())
                .then(RequiredArgumentBuilder.<JSPlayer,Float>argument("x", floatArg())
                .then(RequiredArgumentBuilder.<JSPlayer,Float>argument("y", floatArg())
                .executes(context -> {
                    Entity entity = context.getSource().getInternal().getServer().entityById(getInteger(context, "id"));
                    if(entity == null){
                        context.getSource().sendChatMessage("Entity not found");
                        return 1;
                    }
                    entity.applyKnockback(getFloat(context, "x"), getFloat(context, "y"));
                    return 1;
                }))))
        );
        this.dispatcher.register(LiteralArgumentBuilder.<JSPlayer>literal("explosion")
                .then(RequiredArgumentBuilder.<JSPlayer,Float>argument("x", floatArg())
                .then(RequiredArgumentBuilder.<JSPlayer,Float>argument("y", floatArg())
                .then(RequiredArgumentBuilder.<JSPlayer,Integer>argument("w", integer())
                .then(RequiredArgumentBuilder.<JSPlayer,Float>argument("power", floatArg())
                .then(RequiredArgumentBuilder.<JSPlayer,Float>argument("radius", floatArg())
                .executes(context -> {
                    World world = context.getSource().getInternal().getServer().worldById(getInteger(context, "w"));
                    if(world == null){
                        context.getSource().sendChatMessage("World not found");
                        return 1;
                    }
                    world.spawnExplosion(getFloat(context, "x"), getFloat(context, "y"), getFloat(context, "power"), getFloat(context, "radius"));
                    return 1;
                }))))))
        );
        this.dispatcher.register(LiteralArgumentBuilder.<JSPlayer>literal("give")
                .then(RequiredArgumentBuilder.<JSPlayer,Integer>argument("id", integer())
                .then(RequiredArgumentBuilder.<JSPlayer,String>argument("item", string())
                .then(RequiredArgumentBuilder.<JSPlayer,Integer>argument("count", integer())
                .executes(context -> {
                    Entity entity = context.getSource().getInternal().getServer().entityById(getInteger(context, "id"));
                    if(entity == null){
                        context.getSource().sendChatMessage("Entity not found");
                        return 1;
                    }
                    if(entity.getInventory() == null){
                        context.getSource().sendChatMessage("That entity does not have inventory");
                        return 1;
                    }
                    Item item = context.getSource().getInternal().getServer().getScriptingManager().itemRegistry.getItems().get(getString(context, "item"));
                    if(item == null){
                        context.getSource().sendChatMessage("Item not found");
                        return 1;
                    }
                    int count = getInteger(context, "count");
                    if(count < 0 || count > item.getStackSize()){
                        context.getSource().sendChatMessage("Item count not within 0.." + item.getStackSize());
                        return 1;
                    }
                    entity.getInventory().addItem(new ItemStack(item, count));
                    return 1;
                }))))
        );
    }
    public void register(String name, ScriptObjectMirror cmd, EArgumentType... types){
        RequiredArgumentBuilder requiredArgumentBuilder = null;
        if(types.length > 0) {
            requiredArgumentBuilder = types[types.length - 1].creator.apply(types.length - 1);
            requiredArgumentBuilder.executes(context -> {
                Object[] args = new Object[types.length];
                for(int i = 0;i < types.length;i++){
                    args[i] = types[i].getter.apply(context,i);
                }
                cmd.call(context.getSource(), args);
                return 1;
            });
            for (int i = types.length - 2; i >= 0; i--) {
                RequiredArgumentBuilder argumentBuilder = types[i].creator.apply(i);
                argumentBuilder.then(requiredArgumentBuilder);
                requiredArgumentBuilder = argumentBuilder;
            }
        }
        LiteralArgumentBuilder literalArgumentBuilder = LiteralArgumentBuilder.<JSPlayer>literal(name);
        if(requiredArgumentBuilder != null)
            literalArgumentBuilder.then(requiredArgumentBuilder);
        else {
            literalArgumentBuilder.executes(context -> {
                Object[] args = new Object[types.length];
                for(int i = 0;i < types.length;i++){
                    args[i] = types[i].getter.apply(context,i);
                }
                cmd.call(context.getSource(), args);
                return 1;
            });
        }
        this.dispatcher.register(literalArgumentBuilder);
        /*LiteralArgumentBuilder literalArgumentBuilder = LiteralArgumentBuilder.<JSPlayer>literal(name);
        ArgumentBuilder argumentBuilder = literalArgumentBuilder;
        for(int i = 0;i < types.length;i++){
            RequiredArgumentBuilder requiredArgumentBuilder = types[i].creator.apply(i);
            argumentBuilder.then(requiredArgumentBuilder);
            argumentBuilder = requiredArgumentBuilder;
        }*/


    }
    public enum EArgumentType {
        INTEGER((id)-> argument("arg"+id, integer()), (context, id)->getInteger(context,"arg"+id)),
        FLOAT((id)-> argument("arg"+id, floatArg()), (context, id)->getFloat(context,"arg"+id)),
        WORD((id)-> argument("arg"+id, word()), (context, id)->getString(context,"arg"+id)),
        STRING((id)-> argument("arg"+id, string()), (context, id)->getString(context,"arg"+id)),
        GREEDY_STRING((id)->argument("arg"+id, greedyString()), (context,id)->getString(context,"arg"+id));
        public final Function<Integer,RequiredArgumentBuilder> creator;
        public final BiFunction<CommandContext,Integer,Object> getter;
        EArgumentType(Function<Integer, RequiredArgumentBuilder> creator, BiFunction<CommandContext,Integer, Object> getter) {
            this.creator = creator;
            this.getter = getter;
        }
    }
    public void execute(JSPlayer player, String text){
        try {
            dispatcher.execute(text, player);
        } catch (CommandSyntaxException e) {
            player.sendChatMessage(e.getMessage());
        }
    }
}
