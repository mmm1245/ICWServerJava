package com.github.industrialcraft.icwserver.inventory.data;

import com.github.industrialcraft.icwserver.net.messages.PlayerAttackMessage;
import com.github.industrialcraft.icwserver.world.entity.PlayerEntity;
import com.github.industrialcraft.inventorysystem.ItemStack;

public interface IPlayerAttackHandler {
    void onAttack(PlayerEntity player, ItemStack stack, PlayerAttackMessage message);
}
