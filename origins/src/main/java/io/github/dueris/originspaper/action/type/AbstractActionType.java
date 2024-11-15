package io.github.dueris.originspaper.action.type;

import io.github.dueris.calio.data.SerializableData;
import io.github.dueris.calio.util.Validatable;
import io.github.dueris.originspaper.action.AbstractAction;
import io.github.dueris.originspaper.action.ActionConfiguration;
import io.github.dueris.originspaper.data.TypedDataObjectFactory;
import io.github.dueris.originspaper.util.context.TypeActionContext;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;

public abstract class AbstractActionType<T extends TypeActionContext<?>, A extends AbstractAction<T, ?>> implements Consumer<T>, Validatable {

	private A action = null;
	private boolean initialized = false;

	@ApiStatus.Internal
	public final void init(A action) {

		if (action.getActionType() != this) {
			throw new IllegalArgumentException("Cannot initialize action type \"" + getConfig().id() + "\" with mismatched action!");
		}

		this.action = action;
		this.initialized = true;

	}

	@Override
	public abstract void accept(T context);

	@SuppressWarnings("unchecked")
	@Override
	public void validate() throws Exception {

		TypedDataObjectFactory<AbstractActionType<T, A>> dataFactory = (TypedDataObjectFactory<AbstractActionType<T, A>>) getConfig().dataFactory();
		SerializableData.Instance data = dataFactory.toData(this);

		data.validate();

	}

	@NotNull
	public abstract ActionConfiguration<?> getConfig();

	public final A getAction() {

		if (initialized) {
			return Objects.requireNonNull(action, "Action of initialized action type \"" + getConfig().id() + "\" was null!");
		} else {
			throw new IllegalStateException("Action type \"" + getConfig().id() + "\" wasn't initialized yet!");
		}

	}

	public abstract A createAction();

}
