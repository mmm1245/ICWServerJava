package com.github.industrialcraft.icwserver.script;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

public class JSUtilFunctions {
    public static void defineObjectAssign(ScriptEngine engine) throws ScriptException {
        engine.eval("if (typeof Object.assign != 'function') {\n" +
                "  // Must be writable: true, enumerable: false, configurable: true\n" +
                "  Object.defineProperty(Object, \"assign\", {\n" +
                "    value: function assign(target, varArgs) { // .length of function is 2\n" +
                "      'use strict';\n" +
                "      if (target == null) { // TypeError if undefined or null\n" +
                "        throw new TypeError('Cannot convert undefined or null to object');\n" +
                "      }\n" +
                "\n" +
                "      var to = Object(target);\n" +
                "\n" +
                "      for (var index = 1; index < arguments.length; index++) {\n" +
                "        var nextSource = arguments[index];\n" +
                "\n" +
                "        if (nextSource != null) { // Skip over if undefined or null\n" +
                "          for (var nextKey in nextSource) {\n" +
                "            // Avoid bugs when hasOwnProperty is shadowed\n" +
                "            if (Object.prototype.hasOwnProperty.call(nextSource, nextKey)) {\n" +
                "              to[nextKey] = nextSource[nextKey];\n" +
                "            }\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "      return to;\n" +
                "    },\n" +
                "    writable: true,\n" +
                "    configurable: true\n" +
                "  });\n" +
                "}");
    }
    public static void defineEntityDataMerge(ScriptEngine engine) throws ScriptException {
        engine.eval("function mergeEntityData(entity, data){\n" +
                "    entity.data = Object.assign(entity.data, data);\n" +
                "}");
    }
    public static void defineDeepCopy(ScriptEngine engine) throws ScriptException {
        engine.eval("function deepCopyObject(obj){\n" +
                "    return Object.assign(obj);\n" +
                "}");
    }
    public static void defineJsonToString(ScriptEngine engine) throws ScriptException {
        engine.eval("function jsonToString(obj){\n" +
                "    return JSON.stringify(obj);\n" +
                "}");
    }
}
