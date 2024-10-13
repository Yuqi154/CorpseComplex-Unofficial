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

package top.theillusivec4.corpsecomplex.common.modules.enchantment;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;
import top.theillusivec4.corpsecomplex.common.config.CorpseComplexConfig;

import javax.annotation.Nonnull;

public class SoulbindingEnchantment extends Enchantment {
  private static final EnchantmentCategory category = EnchantmentCategory.create("Any", (item) -> true);

  public SoulbindingEnchantment() {
    super(Rarity.VERY_RARE, category, EquipmentSlot.values());
  }

  @Nonnull
  @Override
  public Rarity getRarity() {
    return CorpseComplexConfig.rarity != null ? CorpseComplexConfig.rarity : super.getRarity();
  }

  @Override
  public int getMaxLevel() {
    return CorpseComplexConfig.maxSoulbindingLevel > 0 ? CorpseComplexConfig.maxSoulbindingLevel
        : super.getMaxLevel();
  }

  @Override
  public int getMinCost(int enchantmentLevel) {
    return 1 + 10 * (enchantmentLevel - 1);
  }

  @Override
  public int getMaxCost(int enchantmentLevel) {
    return super.getMaxCost(enchantmentLevel) + 50;
  }

  @Override
  protected boolean checkCompatibility(Enchantment ench) {
    ResourceLocation rl = ResourceLocation.tryParse(ench.getDescriptionId());
    boolean isSoulbound = false;

    if (rl != null) {
      isSoulbound = rl.getPath().equals("soulbound");
    }
    return !isSoulbound && ench != Enchantments.VANISHING_CURSE && super.checkCompatibility(ench);
  }

  @Override
  public boolean isTreasureOnly() {
    return CorpseComplexConfig.isTreasure;
  }

  @Override
  public boolean canApplyAtEnchantingTable(@Nonnull ItemStack stack) {
    return CorpseComplexConfig.canApplyEnchantingTable;
  }

  @Override
  public boolean isTradeable() {
    return CorpseComplexConfig.isVillagerTrade;
  }

  @Override
  public boolean isDiscoverable() {
    return CorpseComplexConfig.isLootable;
  }

  @Override
  public boolean isAllowedOnBooks() {
    return CorpseComplexConfig.allowedOnBooks;
  }
}
