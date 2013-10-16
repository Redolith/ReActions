/*  
 *  ReActions, Minecraft bukkit plugin
 *  (c)2012-2013, fromgate, fromgate@gmail.com
 *  http://dev.bukkit.org/server-mods/reactions/
 *   * 
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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import me.fromgate.reactions.RAUtil;
import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.activators.Activator.ActVal;
import me.fromgate.reactions.activators.Activator.FlagVal;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;



public class Activators {

    ReActions plg;
    RAUtil u;
    static ReActions plugin;
    List<Activator> act;
    Map<String,YamlConfiguration> ymls;


    public Activators (ReActions reactions){
        this.plg = reactions;
        plugin = reactions;
        this.act = new ArrayList<Activator>();
        this.ymls = new HashMap<String,YamlConfiguration>();
        this.u = plg.getUtils();
        this.loadActivators();
    }


    public void loadActivators() {
        List<String> groups = findGroupsInDir();
        if (!groups.isEmpty())
            for (String group : groups)
                loadActivators(group);
    }

    private List<String> findGroupsInDir(){
        List<String> grps = new ArrayList<String>();
        File dir = new File (plg.getDataFolder() + File.separator+"Activators"+File.separator);
        if (!dir.exists()) dir.mkdirs();
        for (String fstr : dir.list())
            if (fstr.endsWith(".yml")) grps.add(fstr.substring(0, fstr.length()-4));
        return grps;
    }

    public boolean contains(String name){
        boolean rst = false;
        for (Activator a : act){
            if (a.equals(name)) return true;
        }
        return rst;
    }

    public int size(){
        return act.size();
    }

    public void clear(){
        act.clear();
    }

    public List<Activator> getActivatorInLocation(World world, int x, int y, int z){
        return getActivatorInLocation (new Location (world, x, y, z));
    }

    public List<Activator> getActivatorInLocation(Location loc){
        List<Activator> found = new ArrayList<Activator> ();
        for (Activator a : act)	
            if (a.isLocatedAt(loc))
                found.add(a);
        return found;
    }

    public boolean addActivator (Activator a){
        if (contains(a.name)) return false;
        act.add(a);
        return true;
    }

    public void removeActivator (String name){
        if (act.isEmpty()) return;
        for (int i=act.size()-1; i>=0;i--)
            if (act.get(i).equals(name)) act.remove(i);
    }

    public Activator get(String name){
        for (Activator a : act)
            if (a.equals(name)) return a;
        return null;
    }

    public boolean clearFlags(String name){
        Activator a = get(name);
        if (a == null) return false;
        a.clearFlags();
        return true;
    }

    public boolean clearActions(String name){
        Activator a = get(name);
        if (a == null) return false;
        a.clearActions();
        return true;
    }

    public boolean clearReactions(String name){
        Activator a = get(name);
        if (a == null) return false;
        a.clearReactions();
        return true;
    }

    public boolean addFlag(String activator, String flag, String param,boolean not){
        Activator a =  get(activator);
        if (a == null) return false;
        a.addFlag(flag, param, not);
        return true;
    }

    public boolean addAction(String activator, String action, String param){
        Activator a =  get(activator);
        if (a == null) return false;
        a.addAction(action, param);
        return true;
    }

    public boolean addReaction(String activator, String action, String param){
        Activator a =  get(activator);
        if (a == null) return false;
        a.addReaction(action, param);
        return true;
    }

    public void saveActivators(){
        File dir = new File (plg.getDataFolder() + File.separator+"Activators"+File.separator);
        if (!dir.exists()) dir.mkdirs();
        for (File f : dir.listFiles())
            f.delete();
        for (String group : findGroupsFromActivators())
            saveActivators(group);
    }

    public Set<String> findGroupsFromActivators(){
        Set<String> grps = new HashSet<String>();
        for (Activator a : act)
            grps.add(a.getGroup());
        return grps;
    }

    public void saveActivators(String group){
        File f = new File (plg.getDataFolder()+File.separator+"Activators"+File.separator+group+".yml");
        try {
            if (f.exists()) f.delete();
            f.createNewFile();	
        } catch (Exception e){
            u.log("Failed to create configuration to file "+f.getAbsolutePath());
            e.printStackTrace();
            return;			
        }
        YamlConfiguration cfg = new YamlConfiguration();
        for (Activator a : act){
            if (a.group.equalsIgnoreCase(group)) a.saveActivator(cfg);
        }


        try {
            cfg.save(f);
        } catch (Exception e){
            u.log("Failed to save configuration to file "+f.getAbsolutePath());
            e.printStackTrace();
            return;			
        }
    }


    public void loadActivators(String group){
        File f = new File (plg.getDataFolder()+File.separator+"Activators"+File.separatorChar+group+".yml");
        if (!f.exists()) return;
        YamlConfiguration cfg = new YamlConfiguration();
        try {
            cfg.load(f);
        } catch (Exception e) {
            u.log("Failed to load configuration from file "+f.getAbsolutePath());
            e.printStackTrace();
            return;
        }

        for (String type : cfg.getKeys(false)){
            //if (!isValidActivatorType(type)) continue;
            if (!ActivatorType.isValid(type)) continue;
            ConfigurationSection cs = cfg.getConfigurationSection(type);
            if (cs == null) continue;
            for (String name : cs.getKeys(false)){
                if (ActivatorType.isValid(type)){
                    Activator a = createActivator (type,name,group,cfg);
                    if (a==null) continue;
                    addActivator (a);
                }
            }
        }
    }

    private Activator createActivator (String type, String name, String group, YamlConfiguration cfg){
        try{
            Object a = ActivatorType.valueOf(type.toUpperCase()).getActivatorClass().getDeclaredConstructor(String.class,String.class,YamlConfiguration.class).newInstance(name,group,cfg);
            return (Activator) a;
        } catch (Exception e){
            u.logOnce("cannotcreate"+type, "Failed to create new activator. Type: "+type);
        }
        return null;
    }

    public List<String> getActivatorsList(){
        List<String> lst = new ArrayList<String>();
        if (!act.isEmpty())
            for (int i = 0; i<act.size();i++)
                lst.add( "&a"+act.get(i).toString());
        return lst;
    }

    public List<String> getActivatorsList(String type){
        List<String> lst = new ArrayList<String>();
        if (!act.isEmpty())
            for (int i = 0; i<act.size();i++)
                if (act.get(i).getType().equalsIgnoreCase(type))
                    lst.add( "&a"+act.get(i).toString());
        return lst;	
    }

    public List<String> getActivatorsListGroup(String group){
        List<String> lst = new ArrayList<String>();
        if (!act.isEmpty())
            for (int i = 0; i<act.size();i++)
                if (act.get(i).getGroup().equalsIgnoreCase(group))
                    lst.add( "&a"+act.get(i).toString());
        return lst;			
    }


    public void activate (Event event){
        if (act.isEmpty()) return;
        for (int i = 0; i<act.size(); i++){
            Activator a = act.get(i);
            if (!ActivatorType.isValid(a.getType())) continue;
                if(ActivatorType.valueOf(a.getType().toUpperCase()).getEventClass().isInstance(event))
                a.activate(event);
        }
    }


    public boolean copyAll (String actfrom, String actto){
        if (!contains(actfrom)) return false;
        if (!contains(actto)) return false;
        copyActions (actfrom,actto);
        copyReactions (actfrom,actto);
        copyFlags (actfrom,actto);
        return true;
    }

    public boolean copyActions (String actfrom, String actto){
        if (!contains(actfrom)) return false;
        if (!contains(actto)) return false;
        Activator afrom = get(actfrom);
        Activator ato = get(actto); 
        ato.clearActions();
        if (!afrom.getActions().isEmpty()){
            for (ActVal action : afrom.getActions())
                ato.addAction(action.flag, action.value);
        }
        return true;
    }

    public boolean copyReactions (String actfrom, String actto){
        if (!contains(actfrom)) return false;
        if (!contains(actto)) return false;
        Activator afrom = get(actfrom);
        Activator ato = get(actto); 
        ato.clearReactions();
        if (!afrom.getReactions().isEmpty()){
            for (ActVal action : afrom.getReactions())
                ato.addReaction(action.flag, action.value);
        }
        return true;
    }

    public boolean copyFlags (String actfrom, String actto){
        if (!contains(actfrom)) return false;
        if (!contains(actto)) return false;
        Activator afrom = get(actfrom);
        Activator ato = get(actto); 
        ato.clearFlags();
        if (!afrom.getFlags().isEmpty()){
            for (FlagVal flag : afrom.getFlags())
                ato.addFlag(flag.flag, flag.value, flag.not);
        }
        return true;
    }

    public boolean setGroup (String activator, String group){
        if (!contains(activator)) return false;
        get (activator).setGroup(group);
        return true;
    }

    public String getGroup(String activator){
        if (!contains(activator)) return "activator";
        return get (activator).getGroup();
    }
    
    

}
