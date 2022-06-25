package com.github.industrialcraft.icwserver.inventory;

import com.github.industrialcraft.icwserver.inventory.data.IPlayerAttackHandler;
import com.github.industrialcraft.icwserver.net.messages.PlayerAttackMessage;
import com.github.industrialcraft.icwserver.particle.GunshotParticle;
import com.github.industrialcraft.icwserver.physics.Raytracer;
import com.github.industrialcraft.icwserver.world.entity.Entity;
import com.github.industrialcraft.icwserver.world.entity.PlayerEntity;
import com.github.industrialcraft.icwserver.world.entity.ThrownEntity;
import com.github.industrialcraft.icwserver.world.entity.data.EDamageType;
import com.github.industrialcraft.icwserver.world.entity.data.IDamagable;
import com.github.industrialcraft.inventorysystem.IItem;
import mikera.vectorz.Vector2;

public class Items {
    public static IItem STONE = new ItemWithAttackAction(15, "stone") {
        @Override
        public void onAttack(PlayerEntity player, PlayerAttackMessage message) {
            Vector2 dir = new Vector2((int) (Math.cos(Math.toRadians((message.angle + 360) % 360)) * 10), (int) (Math.sin(Math.toRadians((message.angle + 360) % 360)) * 10));
            dir.normalise();
            dir.multiply(4.5);
            new ThrownEntity(player.getLocation().addXY(2, 15), (float) dir.x, (float) dir.y, player.id, player.getHandItemStack().clone(1));

            player.getHandItemStack().removeCount(1);
        }
    };
    public static IItem PISTOL = new ItemWithAttackAction(1, "pistol") {
        @Override
        public void onAttack(PlayerEntity player, PlayerAttackMessage message) {
            Entity entity = Raytracer.raytrace(player.getLocation().addXY(2, 15), message.angle, 250, ent -> ent.id != player.id);
            if (entity instanceof IDamagable damagable) {
                damagable.damage(-40, EDamageType.BULLET);
            }
            player.getLocation().world().addParticle(new GunshotParticle(5, player.getLocation().x()+2, player.getLocation().y()+15, message.angle, 250, 1));
        }
    };
    public static IItem LOG = new Item(15, "log");
    public static IItem PLANK = new Item(15, "plank");

    public static abstract class ItemWithAttackAction extends Item implements IPlayerAttackHandler{
        public ItemWithAttackAction(int stackSize, String identifier) {
            super(stackSize, identifier);
        }
    }
}
