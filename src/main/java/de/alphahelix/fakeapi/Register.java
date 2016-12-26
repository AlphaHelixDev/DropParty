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

import de.alphahelix.fakeapi.files.*;
import de.alphahelix.fakeapi.utils.ArmorstandUtil;

public class Register {

    private static FakeAPI fakeAPI;
    private static ArmorstandLocationsFile armorstandLocationsFile;
    private static EndercrystalLocationsFile endercrystalLocationsFile;
    private static PlayerLocationsFile playerLocationsFile;
    private static ItemLocationsFile itemLocationsFile;
    private static MobLocationsFile mobLocationsFile;
    private static BigItemLocationsFile bigItemLocationsFile;

    public Register(FakeAPI fakeAPI) {
        Register.fakeAPI = fakeAPI;
    }

    public static FakeAPI getFakeAPI() {
        return fakeAPI;
    }

    public static ArmorstandLocationsFile getArmorstandLocationsFile() {
        return armorstandLocationsFile;
    }

    public static EndercrystalLocationsFile getEndercrystalLocationsFile() {
        return endercrystalLocationsFile;
    }

    public static ItemLocationsFile getItemLocationsFile() {
        return itemLocationsFile;
    }

    public static PlayerLocationsFile getPlayerLocationsFile() {
        return playerLocationsFile;
    }

    public static MobLocationsFile getMobLocationsFile() {
        return mobLocationsFile;
    }

    public static BigItemLocationsFile getBigItemLocationsFile() {
        return bigItemLocationsFile;
    }

    public void initAll() {
        armorstandLocationsFile = new ArmorstandLocationsFile(getFakeAPI());
        endercrystalLocationsFile = new EndercrystalLocationsFile(getFakeAPI());
        playerLocationsFile = new PlayerLocationsFile(getFakeAPI());
        itemLocationsFile = new ItemLocationsFile(getFakeAPI());
        mobLocationsFile = new MobLocationsFile(getFakeAPI());
        bigItemLocationsFile = new BigItemLocationsFile(getFakeAPI());
        new ArmorstandUtil();
        new FakeEventListener(getFakeAPI(), this);
    }
}
