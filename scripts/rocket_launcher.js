var rocket_launcher = itemRegistry.createTemplate("ROCKET_LAUNCHER", 1);
rocket_launcher.withAttackHandler((function(player, angle, power) {
    entities.ROCKET.spawn(player.location(), {shooter:player.getId()}).applyKnockbackAtAngle(msg.angle, 5);
}));
rocket_launcher.addRenderState("default", "assets/items/rocket_launcher.png");
rocket_launcher.register();