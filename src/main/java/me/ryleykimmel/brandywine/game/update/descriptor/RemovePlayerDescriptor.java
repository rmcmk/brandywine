package me.ryleykimmel.brandywine.game.update.descriptor;

import me.ryleykimmel.brandywine.game.model.player.Player;
import me.ryleykimmel.brandywine.game.update.PlayerDescriptor;
import me.ryleykimmel.brandywine.game.update.Updater;
import me.ryleykimmel.brandywine.network.game.frame.FrameBuilder;
import me.ryleykimmel.brandywine.network.msg.impl.PlayerUpdateMessage;

/**
 * A PlayerDescriptor which encodes the removal of a Player.
 * 
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class RemovePlayerDescriptor extends PlayerDescriptor {

  public RemovePlayerDescriptor(Player player, Updater updater) {
    super(player, updater);
  }

  @Override
  public void encode(PlayerUpdateMessage message, FrameBuilder builder, FrameBuilder blockBuilder) {
    updater.getDescriptorEncoders().encode(this, message, builder, blockBuilder);
    // XXX: Maybe remove this method and clear the pending updates when this descriptor is
    // constructed?
  }

}
