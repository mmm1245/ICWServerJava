var axe = itemRegistry.createTemplate("AXE", 1);
axe.withAttackHandler((function(player, angle, power) {
    var target = gameServer.raytrace(player.location(), angle, 40, (function(ent){
        return ent.getId() != player.getId();
    }));
    if(target.entity != null)
        target.entity.damage(-30, EDamageType.HEAVY_BLADE);
    gameServer.spawnParticle("fist", player.location(), 5).addNumber("angle", angle);
}));
axe.addRenderState("default", "assets/items/axe.png");
axe.register();