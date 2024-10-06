/*
 * Copyright (c) 2017-2020 C4
 *
 * This file is part of Corpse Complex, a mod made for Minecraft.
 *
 * Corpse Complex is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Corpse Complex is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Corpse Complex.  If not, see <https://www.gnu.org/licenses/>.
 */

package top.theillusivec4.corpsecomplex.common.modules.inventory.inventories;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;
import top.theillusivec4.corpsecomplex.common.capability.DeathStorageCapability.IDeathStorage;
import top.theillusivec4.corpsecomplex.common.modules.inventory.InventorySetting;
import top.theillusivec4.corpsecomplex.common.util.Enums.InventorySection;
import top.theillusivec4.corpsecomplex.common.util.InventoryHelper;

public class VanillaInventory implements IInventory {

  @Override
  public void storeInventory(IDeathStorage deathStorage) {
    Player player = deathStorage.getPlayer();

    if (player != null) {
      Inventory inventory = player.getInventory();
      InventorySetting setting = deathStorage.getSettings().getInventorySettings();
      ListTag list = new ListTag();

      for (int i = 0; i < 9; i++) {
        InventoryHelper.process(player, inventory.getItem(i), i, list,
            i == inventory.selected ? InventorySection.MAINHAND : InventorySection.HOTBAR,
            setting);
      }

      for (int i = 9; i < 36; i++) {
        InventoryHelper
            .process(player, inventory.getItem(i), i, list, InventorySection.MAIN, setting);
      }
      InventoryHelper
          .process(player, inventory.getItem(36), 36, list, InventorySection.FEET, setting);
      InventoryHelper
          .process(player, inventory.getItem(37), 37, list, InventorySection.LEGS, setting);
      InventoryHelper
          .process(player, inventory.getItem(38), 38, list, InventorySection.CHEST, setting);
      InventoryHelper
          .process(player, inventory.getItem(39), 39, list, InventorySection.HEAD, setting);
      InventoryHelper
          .process(player, inventory.getItem(40), 40, list, InventorySection.OFFHAND,
              setting);
      deathStorage.addInventory("vanilla", list);
    }
  }

  @Override
  public void retrieveInventory(IDeathStorage newStorage, IDeathStorage oldStorage) {
    Player player = newStorage.getPlayer();
    Player oldPlayer = oldStorage.getPlayer();

    if (player != null && oldPlayer != null) {
      ListTag list = (ListTag) oldStorage.getInventory("vanilla");

      if (list != null) {
        Inventory inventory = player.getInventory();

        for (int i = 0; i < list.size(); ++i) {
          CompoundTag compoundnbt = list.getCompound(i);
          int slot = compoundnbt.getInt("Slot");
          ItemStack itemstack = ItemStack.of(compoundnbt);
          if (!itemstack.isEmpty()) {
            ItemStack existing = inventory.getItem(slot);

            if (existing.isEmpty()) {
              inventory.setItem(slot, itemstack);
            } else {
              ItemHandlerHelper.giveItemToPlayer(inventory.player, itemstack);
            }
          }
        }
      }
    }
  }
}
