package me.ryleykimmel.brandywine.def;

import org.apache.commons.lang3.builder.Builder;

/**
 * An abstract implementation of a {@link Builder} which builds some {@link Definition}.
 *
 * @author Ryley Kimmel
 * @param <T> The type of Definition this builder will compute or construct.
 */
public abstract class DefinitionBuilder<T extends Definition> implements Builder<T> {

	/**
	 * The id of the Definition.
	 */
	protected final int id;

	/**
	 * Constructs a new {@link DefinitionBuilder} with the specified id.
	 *
	 * @param id The id of the Definition.
	 */
	public DefinitionBuilder(int id) {
		this.id = id;
	}

	/**
	 * Gets the id of this Definition.
	 *
	 * @return The id of this Definition.
	 */
	public final int getId() {
		return id;
	}

}