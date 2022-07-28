var tree = entityRegistry.createTemplate("TREE", 100);
tree.withPhysicsData(20, 50, EPhysicsLayers.OBJECT);
tree.withOnSpawn((function() {

}));
tree.withDamageTypeModifier((function(type) {
    log.info(type);
    if(type.isHot)
        return 1.2;
    if(!type.isPiercing)
        return 0;
    var retval = 1;
    if(type.isSmall)
        retval = 0.2;
    if(type.isHeavy)
        retval *= 2;
    return retval;
}));
tree.withOnDeath((function() {
    gameServer.spawnItem(this.location(), items.LOG.create(3));
}));
tree.addRenderState("default", "assets/entity/tree.png");
tree.register();