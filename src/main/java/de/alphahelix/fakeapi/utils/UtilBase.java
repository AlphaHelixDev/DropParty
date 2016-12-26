package de.alphahelix.fakeapi.utils;

import de.alphahelix.alphalibary.reflection.ReflectionUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class UtilBase {

    private static Constructor<?> packetPlayOutSpawnEntity;
    private static Constructor<?> packetPlayOutSpawnEntityLiving;
    private static Constructor<?> packetPlayOutNamedEntitySpawn;
    private static Constructor<?> packetPlayOutRelEntityMove;
    private static Constructor<?> packetPlayOutEntityTeleport;
    private static Constructor<?> packetPlayOutEntityEquipment;
    private static Constructor<?> packetPlayOutEntityDestroy;
    private static Constructor<?> packetPlayOutEntityMetadata;
    private static Constructor<?> packetPlayOutEntityHeadRotation;
    private static Constructor<?> packetPlayOutEntityLook;
    private static Constructor<?> packetPlayOutAnimation;

    private static Method setLocation;
    private static Method setInvisible;
    private static Method setCustomName;
    private static Method setCustomNameVisible;
    private static Method getDataWatcher;
    private static Method watch;
    private static Method update;

    static {
        try {
            packetPlayOutRelEntityMove = ReflectionUtil.getNmsClass("PacketPlayOutRelEntityMove").getConstructor(int.class, byte.class, byte.class, byte.class, boolean.class);
            packetPlayOutEntityTeleport = ReflectionUtil.getNmsClass("PacketPlayOutEntityTeleport").getConstructor(int.class, int.class, int.class, int.class, byte.class, byte.class, boolean.class);
            packetPlayOutEntityEquipment = ReflectionUtil.getNmsClass("PacketPlayOutEntityEquipment").getConstructor(int.class, int.class, ReflectionUtil.getNmsClass("ItemStack"));
            packetPlayOutSpawnEntity = ReflectionUtil.getNmsClass("PacketPlayOutSpawnEntity").getConstructor(ReflectionUtil.getNmsClass("Entity"), int.class);
            packetPlayOutSpawnEntityLiving = ReflectionUtil.getNmsClass("PacketPlayOutSpawnEntityLiving").getConstructor(ReflectionUtil.getNmsClass("EntityLiving"));
            packetPlayOutEntityDestroy = ReflectionUtil.getNmsClass("PacketPlayOutEntityDestroy").getConstructor(int[].class);
            packetPlayOutEntityMetadata = ReflectionUtil.getNmsClass("PacketPlayOutEntityMetadata").getConstructor(int.class, ReflectionUtil.getNmsClass("DataWatcher"), boolean.class);
            packetPlayOutEntityHeadRotation = ReflectionUtil.getNmsClass("PacketPlayOutEntityHeadRotation").getConstructor(ReflectionUtil.getNmsClass("Entity"), byte.class);
            packetPlayOutEntityLook = ReflectionUtil.getNmsClass("PacketPlayOutEntity$PacketPlayOutEntityLook").getConstructor(int.class, byte.class, byte.class, boolean.class);
            packetPlayOutAnimation = ReflectionUtil.getNmsClass("PacketPlayOutAnimation").getConstructor(ReflectionUtil.getNmsClass("Entity"), int.class);
            packetPlayOutNamedEntitySpawn = ReflectionUtil.getNmsClass("PacketPlayOutNamedEntitySpawn").getConstructor(ReflectionUtil.getNmsClass("EntityHuman"));

            setLocation = ReflectionUtil.getNmsClass("Entity").getMethod("setLocation", double.class, double.class, double.class, float.class, float.class);
            setInvisible = ReflectionUtil.getNmsClass("Entity").getMethod("setInvisible", boolean.class);
            setCustomName = ReflectionUtil.getNmsClass("Entity").getMethod("setCustomName", String.class);
            setCustomNameVisible = ReflectionUtil.getNmsClass("Entity").getMethod("setCustomNameVisible", boolean.class);
            getDataWatcher = ReflectionUtil.getNmsClass("Entity").getMethod("getDataWatcher");
            watch = ReflectionUtil.getNmsClass("DataWatcher").getMethod("watch", int.class, Object.class);
            update = ReflectionUtil.getNmsClass("DataWatcher").getMethod("update", int.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

    }

    public static Constructor<?> getPacketPlayOutSpawnEntity() {
        return packetPlayOutSpawnEntity;
    }

    public static Constructor<?> getPacketPlayOutSpawnEntityLiving() {
        return packetPlayOutSpawnEntityLiving;
    }

    public static Constructor<?> getPacketPlayOutRelEntityMove() {
        return packetPlayOutRelEntityMove;
    }

    public static Constructor<?> getPacketPlayOutEntityTeleport() {
        return packetPlayOutEntityTeleport;
    }

    public static Constructor<?> getPacketPlayOutEntityEquipment() {
        return packetPlayOutEntityEquipment;
    }

    public static Constructor<?> getPacketPlayOutEntityDestroy() {
        return packetPlayOutEntityDestroy;
    }

    public static Constructor<?> getPacketPlayOutEntityMetadata() {
        return packetPlayOutEntityMetadata;
    }

    public static Constructor<?> getPacketPlayOutEntityHeadRotation() {
        return packetPlayOutEntityHeadRotation;
    }

    public static Constructor<?> getPacketPlayOutEntityLook() {
        return packetPlayOutEntityLook;
    }

    public static Constructor<?> getPacketPlayOutAnimation() {
        return packetPlayOutAnimation;
    }

    public static Constructor<?> getPacketPlayOutNamedEntitySpawn() {
        return packetPlayOutNamedEntitySpawn;
    }

    public static Method setLocation() {
        return setLocation;
    }

    public static Method setInvisible() {
        return setInvisible;
    }

    public static Method setCustomName() {
        return setCustomName;
    }

    public static Method setCustomNameVisible() {
        return setCustomNameVisible;
    }

    public static Method getDataWatcher() {
        return getDataWatcher;
    }

    public static Method watch() {
        return watch;
    }

    public static Method update() {
        return update;
    }
}
