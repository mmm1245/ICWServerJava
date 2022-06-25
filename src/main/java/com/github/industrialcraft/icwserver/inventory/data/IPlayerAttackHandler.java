package com.github.industrialcraft.icwserver.inventory.data;

import com.github.industrialcraft.icwserver.net.messages.PlayerAttackMessage;
import com.github.industrialcraft.icwserver.world.entity.PlayerEntity;

public interface IPlayerAttackHandler {
    void onAttack(PlayerEntity player, PlayerAttackMessage message);
}
