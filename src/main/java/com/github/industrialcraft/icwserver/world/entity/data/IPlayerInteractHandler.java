package com.github.industrialcraft.icwserver.world.entity.data;

import com.github.industrialcraft.icwserver.net.messages.InteractEntityMessage;
import com.github.industrialcraft.icwserver.world.entity.PlayerEntity;

public interface IPlayerInteractHandler {
    void onPlayerInteract(PlayerEntity player, InteractEntityMessage message);
}