package dev.themasteredpanda.mmobmoney;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Set;

public class MMobMoney extends JavaPlugin {

    public static String PREFIX;
    @Getter
    public Economy econ;
    @Getter
    public HashMap<EntityType, Double> mobs = Maps.newHashMap();

    @Override
    public void onEnable()
    {
        saveResource("config.yml", false);
        PREFIX = ChatColor.translateAlternateColorCodes('&', getConfig().getString("mobmoney.prefix"));
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            warn("Couldn't get economy provider.");
            getPluginLoader().disablePlugin(this);
            return;
        }

        econ = rsp.getProvider();
        populateMobMap();
        Bukkit.getPluginManager().registerEvents(new MobListener(this), this);
    }

    public void log(String message)
    {
       getLogger().info(message);
    }

    public void warn(String message)
    {
        getLogger().warning(message);
    }

    private void populateMobMap()
    {
        Set<String> keys = getConfig().getConfigurationSection("mobmoney.mobs").getKeys(true);

        for (String key : keys) {
            EntityType type = null;
            double value = 0.00;

            try {
                type = EntityType.valueOf(key.toUpperCase());
            } catch (IllegalArgumentException ex) {
                warn(ex.getMessage());
                ex.printStackTrace();
                continue;
            }

            if (!type.isAlive() && type.isSpawnable()) {
               warn(key + " is not a valid mob. It must be alive and spawnable.");
               continue;
            }

            try {
                value = getConfig().getDouble("mobmoney.mobs." + key);
            } catch (IllegalArgumentException ex) {
               warn(key + "'s value is not a double (n.nn)");
               continue;
            }

            mobs.put(type, value);
        }
    }
}
