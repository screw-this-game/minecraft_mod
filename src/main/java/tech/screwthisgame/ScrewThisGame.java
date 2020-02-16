package tech.screwthisgame;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tech.screwthisgame.effects.Effect;
import tech.screwthisgame.effects.ExecuteCommandEffect;
import tech.screwthisgame.effects.PotionEffect;
import tech.screwthisgame.effects.SummonCreatureEffect;

import java.util.HashMap;
import java.util.function.Consumer;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ScrewThisGame.MODID)
public class ScrewThisGame
{
    public ScrewThisGame() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ScrewThisGame::CommonSetup);
    }

    public static final String MODID = "screw-this-game";
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();

    // Hate the naming of isRemote so little convenience function here for my sanity.
    public static boolean isClient(boolean isRemote) {
        return isRemote;
    }

    public static boolean isServer(boolean isRemote) {
        return !isRemote;
    }

    public static HashMap<String, Effect> effectMap;

    public static void CommonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("Common Setup");
        setupEffectMap();
    }

    public static void setupEffectMap() {
        LOGGER.info("Setting up effects!");
        effectMap = new HashMap<>();
        effectMap.put("summonAngryBee", new SummonCreatureEffect(EntityType.field_226289_e_, (entity, player) -> player.attackTargetEntityWithCurrentItem(entity)));
        effectMap.put("summonBlaze", new SummonCreatureEffect(EntityType.BLAZE));
        effectMap.put("summonCaveSpider", new SummonCreatureEffect(EntityType.CAVE_SPIDER));
        effectMap.put("summonCreeper", new SummonCreatureEffect(EntityType.CREEPER));
        effectMap.put("summonEnderman", new SummonCreatureEffect(EntityType.ENDERMAN));
        effectMap.put("summonHusk", new SummonCreatureEffect(EntityType.HUSK));
        effectMap.put("summonPhantom", new SummonCreatureEffect(EntityType.PHANTOM));
        effectMap.put("summonPillager", new SummonCreatureEffect(EntityType.PILLAGER));
        effectMap.put("summonSkeleton", new SummonCreatureEffect(EntityType.SKELETON));
        effectMap.put("summonSpider", new SummonCreatureEffect(EntityType.SPIDER));
        effectMap.put("summonTNT", new SummonCreatureEffect(EntityType.TNT, (entity, player) -> ((TNTEntity) entity).setFuse(100)));
        effectMap.put("summonWitch", new SummonCreatureEffect(EntityType.WITCH));
        effectMap.put("summonZombie", new SummonCreatureEffect(EntityType.ZOMBIE));
        effectMap.put("suffocate", new ExecuteCommandEffect("/fill %d %d %d %d %d %d minecraft:gravel replace minecraft:air", (command, player) -> {
            BlockPos coords = player.getPosition();
            return String.format(command, coords.getX()+2, coords.getY(), coords.getZ()+2, coords.getX()-2, coords.getY()+3, coords.getZ()-2);
        }));

        effectMap.put("potionDizzy", new PotionEffect(new EffectInstance(Effects.NAUSEA, 100)));
        effectMap.put("potionPoison", new PotionEffect(new EffectInstance(Effects.POISON, 100)));
        effectMap.put("potionWither", new PotionEffect(new EffectInstance(Effects.WITHER, 100)));
        effectMap.put("potionBlind", new PotionEffect(new EffectInstance(Effects.BLINDNESS, 100)));
        effectMap.put("potionHunger", new PotionEffect(new EffectInstance(Effects.HUNGER, 100)));
        effectMap.put("potionFatigue", new PotionEffect(new EffectInstance(Effects.MINING_FATIGUE, 100)));
        effectMap.put("potionUnluck", new PotionEffect(new EffectInstance(Effects.UNLUCK, 100)));
        effectMap.put("potionSlowness", new PotionEffect(new EffectInstance(Effects.SLOWNESS, 100)));
        effectMap.put("potionBadOmen", new PotionEffect(new EffectInstance(Effects.BAD_OMEN, 100)));
    }

}
