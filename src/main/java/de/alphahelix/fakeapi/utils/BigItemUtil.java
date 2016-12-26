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
import de.alphahelix.fakeapi.FakeMobType;
import de.alphahelix.fakeapi.Register;
import de.alphahelix.fakeapi.instances.FakeBigItem;
import de.alphahelix.fakeapi.instances.FakeMob;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.logging.Level;

public class BigItemUtil extends UtilBase {

    private static HashMap<String, BukkitTask> splitMap = new HashMap<>();

    /**
     * Spawns in a {@link FakeBigItem} for every {@link Player} on the server
     *
     * @param loc       {@link Location} where the {@link FakeBigItem} should be spawned at
     * @param name      of the {@link FakeBigItem} inside the file and above his head
     * @param itemStack the {@link ItemStack} which should be shown
     */
    public static void spawnBigItemForAll(Location loc, String name, ItemStack itemStack) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            spawnBigItemForPlayer(p, loc, name, itemStack);
        }
    }

    /**
     * Spawns in a {@link FakeBigItem} for the {@link Player}
     *
     * @param p         the {@link Player} to spawn the {@link FakeBigItem} for
     * @param loc       {@link Location} where the {@link FakeBigItem} should be spawned at
     * @param name      of the {@link FakeBigItem} inside the file and above his head
     * @param itemStack the {@link ItemStack} which should be shown
     * @return the new spawned {@link FakeBigItem}
     */
    public static FakeBigItem spawnBigItemForPlayer(Player p, Location loc, String name, ItemStack itemStack) {
        try {
            FakeMob fakeGiant = MobUtil.spawnTemporaryMobForPlayer(p, loc, name, FakeMobType.GIANT);
            Object giant = fakeGiant.getNmsEntity();
            Object dw = getDataWatcher().invoke(giant);

            setInvisible().invoke(giant, true);

            ReflectionUtil.sendPacket(p, getPacketPlayOutEntityMetadata().newInstance(ReflectionUtil.getEntityID(giant), dw, true));

            MobUtil.equipMobForPlayer(p, fakeGiant, itemStack, EquipSlot.HAND);

            Register.getBigItemLocationsFile().addBigItemToFile(loc, name, itemStack);
            FakeAPI.addFakeBigItem(p, new FakeBigItem(loc, name, giant, itemStack));
            return new FakeBigItem(loc, name, giant, itemStack);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Spawns in a temporary {@link FakeBigItem} (disappears after rejoin) for every {@link Player} on the server
     *
     * @param loc   {@link Location} where the {@link FakeBigItem} should be spawned at
     * @param name  of the {@link FakeBigItem} inside the file and above his head
     * @param stack the {@link ItemStack} which should be shown
     */
    public static void spawnTemporaryBigItemForAll(Location loc, String name, ItemStack stack) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            spawnTemporaryBigItemForPlayer(p, loc, name, stack);
        }
    }

    /**
     * Spawns in a temporary {@link FakeBigItem} (disappears after rejoin) for the {@link Player}
     *
     * @param p     the {@link Player} to spawn the {@link FakeBigItem} for
     * @param loc   {@link Location} where the {@link FakeBigItem} should be spawned at
     * @param name  of the {@link FakeBigItem} inside the file and above his head
     * @param stack the {@link ItemStack} which should be shown
     * @return the new spawned {@link FakeBigItem}
     */
    public static FakeBigItem spawnTemporaryBigItemForPlayer(Player p, Location loc, String name, ItemStack stack) {
        try {
            FakeMob fakeGiant = MobUtil.spawnTemporaryMobForPlayer(p, loc, name, FakeMobType.GIANT);
            Object giant = fakeGiant.getNmsEntity();
            Object dw = getDataWatcher().invoke(giant);

            setInvisible().invoke(giant, true);

            ReflectionUtil.sendPacket(p, getPacketPlayOutEntityMetadata().newInstance(ReflectionUtil.getEntityID(giant), dw, true));

            MobUtil.equipMobForPlayer(p, fakeGiant, stack, EquipSlot.HAND);

            FakeAPI.addFakeBigItem(p, new FakeBigItem(loc, name, giant, stack));
            return new FakeBigItem(loc, name, giant, stack);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Removes a {@link FakeBigItem} from the {@link org.bukkit.World} for every {@link Player}
     *
     * @param item the {@link FakeBigItem} to remove
     */
    public static void destroyBigItemForAll(FakeBigItem item) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            destroyBigItemForPlayer(p, item);
        }
    }

    /**
     * Removes a {@link FakeBigItem} for on {@link Player} from the {@link org.bukkit.World}
     *
     * @param p    the {@link Player} to destroy the {@link FakeBigItem} for
     * @param item the {@link FakeBigItem} to remove
     */
    public static void destroyBigItemForPlayer(Player p, FakeBigItem item) {
        try {
            ReflectionUtil.sendPacket(p, getPacketPlayOutEntityDestroy().newInstance(new int[]{ReflectionUtil.getEntityID(item.getNmsEntity())}));
            FakeAPI.removeFakeBigItem(p, item);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Teleport a {@link FakeBigItem} to a specific {@link Location} in certain intervals, which is visible for all Players
     *
     * @param to            the {@link Location} where the {@link FakeBigItem} should be teleported to
     * @param teleportCount the amount of teleportation that should be made
     * @param wait          the amount of time to wait 'till the next teleport starts
     * @param item          the {@link FakeBigItem} which should be teleported
     */
    public static void splitTeleportArmorstandForAll(Location to, int teleportCount, long wait, FakeBigItem item) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            splitTeleportBigItemForPlayer(p, to, teleportCount, wait, item);
        }
    }

    /**
     * Teleport a {@link FakeBigItem} to a specific {@link Location} in certain intervals, which is visible for all Players
     *
     * @param p             the {@link Player} to teleport the {@link FakeBigItem} for
     * @param to            the {@link Location} where the {@link FakeBigItem} should be teleported to
     * @param teleportCount the amount of teleportation that should be made
     * @param wait          the amount of time to wait 'till the next teleport starts
     * @param item          the {@link FakeBigItem} which should be teleported
     */
    public static void splitTeleportBigItemForPlayer(final Player p, final Location to, final int teleportCount, final long wait, final FakeBigItem item) {
        try {
            final Location currentLocation = item.getCurrentlocation();
            Vector between = to.toVector().subtract(currentLocation.toVector());

            final double toMoveInX = between.getX() / teleportCount;
            final double toMoveInY = between.getY() / teleportCount;
            final double toMoveInZ = between.getZ() / teleportCount;

            splitMap.put(p.getName(), new BukkitRunnable() {
                public void run() {
                    if (!FakeAPI.isSameLocation(currentLocation, to)) {
                        teleportBigItemForPlayer(p, currentLocation.add(new Vector(toMoveInX, toMoveInY, toMoveInZ)), item);
                    } else
                        this.cancel();
                }
            }.runTaskTimer(FakeAPI.getFakeAPI(), 0, wait));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Cancels all teleport tasks for the {@link Player}
     *
     * @param p the {@link Player} to cancel all teleport tasks
     */
    public static void cancelAllSplittedTasks(Player p) {
        if (splitMap.containsKey(p.getName())) {
            splitMap.get(p.getName()).cancel();
            splitMap.remove(p.getName());
        }
    }


    /**
     * Teleports a {@link FakeBigItem} to a specific {@link Location} for every {@link Player} on the server
     *
     * @param loc  the {@link Location} to teleport the {@link FakeBigItem} to
     * @param item the {@link FakeBigItem} which should be teleported
     */
    public static void teleportBigItemForAll(Location loc, FakeBigItem item) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            teleportBigItemForPlayer(p, loc, item);
        }
    }

    /**
     * Teleports a {@link FakeBigItem} to a specific {@link Location} for the given {@link Player}
     *
     * @param p    the {@link Player} to teleport the {@link FakeBigItem} for
     * @param loc  the {@link Location} to teleport the {@link FakeBigItem} to
     * @param item the {@link FakeBigItem} which should be teleported
     */
    public static void teleportBigItemForPlayer(Player p, Location loc, FakeBigItem item) {
        try {
            ReflectionUtil.sendPacket(p, getPacketPlayOutEntityTeleport().newInstance(
                    ReflectionUtil.getEntityID(item.getNmsEntity()),
                    FakeAPI.floor(loc.getBlockX() * 32.0D),
                    FakeAPI.floor(loc.getBlockY() * 32.0D),
                    FakeAPI.floor(loc.getBlockZ() * 32.0D),
                    (byte) ((int) (loc.getYaw() * 256.0F / 360.0F)),
                    (byte) ((int) (loc.getPitch() * 256.0F / 360.0F)),
                    true));

            item.setCurrentlocation(loc);
        } catch (NullPointerException | IllegalArgumentException e) {
            Bukkit.getLogger().log(Level.SEVERE, "[FakeAPI] Use {FakeEntity}.getNmsEntity() for the Object parameter!");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
