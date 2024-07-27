package me.dueris.originspaper.factory.powers.apoli;

import me.dueris.calio.data.FactoryData;
import me.dueris.calio.data.factory.FactoryJsonArray;
import me.dueris.calio.data.factory.FactoryJsonObject;
import me.dueris.originspaper.OriginsPaper;
import me.dueris.originspaper.factory.data.types.modifier.Modifier;
import me.dueris.originspaper.util.Util;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.BinaryOperator;

public class ModifyExperienceGainPower extends ModifierPower implements Listener {

	public ModifyExperienceGainPower(String name, String description, boolean hidden, FactoryJsonObject condition, int loading_priority, FactoryJsonObject modifier, FactoryJsonArray modifiers) {
		super(name, description, hidden, condition, loading_priority, modifier, modifiers);
	}

	public static FactoryData registerComponents(FactoryData data) {
		return ModifierPower.registerComponents(data).ofNamespace(OriginsPaper.apoliIdentifier("modify_xp_gain"));
	}

	@EventHandler
	public void run(@NotNull PlayerExpChangeEvent e) {
		Player p = e.getPlayer();
		if (getPlayers().contains(p)) {
			try {
				if (isActive(p)) {
					for (Modifier modifier : getModifiers()) {
						Float value = modifier.value();
						String operation = modifier.operation();
						BinaryOperator mathOperator = Util.getOperationMappingsFloat().get(operation);
						if (mathOperator != null) {
							float result = (float) mathOperator.apply(e.getAmount(), value);
							e.setAmount(Math.toIntExact(Long.valueOf(String.valueOf(result))));
						}
					}

				}
			} catch (Exception ev) {
				ev.printStackTrace();
			}
		}
	}

}
