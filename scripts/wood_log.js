var wood_log = itemRegistry.createTemplate("LOG", 20);
wood_log.withFuelEfficiency(100);
wood_log.addRenderState("default", "assets/items/log.png");
wood_log.register();