package io.github.dueris.originspaper.origin;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import io.github.dueris.calio.SerializableDataTypes;
import io.github.dueris.calio.data.SerializableData;
import io.github.dueris.calio.data.SerializableDataType;
import io.github.dueris.calio.parser.RootResult;
import io.github.dueris.calio.util.Util;
import io.github.dueris.calio.util.holder.TriPair;
import io.github.dueris.originspaper.data.OriginsDataTypes;
import io.github.dueris.originspaper.data.types.Impact;
import io.github.dueris.originspaper.data.types.OriginUpgrade;
import io.github.dueris.originspaper.util.AsyncUpgradeTracker;
import io.github.dueris.originspaper.util.ComponentUtil;
import io.github.dueris.originspaper.util.LangFile;
import net.kyori.adventure.text.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;

public class Origin {
	public static Origin EMPTY;
	public static SerializableDataType<RootResult<Origin>> DATA = SerializableDataType.of(
		(jsonElement) -> {
			if (!(jsonElement instanceof JsonObject jo)) {
				throw new JsonSyntaxException("Expected JsonObject for root 'Origin'");
			}

			try {
				SerializableData.Instance compound = SerializableDataType.compound(getFactory(), jo, Origin.class);
				return new RootResult<>(
					Util.generateConstructor(Origin.class, getFactory()), compound
				);
			} catch (NoSuchMethodException | IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}, Origin.class
	);
	private final List<ResourceLocation> powers;
	private final net.minecraft.world.item.ItemStack icon;
	private final boolean unchoosable;
	private final int order;
	private final Impact impact;
	private final int loadingPriority;
	private final OriginUpgrade upgrade;
	private final TextComponent name;
	private final TextComponent description;
	private final ResourceLocation key;

	public Origin(@NotNull ResourceLocation key, List<ResourceLocation> powers, net.minecraft.world.item.ItemStack icon, boolean unchoosable, int order,
				  Impact impact, int loadingPriority, OriginUpgrade upgrade, net.minecraft.network.chat.Component name, net.minecraft.network.chat.Component description) {
		this.key = key;
		this.powers = powers;
		this.icon = icon;
		this.unchoosable = unchoosable;
		this.order = order;
		this.impact = impact;
		this.loadingPriority = loadingPriority;
		this.upgrade = upgrade;
		if (upgrade != null) {
			AsyncUpgradeTracker.upgrades.put(this, new TriPair<>(upgrade.advancementCondition(), upgrade.upgradeToOrigin(), upgrade.announcement()));
		}
		this.name = ComponentUtil.nmsToKyori(
			LangFile.translatable((name != null ? name.getString() : "origin.$namespace.$path.name")
				.replace("$namespace", key.getNamespace()).replace("$path", key.getPath()))
		);
		this.description = ComponentUtil.nmsToKyori(
			LangFile.translatable((description != null ? description.getString() : "origin.$namespace.$path.description")
				.replace("$namespace", key.getNamespace()).replace("$path", key.getPath()))
		);
	}

	public static SerializableData getFactory() {
		return SerializableData.serializableData()
			.add("powers", SerializableDataTypes.list(SerializableDataTypes.IDENTIFIER), new LinkedList<>())
			.add("icon", SerializableDataTypes.ITEM_STACK, Items.PLAYER_HEAD.getDefaultInstance())
			.add("unchoosable", SerializableDataTypes.BOOLEAN, false)
			.add("order", SerializableDataTypes.INT, Integer.MAX_VALUE)
			.add("impact", OriginsDataTypes.IMPACT, Impact.NONE)
			.add("loading_priority", SerializableDataTypes.INT, 0)
			.add("upgrades", OriginsDataTypes.ORIGIN_UPGRADE, null)
			.add("name", SerializableDataTypes.TEXT, null)
			.add("description", SerializableDataTypes.TEXT, null);
	}

	public List<ResourceLocation> powers() {
		return powers;
	}

	public ItemStack icon() {
		return icon == null ? new ItemStack(Items.PLAYER_HEAD) : icon;
	}

	public boolean unchoosable() {
		return unchoosable;
	}

	public int order() {
		return order;
	}

	public Impact impact() {
		return impact;
	}

	public int impactValue() {
		return impact.getImpactValue();
	}

	public int loadingPriority() {
		return loadingPriority;
	}

	public TextComponent getName() {
		return name;
	}

	public TextComponent getDescription() {
		return description;
	}

	public @NotNull ResourceLocation getId() {
		return key;
	}

	public @NotNull String getTag() {
		return key.toString();
	}

	@Nullable
	public OriginUpgrade getUpgrade() {
		return upgrade;
	}

	@Contract(pure = true)
	@Override
	public @NotNull String toString() {
		return "Origin[" +
			"powers=" + powers + ", " +
			"icon=" + icon + ", " +
			"unchoosable=" + unchoosable + ", " +
			"order=" + order + ", " +
			"impact=" + impact + ", " +
			"loadingPriority=" + loadingPriority + ", " +
			"name=" + name + ", " +
			"description=" + description + ']';
	}
}
