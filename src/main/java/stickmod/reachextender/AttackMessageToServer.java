package stickmod.reachextender;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

/**
 * This Network Message is sent from the client to the server, to tell it to spawn projectiles at a particular location.
 * Typical usage:
 * PREQUISITES:
 *   have previously setup SimpleNetworkWrapper, registered the message class and the handler
 *
 * 1) User creates an AirStrikeMessageToServer(projectile, targetCoordinates)
 * 2) simpleNetworkWrapper.sendToServer(airstrikeMessageToServer);
 * 3) network code calls airstrikeMessageToServer.toBytes() to copy the message member variables to a ByteBuffer, ready for sending
 * ... bytes are sent over the network and arrive at the server....
 * 4) network code creates AirStrikeMessageToServer()
 * 5) network code calls airstrikeMessageToServer.fromBytes() to read from the ByteBuffer into the member variables
 * 6) the handler.onMessage(airStrikeMessageToServer) is called to process the message
 *
 * User: The Grey Ghost
 * Date: 15/01/2015
 */
public class AttackMessageToServer implements IMessage
{
  public AttackMessageToServer(Entity entity)
  {
    entityID = entity.getEntityId();
    messageIsValid = true;
  }

  public boolean isMessageValid() {
    return messageIsValid;
  }

  public int getEntityID() {
    return entityID;
  }

  // for use by the message handler only.
  public AttackMessageToServer()
  {
    messageIsValid = false;
  }

  /**
   * Called by the network code once it has received the message bytes over the network.
   * Used to read the ByteBuf contents into your member variables
   * @param buf
   */
  @Override
  public void fromBytes(ByteBuf buf)
  {
    try {
      this.entityID = buf.readInt();
    } catch (IndexOutOfBoundsException ioe) {
      System.err.println("Exception while reading AttackMessageToServer: " + ioe);
      return;
    }
    messageIsValid = true;
  }

  /**
   * Called by the network code.
   * Used to write the contents of your message member variables into the ByteBuf, ready for transmission over the network.
   * @param buf
   */
  @Override
  public void toBytes(ByteBuf buf)
  {
    if (!messageIsValid) return;
    buf.writeInt(entityID);
  }

  @Override
  public String toString()
  {
    return "AttackMessageToServer[entityID=" + String.valueOf(entityID) + "]";
  }

  private boolean messageIsValid;
  private int entityID;
}
