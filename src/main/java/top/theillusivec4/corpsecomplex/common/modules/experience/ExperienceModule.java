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

package top.theillusivec4.corpsecomplex.common.modules.experience;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.corpsecomplex.CorpseComplex;
import top.theillusivec4.corpsecomplex.common.capability.DeathStorageCapability;
import top.theillusivec4.corpsecomplex.common.capability.DeathStorageCapability.IDeathStorage;
import top.theillusivec4.corpsecomplex.common.util.Enums.XpDropMode;
@Mod.EventBusSubscriber(modid = CorpseComplex.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ExperienceModule {

  @SubscribeEvent
  public static void playerXpDrop(final LivingExperienceDropEvent evt) {

    if (evt.getEntity() instanceof Player) {
      Player player = (Player) evt.getEntity();
      DeathStorageCapability.getCapability(player).ifPresent(deathStorage -> {
        double lose = deathStorage.getSettings().getExperienceSettings().getLostXp();

        if (lose <= 0.0D) {
          evt.setCanceled(true);
          return;
        }
        int totalPoints = getExperiencePoints(player);
        int level = player.experienceLevel;
        int lostPoints = (int) (lose * totalPoints);
        int keptXp = totalPoints - lostPoints;
        player.experienceProgress = 0;
        player.totalExperience = 0;
        player.experienceLevel = 0;
        player.giveExperiencePoints(keptXp);
        int lostLevels = level - player.experienceLevel;
        int droppedXp = getDroppedExperiencePoints(player, lostLevels, lostPoints, deathStorage);
        evt.setDroppedExperience(droppedXp);
      });
    }
  }

  @SubscribeEvent
  public static void playerRespawn(final PlayerEvent.Clone evt) {

    if (evt.isWasDeath()) {
      Player playerEntity = evt.getOriginal();
      DeathStorageCapability.getCapability(playerEntity).ifPresent(deathStorage -> {
        Player original = evt.getOriginal();
        if (deathStorage.getSettings().getExperienceSettings().getLostXp() < 1) {
          playerEntity.experienceProgress = original.experienceProgress;
          playerEntity.experienceLevel = original.experienceLevel;
          playerEntity.totalExperience = original.totalExperience;
        }
      });
    }
  }

  private static int getExperiencePointsFromLevel(int levels) {
    int points = 0;

    if (levels <= 16) {
      points += levels * levels + 6 * levels;
    } else if (levels <= 31) {
      points += levels * levels * 2.5f - levels * 40.5f + 360;
    } else {
      points += levels * levels * 4.5f - levels * 162.5f + 2220;
    }
    return points;
  }

  private static float getLevelsFromExperiencePoints(int points) {
    int levels;

    if (points < 394) {
      levels = (int) (-3 + Math.sqrt(points + 9));
    } else if (points < 1628) {
      levels = (int) (8.1 + 0.1 * Math.sqrt(40 * points - 7839));
    } else {
      levels = (int) (18.0556 + 0.0555556 * Math.sqrt(72 * points - 54215));
    }
    return levels;
  }

  private static int getExperiencePoints(Player player) {
    int points = Math.round(player.getXpNeededForNextLevel() * player.experienceProgress);
    return points + getExperiencePointsFromLevel(player.experienceLevel);
  }

  private static int getDroppedExperiencePoints(Player player, int lostLevels, int lostPoints,
                                                IDeathStorage deathStorage) {

    if (!player.isSpectator()) {
      int i;
      ExperienceSetting setting = deathStorage.getSettings().getExperienceSettings();

      if (setting.getXpDropMode() == XpDropMode.PER_LEVEL) {
        i = lostLevels * setting.getDroppedXpPerLevel();
      } else {
        i = (int) (lostPoints * setting.getDroppedXpPercent());
      }
      return Math.min(i, setting.getMaxDroppedXp());
    } else {
      return 0;
    }
  }
}
