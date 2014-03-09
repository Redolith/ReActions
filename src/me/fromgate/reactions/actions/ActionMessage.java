package me.fromgate.reactions.actions;

import java.util.Map;
import java.util.Set;
import me.fromgate.reactions.util.ParamUtil;
import me.fromgate.reactions.util.Util;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class ActionMessage extends Action {

    @Override
    public boolean execute(Player p, Map<String, String> params) {
        sendMessage (p,params);
        return true;
    }
    
    
    private void sendMessage(Player player, Map<String, String> params){
        Set<Player> players = Util.getPlayerList(params,player);
        String message = removeParams (params);
        if (message.isEmpty()) return;
        
        for (Player p : players){
            String key = "reactions-msg-"+this.getActivatorName()+(this.isAction() ? "act" : "react");    
            boolean showmsg = false;
            if (this.getActivator().isAnnoying()){
                if (!p.hasMetadata(key)) {
                    showmsg = true;
                    p.setMetadata(key, new FixedMetadataValue(plg(),System.currentTimeMillis()));
                } else {
                    Long before = p.getMetadata(key).get(0).asLong();
                    Long now = System.currentTimeMillis();
                    if ((now-before)>(plg().sameMessagesDelay*1000)){
                        showmsg = true;
                        p.setMetadata(key, new FixedMetadataValue(plg(),now));
                    }
                }
            } else showmsg = true;
            if (showmsg) u().printMsg(p, message);
        }
    }
    
	private String removeParams(Map<String, String> params){
		String message = ParamUtil.getParam(params, "param-line", "");
		if (message.isEmpty()) return message;
		if (params.size()<=1) return message;
		String [] msgArray = message.split(" ");
		for (String key : params.keySet()){
			for (int i = 0; i<msgArray.length; i++){
				String msgPart = msgArray[i].toLowerCase();
				if (msgPart.startsWith(key.toLowerCase()+":")) msgArray[i]="";
			}
		}
		String newMessage = "";
		for (int i = 0; i<msgArray.length; i++){
			if (msgArray[i].isEmpty()) continue;
			newMessage = newMessage.isEmpty() ? msgArray[i] : newMessage+" "+msgArray[i];
		}
		return newMessage;
	}


}
