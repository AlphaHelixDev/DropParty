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

import com.mojang.authlib.GameProfile;
import de.alphahelix.alphalibary.item.SkullItemBuilder;
import de.alphahelix.alphalibary.reflection.ReflectionUtil;
import de.alphahelix.fakeapi.FakeAPI;
import de.alphahelix.fakeapi.Register;
import de.alphahelix.fakeapi.instances.FakeArmorstand;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.lang.reflect.Constructor;
import java.util.HashMap;

import static org.bukkit.Bukkit.getOnlinePlayers;

public class ArmorstandUtil extends UtilBase {

    private static HashMap<String, BukkitTask> followMap = new HashMap<>();
    private static HashMap<String, BukkitTask> splitMap = new HashMap<>();

    private static Constructor<?> entityArmorstand;

    static {
        try {
            entityArmorstand = ReflectionUtil.getNmsClass("EntityArmorStand").getConstructor(ReflectionUtil.getNmsClass("World"));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * Spawns in a {@link FakeArmorstand} for every {@link Player} on the server
     *
     * @param loc  {@link Location} where the {@link FakeArmorstand} should be spawned at
     * @param name of the {@link FakeArmorstand} inside the file and above his head
     */
    public static void spawnArmorstandForAll(Location loc, String name) {
        for (Player p : getOnlinePlayers()) {
            spawnArmorstandForPlayer(p, loc, name);
        }
    }

    /**
     * Spawns in a {@link FakeArmorstand} for the {@link Player}
     *
     * @param p    the {@link Player} to spawn the {@link FakeArmorstand} for
     * @param loc  {@link Location} where the {@link FakeArmorstand} should be spawned at
     * @param name of the {@link FakeArmorstand} inside the file and above his head
     * @return the new spawned {@link FakeArmorstand}
     */
    public static FakeArmorstand spawnArmorstandForPlayer(Player p, Location loc, String name) {
        try {
            Object armorstand = entityArmorstand.newInstance(ReflectionUtil.getWorldServer(p.getWorld()));

            setLocation().invoke(armorstand, loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
            setInvisible().invoke(armorstand, true);
            setCustomName().invoke(armorstand, name);
            setCustomNameVisible().invoke(armorstand, true);

            ReflectionUtil.sendPacket(p, getPacketPlayOutSpawnEntityLiving().newInstance(armorstand));

            Register.getArmorstandLocationsFile().addArmorstandToFile(loc, name);
            FakeAPI.addFakeArmorstand(p, new FakeArmorstand(loc, name, armorstand));
            return new FakeArmorstand(loc, name, armorstand);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Spawns in a temporary {@link FakeArmorstand} (disappears after rejoin) for every {@link Player} on the server
     *
     * @param loc  {@link Location} where the {@link FakeArmorstand} should be spawned at
     * @param name of the {@link FakeArmorstand} inside the file and above his head
     */
    public static void spawnTemporaryArmorstandForAll(Location loc, String name) {
        for (Player p : getOnlinePlayers()) {
            spawnTemporaryArmorstandForPlayer(p, loc, name);
        }
    }

    /**
     * Spawns in a temporary {@link FakeArmorstand} (disappears after rejoin) for the {@link Player}
     *
     * @param p    the {@link Player} to spawn the {@link FakeArmorstand} for
     * @param loc  {@link Location} where the {@link FakeArmorstand} should be spawned at
     * @param name of the {@link FakeArmorstand} inside the file and above his head
     * @return the new spawned {@link FakeArmorstand}
     */
    public static FakeArmorstand spawnTemporaryArmorstandForPlayer(Player p, Location loc, String name) {
        try {
            Object armorstand = entityArmorstand.newInstance(ReflectionUtil.getWorldServer(p.getWorld()));

            setLocation().invoke(armorstand, loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
            setInvisible().invoke(armorstand, true);
            setCustomName().invoke(armorstand, name);
            setCustomNameVisible().invoke(armorstand, true);

            ReflectionUtil.sendPacket(p, getPacketPlayOutSpawnEntityLiving().newInstance(armorstand));

            FakeAPI.addFakeArmorstand(p, new FakeArmorstand(loc, name, armorstand));
            return new FakeArmorstand(loc, name, armorstand);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Moves the given {@link FakeArmorstand}
     *
     * @param x          blocks in x direction
     * @param y          blocks in y direction
     * @param z          blocks in z direction
     * @param armorstand the {@link FakeArmorstand} which should be moved
     */
    public static void moveArmorstandForAll(double x, double y, double z, FakeArmorstand armorstand) {
        for (Player p : getOnlinePlayers()) {
            moveArmorstandForPlayer(p, x, y, z, armorstand);
        }
    }

    /**
     * Moves the given {@link FakeArmorstand}
     *
     * @param p          the {@link Player} to move the {@link FakeArmorstand} for
     * @param x          blocks in x direction
     * @param y          blocks in y direction
     * @param z          blocks in z direction
     * @param armorstand the {@link FakeArmorstand} which should be moved
     */
    public static void moveArmorstandForPlayer(Player p, double x, double y, double z, FakeArmorstand armorstand) {
        try {
            ReflectionUtil.sendPacket(p, getPacketPlayOutRelEntityMove().newInstance(
                    ReflectionUtil.getEntityID(armorstand.getNmsEntity()),
                    ((byte) (x * 32)),
                    ((byte) (y * 32)),
                    ((byte) (z * 32)),
                    true));
            armorstand.setCurrentlocation(armorstand.getCurrentlocation().add(x, y, z));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Teleport a {@link FakeArmorstand} to a specific {@link Location} in certain intervals, which is visible for all Players
     *
     * @param to            the {@link Location} where the {@link FakeArmorstand} should be teleported to
     * @param teleportCount the amount of teleportation that should be made
     * @param wait          the amount of time to wait 'till the next teleport starts
     * @param armorstand    the {@link FakeArmorstand} which should be teleported
     */
    public static void splitTeleportArmorstandForAll(Location to, int teleportCount, long wait, FakeArmorstand armorstand) {
        for (Player p : getOnlinePlayers()) {
            splitTeleportArmorstandForPlayer(p, to, teleportCount, wait, armorstand);
        }
    }

    /**
     * Teleport a {@link FakeArmorstand} to a specific {@link Location} in certain intervals, which is visible for all Players
     *
     * @param p             the {@link Player} to teleport the {@link FakeArmorstand} for
     * @param to            the {@link Location} where the {@link FakeArmorstand} should be teleported to
     * @param teleportCount the amount of teleportation that should be made
     * @param wait          the amount of time to wait 'till the next teleport starts
     * @param armorstand    the {@link FakeArmorstand} which should be teleported
     */
    public static void splitTeleportArmorstandForPlayer(final Player p, final Location to, final int teleportCount, final long wait, final FakeArmorstand armorstand) {
        final Location currentLocation = armorstand.getCurrentlocation();
        Vector between = to.toVector().subtract(currentLocation.toVector());

        final double toMoveInX = between.getX() / teleportCount;
        final double toMoveInY = between.getY() / teleportCount;
        final double toMoveInZ = between.getZ() / teleportCount;

        splitMap.put(p.getName(), new BukkitRunnable() {
            public void run() {
                if (!FakeAPI.isSameLocation(currentLocation, to)) {
                    teleportArmorstandForPlayer(p, currentLocation.add(new Vector(toMoveInX, toMoveInY, toMoveInZ)), armorstand);
                } else
                    this.cancel();
            }
        }.runTaskTimer(FakeAPI.getFakeAPI(), 0, wait));
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
     * Teleports a {@link FakeArmorstand} to a specific {@link Location} for every {@link Player} on the server
     *
     * @param loc        the {@link Location} to teleport the {@link FakeArmorstand} to
     * @param armorstand the {@link FakeArmorstand} which should be teleported
     */
    public static void teleportArmorstandForAll(Location loc, FakeArmorstand armorstand) {
        for (Player p : getOnlinePlayers()) {
            teleportArmorstandForPlayer(p, loc, armorstand);
        }
    }

    /**
     * Teleports a {@link FakeArmorstand} to a specific {@link Location} for the given {@link Player}
     *
     * @param p          the {@link Player} to teleport the {@link FakeArmorstand} for
     * @param loc        the {@link Location} to teleport the {@link FakeArmorstand} to
     * @param armorstand the {@link FakeArmorstand} which should be teleported
     */
    public static void teleportArmorstandForPlayer(Player p, Location loc, FakeArmorstand armorstand) {
        try {
            ReflectionUtil.sendPacket(p, getPacketPlayOutEntityTeleport().newInstance(
                    ReflectionUtil.getEntityID(armorstand.getNmsEntity()),
                    FakeAPI.floor(loc.getBlockX() * 32.0D),
                    FakeAPI.floor(loc.getBlockY() * 32.0D),
                    FakeAPI.floor(loc.getBlockZ() * 32.0D),
                    (byte) ((int) (loc.getYaw() * 256.0F / 360.0F)),
                    (byte) ((int) (loc.getPitch() * 256.0F / 360.0F)),
                    true));

            armorstand.setCurrentlocation(loc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Equip a {@link FakeArmorstand} with a {@link ItemStack} for every {@link Player} on the server
     *
     * @param armorstand the {@link FakeArmorstand} which should get equipped
     * @param item       the {@link ItemStack} which the {@link FakeArmorstand} should receive
     * @param slot       the {@link EquipSlot} where the {@link ItemStack} should be placed at
     */
    public static void equipArmorstandForAll(FakeArmorstand armorstand, ItemStack item, EquipSlot slot) {
        for (Player p : getOnlinePlayers()) {
            equipArmorstandForPlayer(p, armorstand, item, slot);
        }
    }

    /**
     * Equip a {@link FakeArmorstand} with a {@link ItemStack} for the {@link Player}
     *
     * @param p          the {@link Player} to equip the {@link FakeArmorstand} for
     * @param armorstand the {@link FakeArmorstand} which should get equipped
     * @param item       the {@link ItemStack} which the {@link FakeArmorstand} should receive
     * @param slot       the {@link EquipSlot} where the {@link ItemStack} should be placed at
     */
    public static void equipArmorstandForPlayer(Player p, FakeArmorstand armorstand, ItemStack item, EquipSlot slot) {
        try {
            ReflectionUtil.sendPacket(p, getPacketPlayOutEntityEquipment().newInstance(
                    ReflectionUtil.getEntityID(armorstand.getNmsEntity()),
                    slot.getNmsSlot(),
                    ReflectionUtil.getObjectNMSItemStack(item)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the head of the {@link FakeArmorstand} to a custom {@link org.bukkit.material.Skull} which everybody on the server can see
     * You can use custom textures in the format of a 1.7 skin here
     *
     * @param armorstand the {@link FakeArmorstand} which should get equipped
     * @param textureURL the URL where to find the plain 1.7 skin
     */
    public static void equipArmorstandSkullForAll(FakeArmorstand armorstand, String textureURL) {
        for (Player p : getOnlinePlayers()) {
            equipArmorstandSkullForPlayer(p, armorstand, textureURL);
        }
    }

    /**
     * Sets the head of the {@link FakeArmorstand} to a custom {@link org.bukkit.material.Skull} for a specific {@link Player}
     * You can use custom textures in the format of a 1.7 skin here
     *
     * @param p          the {@link Player} to show the custom Skull
     * @param armorstand the {@link FakeArmorstand} which should get equipped
     * @param textureURL the URL where to find the plain 1.7 skin
     */
    public static void equipArmorstandSkullForPlayer(Player p, FakeArmorstand armorstand, String textureURL) {
        equipArmorstandForPlayer(p, armorstand, SkullItemBuilder.getSkull(textureURL), EquipSlot.HELMET);
    }

    /**
     * Sets the head of the {@link FakeArmorstand} to a custom {@link org.bukkit.material.Skull} which everybody on the server can see
     * You can use custom textures in the format of a 1.7 skin here
     *
     * @param armorstand the {@link FakeArmorstand} which should get equipped
     * @param profile    the {@link GameProfile} of the owner of the skull
     */
    public static void equipArmorstandSkullForAll(FakeArmorstand armorstand, GameProfile profile) {
        for (Player p : getOnlinePlayers()) {
            equipArmorstandSkullForPlayer(p, armorstand, profile);
        }
    }

    /**
     * Sets the head of the {@link FakeArmorstand} to a custom {@link org.bukkit.material.Skull} for a specific {@link Player}
     * You can use custom textures in the format of a 1.7 skin here
     *
     * @param p          the {@link Player} to show the custom Skull
     * @param armorstand the {@link FakeArmorstand} which should get equipped
     * @param profile    the {@link GameProfile} of the owner of the skull
     */
    public static void equipArmorstandSkullForPlayer(Player p, FakeArmorstand armorstand, GameProfile profile) {
        try {
            equipArmorstandForPlayer(p, armorstand, SkullItemBuilder.getSkull(profile), EquipSlot.HELMET);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Make a {@link FakeArmorstand} follow a specific {@link Player}, which everybody on the server can see
     *
     * @param toFollow   the {@link Player} which the {@link FakeArmorstand} should follow
     * @param armorstand the {@link FakeArmorstand} which should follow the {@link Player}
     */
    public static void followArmorstandForAll(Player toFollow, FakeArmorstand armorstand) {
        for (Player p : getOnlinePlayers()) {
            followArmorstandForPlayer(p, toFollow, armorstand);
        }
    }

    /**
     * Make a {@link FakeArmorstand} follow a specific {@link Player}, which only the {@link Player} can see
     *
     * @param p          the {@link Player} to see the following {@link FakeArmorstand}
     * @param toFollow   the {@link Player} which the {@link FakeArmorstand} should follow
     * @param armorstand the {@link FakeArmorstand} which should follow the {@link Player}
     */
    public static void followArmorstandForPlayer(final Player p, final Player toFollow, final FakeArmorstand armorstand) {
        followMap.put(p.getName(), new BukkitRunnable() {
            @Override
            public void run() {
                teleportArmorstandForPlayer(p, toFollow.getLocation(), armorstand);
            }
        }.runTaskTimer(FakeAPI.getFakeAPI(), 0, 1));
    }

    /**
     * Check if a {@link FakeArmorstand} follows a {@link Player}
     *
     * @param toCheck the {@link Player} to check if he has a {@link FakeArmorstand} which follows him
     * @return if the {@link Player} has a {@link FakeArmorstand} which followes him
     */
    public static boolean hasFollower(Player toCheck) {
        return followMap.containsKey(toCheck.getName());
    }

    /**
     * Make every {@link FakeArmorstand} unfollow his {@link Player} for everybody on the server
     */
    public static void unFollowArmorstandForAll() {
        for (Player p : getOnlinePlayers()) {
            unFollowArmorstandForPlayer(p);
        }
    }

    /**
     * Make a {@link FakeArmorstand} unfollow his {@link Player}
     *
     * @param p the {@link Player} who shouldn't be followed anylonger
     */
    public static void unFollowArmorstandForPlayer(Player p) {
        if (followMap.containsKey(p.getName())) {
            followMap.get(p.getName()).cancel();
            followMap.remove(p.getName());
        }
    }

    /**
     * Removes a {@link FakeArmorstand} from the {@link org.bukkit.World} for every {@link Player}
     *
     * @param armorstand the {@link FakeArmorstand} to remove
     */
    public static void destroyArmorstandForAll(FakeArmorstand armorstand) {
        for (Player p : getOnlinePlayers()) {
            destroyArmorstandForPlayer(p, armorstand);
        }
    }

    /**
     * Removes a {@link FakeArmorstand} for on {@link Player} from the {@link org.bukkit.World}
     *
     * @param p          the {@link Player} to destroy the {@link FakeArmorstand} for
     * @param armorstand the {@link FakeArmorstand} to remove
     */
    public static void destroyArmorstandForPlayer(Player p, FakeArmorstand armorstand) {
        try {
            ReflectionUtil.sendPacket(p, getPacketPlayOutEntityDestroy().newInstance(new int[]{ReflectionUtil.getEntityID(armorstand.getNmsEntity())}));
            FakeAPI.removeFakeArmorstand(p, armorstand);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets a new name for the {@link FakeArmorstand} for every {@link Player}
     *
     * @param name       the actual new name of the {@link FakeArmorstand}
     * @param armorstand the {@link FakeArmorstand} to change the name for
     */
    public static void setArmorstandnameForAll(String name, FakeArmorstand armorstand) {
        for (Player p : getOnlinePlayers()) {
            setArmorstandnameForPlayer(p, name, armorstand);
        }
    }

    /**
     * Sets a new name for the {@link FakeArmorstand} for the {@link Player}
     *
     * @param p          the {@link Player} to see the new name of the {@link FakeArmorstand}
     * @param name       the actual new name of the {@link FakeArmorstand}
     * @param armorstand the {@link FakeArmorstand} to change the name for
     */
    public static void setArmorstandnameForPlayer(Player p, String name, FakeArmorstand armorstand) {
        try {
            setCustomName().invoke(armorstand.getNmsEntity(), name.replace("&", "§").replace("_", " "));

            Object dw = getDataWatcher().invoke(armorstand.getNmsEntity());

            ReflectionUtil.sendPacket(p, getPacketPlayOutEntityMetadata().newInstance(ReflectionUtil.getEntityID(armorstand.getNmsEntity()), dw, true));
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
