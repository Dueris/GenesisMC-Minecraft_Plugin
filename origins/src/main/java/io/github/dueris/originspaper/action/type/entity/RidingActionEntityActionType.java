package io.github.dueris.originspaper.action.type.entity;

import io.github.dueris.calio.data.SerializableData;
import io.github.dueris.calio.data.SerializableDataTypes;
import io.github.dueris.originspaper.action.ActionConfiguration;
import io.github.dueris.originspaper.action.BiEntityAction;
import io.github.dueris.originspaper.action.EntityAction;
import io.github.dueris.originspaper.action.type.EntityActionType;
import io.github.dueris.originspaper.action.type.EntityActionTypes;
import io.github.dueris.originspaper.condition.BiEntityCondition;
import io.github.dueris.originspaper.data.TypedDataObjectFactory;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class RidingActionEntityActionType extends EntityActionType {

	public static final TypedDataObjectFactory<RidingActionEntityActionType> DATA_FACTORY = TypedDataObjectFactory.simple(
		new SerializableData()
			.add("action", EntityAction.DATA_TYPE.optional(), Optional.empty())
			.add("bientity_action", BiEntityAction.DATA_TYPE.optional(), Optional.empty())
			.add("bientity_condition", BiEntityCondition.DATA_TYPE.optional(), Optional.empty())
			.add("recursive", SerializableDataTypes.BOOLEAN, false),
		data -> new RidingActionEntityActionType(
			data.get("action"),
			data.get("bientity_action"),
			data.get("bientity_condition"),
			data.get("recursive")
		),
		(actionType, serializableData) -> serializableData.instance()
			.set("action", actionType.entityAction)
			.set("bientity_action", actionType.biEntityAction)
			.set("bientity_condition", actionType.biEntityCondition)
			.set("recursive", actionType.recursive)
	);

	private final Optional<EntityAction> entityAction;
	private final Optional<BiEntityAction> biEntityAction;

	private final Optional<BiEntityCondition> biEntityCondition;
	private final boolean recursive;

	public RidingActionEntityActionType(Optional<EntityAction> entityAction, Optional<BiEntityAction> biEntityAction, Optional<BiEntityCondition> biEntityCondition, boolean recursive) {
		this.entityAction = entityAction;
		this.biEntityAction = biEntityAction;
		this.biEntityCondition = biEntityCondition;
		this.recursive = recursive;
	}

	@Override
	protected void execute(Entity entity) {

		if (!entity.isPassenger()) {
			return;
		}

		Entity vehicle;
		do {

			vehicle = entity.getVehicle();
			Entity finalVehicle = vehicle;

			if (biEntityCondition.map(condition -> condition.test(entity, finalVehicle)).orElse(true)) {
				entityAction.ifPresent(action -> action.execute(finalVehicle));
				biEntityAction.ifPresent(action -> action.execute(entity, finalVehicle));
			}

			if (!recursive) {
				break;
			}

		}

		while (vehicle != null);

	}

	@Override
	public @NotNull ActionConfiguration<?> getConfig() {
		return EntityActionTypes.RIDING_ACTION;
	}

}
