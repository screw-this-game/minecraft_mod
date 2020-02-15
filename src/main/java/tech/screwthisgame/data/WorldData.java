package tech.screwthisgame.data;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;
import tech.screwthisgame.ScrewThisGame;

import java.util.UUID;

public class WorldData extends WorldSavedData {

    public static final String NAME = ScrewThisGame.MODID + "_WORLDDATA";

    public String connectionStatus;
    public UUID clientID;

    public WorldData() {
        super(NAME);
        connectionStatus = "";
        clientID = null;
    }

    @Override
    public void read(CompoundNBT nbt) {
        connectionStatus = nbt.getString(NAME + "_collectionStatus");
        clientID = nbt.getUniqueId(NAME + "_clientID");
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putString(NAME + "_collectionStatus", connectionStatus);
        compound.putUniqueId(NAME + "_clientID", clientID);
        return compound;
    }

    public static WorldData get(World world) {
        if (!(world instanceof ServerWorld)) {
            throw new IllegalArgumentException("Cannot run on client!");
        }

        ServerWorld overworld = world.getServer().getWorld(DimensionType.OVERWORLD);
        return overworld.getSavedData().getOrCreate(WorldData::new, NAME);
    }


}
