package me.fromgate.reactions.util;

import java.util.HashMap;
import java.util.Map;

import me.fromgate.reactions.RAUtil;
import me.fromgate.reactions.ReActions;

public class ParamUtil {
    
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////
    // Парсер
    // преобразуем строку вида <команда> <параметр=значение> <параметр=значение> <параметр=значение> в Map {команда="", параметр=значеие....}
    public static Map<String,String> parseParams(String param){
        Map<String,String> params = new HashMap<String,String>();
        if (!param.isEmpty()){
            params.put("param-line", param);
            String[]ln = param.split(" ");
            if (ln.length>0)
                for (int i = 0; i < ln.length; i++){
                    String key = ln[i];
                    String value = "";
                    if (ln[i].contains(":")){
                        key = ln[i].substring(0,ln[i].indexOf(":"));
                        value = ln[i].substring(ln[i].indexOf(":")+1);
                    }

                    if (value.isEmpty()){
                        value = key;
                        key = "param";
                    } 
                    params.put(key, value);
                }
        }
        return params;
    }

    public static Map<String,String> parseParams(String param,String defaultkey){
        Map<String,String> params = new HashMap<String,String>();
        if (param.isEmpty()) return params;
        String[]ln = param.split(" ");
        if (ln.length>0)
            for (int i = 0; i < ln.length; i++){
                String key = ln[i];
                String value = "";
                if (ln[i].contains(":")){
                    key = ln[i].substring(0,ln[i].indexOf(":"));
                    value = ln[i].substring(ln[i].indexOf(":")+1);
                } else {
                    value = key;
                    key = defaultkey;
                }
                params.put(key, value);
            }
        return params;
    }
    
    private static RAUtil u(){
        return ReActions.util;
    }
    
    public static String getParam(Map<String,String> params, String key, String defparam){
        if (!params.containsKey(key)) return defparam;
        return params.get(key);
    }

    public static int getParam(Map<String,String> params, String key, int defparam){
        if (!params.containsKey(key)) return defparam;
        String str = params.get(key);
        //if (!str.matches("[1-9]+[0-9]*")) return defparam;
        if (!u().isIntegerGZ(str)) return defparam;
        return Integer.parseInt(str);
    }

    public static float getParam(Map<String,String> params, String key, float defparam){
        if (!params.containsKey(key)) return defparam;
        String str = params.get(key);
        if (!str.matches("[0-9]+\\.?[0-9]*")) return defparam;
        return Float.parseFloat(str);
    }

    public static double getParam(Map<String,String> params, String key, double defparam){
        if (!params.containsKey(key)) return defparam;
        String str = params.get(key);
        if (!str.matches("[0-9]+\\.?[0-9]*")) return defparam;
        return Double.parseDouble(str);
    }


    public static boolean getParam(Map<String,String> params, String key, boolean defparam){
        if (!params.containsKey(key)) return defparam;
        String str = params.get(key);
        return (str.equalsIgnoreCase("true")||str.equalsIgnoreCase("on")||str.equalsIgnoreCase("yes"));
    }

    public static String toString(Map<String,String> params){
        String str ="";
        for (String key : params.keySet())
            str +=key+"["+params.get(key)+"] ";
        return str.isEmpty() ? "empty" : str;
    }
    
    public static boolean isParamExists (Map<String,String> params, String key){
        return params.containsKey(key);
    }
    
    

}
