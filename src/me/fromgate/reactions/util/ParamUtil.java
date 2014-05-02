package me.fromgate.reactions.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.fromgate.reactions.RAUtil;
import me.fromgate.reactions.ReActions;

public class ParamUtil {
    
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////
    // преобразуем строку вида <команда> <параметр=значение> <параметр=значение> <параметр={значение в несколько слов}> в Map {команда="", параметр=значеие....}
	public static Map<String,String> parseParams(String param){
		return parseParams(param, "param"); 
	}
	
    public static Map<String,String> parseParams(String param, String defaultKey){
    	Map<String,String> params = new HashMap<String,String>();
    	if (!param.isEmpty()) params.put("param-line", param);
    	Pattern pattern = Pattern.compile("(\\S+:(\\{.*\\}\\s|\\{.*\\}|\\S+))|(\\S+)");
    	Matcher matcher = pattern.matcher(param);
    	while (matcher.find()){
    		String paramPart = matcher.group().trim();
    		String key = paramPart;
            String value = "";
            if (matcher.group().contains(":")){
                key = paramPart.substring(0,paramPart.indexOf(":"));
                value = paramPart.substring(paramPart.indexOf(":")+1);
            }
            if (value.isEmpty()){
                value = key;
                key = defaultKey;
            }
            if (value.matches("\\{.*\\}")) value = value.substring(1,value.length()-1);
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
  
    public static boolean isParamExists (Map<String,String> params, String... keys){
    	for (String key: keys)
    		if (!params.containsKey(key)) return false;
        return true;
    }

    

}
