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

package top.theillusivec4.corpsecomplex.common.modules.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.corpsecomplex.common.util.Enums.PermissionMode;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class EffectsOverride {

  @Nullable
  private final List<ItemStack> cures;
  @Nullable
  private final List<MobEffectInstance> effects;
  @Nullable
  private final PermissionMode keepEffectsMode;
  @Nullable
  private final List<MobEffect> keepEffects;

  private EffectsOverride(Builder builder) {
    this.cures = builder.cures;
    this.effects = builder.effects;
    this.keepEffects = builder.keepEffects;
    this.keepEffectsMode = builder.keepEffectsMode;
  }

  public Optional<List<ItemStack>> getCures() {
    return Optional.ofNullable(this.cures);
  }

  public Optional<List<MobEffectInstance>> getEffects() {
    return Optional.ofNullable(this.effects);
  }

  public Optional<PermissionMode> getKeepEffectsMode() {
    return Optional.ofNullable(this.keepEffectsMode);
  }

  public Optional<List<MobEffect>> getKeepEffects() {
    return Optional.ofNullable(this.keepEffects);
  }

  public static class Builder {

    private List<ItemStack> cures;
    private List<MobEffectInstance> effects;
    private PermissionMode keepEffectsMode;
    private List<MobEffect> keepEffects;

    public Builder cures(List<ItemStack> cures) {
      this.cures = cures;
      return this;
    }

    public Builder effects(List<MobEffectInstance> effects) {
      this.effects = effects;
      return this;
    }

    public Builder keepEffects(List<MobEffect> keepEffects) {
      this.keepEffects = keepEffects;
      return this;
    }

    public Builder keepEffectsMode(PermissionMode keepEffectsMode) {
      this.keepEffectsMode = keepEffectsMode;
      return this;
    }

    public EffectsOverride build() {
      return new EffectsOverride(this);
    }
  }
}
