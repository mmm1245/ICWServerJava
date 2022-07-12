function stoneCallback(){
    log.info("callback");
}

var stone = itemRegistry.createTemplate("STONE", 100);
stone.withAttackHandler(stoneCallback);
stone.register();

var orb = entityRegistry.createTemplate("ORB", 20);
orb.withOnSpawn((function() {
    this.data = {cnt:0};
}));
orb.withOnTick((function() {
   this.data.cnt++;
}));
orb.withAnimationStateProvider((function() {
   return this.data.cnt;
}));
orb.register();

//log.info(stoneCallback);
//log.test(stoneCallback);
events.PLAYER_JOIN.register((function(pl) {
    log.info("callback: " + pl.getConnection().profile.name());
}));

events.CREATE_WORLD.register((function(world) {
    log.info(entities);
    entities.ORB.spawn(world.spawnpoint());
}));