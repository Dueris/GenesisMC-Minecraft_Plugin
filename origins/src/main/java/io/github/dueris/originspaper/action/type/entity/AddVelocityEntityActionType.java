package io.github.dueris.originspaper.action.type.entity;

import io.github.dueris.originspaper.action.ActionConfiguration;
import io.github.dueris.originspaper.action.type.EntityActionType;
import io.github.dueris.originspaper.action.type.EntityActionTypes;
import io.github.dueris.originspaper.data.ApoliDataTypes;
import io.github.dueris.originspaper.data.TypedDataObjectFactory;
import io.github.dueris.originspaper.util.Space;
import io.github.dueris.calio.data.SerializableData;
import io.github.dueris.calio.data.SerializableDataTypes;
import net.minecraft.world.entity.Entity;
import org.apache.commons.lang3.function.TriConsumer;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public class AddVelocityEntityActionType extends EntityActionType {

    public static final TypedDataObjectFactory<AddVelocityEntityActionType> DATA_FACTORY = TypedDataObjectFactory.simple(
        new SerializableData()
            .add("x", SerializableDataTypes.FLOAT, 0F)
            .add("y", SerializableDataTypes.FLOAT, 0F)
            .add("z", SerializableDataTypes.FLOAT, 0F)
            .add("space", ApoliDataTypes.SPACE, Space.WORLD)
            .add("set", SerializableDataTypes.BOOLEAN, false),
        data -> new AddVelocityEntityActionType(
            new Vector3f(
                data.get("x"),
                data.get("y"),
                data.get("z")
            ),
            data.get("space"),
            data.get("set")
        ),
        (actionType, serializableData) -> serializableData.instance()
            .set("x", actionType.velocity.x())
            .set("y", actionType.velocity.y())
            .set("z", actionType.velocity.z())
            .set("space", actionType.space)
            .set("set", actionType.set)
    );

    private final Vector3f velocity;
    private final Space space;

    private final boolean set;

    public AddVelocityEntityActionType(Vector3f velocity, Space space, boolean set) {
        this.velocity = velocity;
        this.space = space;
        this.set = set;
    }

    @Override
    protected void execute(Entity entity) {

        TriConsumer<Float, Float, Float> method = set
            ? entity::setDeltaMovement
            : entity::push;

        space.toGlobal(velocity, entity);
        method.accept(velocity.x(), velocity.y(), velocity.z());

        entity.hurtMarked = true;

    }

    @Override
    public @NotNull ActionConfiguration<?> getConfig() {
        return EntityActionTypes.ADD_VELOCITY;
    }

}
