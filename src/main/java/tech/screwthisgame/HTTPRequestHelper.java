package tech.screwthisgame;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.*;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import tech.screwthisgame.events.BackendConnectionEvent;

import java.io.IOException;
import java.util.UUID;

public class HTTPRequestHelper {
    static OkHttpClient client = new OkHttpClient();
    static ObjectMapper mapper = new ObjectMapper();

    public void performConnection(World world) {
        Request request = new Request.Builder()
                .url("https://stg-api.monotron.me/client/register")
                .header("X-Client-Type", "MINECRAFT")
                .post(RequestBody.create(MediaType.parse("application/json"), ""))
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                ScrewThisGame.LOGGER.error("Failure!!!!!!!!!!!!!!!");
                ScrewThisGame.LOGGER.error(e.getMessage());
                MinecraftForge.EVENT_BUS.post(new BackendConnectionEvent(false, null, world));
            }

            @Override
            public void onResponse(Response response) throws IOException {
                ScrewThisGame.LOGGER.error("Success!!!!!!!!!!!!!!!");
                EstablishConnectionBody result = mapper.readValue(response.body().string(), EstablishConnectionBody.class);
                MinecraftForge.EVENT_BUS.post(new BackendConnectionEvent(true, result, world));
            }
        });
    }

    public static class EstablishConnectionBody {
        public String status;
        public UUID clientId;
    }
}
