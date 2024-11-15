package io.github.dueris.originspaper.condition.type.entity;

import io.github.dueris.calio.data.SerializableData;
import io.github.dueris.calio.data.SerializableDataTypes;
import io.github.dueris.originspaper.condition.BiEntityCondition;
import io.github.dueris.originspaper.condition.ConditionConfiguration;
import io.github.dueris.originspaper.condition.type.EntityConditionType;
import io.github.dueris.originspaper.condition.type.EntityConditionTypes;
import io.github.dueris.originspaper.data.ApoliDataTypes;
import io.github.dueris.originspaper.data.TypedDataObjectFactory;
import io.github.dueris.originspaper.util.Comparison;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class RidingRecursiveEntityConditionType extends EntityConditionType {

	public static final TypedDataObjectFactory<RidingRecursiveEntityConditionType> DATA_FACTORY = TypedDataObjectFactory.simple(
		new SerializableData()
			.add("bientity_condition", BiEntityCondition.DATA_TYPE.optional(), Optional.empty())
			.add("comparison", ApoliDataTypes.COMPARISON, Comparison.GREATER_THAN)
			.add("compare_to", SerializableDataTypes.INT, 0),
		data -> new RidingRecursiveEntityConditionType(
			data.get("bientity_condition"),
			data.get("comparison"),
			data.get("compare_to")
		),
		(conditionType, serializableData) -> serializableData.instance()
			.set("bientity_condition", conditionType.biEntityCondition)
			.set("comparison", conditionType.comparison)
			.set("compare_to", conditionType.compareTo)
	);

	private final Optional<BiEntityCondition> biEntityCondition;

	private final Comparison comparison;
	private final int compareTo;

	public RidingRecursiveEntityConditionType(Optional<BiEntityCondition> biEntityCondition, Comparison comparison, int compareTo) {
		this.biEntityCondition = biEntityCondition;
		this.comparison = comparison;
		this.compareTo = compareTo;
	}

	@Override
	public boolean test(Entity entity) {

		Entity vehicle = entity.getVehicle();
		int matches = 0;

		if (vehicle == null) {
			return false;
		} else {

			while (vehicle != null) {

				final Entity finalVehicle = vehicle;
				if (biEntityCondition.map(condition -> condition.test(entity, finalVehicle)).orElse(true)) {
					++matches;
				}

				vehicle = vehicle.getVehicle();

			}

			return comparison.compare(matches, compareTo);

		}

	}

	@Override
	public @NotNull ConditionConfiguration<?> getConfig() {
		return EntityConditionTypes.RIDING_RECURSIVE;
	}

}
