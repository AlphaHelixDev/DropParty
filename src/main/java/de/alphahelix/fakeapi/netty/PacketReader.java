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

package de.alphahelix.fakeapi.netty;

import de.alphahelix.alphalibary.reflection.ReflectionUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class PacketReader {

    private static Field channelField;

    private static ArrayList<String> usedChannelsNames = new ArrayList<>();

    static {
        for (Field f : ReflectionUtil.getNmsClass("NetworkManager").getDeclaredFields()) {
            if (f.getType().isAssignableFrom(Channel.class)) {
                channelField = f;
                channelField.setAccessible(true);
                break;
            }
        }
    }

    public ChannelHandler listen(final Player p, final PacketReceivingHandler handler, String channelName) {
        Channel ch = getNettyChannel(p);
        ChannelPipeline pipe = ch.pipeline();

        ChannelHandler handle = new MessageToMessageDecoder<Object>() {
            @Override
            protected void decode(ChannelHandlerContext chc, Object packet, List<Object> out) throws Exception {
                if (packet.getClass().isAssignableFrom(ReflectionUtil.getNmsClass("PacketPlayInUseEntity"))) {
                    if (!handler.handle(p, packet)) {
                        out.add(packet);
                    }
                    return;
                }
                out.add(packet);
            }
        };
        if (!usedChannelsNames.contains(channelName)) {
            pipe.addAfter("decoder", channelName, handle);
            usedChannelsNames.add(channelName);
        }
        return handle;
    }

    public boolean close(Player p, ChannelHandler handler) {
        try {
            ChannelPipeline pipe = getNettyChannel(p).pipeline();
            pipe.remove(handler);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Channel getNettyChannel(Player p) {
        try {
            Object manager = ReflectionUtil.getNmsClass("PlayerConnection").getField("networkManager").get(ReflectionUtil.getNmsClass("EntityPlayer").getField("playerConnection").get(ReflectionUtil.getEntityPlayer(p)));
            return (Channel) channelField.get(manager);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
