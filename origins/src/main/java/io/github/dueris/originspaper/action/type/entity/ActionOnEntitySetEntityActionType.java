package io.github.dueris.originspaper.action.type.entity;

import io.github.dueris.originspaper.action.ActionConfiguration;
import io.github.dueris.originspaper.action.BiEntityAction;
import io.github.dueris.originspaper.action.context.BiEntityActionContext;
import io.github.dueris.originspaper.action.type.EntityActionType;
import io.github.dueris.originspaper.action.type.EntityActionTypes;
import io.github.dueris.originspaper.condition.BiEntityCondition;
import io.github.dueris.originspaper.data.ApoliDataTypes;
import io.github.dueris.originspaper.data.TypedDataObjectFactory;
import io.github.dueris.originspaper.power.PowerReference;
import io.github.dueris.originspaper.power.type.EntitySetPowerType;
import io.github.dueris.calio.data.SerializableData;
import io.github.dueris.calio.data.SerializableDataTypes;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class ActionOnEntitySetEntityActionType extends EntityActionType {

    public static final TypedDataObjectFactory<ActionOnEntitySetEntityActionType> DATA_FACTORY = TypedDataObjectFactory.simple(
        new SerializableData()
            .add("set", ApoliDataTypes.POWER_REFERENCE)
            .add("bientity_action", BiEntityAction.DATA_TYPE)
            .add("bientity_condition", BiEntityCondition.DATA_TYPE.optional(), Optional.empty())
            .add("reverse", SerializableDataTypes.BOOLEAN, false)
            .add("limit", SerializableDataTypes.POSITIVE_INT.optional(), Optional.empty()),
        data -> new ActionOnEntitySetEntityActionType(
            data.get("set"),
            data.get("bientity_action"),
            data.get("bientity_condition"),
            data.get("reverse"),
            data.get("limit")
        ),
        (actionType, serializableData) -> serializableData.instance()
            .set("set", actionType.set)
            .set("bientity_action", actionType.biEntityAction)
            .set("bientity_condition", actionType.biEntityCondition)
            .set("reverse", actionType.reverse)
            .set("limit", actionType.limit)
    );

    private final PowerReference set;

    private final BiEntityAction biEntityAction;
    private final Optional<BiEntityCondition> biEntityCondition;

    private final boolean reverse;
    private final Optional<Integer> limit;

    public ActionOnEntitySetEntityActionType(PowerReference set, BiEntityAction biEntityAction, Optional<BiEntityCondition> biEntityCondition, boolean reverse, Optional<Integer> limit) {
        this.set = set;
        this.biEntityAction = biEntityAction;
        this.biEntityCondition = biEntityCondition;
        this.reverse = reverse;
        this.limit = limit;
    }

    @Override
    protected void execute(Entity entity) {

        if (!(set.getPowerTypeFrom(entity) instanceof EntitySetPowerType entitySet)) {
            return;
        }

        List<UUID> uuids = new ObjectArrayList<>(entitySet.getIterationSet());
        AtomicInteger processedUuids = new AtomicInteger();

        if (reverse) {
            Collections.reverse(uuids);
        }

        for (UUID uuid : uuids) {

            Entity entityFromSet = entitySet.getEntity(uuid);
            BiEntityActionContext context = new BiEntityActionContext(entity, entityFromSet);

            if (biEntityCondition.map(condition -> condition.test(context.forCondition())).orElse(true)) {
                biEntityAction.accept(context);
            }

            if (limit.map(val -> processedUuids.incrementAndGet() >= val).orElse(false)) {
                break;
            }

        }

    }

    @Override
    public @NotNull ActionConfiguration<?> getConfig() {
        return EntityActionTypes.ACTION_ON_ENTITY_SET;
    }

}
