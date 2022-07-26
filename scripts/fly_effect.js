statusEffectRegistry.createTemplate("FLY").withOnTick((function(entity) {
   var player = entity.toPlayerIP();
   if(player != null)
        player.playerAbilities().topDown = true;
   var po = entity.getPhysicalObject();
   if(po != null)
        po.gravity = 0;
})).register();