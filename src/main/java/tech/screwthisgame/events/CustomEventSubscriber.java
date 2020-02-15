package tech.screwthisgame.events;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import tech.screwthisgame.ScrewThisGame;
import tech.screwthisgame.data.WorldData;

@Mod.EventBusSubscriber(modid = ScrewThisGame.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CustomEventSubscriber {
    @SubscribeEvent
    public static void onBackendConnection(BackendConnectionEvent event) {
        ScrewThisGame.LOGGER.error(event.success ? "Success!" : "Oh no!");
        WorldData data = WorldData.get(event.world);
        data.connectionStatus = event.result.status;
        if (event.success) data.clientID = event.result.clientId;
        data.markDirty();
    }
}
