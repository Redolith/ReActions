package me.fromgate.reactions.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Param {
    private String paramStr = "";
    private Map<String, String> params = new HashMap<>();

    private final static Pattern PARAM_PATTERN = Pattern.compile("\\S+:\\{[^\\{\\}]*\\}|\\S+");
    private final static Pattern PARAM_BRACKET = Pattern.compile("\\{.*\\}");
    private final static Pattern PARAM_BRACKET_SE = Pattern.compile("^\\{.*\\}$");
    private final static Pattern INT = Pattern.compile("-?[1-9]+[0-9]*");
    private final static Pattern FLOAT = Pattern.compile("-?[0-9]+\\.?[0-9]*");
    private final static Pattern BOOLEAN = Pattern.compile("(?i)true|on|yes");

    public Param(String param) {
        this(param, "param");
    }

    public Param(String param, String defaultKey) {
        this.paramStr = param;
        this.params = parseParams(param, defaultKey);
        this.params.put("param-line", this.paramStr); // очередная залипуха
    }

    public void setParamString(String paramStr) {
        this.paramStr = paramStr;
    }


    public Param(Map<String, String> params) {
        this.params.putAll(params);
        StringBuilder sb = new StringBuilder();
        for (String key : params.keySet()) {
            if (sb.length() > 0) sb.append(" ");
            sb.append(key).append(":");
            String value = params.get(key);
            if (value.contains(" ") && !PARAM_BRACKET_SE.matcher(value).matches())
                sb.append("{").append(value).append("}");
            else sb.append(value);
        }
        this.paramStr = sb.toString();
    }


    public Param() {
        this.params = new HashMap<>();
        this.paramStr = "";
    }

    /**
     * Преобразует строку вида <параметр>/<параметр>/<параметр> в объект Param
     *
     * @param oldFormat — строка старого формата
     * @param divider   — разделитель (любой, а не только "/")
     * @param keys      — перечень ключей для параметров
     * @return — возвращает
     * <p>
     * Пример:
     * fromOldFormat ("123/test/953","/","num1","word1","num2");
     * - создаст объект Param со следующими параметрами и значениями:
     * - param-line: 123/test/953
     * - num1: 123
     * - word1: test
     * - num2: 953
     */
    public static Param fromOldFormat(String oldFormat, String divider, String... keys) {
        Param param = new Param(oldFormat);
        param.setParamString(oldFormat);
        if (param.hasAnyParam(keys)) return param;
        param = new Param();
        param.setParamString(oldFormat);
        param.set("param-line", oldFormat); // и снова залипуха
        String[] ln = oldFormat.split(Pattern.quote(divider), keys.length);
        if (ln.length == 0) return param;
        for (int i = 0; i < Math.min(ln.length, keys.length); i++)
            param.set(keys[i], ln[i]);
        return param;
    }

    public String getParam(String key, String defParam) {
        if (!params.containsKey(key)) return defParam;
        return params.get(key);
    }

    public int getParam(String key, int defParam) {
        if (!params.containsKey(key)) return defParam;
        String str = params.get(key);
        if (!INT.matcher(str).matches()) return defParam;
        return Integer.parseInt(str);
    }


    public float getParam(String key, float defParam) {
        if (!params.containsKey(key)) return defParam;
        String str = params.get(key);
        if (!FLOAT.matcher(str).matches()) return defParam;
        return Float.parseFloat(str);
    }

    public double getParam(String key, double defParam) {
        if (!params.containsKey(key)) return defParam;
        String str = params.get(key);
        if (!FLOAT.matcher(str).matches()) return defParam;
        return Double.parseDouble(str);
    }

    public boolean getParam(String key, boolean defValue) {
        if (!params.containsKey(key)) return defValue;
        String str = params.get(key);
        return (BOOLEAN.matcher(str).matches());
    }

    public String toString() {
        return this.paramStr;
    }

    public boolean isParamsExists(String... keys) {
        for (String key : keys) {
            if (!params.containsKey(key)) {
                return false;
            }
        }
        return true;
    }

    public boolean hasAnyParam(String... keys) {
        for (String key : keys) {
            if (params.containsKey(key)) return true;
        }
        return false;
    }

    public boolean matchAnyParam(Pattern... patterns) {
        for (Pattern pattern : patterns) {
            for (String param : params.keySet()) {
                if (pattern.matcher(param).matches()) return true;
            }
        }
        return false;
    }

    public boolean matchAnyParam(String... keys) {
        for (String key : keys) {
            for (String param : params.keySet()) {
                if (param.matches(key)) return true;
            }
        }
        return false;
    }

    public static Map<String, String> parseParams(String param, String defaultKey) {
        Map<String, String> params = new HashMap<>();
        Matcher matcher = PARAM_PATTERN.matcher(hideBkts(param));
        while (matcher.find()) {
            String paramPart = matcher.group().trim().replace("#BKT1#", "{").replace("#BKT2#", "}");
            String key = paramPart;
            String value = "";
            if (matcher.group().contains(":")) {
                key = paramPart.substring(0, paramPart.indexOf(":"));
                value = paramPart.substring(paramPart.indexOf(":") + 1);
            }
            if (value.isEmpty()) {
                value = key;
                key = defaultKey;
            }
            if (PARAM_BRACKET.matcher(value).matches()) value = value.substring(1, value.length() - 1);
            params.put(key, value);
        }
        return params;
    }

    private static String hideBkts(String s) {
        int count = 0;
        String r = "";
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            String a = String.valueOf(c);
            if (c == '{') {
                count++;
                if (count != 1) a = "#BKT1#";
            } else if (c == '}') {
                if (count != 1) a = "#BKT2#";
                count--;
            }
            r = r + a;
        }
        return r;
    }

    public Set<String> keySet() {
        return this.params.keySet();
    }

    public String getParam(String key) {
        return this.params.containsKey(key) ? this.params.get(key) : "";
    }

    public Map<String, String> getMap() {
        return this.params;
    }

    public boolean isEmpty() {
        return this.params.isEmpty();
    }

    public void set(String key, String value) {
        params.put(key, value);
    }

    public static Param parseParams(String paramStr) {
        return new Param(paramStr);
    }

    public void remove(String key) {
        if (this.params.containsKey(key)) this.params.remove(key);
    }

    public int size() {
        return this.params.size();
    }

}
