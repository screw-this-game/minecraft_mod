package tech.screwthisgame.effects;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.EffectInstance;

import net.minecraft.world.server.ServerWorld;

public class PotionEffect implements Effect {
    private final net.minecraft.potion.Effect effect;

    public PotionEffect(net.minecraft.potion.Effect effect) {
        this.effect = effect;
    }

    @Override
    public void Act(ServerWorld world) {
        for (ServerPlayerEntity player : world.getPlayers()) {
            player.addPotionEffect(new EffectInstance(effect, 2400));
        }
    }
}
