package tech.screwthisgame;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
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

        effectMap.put("summonLightning", world -> { for (ServerPlayerEntity player : world.getPlayers()) {
            BlockPos pos = player.getPosition();
            LightningBoltEntity lightning = new LightningBoltEntity(world, pos.getX(), pos.getY(), pos.getZ(), false);
            world.addEntity(lightning);
        }});

        effectMap.put("potionDizzy", new PotionEffect(Effects.NAUSEA));
        effectMap.put("potionPoison", new PotionEffect(Effects.POISON));
        effectMap.put("potionWither", new PotionEffect(Effects.WITHER));
        effectMap.put("potionBlind", new PotionEffect(Effects.BLINDNESS));
        effectMap.put("potionHunger", new PotionEffect(Effects.HUNGER));
        effectMap.put("potionFatigue", new PotionEffect(Effects.MINING_FATIGUE));
        effectMap.put("potionUnluck", new PotionEffect(Effects.UNLUCK));
        effectMap.put("potionSlowness", new PotionEffect(Effects.SLOWNESS));
        effectMap.put("potionBadOmen", new PotionEffect(Effects.BAD_OMEN));
    }
}
