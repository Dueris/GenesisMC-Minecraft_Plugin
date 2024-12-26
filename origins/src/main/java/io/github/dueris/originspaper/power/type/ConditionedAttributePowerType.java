package io.github.dueris.originspaper.power.type;

import com.mojang.serialization.DataResult;
import io.github.dueris.calio.data.SerializableData;
import io.github.dueris.calio.data.SerializableDataTypes;
import io.github.dueris.originspaper.condition.EntityCondition;
import io.github.dueris.originspaper.data.ApoliDataTypes;
import io.github.dueris.originspaper.data.TypedDataObjectFactory;
import io.github.dueris.originspaper.power.PowerConfiguration;
import io.github.dueris.originspaper.util.AttributedEntityAttributeModifier;
import io.github.dueris.originspaper.util.Util;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class ConditionedAttributePowerType extends AttributePowerType {

	public static final TypedDataObjectFactory<ConditionedAttributePowerType> DATA_FACTORY = PowerType.createConditionedDataFactory(
		new SerializableData()
			.add("modifier", ApoliDataTypes.ATTRIBUTED_ATTRIBUTE_MODIFIER, null)
			.addFunctionedDefault("modifiers", ApoliDataTypes.ATTRIBUTED_ATTRIBUTE_MODIFIERS, data -> Util.singletonListOrNull(data.get("modifier")))
			.add("update_health", SerializableDataTypes.BOOLEAN, true)
			.add("tick_rate", SerializableDataTypes.POSITIVE_INT, 20)
			.validate(Util.validateAnyFieldsPresent("modifier", "modifiers")),
		(data, condition) -> new ConditionedAttributePowerType(
			data.get("modifiers"),
			data.get("update_health"),
			data.get("tick_rate"),
			condition
		),
		(powerType, serializableData) -> serializableData.instance()
			.set("modifiers", powerType.attributedModifiers())
			.set("update_health", powerType.shouldUpdateHealth())
			.set("tick_rate", powerType.tickRate)
	);

	protected final int tickRate;

	private Integer startTicks = null;
	private Integer endTicks = null;

	private boolean wasActive = false;

	public ConditionedAttributePowerType(List<AttributedEntityAttributeModifier> attributedModifiers, boolean updateHealth, int tickRate, Optional<EntityCondition> condition) {
		super(attributedModifiers, updateHealth, condition);
		this.tickRate = tickRate;
		this.setTicking(true);
	}

	@Override
	public @NotNull PowerConfiguration<?> getConfig() {
		return PowerTypes.CONDITIONED_ATTRIBUTE;
	}

	@Override
	public void onAdded() {

	}

	@Override
	public void onRemoved() {

	}

	@Override
	public void onLost() {
		removeTempModifiers(getHolder());
	}

	@Override
	public void serverTick() {

		if (isActive()) {

			if (startTicks == null) {
				startTicks = getHolder().tickCount % tickRate;
				endTicks = null;
			} else if (!wasActive && getHolder().tickCount % tickRate == startTicks) {
				applyTempModifiers(getHolder());
				this.wasActive = true;
			}

		} else if (wasActive) {

			if (endTicks == null) {
				startTicks = null;
				endTicks = getHolder().tickCount % tickRate;
			} else if (getHolder().tickCount % tickRate == endTicks) {
				removeTempModifiers(getHolder());
				this.wasActive = false;
			}

		}

	}

}
