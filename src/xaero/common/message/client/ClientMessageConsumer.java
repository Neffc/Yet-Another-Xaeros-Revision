package xaero.common.message.client;

import xaero.common.message.MinimapMessage;

public interface ClientMessageConsumer<T extends MinimapMessage<T>> {
   void handle(T var1);
}
