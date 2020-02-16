package tech.screwthisgame.data;

import net.minecraft.nbt.*;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;
import tech.screwthisgame.ScrewThisGame;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

public class WorldData extends WorldSavedData {

    public static final String NAME = ScrewThisGame.MODID + "_WORLDDATA";

    public String connectionStatus;
    public UUID clientID;

    public ArrayList<String> queuedEvents;

    public WorldData() {
        super(NAME);
        connectionStatus = "";
        clientID = null;
        queuedEvents = new ArrayList<>();
    }

    @Override
    public void read(CompoundNBT nbt) {
        connectionStatus = nbt.getString(NAME + "_collectionStatus");
        if (nbt.hasUniqueId(NAME + "_clientID"))
            clientID = nbt.getUniqueId(NAME + "_clientID");
        queuedEvents = Arrays.stream(new String(nbt.getByteArray(NAME + "_queuedEvents"),
                Charset.defaultCharset()).split("\\|")).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putString(NAME + "_collectionStatus", connectionStatus);
        if (clientID != null)
            compound.putUniqueId(NAME + "_clientID", clientID);
        compound.putByteArray(NAME + "_queuedEvents", String.join("\\|", queuedEvents).getBytes(Charset.defaultCharset()));
        return compound;
    }

    public void setConnectionInfo(String connectionStatus, UUID clientID) {
        this.clientID = clientID;
        this.connectionStatus = connectionStatus;
        this.markDirty();
    }

    public void addQueuedEvents(ArrayList<String> events) {
        this.queuedEvents.addAll(events);
        this.markDirty();
    }


    public static WorldData get(World world) {
        if (!(world instanceof ServerWorld)) {
            throw new IllegalArgumentException("Cannot run on client!");
        }
        return ((ServerWorld) world).getSavedData().getOrCreate(WorldData::new, NAME);
    }
}
