package me.dueris.genesismc.factory.powers.apoli;

import me.dueris.genesismc.factory.CraftApoli;
import me.dueris.genesismc.factory.conditions.ConditionExecutor;
import me.dueris.genesismc.factory.powers.CraftPower;
import me.dueris.genesismc.registry.registries.Layer;
import me.dueris.genesismc.registry.registries.Power;
import me.dueris.genesismc.util.entity.OriginPlayerAccessor;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ArrayList;

public class ModifyHarvestPower extends CraftPower implements Listener {

	@EventHandler(priority = EventPriority.MONITOR)
	public void runD(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if (modify_harvest.contains(p)) {
			if (p.getGameMode().equals(GameMode.CREATIVE)) return;
			for (Layer layer : CraftApoli.getLayersFromRegistry()) {
				for (Power power : OriginPlayerAccessor.getPowers(p, getType(), layer)) {
					if (ConditionExecutor.testEntity(power.getJsonObject("condition"), (CraftEntity) p)) {
						setActive(p, power.getTag(), true);
						if (e.isCancelled()) return;
						boolean willDrop = ((CraftPlayer) p).getHandle().hasCorrectToolForDrops(((CraftBlock) e.getBlock()).getNMS());
						if (power.getBooleanOrDefault("allow", true) && !willDrop) {
							e.getBlock().getDrops().forEach((itemStack -> p.getWorld().dropItemNaturally(e.getBlock().getLocation(), itemStack)));
						}
					} else {
						setActive(p, power.getTag(), false);
					}
				}
			}
		}
	}

	@Override
	public String getType() {
		return "apoli:modify_harvest";
	}

	@Override
	public ArrayList<Player> getPlayersWithPower() {
		return modify_harvest;
	}
}
