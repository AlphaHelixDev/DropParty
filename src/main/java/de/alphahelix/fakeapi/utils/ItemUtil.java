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

package de.alphahelix.fakeapi.utils;

import de.alphahelix.alphalibary.reflection.ReflectionUtil;
import de.alphahelix.fakeapi.FakeAPI;
import de.alphahelix.fakeapi.Register;
import de.alphahelix.fakeapi.instances.FakeItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;

public class ItemUtil extends UtilBase {

    private static Constructor<?> entityItem;

    static {
        try {
            entityItem = ReflectionUtil.getNmsClass("EntityItem")
                    .getConstructor(ReflectionUtil.getNmsClass("World"),
                            double.class, double.class, double.class, ReflectionUtil.getNmsClass("ItemStack"));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * Spawns in a {@link FakeItem} for every {@link Player} on the server
     *
     * @param loc  {@link Location} where the {@link FakeItem} should be spawned at
     * @param name of the {@link FakeItem} inside the file and above his head
     * @param type the {@link Material} which should be spawned
     */
    public static void spawnItemForAll(Location loc, String name, Material type) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            spawnItemForPlayer(p, loc, name, type);
        }
    }

    /**
     * Spawns in a {@link FakeItem} for the {@link Player}
     *
     * @param p    the {@link Player} to spawn the {@link FakeItem} for
     * @param loc  {@link Location} where the {@link FakeItem} should be spawned at
     * @param name of the {@link FakeItem} inside the file and above his head
     * @param type the {@link Material} which should be spawned
     * @return the new spawned {@link FakeItem}
     */
    public static FakeItem spawnItemForPlayer(Player p, Location loc, String name, Material type) {
        try {
            Object item = entityItem.newInstance(ReflectionUtil.getWorldServer(p.getWorld())
                    , loc.getX()
                    , loc.getY()
                    , loc.getZ()
                    , ReflectionUtil.getObjectNMSItemStack(new ItemStack(type)));

            Object dw = getDataWatcher().invoke(item);

            watch().invoke(dw, 10, ReflectionUtil.getObjectNMSItemStack(new ItemStack(type)));

            update().invoke(dw, 10);

            ReflectionUtil.sendPacket(p, getPacketPlayOutSpawnEntity().newInstance(item, 2));

            ReflectionUtil.sendPacket(p, getPacketPlayOutEntityMetadata().newInstance(ReflectionUtil.getEntityID(item), dw, true));

            Register.getItemLocationsFile().addItemToFile(loc, name, type);
            FakeAPI.addFakeItem(p, new FakeItem(loc, name, item, type));
            return new FakeItem(loc, name, item, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Spawns in a temporary {@link FakeItem} (disappears after rejoin) for every {@link Player} on the server
     *
     * @param loc  {@link Location} where the {@link FakeItem} should be spawned at
     * @param name of the {@link FakeItem} inside the file and above his head
     * @param type the {@link Material} which should be spawned
     */
    public static void spawnTemporaryItemForAll(Location loc, String name, Material type) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            spawnTemporaryItemForPlayer(p, loc, name, type);
        }
    }

    /**
     * Spawns in a temporary {@link FakeItem} (disappears after rejoin) for the {@link Player}
     *
     * @param p    the {@link Player} to spawn the {@link FakeItem} for
     * @param loc  {@link Location} where the {@link FakeItem} should be spawned at
     * @param name of the {@link FakeItem} inside the file and above his head
     * @param type the {@link Material} which should be spawned
     * @return the new spawned {@link FakeItem}
     */
    public static FakeItem spawnTemporaryItemForPlayer(Player p, Location loc, String name, Material type) {
        try {
            Object item = entityItem.newInstance(ReflectionUtil.getWorldServer(p.getWorld())
                    , loc.getX()
                    , loc.getY()
                    , loc.getZ()
                    , ReflectionUtil.getObjectNMSItemStack(new ItemStack(type)));

            Object dw = getDataWatcher().invoke(item);

            watch().invoke(dw, 10, ReflectionUtil.getObjectNMSItemStack(new ItemStack(type)));
            update().invoke(dw, 10);

            ReflectionUtil.sendPacket(p, getPacketPlayOutSpawnEntity().newInstance(item, 2));
            ReflectionUtil.sendPacket(p, getPacketPlayOutEntityMetadata().newInstance(ReflectionUtil.getEntityID(item), dw, true));

            FakeAPI.addFakeItem(p, new FakeItem(loc, name, item, type));
            return new FakeItem(loc, name, item, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Removes a {@link FakeItem} from the {@link org.bukkit.World} for every {@link Player}
     *
     * @param item the {@link FakeItem} to remove
     */
    public static void destroyItemForAll(FakeItem item) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            destroyItemForPlayer(p, item);
        }
    }

    /**
     * Removes a {@link FakeItem} for on {@link Player} from the {@link org.bukkit.World}
     *
     * @param p    the {@link Player} to destroy the {@link FakeItem} for
     * @param item the {@link FakeItem} to remove
     */
    public static void destroyItemForPlayer(Player p, FakeItem item) {
        try {
            ReflectionUtil.sendPacket(p, getPacketPlayOutEntityDestroy().newInstance(new int[]{ReflectionUtil.getEntityID(item.getNmsEntity())}));
            FakeAPI.removeFakeItem(p, item);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets a new name for the {@link FakeItem} for every {@link Player}
     *
     * @param name the actual new name of the {@link FakeItem}
     * @param item the {@link FakeItem} to change the name for
     */
    public static void setItemnameForAll(String name, FakeItem item) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            setItemnameForPlayer(p, name, item);
        }
    }

    /**
     * Sets a new name for the {@link FakeItem} for the {@link Player}
     *
     * @param p    the {@link Player} to see the new name of the {@link FakeItem}
     * @param name the actual new name of the {@link FakeItem}
     * @param item the {@link FakeItem} to change the name for
     */
    public static void setItemnameForPlayer(Player p, String name, FakeItem item) {
        try {
            setCustomName().invoke(item.getNmsEntity(), name.replace("&", "§").replace("_", " "));
            setCustomName().invoke(item.getNmsEntity(), true);

            Object dw = getDataWatcher().invoke(item.getNmsEntity());

            ReflectionUtil.sendPacket(p, getPacketPlayOutEntityMetadata().newInstance(ReflectionUtil.getEntityID(item.getNmsEntity()), dw, true));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
