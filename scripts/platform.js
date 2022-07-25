var platform = entityRegistry.createTemplate("PLATFORM", 1);
platform.withPhysicsData(0, 0, EPhysicsLayers.WALL);
platform.withOnSpawn((function() {
    this.data = Object.assign({width:100,height:5}, this.data);
    this.getPhysicalObject().knockbackEffectivity = 0;
}));
platform.withOnTick((function() {
   this.getPhysicalObject().resize(this.data.width, this.data.height);
}));
platform.withDamageTypeModifier((function() {
   return 0;
}));
platform.addRenderState("default", "assets/entities/platform.png");
platform.register();

events.CREATE_WORLD.register((function(world) {
    entities.PLATFORM.spawn(world.location(0, -30), {width:100,height:5});
}));