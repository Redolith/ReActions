package me.fromgate.reactions.placeholders;

import org.bukkit.entity.Player;

public abstract class Placeholder {

    private String id = "UNKNOWN";
    private boolean needPlayer = false;
    private String[] keys = {};

    public Placeholder() {
        if (this.getClass().isAnnotationPresent(PlaceholderDefine.class)) {
            PlaceholderDefine pd = this.getClass().getAnnotation(PlaceholderDefine.class);
            this.id = pd.id();
            this.needPlayer = pd.needPlayer();
            this.keys = pd.keys();
        }
    }


    public String getId() {
        return id;
    }

    public boolean playerRequired() {
        return this.needPlayer;
    }

    public String[] getKeys() {
        return keys;
    }

    protected boolean equalsIgnoreCase(String key, String... values) {
        for (String s : values)
            if (key.equalsIgnoreCase(s)) return true;
        return false;
    }

    public boolean checkKey(String key) {
        for (String k : this.getKeys()) {
            if (k.equalsIgnoreCase(key)) return true;
        }
        return false;
    }


    /**
     * Замена ключеового слова
     *
     * @param player - игрок, если он есть
     * @param key    - Ключевое слово, без параметра и символа "%" в начале
     * @param param  - Параметр (без завершающего символа "%")
     * @return - возврат подстановки.
     */
    public abstract String processPlaceholder(Player player, String key, String param);
}
