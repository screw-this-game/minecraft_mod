package tech.screwthisgame.effects;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.server.ServerWorld;

public class PotionEffect implements Effect {
    private final EffectInstance effect;

    public PotionEffect(EffectInstance effect) {
        this.effect = effect;
    }

    @Override
    public void Act(ServerWorld world) {
        for (ServerPlayerEntity player : world.getPlayers()) {
            player.addPotionEffect(effect);
        }
    }
}
