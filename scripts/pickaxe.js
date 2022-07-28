var pickaxe = itemRegistry.createTemplate("PICKAXE", 1);
pickaxe.withAttackHandler((function(player, angle, power) {
    var target = gameServer.raytrace(player.location(), angle, 40, (function(ent){
        return ent.getId() != player.getId();
    }));
    if(target.entity != null)
        target.entity.damage(-30, EDamageType.HEAVY_SHARP);
    gameServer.spawnParticle("fist", player.location(), 5).addNumber("angle", angle);
}));
pickaxe.addRenderState("default", "assets/items/pickaxe.png");
pickaxe.register();