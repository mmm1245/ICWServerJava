function stoneCallback(){
    log.info("callback");
}

var stone = itemRegistry.createTemplate("STONE", 100);
stone.withAttackHandler(stoneCallback);
stone.withAnimationStateProvider((function() {
     return gameServer.ticksLasted();
 }));
stone.addRenderState("default", "assets/items/stone.png");
stone.register();

var orb = entityRegistry.createTemplate("ORB", 20);
orb.withOnSpawn((function() {
    //this.data = {cnt:0};
}));
orb.withOnTick((function() {
   this.data.cnt++;
}));
orb.withAnimationStateProvider((function() {
   return this.data.cnt;
}));
orb.withInventory(3);
orb.withOnPlayerInteract((function(player,msg) {
    player.openInventory(this);
}));
orb.addRenderState("default", "assets/entities/orb/default.png");
orb.register();

//log.info(stoneCallback);
//log.test(stoneCallback);
events.PLAYER_JOIN.register((function(pl) {
    log.info("callback: " + pl.getConnection().profile.name());
    pl.getInventory().addItem(items.STONE.createStack(5));
}));

events.CREATE_WORLD.register((function(world) {
    log.info(entities);
    entities.ORB.spawn(world.spawnpoint(), {cnt:0});
}));