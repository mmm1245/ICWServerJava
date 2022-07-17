commandManager.register("duel",(function(player1id,player2id){
    var player1 = gameServer.playerById(player1id);
    if(player1 == null){
        this.sendChatMessage("player1 not found");
        return;
    }
    var player2 = gameServer.playerById(player2id);
    if(player2 == null){
        this.sendChatMessage("player2 not found");
        return;
    }
    var duelWorld = gameServer.createWorld();
    player1.teleport(duelWorld.location(0, 0));
    player2.teleport(duelWorld.location(80, 0));
    duelWorld.setData({type:"duel",player1:player1id,player2:player2id});
}), ECommandArgumentType.INTEGER, ECommandArgumentType.INTEGER);

events.PLAYER_DEATH.register((function(player) {
    var world = player.location().world();
    if(world.getData() != null && world.getData().type=="duel"){
        player.sendChatMessage("You lost");
        if(world.getData().player1 == player.getId()){
            gameServer.playerById(world.getData().player2).sendChatMessage("You won");
        } else {
            gameServer.playerById(world.getData().player1).sendChatMessage("You won");
        }
        player.location().world().remove();
    }
}));

events.PLAYER_LEAVE.register((function(player) {
    var world = player.location().world();
    if(world.getData() != null && world.getData().type=="duel"){
        player.sendChatMessage("You lost");
        if(world.getData().player1 == player.getId()){
            gameServer.playerById(world.getData().player2).sendChatMessage("You won");
        } else {
            gameServer.playerById(world.getData().player1).sendChatMessage("You won");
        }
        player.location().world().remove();
    }
}));