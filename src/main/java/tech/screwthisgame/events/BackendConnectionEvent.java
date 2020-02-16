package tech.screwthisgame.events;

import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.Event;
import tech.screwthisgame.util.HTTPRequestHelper;

public class BackendConnectionEvent extends Event {
    public final HTTPRequestHelper.GetClientIDBody result;
    public final World world;
    public final boolean success;

    public BackendConnectionEvent(boolean success, HTTPRequestHelper.GetClientIDBody result, World world) {
        this.success = success;
        this.result = result;
        this.world = world;
    }
}
