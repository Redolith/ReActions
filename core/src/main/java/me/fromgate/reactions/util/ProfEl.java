/*  
 *  ReActions, Minecraft bukkit plugin
 *  (c)2012-2014, fromgate, fromgate@gmail.com
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

package me.fromgate.reactions.util;


public class ProfEl {
    long tick_num;
    String process;
    long execution_time;
    String comment;

    //
    private long start_time;
    private long stop_time;

    public ProfEl(String process, long tick_count) {
        this.process = process;
        this.tick_num = tick_count;
        this.start_time = System.nanoTime();
    }

    public void stopCount(String comment) {
        this.comment = comment;
        this.stop_time = System.nanoTime();
        this.execution_time = stop_time - start_time;
    }

}