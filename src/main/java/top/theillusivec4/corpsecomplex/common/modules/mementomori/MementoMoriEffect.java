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

package top.theillusivec4.corpsecomplex.common.modules.mementomori;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import top.theillusivec4.corpsecomplex.common.config.CorpseComplexConfig;
import top.theillusivec4.corpsecomplex.common.registry.CorpseComplexRegistry;
import top.theillusivec4.corpsecomplex.common.registry.RegistryReference;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

public class MementoMoriEffect extends MobEffect {

  private static final double INT_CHANGE = 1.0D;
  private static final double PERCENT_CHANGE = 0.05D;
  private static final String NAME = "Memento Mori";

  public static final Map<Attribute, AttributeInfo> ATTRIBUTES = new HashMap<>();

  public MementoMoriEffect() {
    super(MobEffectCategory.HARMFUL, 0);
    this.setRegistryName(RegistryReference.MEMENTO_MORI);
  }

  @Override
  public void applyEffectTick(@Nonnull LivingEntity entityLivingBaseIn, int amplifier) {
    MobEffectInstance effect = entityLivingBaseIn
        .getEffect(CorpseComplexRegistry.MEMENTO_MORI);

    if (effect != null) {
      int duration = effect.getDuration();
      ATTRIBUTES.forEach((attribute, info) -> {

        if (info.modifier.getAmount() != 0 && duration % info.tick == 0) {
          AttributeInstance instance = entityLivingBaseIn.getAttribute(attribute);

          if (instance != null) {
            AttributeModifier modifier = instance.getModifier(info.modifier.getId());

            if (modifier != null) {
              instance.removeModifier(modifier);
              instance.addTransientModifier(new AttributeModifier(modifier.getId(), modifier.getName(),
                  modifier.getAmount() + info.tickAmount, modifier.getOperation()));
            }
          }
        }
      });
    }

    if (entityLivingBaseIn.getHealth() > entityLivingBaseIn.getMaxHealth()) {
      entityLivingBaseIn.setHealth(entityLivingBaseIn.getMaxHealth());
    }
  }

  @Override
  public boolean isDurationEffectTick(int duration, int amplifier) {
    return duration < CorpseComplexConfig.SERVER.duration.get() * 20
        && CorpseComplexConfig.SERVER.gradualRecovery.get() && isChangeTick(duration);
  }

  private boolean isChangeTick(int duration) {
    return ATTRIBUTES.values().stream().anyMatch((info -> duration % info.tick == 0));
  }

  @Override
  public boolean isBeneficial() {
    return CorpseComplexConfig.SERVER.beneficial.get();
  }

  @Override
  public void removeAttributeModifiers(LivingEntity entityLivingBaseIn,
                                                  @Nonnull AttributeMap attributeMapIn, int amplifier) {

    for (Entry<Attribute, AttributeInfo> attribute : ATTRIBUTES.entrySet()) {
      AttributeInstance iattributeinstance = attributeMapIn
          .getInstance(attribute.getKey());

      if (iattributeinstance != null) {
        AttributeModifier modifier = iattributeinstance
            .getModifier(attribute.getValue().modifier.getId());

        if (modifier != null) {
          iattributeinstance.removeModifier(modifier);
        }
      }
    }
  }

  @Override
  public void addAttributeModifiers(LivingEntity entityLivingBaseIn,
      @Nonnull AttributeMap attributeMapIn, int amplifier) {

    for (Entry<Attribute, AttributeInfo> attribute : ATTRIBUTES.entrySet()) {
      AttributeInstance iattributeinstance = attributeMapIn
          .getInstance(attribute.getKey());

      if (iattributeinstance != null) {
        AttributeModifier attributemodifier = attribute.getValue().modifier;
        iattributeinstance.removeModifier(attributemodifier);
        iattributeinstance.addTransientModifier(
            new AttributeModifier(attributemodifier.getId(), NAME, attributemodifier.getAmount(),
                attributemodifier.getOperation()));
      }
    }
  }

  public static class AttributeInfo {

    final AttributeModifier modifier;
    int tick;
    double tickAmount;

    public AttributeInfo(double amount, UUID uuid, Operation operation) {
      this.modifier = new AttributeModifier(uuid, NAME, amount, operation);
      setTick(amount);
    }

    private void setTick(double amount) {
      double changeAmount;
      double sign = Math.signum(amount) * -1;
      double changeTick = Math.abs(CorpseComplexConfig.SERVER.duration.get() / amount) * 20;
      boolean percent = this.modifier.getOperation() != Operation.ADDITION;

      if (percent) {
        changeAmount = sign * PERCENT_CHANGE;
        changeTick *= PERCENT_CHANGE;
      } else {
        changeAmount = sign * INT_CHANGE;
        changeTick *= INT_CHANGE;
      }

      if (changeTick >= 1) {
        changeTick = Math.floor(changeTick);
      } else if (percent) {
        changeAmount = sign * (1 / changeTick);
        changeTick = 1;
      } else {
        changeAmount = sign * Math.floor(1 / changeTick);
        changeTick = 1;
      }
      this.tick = (int) changeTick;
      this.tickAmount = changeAmount;
    }
  }
}
