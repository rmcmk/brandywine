package me.ryleykimmel.brandywine.game.message;

import me.ryleykimmel.brandywine.network.message.Message;

/**
 * A {@link Message} which requests login to the server.
 */
public final class LoginMessage extends Message {

  /**
   * A constant dummy value.
   */
  private final int dummy;

  /**
   * The version of the connecting client.
   */
  private final int clientVersion;

  /**
   * The detail level of the connecting client.
   */
  private final int detail;

  /**
   * The archive checksums of the connecting client.
   */
  private final int[] archiveChecksums;

  /**
   * The block length of the connecting client.
   */
  private final int blockLength;

  /**
   * The block operation code of the connecting client.
   */
  private final int blockOperationCode;

  /**
   * The session id of the connecting client.
   */
  private final long clientSessionId;

  /**
   * The server session id of the connecting client.
   */
  private final long serverSessionId;

  /**
   * The user id of the connecting client.
   */
  private final int userId;

  /**
   * The username of the connecting client.
   */
  private final String username;

  /**
   * The password of the connecting client.
   */
  private final String password;

  /**
   * Constructs a new {@link LoginMessage}.
   *
   * @param dummy The constant dummy value.
   * @param clientVersion The version of the connecting client.
   * @param detail The detail level of the connecting client.
   * @param archiveChecksums The archive checksums of the connecting client.
   * @param blockLength The block length of the connecting client.
   * @param blockOperationCode The block operation code of the connecting client.
   * @param clientSessionId The session id of the connecting client.
   * @param serverSessionId The server session id of the connecting client.
   * @param userId The user id of the connecting client.
   * @param username The username of the connecting client.
   * @param password The password of the connecting client.
   */
  public LoginMessage(int dummy,
      int clientVersion,
      int detail,
      int[] archiveChecksums,
      int blockLength,
      int blockOperationCode,
      long clientSessionId,
      long serverSessionId,
      int userId,
      String username,
      String password) {
    this.dummy = dummy;
    this.clientVersion = clientVersion;
    this.detail = detail;
    this.archiveChecksums = archiveChecksums;
    this.blockLength = blockLength;
    this.blockOperationCode = blockOperationCode;
    this.clientSessionId = clientSessionId;
    this.serverSessionId = serverSessionId;
    this.userId = userId;
    this.username = username;
    this.password = password;
  }

  /**
   * Gets the constant dummy value.
   *
   * @return The constant dummy value.
   */
  public int getDummy() {
    return dummy;
  }

  /**
   * Gets the client version.
   *
   * @return The version of the connecting client.
   */
  public int getClientVersion() {
    return clientVersion;
  }

  /**
   * Gets the detail level.
   *
   * @return The detail level of the connecting client.
   */
  public int getDetail() {
    return detail;
  }

  /**
   * Gets the archive checksums.
   *
   * @return The archive checksums of the connecting client.
   */
  public int[] getArchiveChecksums() {
    return archiveChecksums;
  }

  /**
   * Gets the block length.
   *
   * @return The block length of the connecting client.
   */
  public int getBlockLength() {
    return blockLength;
  }

  /**
   * Gets the block operation code.
   *
   * @return The block operation code of the connecting client.
   */
  public int getBlockOperationCode() {
    return blockOperationCode;
  }

  /**
   * Gets the session id of the connecting client.
   *
   * @return The client session id.
   */
  public long getClientSessionId() {
    return clientSessionId;
  }

  /**
   * Gets the server session id of the connecting client.
   *
   * @return The server session id.
   */
  public long getServerSessionId() {
    return serverSessionId;
  }

  /**
   * Gets the user id.
   *
   * @return The user id of the connecting client.
   */
  public int getUserId() {
    return userId;
  }

  /**
   * Gets the username.
   *
   * @return The username of the connecting client.
   */
  public String getUsername() {
    return username;
  }

  /**
   * Gets the password.
   *
   * @return The password of the connecting client.
   */
  public String getPassword() {
    return password;
  }

}
