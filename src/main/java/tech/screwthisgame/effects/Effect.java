package tech.screwthisgame.effects;

import net.minecraft.world.server.ServerWorld;

public interface Effect {
    void Act(ServerWorld world);
}
