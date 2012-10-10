/*  
 *  ReActions, Minecraft bukkit plugin
 *  (c)2012, fromgate, fromgate@gmail.com
 *  http://dev.bukkit.org/server-mods/weatherman/
 *   * 
 *  This file is part of ReActions.
 *  
 *  WeatherMan is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  WeatherMan is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with WeatherMan.  If not, see <http://www.gnorg/licenses/>.
 * 
 */

package fromgate.reactions;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.World;

public class Activator {

	String world;
	int x;
	int y;
	int z;
	
	
	/* нужно ли его делать универсальным? 
	 * или будут массивы кнопок, регионов, карт, книг????
	 * 
	 */
	
	boolean reqflag = false;
	
	//HashMap<String,String> flags = new HashMap<String,String>();     //флаг (условия)
	List<FlagVal> flags = new ArrayList<FlagVal>();
	
	List<ActVal> actions = new ArrayList<ActVal>();
	List<ActVal> reactions = new ArrayList<ActVal>();
	

	//HashMap<String,String> actions = new HashMap<String,String>();   //действия в случае успешной выполнения условий флагов
	//HashMap<String,String> reactions = new HashMap<String,String>(); //действия в случае неуспешной выполнения условий флагов

	public Activator(Location loc){
		this.world = loc.getWorld().getName();
		this.x = loc.getBlockX();
		this.y = loc.getBlockY();
		this.z = loc.getBlockZ();
	}
	
	public Activator(String world, int x, int y, int z){
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public boolean equalLoc (Location loc){
		return 	(loc.getWorld().getName().equalsIgnoreCase(this.world)&&
				(loc.getBlockX()==this.x)&&
				(loc.getBlockY()==this.y)&&
				(loc.getBlockZ()==this.z));
	}
	
	public boolean equalWXYZ (World w, int x, int y, int z){
		return 	(w.getName().equalsIgnoreCase(this.world)&&
				(x==this.x)&&
				(y==this.y)&&
				(z==this.z));
	}

	
	public class ActVal{
		String flag;
		String value;
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

	
	public class FlagVal{
		String flag;
		String value;
		boolean not;
		
		public FlagVal(String f, String v, boolean not){
			this.flag =f;
			this.value = v;
			this.not = true;
		}
		
		
		
		@Override
		public String toString() {
			String str =flag + "=" + value;
			if (this.not) str = "!"+str;
			return str; 
		}
	}


	public void addFlag (String flg, String param, boolean not){
		this.flags.add(new FlagVal (flg, param, not));
	}
	
	public void addFlag (String flgstr){
		if (!flgstr.isEmpty()){
			boolean not = flgstr.startsWith("!");
			String [] fl = (flgstr.replaceFirst("!", "")).split("="); 
			if (fl.length==2) flags.add(new FlagVal (fl[0],fl[1],not));
		}
	}



	public void addAction (String act, String param){
		this.actions.add(new ActVal (act,param));
	}
	
	public void addReAction (String act, String param){
		this.reactions.add(new ActVal (act,param));
	}

	/*public String [] toBookCfg(){
		/* [FLAG]
		 * x=y
		 * [ACTION]
		 * xx=yy
		 * [REACTION]
		 * xxx=yyy
		 *  /
		String [] cfg = new  String [3+flags.size()+actions.size()+reactions.size()];
		int i = 0;
		cfg[i] = "[FLAGS]";
		if (flags.size()>0)
			for (String key : flags.keySet()){
				i++;
				cfg[i]=key+"="+flags.get(key);
			}
		i++;
		cfg[i]="[ACTION]";
		if (actions.size()>0)
			for (int j=0; j<actions.size();j++){
				i++;
				cfg[i]=actions.get(j).flag+"="+actions.get(j).value;	
			}
		
		i++;
		cfg[i]="[REACTION]";
		if (reactions.size()>0)
			for (int j=0; j<reactions.size();j++){
				i++;
				cfg[i]=reactions.get(j).flag+"="+reactions.get(j).value;
			}
		return cfg;
	}*/
	
	@Override
	public String toString() {

		return "[" + world + "] (" + x + ", " + y + ", " + z
				+ ") F:"+this.flags.size()+" A:"+this.actions.size()+" R:"+this.reactions.size();
	}

	


}
