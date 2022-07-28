var gun = itemRegistry.createTemplate("GUN", 1);
gun.withAttackHandler((function(player, angle, power) {
    var result = gameServer.raytrace(player.location(), angle, 500, (function(ent){
        return ent.getId() != player.getId();
    }));
    if(result.entity != null)
        result.entity.damage(-30, EDamageType.BULLET);
    gameServer.spawnParticle("gunshot", player.location(), 5).addNumber("length", result.distance).addNumber("angle", angle);
}));
gun.addRenderState("default", "assets/items/gun.png");
gun.register();