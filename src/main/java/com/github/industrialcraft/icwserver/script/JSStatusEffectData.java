package com.github.industrialcraft.icwserver.script;

import org.openjdk.nashorn.api.scripting.ScriptObjectMirror;

public record JSStatusEffectData(String type, ScriptObjectMirror spawnMethod, ScriptObjectMirror tickMethod) {
}
