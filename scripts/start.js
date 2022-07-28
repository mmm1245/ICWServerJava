function stoneCallback(){
    log.info("callback");
}

var orb = entityRegistry.createTemplate("ORB", 20);
orb.withPhysicsData(10, 10, EPhysicsLayers.OBJECT);
orb.withOnSpawn((function() {
    //this.data = {cnt:0};
}));
orb.withOnTick((function() {
   this.data.cnt++;
}));
orb.withAnimationStateProvider((function() {
   return this.data.cnt;
}));
orb.withInventory(3, null, null);
orb.withOnPlayerInteract((function(player,msg) {
    player.openInventory(this);
}));
orb.addRenderState("default", "assets/entities/orb/default.png");
orb.register();

//log.info(stoneCallback);
//log.test(stoneCallback);
events.PLAYER_JOIN.register((function(pl) {
    log.info("callback: " + pl.profile().name());
    pl.getInventory().addItem(items.STONE.createStack(5));
}));

events.CREATE_WORLD.register((function(world) {
    log.info(entities);
    entities.ORB.spawn(world.spawnpoint(), {cnt:0});
}));

/*var taskid = scheduler.repeating((function() {
    log.info("task called every 2 sec");
}),0,40,null);
log.info("task id is %s", taskid);

var taskid2 = scheduler.schedule((function() {
    log.info("task called after 4 sec");
}),80,null);
log.info("task2 id is %s", taskid2);
var taskid3 = scheduler.times((function() {
    log.info("task called after 2 sec,every 2 sec, 3 times");
}),40,40,3,null);
log.info("task3 id is %s", taskid3);*/
//scheduler.killTask(taskid);

soundEffectRegistry.register("CLAP", "assets/soundEffects/clap.mp3");

statusEffectRegistry.createTemplate("SPAM").withOnTick((function(entity) {
   log.info("spam:%s", entity);
})).register();

events.PLAYER_TICK.register((function(player) {
   var dX = undefined;
   if(player.getData() != null){
        dX = player.getData().xPosLast;
   } else {
    player.setData({});
   }
   if(dX != null){
        if(dX !== player.location().x()){
            player.setPlayerState(playerStates.WALK);
        } else {
            player.setPlayerState(playerStates.IDLE);
        }
   } else {
        player.setPlayerState(playerStates.IDLE);
    }
   player.setData(Object.assign(player.getData(), {xPosLast:player.location().x()}));
}));

playerStateRegistry.createTemplate("IDLE").addPart("assets/player/idle1.png", 5).addPart("assets/player/idle2.png", 5).register();

playerStateRegistry.createTemplate("WALK").addPart("assets/player/w1.png", 3).addPart("assets/player/w2.png", 3).addPart("assets/player/w3.png", 3).addPart("assets/player/w4.png", 3).addPart("assets/player/w5.png", 3).addPart("assets/player/w6.png", 3).addPart("assets/player/w7.png", 3).register();

playerStateRegistry.createTemplate("WIN").addPart("assets/taunts/win/win_1.png", 20).addPart("assets/taunts/win/win_2.png",10).markMotionBlocking().register();