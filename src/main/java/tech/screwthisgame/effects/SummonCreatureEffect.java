package tech.screwthisgame.effects;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import tech.screwthisgame.ScrewThisGame;

import java.util.function.BiConsumer;

public class SummonCreatureEffect implements Effect {
    private final BiConsumer<Entity, ServerPlayerEntity> preProcessing;
    private final EntityType<? extends Entity> entityClass;

    public SummonCreatureEffect(EntityType<? extends Entity> e, BiConsumer<Entity, ServerPlayerEntity> c) {
        entityClass = e;
        preProcessing = c;
    }
    public SummonCreatureEffect(EntityType<? extends Entity> e) {
        this(e, null);
    }

    @Override
    public void Act(ServerWorld world) {
        for (ServerPlayerEntity player : world.getPlayers()) {
            Entity entity = entityClass.create(world);
            BlockPos playerPos = player.getPosition();
            if (entity == null) {
                ScrewThisGame.LOGGER.error("Could not spawn entity!");
                return;
            }
            entity.setPositionAndUpdate(playerPos.getX(), playerPos.getY(), playerPos.getZ());
            if (preProcessing != null) preProcessing.accept(entity, player);
            world.addEntity(entity);
        }
    }
}
