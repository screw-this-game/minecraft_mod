package tech.screwthisgame.events;

import com.google.common.eventbus.Subscribe;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistryEntry;
import tech.screwthisgame.HTTPRequestHelper;
import tech.screwthisgame.ScrewThisGame;
import tech.screwthisgame.data.WorldData;
import tech.screwthisgame.events.BackendConnectionEvent;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = ScrewThisGame.MODID)
public class EventSubscriber {

    static HTTPRequestHelper requestHelper = new HTTPRequestHelper();

    @SubscribeEvent
    public static void onServerStart(WorldEvent.Load event) {
        if (ScrewThisGame.isServer(event.getWorld().isRemote())) {
            requestHelper.performConnection((World) event.getWorld());
        }


    }

    @SubscribeEvent
    public static void onServerClose(WorldEvent.Unload event) {
        World world = (World) event.getWorld();
        if (ScrewThisGame.isServer(world.isRemote())) {
            WorldData data = WorldData.get(world.getWorld());
            data.connectionStatus = "";
            data.clientID = null;
            data.markDirty();
        }
    }
    @SubscribeEvent
    public static void onTick(TickEvent.WorldTickEvent event) {
        if (event.side.isServer()) {
            UUID clientID = WorldData.get(event.world).clientID;
            if (clientID != null) ScrewThisGame.LOGGER.error(clientID.toString());
            else                  ScrewThisGame.LOGGER.error("Not connected!");

        }
    }

    @SubscribeEvent
    public static void onBackendConnection(BackendConnectionEvent event) {
        ScrewThisGame.LOGGER.error(event.success ? "Success!" : "Oh no!");
        WorldData data = WorldData.get(event.world);
        data.connectionStatus = event.result.status;
        if (event.success) data.clientID = event.result.clientId;
        data.markDirty();
    }
}
