/*
 * Copyright 2015 fromgate. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 * 1. You cannot use this file (or part of this file) in commercial projects.
 * 
 * 2. Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer.
 *
 * 3. Redistributions in binary form must reproduce the above copyright notice, this list
 * of conditions and the following disclaimer in the documentation and/or other materials
 * provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ''AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those of the
 * authors and contributors and should not be interpreted as representing official policies,
 * either expressed or implied, of anybody else.
 */

package me.fromgate.reactions.util.item;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class VirtualItem18 extends VirtualItem {

    /**
     * Constructor Create new VirtualItem object
     *
     * @param type - Item type
     */
    public VirtualItem18(Material type) {
        super(type);
    }

    /**
     * Constructor Create new VirtualItem object
     *
     * @param type   - Item type
     * @param data   - durabiltity (data)
     * @param amount - amount
     */
    public VirtualItem18(Material type, int data, int amount) {
        super(type);
        this.setDurability((short) data);
        this.setAmount(amount);
    }

    /**
     * Constructor Create new VirtualItem object based on ItemStack
     *
     * @param item
     */
    public VirtualItem18(ItemStack item) {
        super(item);
    }

    public static VirtualItem18 fromItemStack(ItemStack item) {
        if (item == null || item.getType() == Material.AIR)
            return null;
        return new VirtualItem18(item);
    }

    /**
     * Create VirtualItem object based on parameter-string
     *
     * @param itemStr - String. Format: type:<Type> data:<Data> amount:<Amount>
     *                [AnotherParameters] item:<Type>:<Data>*<Amount>
     *                [AnotherParameters]
     * @return - New VirtualItem object or null (if parse failed)
     */
    public static VirtualItem18 fromString(String itemStr) {
        Map<String, String> params = parseParams(itemStr);
        VirtualItem18 vi = fromMap(params);
        if (vi != null)
            return vi;
        ItemStack item = parseOldItemStack(itemStr);
        if (item != null)
            return new VirtualItem18(item);
        return null;
    }

    /**
     * Create VirtualItem object (deserialize from Map)
     *
     * @param params - Map of parameters and values
     * @return - VirtualItem object
     */
    @SuppressWarnings("deprecation")
    public static VirtualItem18 fromMap(Map<String, String> params) {
        if (params == null || params.isEmpty())
            return null;
        Material type = null;
        int data = 0;
        int amount = 1;
        if (params.containsKey("item") || params.containsKey("default-param")) {
            String itemStr = params.containsKey("item") ? params.get("item")
                    : params.get("default-param");
            String dataStr = "0";
            String amountStr = "1";
            if (itemStr.contains("*")) {
                itemStr = new String(itemStr.substring(0, itemStr.indexOf("*")));
                amountStr = new String(itemStr.substring(itemStr.indexOf("*") + 1));
            }
            if (itemStr.contains(":")) {
                itemStr = new String(itemStr.substring(0, itemStr.indexOf(":")));
                dataStr = new String(itemStr.substring(itemStr.indexOf(":") + 1));
            }
            type = itemStr.matches("[0-9]+") ? Material.getMaterial(Integer
                    .valueOf(itemStr)) : Material.getMaterial(itemStr
                    .toUpperCase());
            data = dataStr.matches("[0-9]+") ? Integer.valueOf(dataStr) : 0;
            amount = getNumber(amountStr); // amountStr.matches("[0-9]+") ?
            if (amount == 0) return null;
        } else if (params.containsKey("type")) {
            String typeStr = getParam(params, "type", "");
            type = typeStr.matches("[0-9]+") ? Material.getMaterial(Integer
                    .valueOf(typeStr)) : Material.getMaterial(typeStr
                    .toUpperCase());
        } else
            return null;
        if (type == null)
            return null;
        data = getNumber(getParam(params, "data", "0"));
        amount = getNumber(getParam(params, "amount", "1"));
        VirtualItem18 vi = new VirtualItem18(type, data, amount);
        vi.setName(getParam(params, "name"));
        vi.setLore(getParam(params, "lore"));
        vi.setEnchantments(getParam(params, "enchantments"));
        vi.setBook(getParam(params, "book-author"), getParam(params, "book-title"), getParam(params, "book-pages"));
        vi.setFireworks(getNumber(getParam(params, "firework-power", "0")), getParam(params, "firework-effects"));
        vi.setColor(getParam(params, "color"));
        vi.setSkull(getParam(params, "skull-owner"));
        vi.setPotionMeta(getParam(params, "potion-effects"));
        vi.setMap(getParam(params, "map-scale", "false").equalsIgnoreCase("true"));
        vi.setEnchantStorage(getParam(params, "stored-enchants"));
        vi.setBanner(params);
        vi.setFireworkEffect(getParam(params, "firework-effects"));
        return vi;
    }

    /**
     * Serialize VirtualItem to Map<String,String>
     *
     * @return
     */
    public Map<String, String> toMap() {
        Map<String, String> params = new LinkedHashMap<String, String>();
        params.put("type", this.getType().name());
        params.put("data", Integer.toString(this.getDurability()));
        params.put("amount", Integer.toString(this.getAmount()));
        putEnchants(params, "enchantments", this.getEnchantments());
        putItemMeta(params, this.getItemMeta());
        if (ADD_REGEX) params.put("regex", "false");
        return params;
    }

    @Override
    protected void putItemMeta(Map<String, String> params, ItemMeta itemMeta) {
        if (itemMeta == null)
            return;
        if (itemMeta.hasDisplayName())
            put(params, "name", itemMeta.getDisplayName().replace('§', '&'));
        if (itemMeta.hasLore())
            put(params, "lore", itemMeta.getLore());
        if (itemMeta instanceof BookMeta) {
            BookMeta bm = (BookMeta) itemMeta;
            put(params, "book-author", bm.getAuthor().replace('§', '&'));
            put(params, "book-title", bm.getTitle().replace('§', '&'));
            if (bm.getPages() != null && !bm.getPages().isEmpty()) {
                List<String> pages = new ArrayList<String>();
                for (String page : bm.getPages()) {
                    String newPage = page.replaceAll("§0\n", "&z");
                    newPage = newPage.replace('§', '&');
                    pages.add(newPage);
                }
                put(params, "book-pages", pages);
            }
        }
        if (itemMeta instanceof FireworkMeta) {
            FireworkMeta fm = (FireworkMeta) itemMeta;
            put(params, "firework-power", fm.getPower());
            put(params, "firework-effects", fireworksToList(fm.getEffects()));
        }

        if (itemMeta instanceof LeatherArmorMeta) {
            LeatherArmorMeta lm = (LeatherArmorMeta) itemMeta;
            put(params, "color", colorToString(lm.getColor(), true));
        }
        if (itemMeta instanceof SkullMeta) {
            SkullMeta sm = (SkullMeta) itemMeta;
            if (sm.hasOwner())
                put(params, "skull-owner", sm.getOwner());
        }
        if (itemMeta instanceof PotionMeta) {
            PotionMeta pm = (PotionMeta) itemMeta;
            if (pm.hasCustomEffects())
                putEffects(params, "potion-effects", pm.getCustomEffects());
        }
        if (itemMeta instanceof MapMeta) {
            MapMeta mm = (MapMeta) itemMeta;
            if (mm.isScaling())
                put(params, "map-scale", "true");
        }

        if (itemMeta instanceof EnchantmentStorageMeta) {
            EnchantmentStorageMeta esm = (EnchantmentStorageMeta) itemMeta;
            if (esm.hasStoredEnchants())
                putEnchants(params, "stored-enchants", esm.getStoredEnchants());
        }

        if (itemMeta instanceof BannerMeta)
            putBannerMeta(params, (BannerMeta) itemMeta);

        if (itemMeta instanceof FireworkEffectMeta)
            putFireworkEffectMeta(params, (FireworkEffectMeta) itemMeta);
    }


    private void putBannerMeta(Map<String, String> params, BannerMeta bm) {
        put(params, "color", bm.getBaseColor().name());
        StringBuilder sb = new StringBuilder();
        for (org.bukkit.block.banner.Pattern p : bm.getPatterns()) {
            if (sb.length() > 0)
                sb.append(";");
            sb.append(p.getPattern().name()).append(":")
                    .append(p.getColor().name());
        }
        if (sb.length() > 0)
            put(params, "patterns", sb.toString());
    }

    protected static double getColorDistance(Color c1, Color c2) {
        double rmean = (c1.getRed() + c2.getRed()) / 2.0;
        double r = c1.getRed() - c2.getRed();
        double g = c1.getGreen() - c2.getGreen();
        int b = c1.getBlue() - c2.getBlue();
        double weightR = 2 + rmean / 256.0;
        double weightG = 4.0;
        double weightB = 2 + (255 - rmean) / 256.0;
        return weightR * r * r + weightG * g * g + weightB * b * b;
    }

    protected static DyeColor getClosestColor(Color color) {
        int index = 0;
        double best = -1;
        for (int i = 0; i < DyeColor.values().length; i++) {
            double distance = getColorDistance(color,
                    DyeColor.values()[i].getColor());
            if (distance < best || best == -1) {
                best = distance;
                index = i;
            }
        }
        return DyeColor.values()[index];
    }

    /**
     * Deserialize banner parameters
     *
     * @param params
     */
    private void setBanner(Map<String, String> params) {
        if (!(this.getItemMeta() instanceof BannerMeta))
            return;
        BannerMeta bm = (BannerMeta) this.getItemMeta();
        String colorStr = getParam(params, "color");
        if (colorStr != null && !colorStr.isEmpty()) {
            DyeColor dc = parseDyeColor(colorStr);
            if (dc != null)
                bm.setBaseColor(dc);
        }


        String patternStr = getParam(params, "patterns");
        if (patternStr != null && !patternStr.isEmpty()) {
            String[] ln = patternStr.split(";");
            for (String pStr : ln) {
                String pattern = pStr;
                String dc = "";
                if (pStr.contains(":")) {
                    dc = new String(pStr.substring(pStr.indexOf(":") + 1));
                    pattern = new String(pStr.substring(0, pStr.indexOf(":")));
                }

                PatternType pType = PatternType.getByIdentifier(pattern
                        .toUpperCase());
                if (pType == null) {
                    for (PatternType p : PatternType.values())
                        if (p.name().equalsIgnoreCase(pattern))
                            pType = p;
                }

                if (pType == null)
                    continue;
                DyeColor c = parseDyeColor(dc);
                if (c == null)
                    continue;
                bm.addPattern(new org.bukkit.block.banner.Pattern(c, pType));
            }
        }
        this.setItemMeta(bm);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void setColor(String colorStr) {
        if (colorStr == null || colorStr.isEmpty()) return;

        if (this.getItemMeta() instanceof LeatherArmorMeta) {
            Color c = parseColor(colorStr);
            if (c == null) return;
            LeatherArmorMeta lm = (LeatherArmorMeta) this.getItemMeta();
            lm.setColor(c);
            this.setItemMeta(lm);
        } else {
            DyeColor dc = parseDyeColor(colorStr);
            if (dc == null) return;
            switch (this.getType()) {
                case WOOL:
                case STAINED_GLASS:
                case STAINED_CLAY:
                case STAINED_GLASS_PANE:
                    this.setDurability(dc.getWoolData());
                    break;
                case INK_SACK:
                    this.setDurability(dc.getDyeData());
                default:
                    break;
            }
        }
    }

}
