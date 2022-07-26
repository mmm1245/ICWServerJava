var rocket = entityRegistry.createTemplate("ROCKET", 10);
rocket.withPhysicsData(10, 10, EPhysicsLayers.PROJECTILE);
rocket.withOnSpawn((function() {
    this.data = Object.assign({shooter:-1}, this.data);
    this.getPhysicalObject().knockbackFalloff = 0;
    this.getPhysicalObject().gravity = 0;
}));
rocket.withOnTick((function() {
    var shooter = this.data.shooter;
    if(this.getPhysicalObject().collides((function(entity) {return shooter!=entity.id;}), true) != null){
        this.kill();
    }
}));
rocket.withOnDeath((function() {
    gameServer.spawnExplosion(this.location(), 50, 50);
}));
rocket.addRenderState("default", "assets/entity/rocket.png");
rocket.register();