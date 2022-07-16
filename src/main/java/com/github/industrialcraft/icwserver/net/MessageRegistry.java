package com.github.industrialcraft.icwserver.net;

import com.github.industrialcraft.icwserver.net.messages.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;

public class MessageRegistry {
    protected static HashMap<String,MessageCreator> messageRegistry;
    static{
        MessageRegistry.messageRegistry = new HashMap<>();
        MessageRegistry.messageRegistry.put(LoginMessage.TYPE, stream -> new LoginMessage(stream));
        MessageRegistry.messageRegistry.put(ClientPlayerPositionMessage.TYPE, stream -> new ClientPlayerPositionMessage(stream));
        MessageRegistry.messageRegistry.put(PlayerAttackMessage.TYPE, stream -> new PlayerAttackMessage(stream));
        MessageRegistry.messageRegistry.put(InteractEntityMessage.TYPE, stream -> new InteractEntityMessage(stream));
        MessageRegistry.messageRegistry.put(PlayerClickSlotMessage.TYPE, stream -> new PlayerClickSlotMessage(stream));
        MessageRegistry.messageRegistry.put(OpenedInventoryActionMessage.TYPE, stream -> new OpenedInventoryActionMessage(stream));
        MessageRegistry.messageRegistry.put(CustomDataMessage.TYPE, stream -> new CustomDataMessage(stream));
        MessageRegistry.messageRegistry.put(ChatMessage.TYPE, stream -> new ChatMessage(stream));
        MessageRegistry.messageRegistry.put(PlayTauntMessage.TYPE, stream -> new PlayTauntMessage(stream));
    }
    public static Message create(JsonObject json){
        JsonElement typeElement = json.get("type");
        if(typeElement.isJsonNull())
            throw new RuntimeException("type not specified");
        MessageCreator creator = MessageRegistry.messageRegistry.get(typeElement.getAsString());
        if(creator == null)
            throw new RuntimeException("message type " + typeElement.getAsString() + " not known");
        return creator.create(json);
    }


    @FunctionalInterface
    public interface MessageCreator{
        Message create(JsonObject json);
    }
}
