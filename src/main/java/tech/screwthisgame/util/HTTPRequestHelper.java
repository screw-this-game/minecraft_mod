package tech.screwthisgame.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.*;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import tech.screwthisgame.ScrewThisGame;
import tech.screwthisgame.data.WorldData;
import tech.screwthisgame.events.BackendConnectionEvent;
import tech.screwthisgame.events.ReceivedEffectsEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class HTTPRequestHelper {
    static OkHttpClient client = new OkHttpClient();
    static ObjectMapper mapper = new ObjectMapper();

    public void getClientID(World world) {
        Request request = new Request.Builder()
                .url("https://stg-api.monotron.me/client/register")
                .header("X-Client-Type", "MINECRAFT")
                .post(RequestBody.create(MediaType.parse("application/json"), ""))
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                ScrewThisGame.LOGGER.error(e.getMessage());
                MinecraftForge.EVENT_BUS.post(new BackendConnectionEvent(false, null, world));
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.code() != 200) {
                    ScrewThisGame.LOGGER.warn(String.format("Bad response code for getClientID: %d", response.code()));
                }
                GetClientIDBody result;
                try {
                    result = mapper.readValue(response.body().string(), GetClientIDBody.class);
                } catch(IOException e) {
                    ScrewThisGame.LOGGER.warn(e);
                    return;
                }
                MinecraftForge.EVENT_BUS.post(new BackendConnectionEvent(true, result, world));
            }
        });
    }

    public void getEffects(World world) {
        UUID clientID = WorldData.get(world).clientID;
        if (clientID == null) {
            ScrewThisGame.LOGGER.warn("Trying to get effects with no client ID!");
            return;
        }
        Request request = new Request.Builder()
                .url(String.format("https://stg-api.monotron.me/client/%s/effects", clientID.toString()))
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                ScrewThisGame.LOGGER.error(e.getMessage());
            }

            @Override
            public void onResponse(Response response) {
                if (response.code() != 200) {
                    ScrewThisGame.LOGGER.warn(String.format("Bad response code for getEffects: %d", response.code()));
                    return;
                }
                GetEffectsBody result;
                try {
                    result = mapper.readValue(response.body().string(), GetEffectsBody.class);
                } catch(IOException e) {
                    ScrewThisGame.LOGGER.warn(e);
                    return;
                }
                MinecraftForge.EVENT_BUS.post(new ReceivedEffectsEvent(result, world));
            }
        });
    }

    public void sendEffectsToServer(World world) {
        UUID clientID = WorldData.get(world).clientID;
        if (clientID == null) {
            ScrewThisGame.LOGGER.error("Trying to send effects with no client ID!");
            return;
        }
        Request request = new Request.Builder()
                .url(String.format("https://stg-api.monotron.me/frontend/effects/%s?effectName=%s", clientID.toString()))
                .put(RequestBody.create(MediaType.parse("application/json"), ""))
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                ScrewThisGame.LOGGER.error(e.getMessage());
            }

            @Override
            public void onResponse(Response response) {
                if (response.code() != 200) {
                    ScrewThisGame.LOGGER.warn(String.format("Bad response code for getEffects: %d", response.code()));
                    return;
                }
                GetEffectsBody result;
                try {
                    result = mapper.readValue(response.body().string(), GetEffectsBody.class);
                } catch(IOException e) {
                    ScrewThisGame.LOGGER.warn(e);
                    return;
                }
                MinecraftForge.EVENT_BUS.post(new ReceivedEffectsEvent(result, world));
            }
        });

    }

    public static class GetClientIDBody {
        public String status;
        public UUID clientId;
    }

    public static class GetEffectsBody {
        public String status;
        public ArrayList<String> effects;
    }
}
