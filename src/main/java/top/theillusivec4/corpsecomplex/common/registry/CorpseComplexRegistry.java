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

package top.theillusivec4.corpsecomplex.common.registry;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.enchantment.Enchantment;

import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import top.theillusivec4.corpsecomplex.CorpseComplex;
import top.theillusivec4.corpsecomplex.common.modules.mementomori.MementoMoriEffect;
import top.theillusivec4.corpsecomplex.common.modules.soulbinding.SoulbindingEnchantment;

public class CorpseComplexRegistry {

  private static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, CorpseComplex.MODID);

  public static final RegistryObject<MobEffect> MEMENTO_MORI = MOB_EFFECTS.register(RegistryReference.SOULBINDING, MementoMoriEffect::new);

  private static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, CorpseComplex.MODID);

  public static final RegistryObject<Enchantment> SOULBINDING = ENCHANTMENTS.register(RegistryReference.SOULBINDING, SoulbindingEnchantment::new);

//  @ObjectHolder(RegistryReference.SOULBINDING)
//  public static final Enchantment SOULBINDING;

}
