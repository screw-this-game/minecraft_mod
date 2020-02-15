package tech.screwthisgame;

import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ScrewThisGame.MODID)
public class ScrewThisGame
{
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
}
