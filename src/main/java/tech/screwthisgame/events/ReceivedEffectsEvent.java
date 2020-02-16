package tech.screwthisgame.events;

import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.Event;
import tech.screwthisgame.util.HTTPRequestHelper;

public class ReceivedEffectsEvent extends Event {
    public final HTTPRequestHelper.GetEffectsBody result;
    public final World world;

    public ReceivedEffectsEvent(HTTPRequestHelper.GetEffectsBody result, World world) {
        this.result = result;
        this.world = world;
    }
}
