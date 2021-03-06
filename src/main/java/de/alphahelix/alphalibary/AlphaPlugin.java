/*
 *     Copyright (C) <2016>  <AlphaHelixDev>
 *
 *     This program is free software: you can redistribute it under the
 *     terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.alphahelix.alphalibary;

import org.bukkit.plugin.java.JavaPlugin;

public class AlphaPlugin extends JavaPlugin {

    private AlphaPlugin plugin;
    private String prefix;

    @Override
    public void onEnable() {
        setPluginInstance(this);
    }

    /**
     * @return the plugin
     */
    public AlphaPlugin getPluginInstance() {
        return this.plugin;
    }

    /**
     * @param plugin the plugin to set
     */
    private void setPluginInstance(AlphaPlugin plugin) {
        this.plugin = plugin;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
