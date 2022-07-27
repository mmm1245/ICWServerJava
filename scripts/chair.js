var chair = entityRegistry.createTemplate("CHAIR", 10);
chair.withPhysicsData(10, 10, EPhysicsLayers.OBJECT);
chair.withPassengerData(10, 5);
chair.withOnPlayerInteract((function(player,msg) {
    this.trySetPassenger(player);
}));
chair.addRenderState("default", "assets/entity/chair.png");
chair.register();