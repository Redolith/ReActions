/*  
 *  ReActions, Minecraft bukkit plugin
 *  (c)2012-2013, fromgate, fromgate@gmail.com
 *  http://dev.bukkit.org/server-mods/reactions/
 *   
 *  This file is part of ReActions.
 *  
 *  ReActions is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  ReActions is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with ReActions.  If not, see <http://www.gnorg/licenses/>.
 * 
 */

package me.fromgate.reactions.activators;

import java.util.ArrayList;
import java.util.List;
import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.flags.Flags;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;

public abstract class Activator {

    String name;
    String group;

    public Activator(String name, String group){
        this.name = name;
        this.group = group;
    }
    
    public Activator(String name, String group,YamlConfiguration cfg){
        this.name = name;
        this.loadActivator(cfg);
        this.group = group;
    }

    private List<FlagVal> flags = new ArrayList<FlagVal>();
    private List<ActVal> actions = new ArrayList<ActVal>();
    private List<ActVal> reactions = new ArrayList<ActVal>();

    public void addFlag(String flag, String param, boolean not){
        flags.add(new FlagVal(Flags.getValidName(flag),param,not));
    }

    public boolean removeFlag (int index){
        if (flags.size()<=index) return false;
        flags.remove(index);
        return true;
    }
    public List<FlagVal> getFlags(){
        return flags;
    }

    public void addAction (String action, String param){
        actions.add(new ActVal (Actions.getValidName(action),param));
    }

    public boolean removeAction (int index){
        if (actions.size()<=index) return false;
        actions.remove(index);
        return true;
    }

    public void addReaction (String action, String param){
        reactions.add(new ActVal (Actions.getValidName(action),param));
    }

    public boolean removeReaction (int index){
        if (reactions.size()<=index) return false;
        reactions.remove(index);
        return true;
    }

    public List<ActVal> getActions(){
        return actions;
    }

    public List<ActVal> getReactions(){
        return reactions;
    }


    public void clearFlags(){
        flags.clear();
    }

    public void clearActions(){
        actions.clear();
    }

    public void clearReactions(){
        reactions.clear();
    }


    /*
     * Надо будет вынести в отдельный файл
     */
    public class ActVal{
        public String flag;
        public String value;
        public ActVal(String f, String v){
            this.flag =f;
            this.value = v;
        }
        public ActVal(String f){
            this.flag =f;
            this.value = "";
        }
        @Override
        public String toString() {
            return flag + "=" + value;
        }
    }

    /*
     * Надо будет вынести в отдельный файл
     */
    public class FlagVal{
        public String flag;
        public String value;
        public boolean not;

        public FlagVal(String f, String v, boolean not){
            this.flag =f;
            this.value = v;
            this.not = not;
        }

        @Override
        public String toString() {
            String str =flag + "=" + value;
            if (this.not) str = "!"+str;
            return str; 
        }
    }

    @Override
    public String toString(){
        return name+" ["+getType()+ "] F:"+this.flags.size()+" A:"+this.actions.size()+" R:"+this.reactions.size();
    }

    @Override
    public int hashCode() {
        /* Надо будет переделать так чтобы получалась сортировка по алфавиту, хм.. и группировка по группам сразу...
         */
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }
    public boolean equals(String name){
        if (name == null) return false;
        if (name.isEmpty()) return false;
        return this.name.equalsIgnoreCase(name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Activator))
            return false;
        Activator other = (Activator) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!this.name.equals(other.name))
            return false;
        return true;
    }

    /*
     *  Группу по идее дублировать не надо... т.е. она должна выставляться "из вне"...
     */
    public void saveActivator(YamlConfiguration cfg){
        String key = getType()+"."+this.name;
        save (key, cfg);
        List<String> flg = new ArrayList<String>();
        if (!flags.isEmpty())
            for (int i = 0; i<flags.size(); i++)
                flg.add(flags.get(i).toString());
        cfg.set(key+".flags", flg);

        flg = new ArrayList<String>();
        if (!actions.isEmpty())
            for (int i = 0; i<actions.size(); i++)
                flg.add(actions.get(i).toString());
        cfg.set(key+".actions", flg);

        flg = new ArrayList<String>();
        if (!reactions.isEmpty())
            for (int i = 0; i<reactions.size(); i++)
                flg.add(reactions.get(i).toString());
        cfg.set(key+".reactions", flg);
    }

    public void loadActivator (YamlConfiguration cfg){
        String key = getType().name()+"."+this.name;
        load (key, cfg);
        List<String> flg = cfg.getStringList(key+".flags");
        for (String flgstr : flg){
            String flag = flgstr;
            String param = "";
            boolean not = false;
            if (flgstr.contains("=")){
                flag  = flgstr.substring(0, flgstr.indexOf("="));
                if (flgstr.indexOf("=")<flgstr.length())
                    param = flgstr.substring(flgstr.indexOf("=")+1,flgstr.length());
            }
            if (flag.startsWith("!")){
                flag=flag.replaceFirst("!", "");
                not = true;
            }
            addFlag(flag, param, not);
        }

        flg = cfg.getStringList(key+".actions");
        for (String flgstr : flg){
            String flag = flgstr;
            String param = "";
            if (flgstr.contains("=")){
                flag  = flgstr.substring(0, flgstr.indexOf("="));
                param = flgstr.substring(flgstr.indexOf("=")+1,flgstr.length());
            }
            addAction(flag, param);
        }

        flg = cfg.getStringList(key+".reactions");
        for (String flgstr : flg){
            String flag = flgstr;
            String param = "";
            if (flgstr.contains("=")){
                flag  = flgstr.substring(0, flgstr.indexOf("="));
                param = flgstr.substring(flgstr.indexOf("=")+1,flgstr.length());
            }
            addReaction(flag, param);
        }
    }

    public void setGroup (String group){
        this.group = group;
    }

    public String getGroup(){
        return this.group;
    }

    public boolean isAnnoying(){
        return false;
    }

    public String getName(){
        return this.name;
    }

    /*public boolean isTypeOf (String str){
        return str.equalsIgnoreCase(getType());
    }*/
    
    public void executeActivator(final Event event){
        Bukkit.getScheduler().runTask(ReActions.instance, new Runnable(){
            @Override
            public void run() {
                activate(event);
            }
        });
    }

    public abstract void activate(Event event); // Наверное всё-таки так
    public abstract boolean isLocatedAt (Location loc);
    public abstract void save(String root, YamlConfiguration cfg);
    public abstract void load(String root, YamlConfiguration cfg);
    //public abstract String getType();
    public abstract ActivatorType getType();
    public boolean isTypeOf(String str){
        return ((getType().name().equalsIgnoreCase(str))||(getType().getAlias().equalsIgnoreCase(str))); 
    }

    public String getTargetPlayer() {
        return "%targetplayer%";
    }

}
