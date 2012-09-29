package fromgate.reactions;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;

public class Clicker {

	String world;
	int x;
	int y;
	int z;
	
	
	/* нужно ли его делать универсальным? 
	 * или будут массивы кнопок, регионов, карт, книг????
	 * 
	 */
	
	boolean reqflag = false;
	
	HashMap<String,String> flags = new HashMap<String,String>();     //флаг (условия)
	
	ArrayList<FlagVal> actions = new ArrayList<FlagVal>();
	ArrayList<FlagVal> reactions = new ArrayList<FlagVal>();
	

	//HashMap<String,String> actions = new HashMap<String,String>();   //действия в случае успешной выполнения условий флагов
	//HashMap<String,String> reactions = new HashMap<String,String>(); //действия в случае неуспешной выполнения условий флагов

	public Clicker(Location loc){
		this.world = loc.getWorld().getName();
		this.x = loc.getBlockX();
		this.y = loc.getBlockY();
		this.z = loc.getBlockZ();
	}
	
	public Clicker(String world, int x, int y, int z){
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
	
	public class FlagVal{
		String flag;
		String value;
		public FlagVal(String f, String v){
			this.flag =f;
			this.value = v;
		}
		public FlagVal(String f){
			this.flag =f;
			this.value = "";
		}
		@Override
		public String toString() {
			return flag + "=" + value;
		}
		
		
		
	}


	public void addAction (String act, String param){
		this.actions.add(new FlagVal (act,param));
	}
	
	public void addReAction (String act, String param){
		this.reactions.add(new FlagVal (act,param));
	}

	public String [] toBookCfg(){
		/* [FLAG]
		 * x=y
		 * [ACTION]
		 * xx=yy
		 * [REACTION]
		 * xxx=yyy
		 */
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
	}
	
	@Override
	public String toString() {

		return "[" + world + "] (" + x + ", " + y + ", " + z
				+ ")"+"F:"+this.flags.size()+" A:"+this.actions.size()+" R:"+this.reactions.size();
	}

	


}
