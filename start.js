function stoneCallback(){
    log.info("callback");
}

var stone = itemRegistry.createTemplate("STONE", 100);
stone.withAttackHandler(stoneCallback);
stone.register();
items.STONE.onAttackHandlerCall(null, null, null);
//log.info(stoneCallback);
//log.test(stoneCallback);
function playerJoin(pl){
    log.info("callback" + pl.getConnection().profile.name());
}
events.PLAYER_JOIN.register(playerJoin);