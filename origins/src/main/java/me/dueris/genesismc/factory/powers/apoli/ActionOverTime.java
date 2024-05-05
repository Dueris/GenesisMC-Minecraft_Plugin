package me.dueris.genesismc.factory.powers.apoli;

import me.dueris.genesismc.factory.actions.Actions;
import me.dueris.genesismc.factory.conditions.ConditionExecutor;
import me.dueris.genesismc.factory.powers.CraftPower;
import me.dueris.genesismc.registry.registries.Power;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class ActionOverTime extends CraftPower {

	private static final HashMap<String /*tag*/, Boolean /*allowed*/> taggedAllowedMap = new HashMap<>();

	@Override
	public void run(Player p, Power power) {
		long interval = power.getNumberOrDefault("interval", 20L).getLong();
		if (Bukkit.getServer().getCurrentTick() % interval == 0) {
			taggedAllowedMap.putIfAbsent(power.getTag(), false);
			if (ConditionExecutor.testEntity(power.getJsonObject("condition"), (CraftEntity) p)) {
				if (!taggedAllowedMap.get(power.getTag())) {
					taggedAllowedMap.put(power.getTag(), true);
					Actions.executeEntity(p, power.getJsonObject("rising_action"));
				}
				setActive(p, power.getTag(), true);
				Actions.executeEntity(p, power.getJsonObject("entity_action"));
			} else {
				if (taggedAllowedMap.get(power.getTag())) {
					taggedAllowedMap.put(power.getTag(), false);
					Actions.executeEntity(p, power.getJsonObject("falling_action"));
				}
				setActive(p, power.getTag(), false);
			}
		}
	}

	@Override
	public String getType() {
		return "apoli:action_over_time";
	}

	@Override
	public ArrayList<Player> getPlayersWithPower() {
		return action_ove_time;
	}

}
