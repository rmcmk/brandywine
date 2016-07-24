package me.ryleykimmel.brandywine.fs.def.impl;

import com.google.common.base.MoreObjects;
import me.ryleykimmel.brandywine.fs.def.Definition;

import java.util.List;

/**
 * Represents the Definition of an Item object.
 */
public final class ItemDefinition extends Definition {

  /**
   * The description of this item.
   */
  private final String description;

  /**
   * The ground menu actions of this item.
   */
  private final List<String> groundMenuActions;

  /**
   * The x, y, and z ground scales of this item.
   */
  private final List<Integer> groundScales;

  /**
   * The inventory menu actions of this item.
   */
  private final List<String> inventoryMenuActions;

  /**
   * The members flag of this item.
   */
  private final boolean members;

  /**
   * The name of this item.
   */
  private final String name;

  /**
   * The note info id of this item.
   */
  private final int noteInfoId;

  /**
   * The noted template id of this item.
   */
  private final int noteTemplateId;

  /**
   * The stackable flag of this item.
   */
  private final boolean stackable;

  /**
   * The team id of this item.
   */
  private final int team;

  /**
   * The value of this item.
   */
  private final int value;

  /**
   * Constructs a new {@link ItemDefinition}.
   *
   * @param description The description of the Item.
   * @param groundMenuActions The ground menu actions of the Item.
   * @param groundScales The ground scales of the Item.
   * @param id The id of the Item.
   * @param inventoryMenuActions The inventory menu actions of the Item.
   * @param members The members state of the Item.
   * @param name The name of the Item.
   * @param noteInfoId The noted info id of the Item.
   * @param noteTemplateId The note template id of the Item.
   * @param stackable The stackable state of the Item.
   * @param team The team id of the Item.
   * @param value The value of of the Item.
   */
  public ItemDefinition(String description, List<String> groundMenuActions,
      List<Integer> groundScales, int id, List<String> inventoryMenuActions, boolean members,
      String name, int noteInfoId, int noteTemplateId, boolean stackable, int team, int value) {
    super(id);
    this.description = description;
    this.groundMenuActions = groundMenuActions;
    this.groundScales = groundScales;
    this.inventoryMenuActions = inventoryMenuActions;
    this.members = members;
    this.name = name;
    this.noteInfoId = noteInfoId;
    this.noteTemplateId = noteTemplateId;
    this.stackable = stackable;
    this.team = team;
    this.value = value;
  }

  /**
   * Gets the description.
   *
   * @return The description.
   */
  public String getDescription() {
    return description;
  }

  /**
   * Gets the {@link List} of ground menu actions.
   *
   * @return The ground menu actions.
   */
  public List<String> getGroundMenuActions() {
    return groundMenuActions;
  }

  /**
   * Gets the {@link List} of ground scale values.
   *
   * @return The ground scale values.
   */
  public List<Integer> getGroundScales() {
    return groundScales;
  }

  /**
   * Gets the {@link List} of inventory menu actions.
   *
   * @return The inventory menu actions.
   */
  public List<String> getInventoryMenuActions() {
    return inventoryMenuActions;
  }

  /**
   * Gets the name.
   *
   * @return The name.
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the note info id.
   *
   * @return The note info id.
   */
  public int getNoteInfoId() {
    return noteInfoId;
  }

  /**
   * Gets the note template id.
   *
   * @return The note template id.
   */
  public int getNoteTemplateId() {
    return noteTemplateId;
  }

  /**
   * Gets the team.
   *
   * @return The team.
   */
  public int getTeam() {
    return team;
  }

  /**
   * Gets the value.
   *
   * @return The value.
   */
  public int getValue() {
    return value;
  }

  /**
   * Gets the members flag.
   *
   * @return {@code true} if this item is members only, otherwise {@code false}.
   */
  public boolean isMembers() {
    return members;
  }

  /**
   * Gets the stackable.
   *
   * @return The stackable.
   */
  public boolean isStackable() {
    return stackable;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("id", id).add("description", description)
        .add("groundMenuActions", groundMenuActions).add("groundScales", groundScales)
        .add("inventoryMenuActions", inventoryMenuActions).add("members", members).add("name", name)
        .add("noteInfoId", noteInfoId).add("noteTemplateId", noteTemplateId)
        .add("stackable", stackable).add("team", team).add("value", value).toString();
  }

}
