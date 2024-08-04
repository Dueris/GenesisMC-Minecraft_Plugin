package me.dueris.originspaper.factory.condition.types.entity;

import io.github.dueris.calio.parser.InstanceDefiner;
import me.dueris.originspaper.OriginsPaper;
import me.dueris.originspaper.data.ApoliDataTypes;
import me.dueris.originspaper.factory.condition.ConditionFactory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import org.jetbrains.annotations.NotNull;

public class InBlockCondition {

	public static @NotNull ConditionFactory<Entity> getFactory() {
		return new ConditionFactory<>(
			OriginsPaper.apoliIdentifier("in_block"),
			InstanceDefiner.instanceDefiner()
				.add("block_condition", ApoliDataTypes.BLOCK_CONDITION),
			(data, entity) -> {
				return ((ConditionFactory<BlockInWorld>) data.get("block_condition")).test(
					new BlockInWorld(entity.level(), entity.blockPosition(), true));
			}
		);
	}
}
