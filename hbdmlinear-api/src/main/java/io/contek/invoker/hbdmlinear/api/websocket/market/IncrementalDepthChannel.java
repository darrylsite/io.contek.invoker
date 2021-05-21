package io.contek.invoker.hbdmlinear.api.websocket.market;

import io.contek.invoker.commons.websocket.SubscriptionState;
import io.contek.invoker.commons.websocket.WebSocketSession;
import io.contek.invoker.hbdmlinear.api.common._Depth;
import io.contek.invoker.hbdmlinear.api.websocket.common.constants.WebSocketDataTypeKeys;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.NotThreadSafe;
import javax.annotation.concurrent.ThreadSafe;

import static io.contek.invoker.commons.websocket.SubscriptionState.SUBSCRIBING;
import static java.lang.String.format;

@ThreadSafe
public final class IncrementalDepthChannel
    extends MarketWebSocketChannel<IncrementalDepthChannel.Id, IncrementalDepthChannel.Message> {

  IncrementalDepthChannel(
      IncrementalDepthChannel.Id id, MarketWebSocketRequestIdGenerator requestIdGenerator) {
    super(id, Message.class, requestIdGenerator);
  }

  @Override
  protected SubscriptionState subscribe(WebSocketSession session) {
    IncrementalDepthChannel.Id id = getId();
    SubscribeIncrementalMarketDepthRequest request = new SubscribeIncrementalMarketDepthRequest();
    request.sub = id.getChannel();
    request.data_type = WebSocketDataTypeKeys._incremental;
    request.id = generateNexRequestId();
    session.send(request);
    return SUBSCRIBING;
  }

  @Immutable
  public static final class Id extends MarketWebSocketChannelId<IncrementalDepthChannel.Message> {

    private Id(String topic) {
      super(topic);
    }

    public static Id of(String contractCode, int size) {
      return new Id(format("market.%s.depth.size_%d.high_freq", contractCode, size));
    }
  }

  @NotThreadSafe
  public static final class Message extends WebSocketTickMessage<Tick> {}

  @NotThreadSafe
  public static final class Tick extends _Depth {

    public String event;
  }

  @NotThreadSafe
  public static final class SubscribeIncrementalMarketDepthRequest
      extends MarketWebSocketSubscribeRequest {

    public String data_type;
  }
}