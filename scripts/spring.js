var spring = entityRegistry.createTemplate("SPRING", 1);
spring.withPhysicsData(20, 5, EPhysicsLayers.OBJECT);
spring.withOnSpawn((function() {
    //this.data = Object.assign({}, this.data);
}));
spring.withOnTick((function() {
    this.getPhysicalObject().forEachCollision(false, (function(ent) {
        var po = ent.getPhysicalObject();
        if(po != null){
            if(po.knockbackY < 0){
                po.knockbackY = -po.knockbackY;
            }
        }
    }));
}));
spring.addRenderState("default", "assets/entity/spring.png");
spring.register();