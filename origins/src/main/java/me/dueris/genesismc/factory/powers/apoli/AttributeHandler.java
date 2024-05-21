package me.dueris.genesismc.factory.powers.apoli;

import me.dueris.calio.data.FactoryData;
import me.dueris.calio.data.factory.FactoryJsonArray;
import me.dueris.calio.data.factory.FactoryJsonObject;
import me.dueris.calio.data.types.OptionalInstance;
import me.dueris.genesismc.GenesisMC;
import me.dueris.genesismc.event.AttributeExecuteEvent;
import me.dueris.genesismc.event.PowerUpdateEvent;
import me.dueris.genesismc.factory.data.types.Modifier;
import me.dueris.genesismc.factory.powers.holder.PowerType;
import me.dueris.genesismc.screen.OriginPage;
import me.dueris.genesismc.util.DataConverter;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerEvent;

import javax.annotation.Nullable;

public class AttributeHandler extends PowerType {
	private final Modifier[] modifiers;
	private final boolean updateHealth;
	private final @Nullable String attribute;

	public AttributeHandler(String name, String description, boolean hidden, FactoryJsonObject condition, int loading_priority, boolean updateHealth, FactoryJsonObject modifier, FactoryJsonArray modifiers, String attribute) {
		super(name, description, hidden, condition, loading_priority);
		this.updateHealth = updateHealth;
		this.attribute = attribute;
		this.modifiers = Modifier.getModifiers(modifier, modifiers);
	}

	public static FactoryData registerComponents(FactoryData data) {
		return PowerType.registerComponents(data).ofNamespace(GenesisMC.apoliIdentifier("attribute"))
			.add("update_health", boolean.class, true)
			.add("modifier", FactoryJsonObject.class, new OptionalInstance())
			.add("modifiers", FactoryJsonArray.class, new OptionalInstance())
			.add("attribute", String.class, new OptionalInstance());
	}

	@EventHandler
	public void powerUpdate(PowerUpdateEvent e) {
		if (!e.getPower().getTag().equalsIgnoreCase(getTag())) return;
		Player p = e.getPlayer();
		OriginPage.setAttributesToDefault(p);
		if (getPlayers().contains(p)) {
			runAttributeModifyPower(e);
		}
	}

	protected void runAttributeModifyPower(PlayerEvent e) {
		Player p = e.getPlayer();
		if (!getPlayers().contains(p)) return;
		for (Modifier modifier : modifiers) {
			try {
				Attribute attributeModifier = attribute == null ? DataConverter.resolveAttribute(modifier.handle.getString("attribute")) : DataConverter.resolveAttribute(attribute);
				AttributeModifier m = DataConverter.convertToAttributeModifier(modifier);
				if (p.getAttribute(attributeModifier) != null) {
					p.getAttribute(attributeModifier).addTransientModifier(m);
				}
				AttributeExecuteEvent attributeExecuteEvent = new AttributeExecuteEvent(p, attributeModifier, this, e.isAsynchronous());
				Bukkit.getServer().getPluginManager().callEvent(attributeExecuteEvent);
			} catch (Exception ev) {
				ev.printStackTrace();
			}
		}
		if (updateHealth) {
			p.sendHealthUpdate();
		}
	}

	public Modifier[] getModifiers() {
		return modifiers;
	}

	public boolean updateHealth() {
		return updateHealth;
	}
}
