package me.ryleykimmel.brandywine.game.command;

import java.util.Arrays;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

/**
 * Represents the arguments of a Command.
 */
public final class CommandArguments {

  /**
   * The arguments.
   */
  private final String[] arguments;

  /**
   * The current argument index.
   */
  private int index;

  /**
   * Constructs a new {@link CommandArguments} with the specified arguments.
   * 
   * @param arguments The Command's arguments.
   */
  public CommandArguments(String[] arguments) {
    this.arguments = arguments.clone();
  }

  /**
   * Gets the remaining amount of arguments.
   * 
   * @return The amount of remaining arguments.
   */
  public int remaining() {
    return arguments.length - index;
  }

  /**
   * Tests whether or not there is at least {@code amount} remaining arguments.
   * 
   * @param amount The amount of arguments.
   * @return {@code true} if there is at least {@code amount} arguments, otherwise {@code false}.
   */
  public boolean hasRemaining(int amount) {
    return remaining() >= amount;
  }

  /**
   * Gets the next argument.
   * 
   * @return The next argument.
   */
  public String getNext() {
    return getString(index++);
  }

  /**
   * Gets the next argument as an {@code int}.
   * 
   * @return The next argument as an {@code int}.
   */
  public int getNextInteger() {
    return getInteger(index++);
  }

  /**
   * Gets the next argument as a {@code boolean}.
   * 
   * @return The next argument as a {@code boolean}.
   */
  public boolean getNextBoolean() {
    return getBoolean(index++);
  }

  /**
   * Gets the next argument as a {@code long}.
   * 
   * @return The next argument as a {@code long}.
   */
  public long getNextLong() {
    return getLong(index++);
  }

  /**
   * Gets the next argument as a {@code double}.
   * 
   * @return The next argument as a {@code double}.
   */
  public double getNextDouble() {
    return getDouble(index++);
  }

  /**
   * Attempts to get the argument at the specified index.
   * 
   * @param index The index of the argument.
   * @return The argument at {@code index}.
   */
  public String getString(int index) {
    checkIndex(index);
    return arguments[index];
  }

  /**
   * Attempts to get an {@code int} at the specified index.
   * 
   * @param index The index of the argument to parse an int.
   * @return The argument at {@code index} represented as an int.
   */
  public int getInteger(int index) {
    checkIndex(index);
    String argument = arguments[index];
    try {
      return Integer.parseInt(argument);
    } catch (NumberFormatException cause) {
      throw new NumberFormatException(
          "Argument: " + argument + " at index: " + index + " cannot be parsed as an integer.");
    }
  }

  /**
   * Attempts to get a {@code boolean} at the specified index.
   * 
   * @param index The index of the argument to parse a boolean.
   * @return The argument at {@code index} represented as a boolean.
   */
  public boolean getBoolean(int index) {
    checkIndex(index);
    String argument = arguments[index];
    if (!"true".equalsIgnoreCase(argument) && !"false".equalsIgnoreCase(argument)) {
      throw new IllegalArgumentException(
          "Only \"true\" or \"false\" is accepted for boolean input.");
    }
    return Boolean.parseBoolean(argument);
  }

  /**
   * Attempts to get a {@code long} at the specified index.
   * 
   * @param index The index of the argument to parse a long.
   * @return The argument at {@code index} represented as a long.
   */
  public long getLong(int index) {
    checkIndex(index);
    String argument = arguments[index];
    try {
      return Long.parseLong(argument);
    } catch (NumberFormatException cause) {
      throw new NumberFormatException(
          "Argument: " + argument + " at index: " + index + " cannot be parsed as a long.");
    }
  }

  /**
   * Attempts to get a {@code double} at the specified index.
   * 
   * @param index The index of the argument to parse a double.
   * @return The argument at {@code index} represented as a double.
   */
  public double getDouble(int index) {
    checkIndex(index);
    String argument = arguments[index];
    try {
      return Double.parseDouble(argument);
    } catch (NumberFormatException cause) {
      throw new NumberFormatException(
          "Argument: " + argument + " at index: " + index + " cannot be parsed as a double.");
    }
  }

  /**
   * Ensures the specified index is within bounds and is non-null.
   * 
   * @param index The current argument index.
   */
  private void checkIndex(int index) {
    Preconditions.checkElementIndex(index, arguments.length,
        "Index out of bounds: " + index + " length: " + arguments.length);
    Preconditions.checkNotNull(arguments[index], "Argument for index: " + index + " is null.");
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("arguments", Arrays.toString(arguments)).toString();
  }

}
