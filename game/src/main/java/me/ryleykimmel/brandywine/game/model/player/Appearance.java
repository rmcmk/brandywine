package me.ryleykimmel.brandywine.game.model.player;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

/**
 * Represents the appearance of a Player.
 */
public final class Appearance {

  /**
   * The mask appended to a Feature's style.
   */
  private static final int STYLE_MASK = 0x100;

  /**
   * Represents some feature on a Player.
   */
  private static final class Feature {

    /**
     * The style of this feature.
     */
    private int style;

    /**
     * The color of this feature.
     */
    private int color;

    /**
     * Constructs a new {@link Feature} with the specified style and color.
     * 
     * @param style The style of this feature.
     * @param color The color of this feature.
     */
    private Feature(int style, int color) {
      this.style = style;
      this.color = color;
    }

    /**
     * Constructs a new {@link Feature} with the specified style.
     * 
     * @param style The style of this feature.
     */
    private Feature(int style) {
      this(style, 0);
    }

    /**
     * Gets the style of this feature.
     *
     * @return The style of this feature.
     */
    private int getStyle() {
      return style;
    }

    /**
     * Gets the color of this feature.
     *
     * @return The color of this feature.
     */
    private int getColor() {
      return color;
    }

    /**
     * Sets the style of this feature.
     *
     * @param style The new style of this feature.
     */
    private void setStyle(int style) {
      this.style = style;
    }

    /**
     * Sets the color of this feature.
     *
     * @param color The new color of the feature.
     */
    private void setColor(int color) {
      this.color = color;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj instanceof Feature) {
        Feature other = (Feature) obj;
        return style == other.style && color == other.color;
      }

      return false;
    }

    @Override
    public int hashCode() {
      return Objects.hash(style, color);
    }

    @Override
    public String toString() {
      return MoreObjects.toStringHelper(this).add("style", style).add("color", color).toString();
    }

  }

  /**
   * Represents the hair features id.
   */
  public static final int HAIR = 0;

  /**
   * Represents the facial hair features id.
   */
  public static final int FACIAL_HAIR = 1;

  /**
   * Represents the chest features id.
   */
  public static final int CHEST = 2;

  /**
   * Represents the arms features id.
   */
  public static final int ARMS = 3;

  /**
   * Represents the hands features id.
   */
  public static final int HANDS = 4;

  /**
   * Represents the legs features id.
   */
  public static final int LEGS = 5;

  /**
   * Represents the feet features id.
   */
  public static final int FEET = 6;

  /**
   * Represents the skin features id.
   */
  public static final int SKIN = 7;

  /**
   * The Gender.
   */
  private Gender gender = Gender.MALE;

  /**
   * A {@link Map} of all of the Features.
   */
  private final Map<Integer, Feature> features = new HashMap<>();

  /**
   * Initializes the default Appearance for the current Gender.
   */
  public void init() {
    init(gender);
  }

  /**
   * Initialize the default Appearance for the specified Gender.
   * 
   * @param gender The Gender to initialize the default Appearance for.
   */
  public void init(Gender gender) {
    switch (gender) {
      case MALE:
        features.put(HAIR, new Feature(0));
        features.put(FACIAL_HAIR, new Feature(14));
        features.put(CHEST, new Feature(18));
        features.put(ARMS, new Feature(26));
        features.put(HANDS, new Feature(34));
        features.put(LEGS, new Feature(36));
        features.put(FEET, new Feature(42));
        features.put(SKIN, new Feature(0));
        break;

      case FEMALE:
        features.put(HAIR, new Feature(45));
        features.put(FACIAL_HAIR, new Feature(-1));
        features.put(CHEST, new Feature(56));
        features.put(ARMS, new Feature(61));
        features.put(HANDS, new Feature(67));
        features.put(LEGS, new Feature(70));
        features.put(FEET, new Feature(79));
        features.put(SKIN, new Feature(0));
        break;
    }
  }

  /**
   * Sets the color of the specified Feature.
   *
   * @param id The Features id.
   * @param color The new color of the Feature.
   */
  public void setColor(int id, int color) {
    Feature feature = get(id);
    feature.setColor(color);
  }

  /**
   * Sets the style of the specified Feature.
   *
   * @param id The Features id.
   * @param style The new style of the Feature.
   */
  public void setStyle(int id, int style) {
    Feature feature = get(id);
    feature.setStyle(style);
  }

  /**
   * Gets the color for the specified Feature.
   *
   * @param id The Features id.
   * @return The color of the Feature.
   */
  public int getColor(int id) {
    return get(id).getColor();
  }

  /**
   * Gets the style of the specified Feature.
   *
   * @param id The Features id.
   * @return The color of the Feature.
   */
  public int getStyle(int id) {
    return get(id).getStyle() | STYLE_MASK;
  }

  /**
   * Gets a Feature for the specified id.
   * 
   * @param id The id of the Feature.
   * @return The Feature for the specified id.
   */
  private Feature get(int id) {
    Preconditions.checkElementIndex(id, features.size());
    return features.get(id);
  }

  /**
   * Gets whether or not this Appearance is male.
   * 
   * @return Iff this Appearance is male.
   */
  public boolean isMale() {
    return gender == Gender.MALE;
  }

  /**
   * Gets whether or not this Appearance is female.
   * 
   * @return Iff this Appearance is female.
   */
  public boolean isFemale() {
    return gender == Gender.FEMALE;
  }

  /**
   * Gets the Gender.
   * 
   * @return The Gender.
   */
  public Gender getGender() {
    return gender;
  }

  /**
   * Sets the Gender.
   * 
   * @param gender The Gender to set.
   */
  public void setGender(Gender gender) {
    this.gender = gender;
  }

}
