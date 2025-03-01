package io.github.dueris.originspaper.condition.type.entity;

import io.github.dueris.calio.data.SerializableData;
import io.github.dueris.calio.data.SerializableDataTypes;
import io.github.dueris.originspaper.access.MovingEntity;
import io.github.dueris.originspaper.condition.ConditionConfiguration;
import io.github.dueris.originspaper.condition.type.EntityConditionType;
import io.github.dueris.originspaper.condition.type.EntityConditionTypes;
import io.github.dueris.originspaper.data.TypedDataObjectFactory;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class MovingEntityConditionType extends EntityConditionType {

	public static final TypedDataObjectFactory<MovingEntityConditionType> DATA_FACTORY = TypedDataObjectFactory.simple(
		new SerializableData()
			.add("horizontally", SerializableDataTypes.BOOLEAN, true)
			.add("vertically", SerializableDataTypes.BOOLEAN, true),
		data -> new MovingEntityConditionType(
			data.get("horizontally"),
			data.get("vertically")
		),
		(conditionType, serializableData) -> serializableData.instance()
			.set("horizontally", conditionType.horizontally)
			.set("vertically", conditionType.vertically)
	);

	private final boolean horizontally;
	private final boolean vertically;

	public MovingEntityConditionType(boolean horizontally, boolean vertically) {
		this.horizontally = horizontally;
		this.vertically = vertically;
	}

	@Override
	public boolean test(Entity entity) {

		if (entity instanceof MovingEntity movingEntity) {
			return (horizontally && movingEntity.apoli$isMovingHorizontally())
				|| (vertically && movingEntity.apoli$isMovingVertically());
		} else {
			return false;
		}

	}

	@Override
	public @NotNull ConditionConfiguration<?> getConfig() {
		return EntityConditionTypes.MOVING;
	}

}
