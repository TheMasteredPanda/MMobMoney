package dev.themasteredpanda.mmobmoney;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class MobListener implements Listener {

    public MMobMoney plugin;

    public MobListener(MMobMoney plugin) {
       plugin.log("Loading listener.");
       this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDeath(EntityDamageByEntityEvent e) {
        plugin.log(e.getEntityType().toString());

        if (!plugin.getMobs().containsKey(e.getEntityType())) {
           return;
        }

        if (!(e.getEntity() instanceof LivingEntity)) {
            return;
        }

        if (((LivingEntity) e.getEntity()).getHealth() - e.getDamage() <= 0 && e.getDamager() instanceof Player) {
            Player p = (Player) e.getDamager();
            plugin.getEcon().depositPlayer(p, plugin.getMobs().get(e.getEntityType()));
            plugin.log("Deposited " + plugin.getEcon().format(plugin.getMobs().get(e.getEntityType())));
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', MMobMoney.PREFIX + ChatColor.GREEN + " Deposited " + plugin.getEcon().format(plugin.getMobs().get(e.getEntityType())) + " for killing entity " + e.getEntityType().toString().toLowerCase() + "."));
        }
    }
}
