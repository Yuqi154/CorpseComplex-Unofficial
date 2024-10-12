package top.theillusivec4.corpsecomplex.common.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class ScrollItem extends Item {
    public ScrollItem() {
        super(new Properties().stacksTo(16));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            Optional<GlobalPos> globalPos = player.getLastDeathLocation();
            if (globalPos.isEmpty()) {
                player.sendSystemMessage(Component.literal("没有可以返回的死亡点"));
                return InteractionResultHolder.fail(itemstack);
            }
            player.startUsingItem(hand);
        }
        return InteractionResultHolder.consume(itemstack);
    }

    @Override
    public void releaseUsing(ItemStack itemStack, Level level, LivingEntity entityLiving, int pTimeLeft){
        if (!level.isClientSide && entityLiving instanceof ServerPlayer player){
            Optional<GlobalPos> globalPos = player.getLastDeathLocation();
            if (globalPos.isPresent()){
                BlockPos blockPos = globalPos.get().pos();
                player.teleportTo(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                player.getCooldowns().addCooldown(this, 20);
                itemStack.shrink(1);
            }
        }
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 20;
    }
    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BOW;
    }
    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        pTooltipComponents.add(Component.literal("返回死亡地点"));
    }

    @Override
    public boolean isFoil(@NotNull ItemStack pStack) {
        return true;
    }
}
