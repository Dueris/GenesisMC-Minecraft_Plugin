package io.github.dueris.originspaper.power.type;

import io.github.dueris.calio.data.SerializableData;
import io.github.dueris.originspaper.action.BlockAction;
import io.github.dueris.originspaper.action.EntityAction;
import io.github.dueris.originspaper.condition.BlockCondition;
import io.github.dueris.originspaper.condition.EntityCondition;
import io.github.dueris.originspaper.data.TypedDataObjectFactory;
import io.github.dueris.originspaper.power.PowerConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ActionOnWakeUpPowerType extends PowerType {

	public static final TypedDataObjectFactory<ActionOnWakeUpPowerType> DATA_FACTORY = PowerType.createConditionedDataFactory(
		new SerializableData()
			.add("entity_action", EntityAction.DATA_TYPE.optional(), Optional.empty())
			.add("block_action", BlockAction.DATA_TYPE.optional(), Optional.empty())
			.add("block_condition", BlockCondition.DATA_TYPE.optional(), Optional.empty()),
		(data, condition) -> new ActionOnWakeUpPowerType(
			data.get("entity_action"),
			data.get("block_action"),
			data.get("block_condition"),
			condition
		),
		(powerType, serializableData) -> serializableData.instance()
			.set("entity_action", powerType.entityAction)
			.set("block_action", powerType.blockAction)
			.set("block_condition", powerType.blockCondition)
	);

	private final Optional<EntityAction> entityAction;
	private final Optional<BlockAction> blockAction;

	private final Optional<BlockCondition> blockCondition;

	public ActionOnWakeUpPowerType(Optional<EntityAction> entityAction, Optional<BlockAction> blockAction, Optional<BlockCondition> blockCondition, Optional<EntityCondition> condition) {
		super(condition);
		this.blockCondition = blockCondition;
		this.entityAction = entityAction;
		this.blockAction = blockAction;
	}

	@Override
	public @NotNull PowerConfiguration<?> getConfig() {
		return PowerTypes.ACTION_ON_WAKE_UP;
	}

	public boolean doesApply(BlockPos pos) {
		return blockCondition
			.map(condition -> condition.test(getHolder().level(), pos))
			.orElse(true);
	}

	public void executeActions(BlockPos pos, Direction direction) {
		blockAction.ifPresent(action -> action.execute(getHolder().level(), pos, Optional.of(direction)));
		entityAction.ifPresent(action -> action.execute(getHolder()));
	}

}
