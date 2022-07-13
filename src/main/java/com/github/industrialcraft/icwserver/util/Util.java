package com.github.industrialcraft.icwserver.util;

import java.util.regex.Pattern;

public class Util {
    private static Pattern playerNamePattern = Pattern.compile("\\w+");
    public static boolean isPlayerNameValid(String name){
        if(name.length() < 3 || name.length() > 16)
            return false;
        return playerNamePattern.matcher(name).matches();
    }
}
