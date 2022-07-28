var rock_generator = entityRegistry.createTemplate("ROCK_GENERATOR", 1);
rock_generator.withPhysicsData(20, 20, EPhysicsLayers.OBJECT);
rock_generator.withOnSpawn((function() {
    this.data = Object.assign({item:"STONE",count:1,maxNeeded:100,needed:100}, this.data);
}));
rock_generator.withOnTick((function() {
    this.getPhysicalObject().gravity = 0;
}));
rock_generator.withOnDamage((function(damage, olddamage) {
    var data = this.getData();
    data.needed += damage;
    if(data.needed <= 0){
        data.needed = data.maxNeeded;
        gameServer.spawnItem(this.location(), items[data.item].create(data.count));
    }
    return true;
}));
rock_generator.withDamageTypeModifier((function(type) {
    if(type == EDamageType.HEAVY_SHARP)
        return 1;
    return 0;
}));

rock_generator.addRenderState("default", "assets/entity/item_spawner.png");
rock_generator.register();