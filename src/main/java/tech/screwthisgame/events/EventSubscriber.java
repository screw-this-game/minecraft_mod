package tech.screwthisgame.events;

import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import tech.screwthisgame.util.HTTPRequestHelper;
import tech.screwthisgame.ScrewThisGame;
import tech.screwthisgame.data.WorldData;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = ScrewThisGame.MODID)
public class EventSubscriber {

    static final HTTPRequestHelper requestHelper = new HTTPRequestHelper();

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
            if (clientID != null) requestHelper.getEffects(event.world);
            else                  ScrewThisGame.LOGGER.error("Not connected!");
        }
    }

    @SubscribeEvent
    public static void onBackendConnection(BackendConnectionEvent event) {
        WorldData data = WorldData.get(event.world);

        if (!event.success) {
            ScrewThisGame.LOGGER.warn("BackendConnectionEvent failure!");
            return;
        }
        data.setConnectionInfo(event.result.status, event.result.clientId);
        ScrewThisGame.LOGGER.info("(onBackend) HERE IT IS:");
        ScrewThisGame.LOGGER.info(event.result.clientId.toString());
        //requestHelper.sendEffectsToServer(event.world);
    }

    @SubscribeEvent
    public static void onReceiveEffects(ReceivedEffectsEvent event) {
        WorldData data = WorldData.get(event.world);
        data.addQueuedEvents(event.result.effects);
        ScrewThisGame.LOGGER.info("(onReceive) HERE IT IS:");
        ScrewThisGame.LOGGER.info(event.result.effects.toString());
        for (String effect : event.result.effects) {
            if (!ScrewThisGame.effectMap.containsKey(effect)) {
                ScrewThisGame.LOGGER.error("Getting effect that we cannot produce: " + effect);
            }
            ScrewThisGame.effectMap.get(effect).Act((ServerWorld) event.world);
        }
    }
}
