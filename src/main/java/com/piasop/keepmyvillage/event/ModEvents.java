package com.piasop.keepmyvillage.event;

import com.piasop.keepmyvillage.KeepMyVillage;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * This class handles all the server-side mod events.
 */
@Mod.EventBusSubscriber(modid = KeepMyVillage.MOD_ID)
public class ModEvents {

    /**
     * This event is fired when a player tries to break a block.
     * We check if the block is inside a village and cancel the event if it is.
     */
    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        // We only want to run this logic on the server side.
        if (event.getLevel().isClientSide()) {
            return;
        }

        // Cast to ServerLevel to access village information.
        ServerLevel serverLevel = (ServerLevel) event.getLevel();
        
        // Check if the block position is within a village's boundaries.
        if (serverLevel.isVillage(event.getPos())) {
            Player player = event.getPlayer();
            
            // --- INICIO DEL CAMBIO FINAL ---

            // If the player is in Creative mode, do nothing and let them break the block.
            // The 'return' statement exits the method immediately.
            if (player.isCreative()) {
                return;
            }

            // --- FIN DEL CAMBIO FINAL ---

            // All the code below will now ONLY run for players NOT in Creative mode.
            
            // Cancel the block breaking event.
            event.setCanceled(true);

            // Get random data for the message
            String villagerName = KeepMyVillage.getRandomName();
            String angryPhrase = KeepMyVillage.getRandomPhrase();
            
            // Create a MutableComponent for the villager's name and the colon.
            MutableComponent nameComponent = Component.literal(villagerName + ":")
                    .withStyle(ChatFormatting.GREEN, ChatFormatting.BOLD);

            // Create another MutableComponent for the angry phrase.
            MutableComponent phraseComponent = Component.literal(" " + angryPhrase)
                    .withStyle(ChatFormatting.RED, ChatFormatting.BOLD);

            // Combine the two styled components into a single final message.
            MutableComponent finalMessage = nameComponent.append(phraseComponent);

            // Send the composed message to the player's chat.
            player.displayClientMessage(finalMessage, false);

            // Play a random angry villager sound at the location.
            serverLevel.playSound(null, event.getPos(), KeepMyVillage.getRandomSound(), SoundSource.NEUTRAL, 1.0F, 1.0F);

            // We no longer need the old creative check, so we only handle the survival 'ghost block' fix.
            player.level().sendBlockUpdated(event.getPos(), event.getState(), event.getState(), 3);
        }
    }
}