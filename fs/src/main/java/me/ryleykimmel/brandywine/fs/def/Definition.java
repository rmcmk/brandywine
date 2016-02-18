package me.ryleykimmel.brandywine.fs.def;

/**
 * Represents a Definition.
 */
public abstract class Definition {

  /**
   * The id of the Definition.
   */
  protected final int id;

  /**
   * Constructs a new {@link Definition} with the specified id.
   *
   * @param id The id of the Definition.
   */
  public Definition(int id) {
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
