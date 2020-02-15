package tech.screwthisgame.events;

import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import tech.screwthisgame.HTTPRequestHelper;
import tech.screwthisgame.ScrewThisGame;
import tech.screwthisgame.data.WorldData;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = ScrewThisGame.MODID)
public class EventSubscriber {

    static HTTPRequestHelper requestHelper = new HTTPRequestHelper();

    @SubscribeEvent
    public static void onServerStart(FMLServerStartedEvent event) {
        World world = event.getServer().getWorld(DimensionType.OVERWORLD);
        if (ScrewThisGame.isServer(world.isRemote())) {
            requestHelper.getClientID(world);
        }
    }

    @SubscribeEvent
    public static void onServerClose(FMLServerStoppingEvent event) {
        World world = event.getServer().getWorld(DimensionType.OVERWORLD);
        if (ScrewThisGame.isServer(world.isRemote())) {
            WorldData data = WorldData.get(world.getWorld());
            data.setConnectionInfo("", null);
        }
    }

    @SubscribeEvent
    public static void onTick(TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.END) return;
        if (event.side.isServer() && event.world.getGameTime() % 100 == 0) {
            UUID clientID = WorldData.get(event.world).clientID;
            if (clientID != null) ScrewThisGame.LOGGER.info(clientID.toString());
            else                  ScrewThisGame.LOGGER.error("Not connected!");

        }
    }

    @SubscribeEvent
    public static void onBackendConnection(BackendConnectionEvent event) {
        WorldData data = WorldData.get(event.world);

        if (!event.success)
            ScrewThisGame.LOGGER.warn("BackendConnectionEvent failure!");
        else
            data.clientID = event.result.clientId;
        data.setConnectionInfo(event.result.status, event.result.clientId);
    }

    @SubscribeEvent
    public static void onReceiveEffects(ReceivedEffectsEvent event) {
        WorldData data = WorldData.get(event.world);
        data.addQueuedEvents(event.result.effects);
    }
}
