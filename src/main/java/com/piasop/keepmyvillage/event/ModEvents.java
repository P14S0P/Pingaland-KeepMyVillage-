package com.piasop.keepmyvillage.event;

import com.piasop.keepmyvillage.core.VillageProtectorState;
import com.piasop.keepmyvillage.core.config.ModConfig;
import com.piasop.keepmyvillage.KeepMyVillage;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = KeepMyVillage.MOD_ID)
public class ModEvents {

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (event.getLevel().isClientSide() || !(event.getLevel() instanceof ServerLevel serverLevel)) {
            return;
        }

        if (serverLevel.isVillage(event.getPos())) {
            Player player = event.getPlayer();

            if (player.isCreative()) {
                return;
            }
            
            Block block = event.getState().getBlock();
            ResourceLocation blockId = ForgeRegistries.BLOCKS.getKey(block);
            if (blockId != null && ModConfig.ALLOWED_BREAKABLE_BLOCKS.get().contains(blockId.toString())) {
                 VillageProtectorState.get(serverLevel).removeBlock(event.getPos());
                return;
            }

            VillageProtectorState protectionData = VillageProtectorState.get(serverLevel);

            if (protectionData.isPlayerPlaced(event.getPos())) {
                protectionData.removeBlock(event.getPos());
                return;
            }
            
            event.setCanceled(true);

            String villagerName = KeepMyVillage.getRandomName();
            String angryPhrase = KeepMyVillage.getRandomPhrase();

            MutableComponent nameComponent = Component.literal(villagerName + ":").withStyle(ChatFormatting.GREEN, ChatFormatting.BOLD);
            MutableComponent phraseComponent = Component.literal(" " + angryPhrase).withStyle(ChatFormatting.RED, ChatFormatting.BOLD);
            player.displayClientMessage(nameComponent.append(phraseComponent), false);

            serverLevel.playSound(null, event.getPos(), KeepMyVillage.getRandomSound(), SoundSource.NEUTRAL, 1.0F, 1.0F);

            player.level().sendBlockUpdated(event.getPos(), event.getState(), event.getState(), 3);
        }
    }
    
    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        if (event.getLevel().isClientSide() || !(event.getEntity() instanceof Player)) {
            return;
        }
        
        ServerLevel level = (ServerLevel) event.getLevel();
        Player player = (Player) event.getEntity();

        if (level.isVillage(event.getPos())) {
            VillageProtectorState protectionData = VillageProtectorState.get(level);
            protectionData.addBlock(event.getPos(), player.getUUID());
        }
    }
}