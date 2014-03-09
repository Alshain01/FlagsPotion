package io.github.alshain01.flagspotion;

import io.github.alshain01.flags.Flag;
import io.github.alshain01.flags.Flags;
import io.github.alshain01.flags.ModuleYML;
import io.github.alshain01.flags.area.Area;
import io.github.alshain01.flags.events.PlayerChangedUniqueAreaEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Flags Potion - Module that adds potion effect flags to the plug-in Flags.
 */
public class FlagsPotion extends JavaPlugin {
    /**
     * Called when this module is enabled
     */
    @Override
    public void onEnable() {
        final PluginManager pm = Bukkit.getServer().getPluginManager();

        if (!pm.isPluginEnabled("Flags")) {
            getLogger().severe("Flags was not found. Shutting down.");
            pm.disablePlugin(this);
        }

        if (!Flags.getBorderPatrolEnabled()) {
            getLogger().severe("Flags Border Patrol is disabled. Shutting down.");
            pm.disablePlugin(this);
        }

        // Connect to the data file and register the flags
        Set<Flag> flags = Flags.getRegistrar().register(new ModuleYML(this, "flags.yml"), "Potion");
        Map<String, Flag> flagMap = new HashMap<String, Flag>();
        for(Flag f : flags) {
            flagMap.put(f.getName(), f);
        }

        // Load plug-in events and data
        Bukkit.getServer().getPluginManager().registerEvents(new AreaListener(this, flagMap), this);
    }

    private class AreaListener implements Listener {
        private final JavaPlugin plugin;
        private final Map<String, Flag> flags;

        AreaListener(JavaPlugin plugin, Map<String, Flag> flags) {
            this.plugin = plugin;
            this.flags = flags;
        }

        @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
        private void onPlayerChangeUniqueArea(PlayerChangedUniqueAreaEvent e) {
            final Area areaTo = e.getArea();
            final Area areaFrom = e.getArea();
            final Player player = e.getPlayer();
            final EffectCollectionBuilder effectBuilder = new EffectCollectionBuilder();

            Flag flag = flags.get("PotionBlindness");
            effectBuilder.newEffect(PotionEffectType.BLINDNESS, areaTo.getValue(flag, false), !areaFrom.getValue(flag, false));

            flag = flags.get("PotionConfusion");
            effectBuilder.newEffect(PotionEffectType.CONFUSION, areaTo.getValue(flag, false), !areaFrom.getValue(flag, false));

            flag = flags.get("PotionDamageResist");
            effectBuilder.newEffect(PotionEffectType.DAMAGE_RESISTANCE , areaTo.getValue(flag, false), !areaFrom.getValue(flag, false));

            flag = flags.get("PotionFastDigging");
            effectBuilder.newEffect(PotionEffectType.FAST_DIGGING , areaTo.getValue(flag, false), !areaFrom.getValue(flag, false));

            flag = flags.get("PotionFireResist");
            effectBuilder.newEffect(PotionEffectType.FIRE_RESISTANCE , areaTo.getValue(flag, false), !areaFrom.getValue(flag, false));

            flag = flags.get("PotionHealthBoost");
            effectBuilder.newEffect(PotionEffectType.HEALTH_BOOST , areaTo.getValue(flag, false), !areaFrom.getValue(flag, false));

            flag = flags.get("PotionIncreaseDamage");
            effectBuilder.newEffect(PotionEffectType.INCREASE_DAMAGE , areaTo.getValue(flag, false), !areaFrom.getValue(flag, false));

            flag = flags.get("PotionInvisibility");
            effectBuilder.newEffect(PotionEffectType.INVISIBILITY , areaTo.getValue(flag, false), !areaFrom.getValue(flag, false));

            flag = flags.get("PotionJump");
            effectBuilder.newEffect(PotionEffectType.JUMP , areaTo.getValue(flag, false), !areaFrom.getValue(flag, false));

            flag = flags.get("PotionNightVision");
            effectBuilder.newEffect(PotionEffectType.NIGHT_VISION , areaTo.getValue(flag, false), !areaFrom.getValue(flag, false));

            flag = flags.get("PotionPoison");
            effectBuilder.newEffect(PotionEffectType.POISON , areaTo.getValue(flag, false), !areaFrom.getValue(flag, false));

            flag = flags.get("PotionRegeneration");
            effectBuilder.newEffect(PotionEffectType.REGENERATION , areaTo.getValue(flag, false), !areaFrom.getValue(flag, false));

            flag = flags.get("PotionSaturation");
            effectBuilder.newEffect(PotionEffectType.SATURATION , areaTo.getValue(flag, false), !areaFrom.getValue(flag, false));

            flag = flags.get("PotionSlow");
            effectBuilder.newEffect(PotionEffectType.SLOW , areaTo.getValue(flag, false), !areaFrom.getValue(flag, false));

            flag = flags.get("PotionSlowDigging");
            effectBuilder.newEffect(PotionEffectType.SLOW_DIGGING , areaTo.getValue(flag, false), !areaFrom.getValue(flag, false));

            flag = flags.get("PotionSpeed");
            effectBuilder.newEffect(PotionEffectType.SPEED , areaTo.getValue(flag, false), !areaFrom.getValue(flag, false));

            flag = flags.get("PotionWaterBreathing");
            effectBuilder.newEffect(PotionEffectType.WATER_BREATHING , areaTo.getValue(flag, false), !areaFrom.getValue(flag, false));

            flag = flags.get("PotionWeakness");
            effectBuilder.newEffect(PotionEffectType.WEAKNESS , areaTo.getValue(flag, false), !areaFrom.getValue(flag, false));

            for(PotionEffect p : effectBuilder.addEffects) {
                player.addPotionEffect(p, true);
            }

            for (PotionEffectType p : effectBuilder.removeEffects) {
                player.removePotionEffect(p);
            }
        }
    }

    private class EffectCollectionBuilder {
        final Set<PotionEffect> addEffects = new HashSet<PotionEffect>();
        final Set<PotionEffectType> removeEffects = new HashSet<PotionEffectType>();

        private void newEffect(PotionEffectType effect, boolean add, boolean remove) {
            if(add) {
                addEffects.add(new PotionEffect(effect, 2147483647, 0));
            } else if(remove) {
                removeEffects.add(effect);
            }
        }
    }
}
