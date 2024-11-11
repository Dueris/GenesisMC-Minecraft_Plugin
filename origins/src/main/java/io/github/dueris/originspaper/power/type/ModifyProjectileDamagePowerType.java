package io.github.dueris.originspaper.power.type;

import io.github.dueris.calio.data.SerializableData;
import io.github.dueris.originspaper.OriginsPaper;
import io.github.dueris.originspaper.action.EntityAction;
import io.github.dueris.originspaper.condition.DamageCondition;
import io.github.dueris.originspaper.condition.EntityCondition;
import io.github.dueris.originspaper.data.ApoliDataTypes;
import io.github.dueris.originspaper.data.TypedDataObjectFactory;
import io.github.dueris.originspaper.power.Power;
import io.github.dueris.originspaper.power.PowerConfiguration;
import io.github.dueris.originspaper.util.modifier.Modifier;
import net.minecraft.util.Tuple;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ModifyProjectileDamagePowerType extends ValueModifyingPowerType {

	public static final TypedDataObjectFactory<ModifyProjectileDamagePowerType> DATA_FACTORY = createConditionedModifyingDataFactory(
		new SerializableData()
			.add("self_action", EntityAction.DATA_TYPE.optional(), Optional.empty())
			.add("target_action", EntityAction.DATA_TYPE.optional(), Optional.empty())
			.add("target_condition", EntityCondition.DATA_TYPE.optional(), Optional.empty())
			.add("damage_condition", DamageCondition.DATA_TYPE.optional(), Optional.empty()),
		(data, modifiers, condition) -> new ModifyProjectileDamagePowerType(
			data.get("self_action"),
			data.get("target_action"),
			data.get("target_condition"),
			data.get("damage_condition"),
			modifiers,
			condition
		),
		(powerType, serializableData) -> serializableData.instance()
			.set("self_action", powerType.selfAction)
			.set("target_action", powerType.targetAction)
			.set("target_condition", powerType.targetCondition)
			.set("damage_condition", powerType.damageCondition)
	);

	private final Optional<EntityAction> selfAction;
	private final Optional<EntityAction> targetAction;

	private final Optional<EntityCondition> targetCondition;
	private final Optional<DamageCondition> damageCondition;

	public ModifyProjectileDamagePowerType(Optional<EntityAction> selfAction, Optional<EntityAction> targetAction, Optional<EntityCondition> targetCondition, Optional<DamageCondition> damageCondition, List<Modifier> modifiers, Optional<EntityCondition> condition) {
		super(modifiers, condition);
		this.selfAction = selfAction;
		this.targetAction = targetAction;
		this.targetCondition = targetCondition;
		this.damageCondition = damageCondition;
	}

	@Override
	public @NotNull PowerConfiguration<?> getConfig() {
		return PowerTypes.MODIFY_PROJECTILE_DAMAGE;
	}

	public boolean doesApply(DamageSource source, float damageAmount, LivingEntity target) {
		return damageCondition.map(condition -> condition.test(source, damageAmount)).orElse(true)
			&& targetCondition.map(condition -> condition.test(target)).orElse(true);
	}

	public void executeActions(Entity target) {
		selfAction.ifPresent(action -> action.execute(getHolder()));
		targetAction.ifPresent(action -> action.execute(target));
	}

}
