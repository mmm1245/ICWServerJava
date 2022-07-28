var log_pile = entityRegistry.createTemplate("LOG_PILE", 20);
log_pile.withPhysicsData(10, 10, EPhysicsLayers.OBJECT);
log_pile.withInventory(4,(function(index,is) {
    if(is == null)
        return true;
    return is.getItem() == items.LOG;
}), null);
log_pile.withAnimationStateProvider((function() {
   return "l_"+Math.floor(this.getInventory().count(items.LOG)/4);
}));
log_pile.withOnTick((function(player,msg) {
    if(this.getInventory().isEmpty())
        this.kill();
}));
log_pile.withOnPlayerInteract((function(player,msg) {
    player.openInventory(this);
}));
log_pile.addRenderState("default", "assets/entities/log_pile/default.png");
for(var i = 0;i <= 20;i++){
    log_pile.addRenderState("l_" + i, "assets/entities/log_pile/l_" + i + ".png");
}
log_pile.register();