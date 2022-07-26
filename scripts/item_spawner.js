var item_spawner = entityRegistry.createTemplate("ITEM_SPAWNER", 1);
item_spawner.withOnSpawn((function() {
    this.data = Object.assign({item:"STONE",count:1,delay:60,timer:0}, this.data);
}));
item_spawner.withOnTick((function() {
    if(this.data.timer <= 0){
        var item = items[this.data.item];
        if(item != null){
            gameServer.spawnItem(this.location(), item.create(this.data.count));
        }
        this.data.timer = this.data.delay;
    } else {
        this.data.timer--;
    }
}));
item_spawner.addRenderState("default", "assets/entity/item_spawner.png");
item_spawner.register();