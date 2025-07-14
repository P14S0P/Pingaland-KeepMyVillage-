package com.piasop.keepmyvillage.core;

import com.piasop.keepmyvillage.KeepMyVillage;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

// --- AÑADIR ESTA LÍNEA DE IMPORTACIÓN ---
import javax.annotation.Nonnull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class VillageProtectorState extends SavedData {

    private final Map<BlockPos, UUID> placedBlocks = new HashMap<>();
    private static final String DATA_NAME = KeepMyVillage.MOD_ID + "_VillageData";

    public VillageProtectorState() {
        super();
    }

    public static VillageProtectorState load(CompoundTag tag) {
        VillageProtectorState state = new VillageProtectorState();
        ListTag list = tag.getList("placedBlocks", 10);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag blockTag = list.getCompound(i);
            BlockPos pos = NbtUtils.readBlockPos(blockTag.getCompound("pos"));
            UUID owner = blockTag.getUUID("owner");
            state.placedBlocks.put(pos, owner);
        }
        return state;
    }

    @Override
    // --- CORRECCIÓN AQUÍ ---
    // Se añade la anotación @Nonnull directamente al parámetro.
    public CompoundTag save(@Nonnull CompoundTag compoundTag) {
        ListTag list = new ListTag();
        placedBlocks.forEach((pos, owner) -> {
            CompoundTag blockTag = new CompoundTag();
            blockTag.put("pos", NbtUtils.writeBlockPos(pos));
            blockTag.putUUID("owner", owner);
            list.add(blockTag);
        });
        compoundTag.put("placedBlocks", list);
        return compoundTag;
    }

    public static VillageProtectorState get(ServerLevel level) {
        DimensionDataStorage storage = level.getDataStorage();
        return storage.computeIfAbsent(VillageProtectorState::load, VillageProtectorState::new, DATA_NAME);
    }

    public void addBlock(BlockPos pos, UUID owner) {
        placedBlocks.put(pos, owner);
        setDirty();
    }

    public void removeBlock(BlockPos pos) {
        if (placedBlocks.containsKey(pos)) {
            placedBlocks.remove(pos);
            setDirty();
        }
    }

    public boolean isPlayerPlaced(BlockPos pos) {
        return placedBlocks.containsKey(pos);
    }



    public boolean isOwner(BlockPos pos, UUID player) {
        return player.equals(placedBlocks.get(pos));
    }
}