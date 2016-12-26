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

package de.alphahelix.fakeapi;

import de.alphahelix.alphalibary.listener.SimpleListener;
import de.alphahelix.fakeapi.events.ArmorstandClickEvent;
import de.alphahelix.fakeapi.events.EndercrystalClickEvent;
import de.alphahelix.fakeapi.events.MobClickEvent;
import de.alphahelix.fakeapi.events.PlayerClickEvent;
import de.alphahelix.fakeapi.instances.NoSuchFakeEntityException;
import de.alphahelix.fakeapi.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class FakeEventListener extends SimpleListener<FakeAPI, Register> {

    public FakeEventListener(FakeAPI plugin, Register register) {
        super(plugin, register);
    }

    @EventHandler
    public void onEventTrigger(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        try {
            if (FakeAPI.isFakeArmorstandInRange(p, 4)) {
                Bukkit.getPluginManager().callEvent(new ArmorstandClickEvent(p, FakeAPI.getLookedAtFakeArmorstand(p, 4), e.getAction()));
            }
            if (FakeAPI.isFakeEndercrystalInRange(p, 4)) {
                Bukkit.getPluginManager().callEvent(new EndercrystalClickEvent(p, FakeAPI.getLookedAtFakeEndercrystal(p, 4), e.getAction()));
            }
            if (FakeAPI.isFakePlayerInRange(p, 4)) {
                Bukkit.getPluginManager().callEvent(new PlayerClickEvent(p, FakeAPI.getLookedAtFakePlayer(p, 4), e.getAction()));
            }
            if (FakeAPI.isFakeMobInRange(e.getPlayer(), 4)) {
                Bukkit.getPluginManager().callEvent(new MobClickEvent(p, FakeAPI.getLookedAtFakeMob(p, 4), e.getAction()));
            }
        } catch (NoSuchFakeEntityException e1) {
            e1.printStackTrace();
        }
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        for (String names : Register.getArmorstandLocationsFile().getPacketArmorstand().keySet()) {
            ArmorstandUtil.spawnArmorstandForPlayer(p,
                    Register.getArmorstandLocationsFile().getPacketArmorstand().get(names),
                    names);
        }
        for (String names : Register.getEndercrystalLocationsFile().getPacketEndercrystal().keySet()) {
            EndercrystalUtil.spawnEndercrystalForPlayer(p,
                    Register.getEndercrystalLocationsFile().getPacketEndercrystal().get(names),
                    names);
        }
        for (String names : Register.getPlayerLocationsFile().getPacketPlayerLocations().keySet()) {
            PlayerUtil.spawnPlayerForPlayer(p,
                    Register.getPlayerLocationsFile().getPacketPlayerLocations().get(names),
                    Register.getPlayerLocationsFile().getPacketPlayerSkins().get(names),
                    names);
        }
        for (String names : Register.getItemLocationsFile().getPacketItemsLocations().keySet()) {
            ItemUtil.spawnItemForPlayer(p,
                    Register.getItemLocationsFile().getPacketItemsLocations().get(names),
                    names,
                    Register.getItemLocationsFile().getPacketItemsTypes().get(names));

        }
        for (String names : Register.getMobLocationsFile().getPacketMobLocations().keySet()) {
            MobUtil.spawnMobForPlayer(p,
                    Register.getMobLocationsFile().getPacketMobLocations().get(names),
                    names,
                    Register.getMobLocationsFile().getPacketMobTypes().get(names));
        }
        for (String names : Register.getBigItemLocationsFile().getPacketBigItemsLocations().keySet()) {
            BigItemUtil.spawnBigItemForPlayer(p,
                    Register.getBigItemLocationsFile().getPacketBigItemsLocations().get(names),
                    names,
                    Register.getBigItemLocationsFile().getPacketBigItemsTypes().get(names));
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (FakeAPI.getFakePlayer().containsKey(p.getName())) {
            PlayerUtil.unFollowPlayerForPlayer(p);
            PlayerUtil.normalizeLookForPlayer(p);
            PlayerUtil.cancelAllSplittedTasks(p);
            FakeAPI.getFakePlayer().remove(p.getName());
        }
        if (FakeAPI.getFakeArmorstand().containsKey(p.getName())) {
            ArmorstandUtil.unFollowArmorstandForPlayer(p);
            ArmorstandUtil.cancelAllSplittedTasks(p);
            FakeAPI.getFakeArmorstand().remove(p.getName());
        }
        if (FakeAPI.getFakeEndercrystal().containsKey(p.getName())) {
            EndercrystalUtil.cancelAllSplittedTasks(p);
            FakeAPI.getFakeEndercrystal().remove(p.getName());
        }
        if (FakeAPI.getFakeMob().containsKey(p.getName())) {
            MobUtil.unFollowPlayerForPlayer(p);
            MobUtil.normalizeLookForPlayer(p);
            MobUtil.cancelAllSplittedTasks(p);
            FakeAPI.getFakeMob().remove(p.getName());
        }
        if (FakeAPI.getFakeBigItem().containsKey(p.getName())) {
            BigItemUtil.cancelAllSplittedTasks(p);
            FakeAPI.getFakeBigItem().remove(p.getName());
        }
    }

//    @EventHandler
//    public void onChat(PlayerChatEvent e) {
//        Player p = e.getPlayer();
//
//        switch (e.getMessage()) {
//            case "a":
//                break;
//        }
//    }
}
