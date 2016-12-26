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

public class EndercrystalLocationsFile extends SimpleFile<FakeAPI> {

    public EndercrystalLocationsFile(FakeAPI pl) {
        super("plugins/FakeAPI", "fake_endercrystals.ht", pl);
    }

    /**
     * Adds a new {@link de.alphahelix.fakeapi.instances.FakeEndercrystal} to the file
     *
     * @param loc  {@link Location} where the {@link de.alphahelix.fakeapi.instances.FakeEndercrystal} is located at
     * @param name of the {@link de.alphahelix.fakeapi.instances.FakeEndercrystal}
     */
    public void addEndercrystalToFile(Location loc, String name) {
        if (!configContains(name)) {
            setLocation(name.replace(" ", "_").replace("§", "&"), loc);
        }
    }

    /**
     * Gets all {@link de.alphahelix.fakeapi.instances.FakeEndercrystal} from the file and returns it as a {@link HashMap}
     *
     * @return the {@link HashMap} with the name as keys and {@link Location}s as values
     */
    public HashMap<String, Location> getPacketEndercrystal() {
        HashMap<String, Location> crystalMap = new HashMap<>();

        for (String names : getKeys(false)) {
            crystalMap.put(names.replace("_", " ").replace("&", "§"), getLocation(names));
        }
        return crystalMap;
    }
}
