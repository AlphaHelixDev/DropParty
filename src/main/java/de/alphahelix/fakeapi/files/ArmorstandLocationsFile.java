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

package de.alphahelix.fakeapi.files;

import de.alphahelix.alphalibary.file.SimpleFile;
import de.alphahelix.fakeapi.FakeAPI;
import org.bukkit.Location;

import java.util.HashMap;

public class ArmorstandLocationsFile extends SimpleFile<FakeAPI> {

    public ArmorstandLocationsFile(FakeAPI pl) {
        super("plugins/FakeAPI", "fake_armorstand.ht", pl);
    }

    /**
     * Adds a new {@link de.alphahelix.fakeapi.instances.FakeArmorstand} to the file
     *
     * @param loc {@link Location} where the {@link de.alphahelix.fakeapi.instances.FakeArmorstand} is located at
     * @param name of the {@link de.alphahelix.fakeapi.instances.FakeArmorstand}
     */
    public void addArmorstandToFile(Location loc, String name) {
        if (!configContains(name)) {
            setLocation(name.replace(" ", "_").replace("§", "&"), loc);
        }
    }

    /**
     * Gets all {@link de.alphahelix.fakeapi.instances.FakeArmorstand} from the file and returns it as a {@link HashMap}
     *
     * @return the {@link HashMap} with the name as keys and {@link Location}s as values
     */
    public HashMap<String, Location> getPacketArmorstand() {
        HashMap<String, Location> standsMap = new HashMap<>();

        for (String names : getKeys(false)) {
            standsMap.put(names.replace("_", " ").replace("&", "§"), getLocation(names));
        }
        return standsMap;
    }
}
