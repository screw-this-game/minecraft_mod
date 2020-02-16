package tech.screwthisgame.effects;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.server.ServerWorld;

import java.util.function.BiFunction;

public class ExecuteCommandEffect implements Effect {

    private final String command;
    private final BiFunction<String, PlayerEntity, String> preProcessing;

    public ExecuteCommandEffect(String command, BiFunction<String, PlayerEntity, String> preProcessing) {
        this.command = command;
        this.preProcessing = preProcessing;
    }

    public ExecuteCommandEffect(String command) {
        this(command, null);
    }

    @Override
    public void Act(ServerWorld world) {
        for (ServerPlayerEntity player : world.getPlayers()) {
            String comm = command;
            if (preProcessing != null) comm = preProcessing.apply(comm, player);
            world.getServer().getCommandManager().handleCommand(world.getServer().getCommandSource(), comm);
        }
    }
}
