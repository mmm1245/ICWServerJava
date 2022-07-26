statusEffectRegistry.createTemplate("FLY").withOnTick((function(entity) {
   var player = entity.toPlayerIP();
   if(player != null)
        player.playerAbilities().topDown = true;
})).register();