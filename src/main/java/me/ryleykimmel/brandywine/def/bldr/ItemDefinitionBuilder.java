package me.ryleykimmel.brandywine.def.bldr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.base.MoreObjects;

import me.ryleykimmel.brandywine.def.DefinitionBuilder;
import me.ryleykimmel.brandywine.def.impl.ItemDefinition;

/**
 * A {@link DefinitionBuilder} for {@link ItemDefinition item definitions}.
 *
 * @author John Major
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class ItemDefinitionBuilder extends DefinitionBuilder<ItemDefinition> {

  /**
   * The default ground scale values.
   */
  private static final List<Integer> DEFAULT_GROUND_SCALES = Arrays.asList(128, 128, 128);

  /**
   * The description of this item.
   */
  private String description = "null";

  /**
   * The ground menu actions of this item.
   */
  private List<String> groundMenuActions = new ArrayList<>(5);

  /**
   * The x, y, and z ground scale values of this item.
   */
  private List<Integer> groundScales = new ArrayList<>(DEFAULT_GROUND_SCALES);

  /**
   * The inventory menu actions of this item.
   */
  private List<String> inventoryMenuActions = new ArrayList<>(5);

  /**
   * The members flag of this item.
   */
  private boolean members = false;

  /**
   * The name of this item.
   */
  private String name = "null";

  /**
   * The note info id of this item.
   */
  private int noteInfoId = -1;

  /**
   * The note template id of this item.
   */
  private int noteTemplateId = -1;

  /**
   * The stackable flag of this item.
   */
  private boolean stackable = false;

  /**
   * The team id of this item.
   */
  private int team = 0;

  /**
   * The value of this item.
   */
  private int value = 1;

  /**
   * Constructs a new {@link ItemDefinitionBuilder} with the specified id.
   *
   * @param id The id of the Item.
   */
  public ItemDefinitionBuilder(int id) {
    super(id);
  }

  @Override
  public ItemDefinition build() {
    return new ItemDefinition(description, groundMenuActions, groundScales, id,
        inventoryMenuActions, members, name, noteInfoId, noteTemplateId, stackable, team, value);
  }

  /**
   * Sets the description of this item.
   *
   * @param description The description.
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Sets a ground scale value.
   *
   * @param scale The scale.
   */
  public void setGroundScale(int scale) {
    groundScales.add(scale);
  }

  /**
   * Sets the ground menu action at the specified index.
   *
   * @param action The action string.
   */
  public void setGroundAction(String action) {
    groundMenuActions.add(action);
  }

  /**
   * Sets the inventory menu action at the specified index.
   *
   * @param action The action string.
   */
  public void setInventoryAction(String action) {
    inventoryMenuActions.add(action);
  }

  /**
   * Sets the members flag of this item.
   *
   * @param members The members flag.
   */
  public void setMembers(boolean members) {
    this.members = members;
  }

  /**
   * Sets the name of this item.
   *
   * @param name The name.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Sets the note info id of this item.
   *
   * @param noteInfoId The note info id.
   */
  public void setNoteInfoId(int noteInfoId) {
    this.noteInfoId = noteInfoId;
  }

  /**
   * Sets the note template id of this item.
   *
   * @param noteTemplateId The note template id.
   */
  public void setNoteTemplateId(int noteTemplateId) {
    this.noteTemplateId = noteTemplateId;
  }

  /**
   * Sets the stackable flag of this item.
   *
   * @param stackable The stackable flag.
   */
  public void setStackable(boolean stackable) {
    this.stackable = stackable;
  }

  /**
   * Sets the team of this item.
   *
   * @param team The team.
   */
  public void setTeam(int team) {
    this.team = team;
  }

  /**
   * Sets the value of this item.
   *
   * @param value The value.
   */
  public void setValue(int value) {
    this.value = value;
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
