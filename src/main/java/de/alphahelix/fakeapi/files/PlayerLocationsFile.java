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

import de.alphahelix.alphalibary.UUID.UUIDFetcher;
import de.alphahelix.alphalibary.file.SimpleFile;
import de.alphahelix.fakeapi.FakeAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import java.util.HashMap;

public class PlayerLocationsFile extends SimpleFile<FakeAPI> {

    public PlayerLocationsFile(FakeAPI pl) {
        super("plugins/FakeAPI", "fake_players.ht", pl);
    }

    /**
     * Adds a new {@link de.alphahelix.fakeapi.instances.FakePlayer} to the file
     *
     * @param loc  {@link Location} where the {@link de.alphahelix.fakeapi.instances.FakePlayer} is located at
     * @param name of the {@link de.alphahelix.fakeapi.instances.FakePlayer}
     * @param skin Name of the {@link OfflinePlayer} (skin of the {@link de.alphahelix.fakeapi.instances.FakePlayer})
     */
    public void addPlayerToFile(Location loc, String name, String skin) {
        if (!configContains(name)) {
            setDefault(name.replace(" ", "_").replace("§", "&") + ".skin", skin);
            setLocation(name.replace(" ", "_").replace("§", "&") + ".loc", loc);
        }
    }

    /**
     * Gets all {@link Location}s of the {@link de.alphahelix.fakeapi.instances.FakePlayer}s from the file and returns it as a {@link HashMap}
     *
     * @return the {@link HashMap} with the name as keys and {@link Location}s as values
     */
    public HashMap<String, Location> getPacketPlayerLocations() {
        HashMap<String, Location> locationMap = new HashMap<>();

        for (String names : getKeys(false)) {
            locationMap.put(names.replace("_", " ").replace("&", "§"), getLocation(names + ".loc"));
        }
        return locationMap;
    }

    /**
     * Gets all {@link OfflinePlayer}s of the {@link de.alphahelix.fakeapi.instances.FakePlayer}s from the file and returns it as a {@link HashMap}
     *
     * @return the {@link HashMap} with the name as keys and {@link OfflinePlayer}s as values
     */
    public HashMap<String, OfflinePlayer> getPacketPlayerSkins() {
        HashMap<String, OfflinePlayer> skinMap = new HashMap<>();

        for (String names : getKeys(false)) {
            skinMap.put(names.replace("_", " ").replace("&", "§"), Bukkit.getOfflinePlayer(UUIDFetcher.getUUID(getString(names + ".skin"))));
        }
        return skinMap;
    }

}
