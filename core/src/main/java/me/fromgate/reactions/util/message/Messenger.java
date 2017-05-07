/*
 *  ReActions, Minecraft bukkit plugin
 *  (c)2012-2017, fromgate, fromgate@gmail.com
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

package me.fromgate.reactions.util.message;

import java.util.Map;

public interface Messenger {

    String colorize(String text);

    boolean broadcast(String colorize);

    boolean log(String text);

    String clean(String text);

    boolean tip(int seconds, Object sender, String text);

    boolean tip(Object sender, String text);

    boolean print(Object sender, String text);

    boolean broadcast(String permission, String text);

    String toString(Object obj, boolean fullFloat);

    Map<String, String> load(String language);

    void save(String langugage, Map<String, String> message);

    boolean isValidSender(Object send);

}
