package io.contek.invoker.okx.api.websocket.common;

import javax.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
public abstract class WebSocketChannelMessage<T> extends WebSocketInboundMessage {

  public String channel;
  public T data;
}