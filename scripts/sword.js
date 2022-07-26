var sword = itemRegistry.createTemplate("SWORD", 1);
sword.withAttackHandler((function(player, angle, power) {
    var target = gameServer.raytrace(player.location(), angle, 80, (function(ent){
        return ent.getId() != player.getId();
    }));
    if(target != null)
        target.damage(-50, EDamageType.BLADE);
    gameServer.spawnParticle("fist", player.location(), 5).addNumber("angle", angle);
}));
sword.addRenderState("default", "assets/items/sword.png");
sword.register();