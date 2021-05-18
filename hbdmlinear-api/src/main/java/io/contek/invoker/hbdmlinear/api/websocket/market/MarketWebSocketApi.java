package io.contek.invoker.hbdmlinear.api.websocket.market;

import com.google.common.collect.ImmutableList;
import io.contek.invoker.commons.actor.IActor;
import io.contek.invoker.commons.actor.ratelimit.RateLimitQuota;
import io.contek.invoker.commons.websocket.*;
import io.contek.invoker.hbdmlinear.api.websocket.common.WebSocketLiveKeeper;
import io.contek.invoker.security.ICredential;

import javax.annotation.concurrent.ThreadSafe;
import java.util.HashMap;
import java.util.Map;

import static io.contek.invoker.hbdmlinear.api.ApiFactory.RateLimits.ONE_IP_WEB_SOCKET_CONNECTION_REQUEST;

@ThreadSafe
public final class MarketWebSocketApi extends BaseWebSocketApi {

  private final WebSocketContext context;
  private final MarketWebSocketRequestIdGenerator requestIdGenerator =
      new MarketWebSocketRequestIdGenerator();

  private final Map<IncrementalDepthChannel.Id, IncrementalDepthChannel> incrementalDepthChannels =
      new HashMap<>();
  private final Map<TradeDetailChannel.Id, TradeDetailChannel> tradeDetailChannels =
      new HashMap<>();

  public MarketWebSocketApi(IActor actor, WebSocketContext context) {
    super(
        actor,
        new MarketWebSocketMessageParser(),
        IWebSocketAuthenticator.noOp(),
        WebSocketLiveKeeper.getInstance());
    this.context = context;
  }

  public IncrementalDepthChannel getIncrementalMarketDepthChannel(IncrementalDepthChannel.Id id) {
    synchronized (incrementalDepthChannels) {
      return incrementalDepthChannels.computeIfAbsent(
          id,
          k -> {
            IncrementalDepthChannel result = new IncrementalDepthChannel(k, requestIdGenerator);
            attach(result);
            return result;
          });
    }
  }

  public TradeDetailChannel getTradeDetailChannel(TradeDetailChannel.Id id) {
    synchronized (tradeDetailChannels) {
      return tradeDetailChannels.computeIfAbsent(
          id,
          k -> {
            TradeDetailChannel result = new TradeDetailChannel(k, requestIdGenerator);
            attach(result);
            return result;
          });
    }
  }

  @Override
  protected ImmutableList<RateLimitQuota> getRequiredQuotas() {
    return ONE_IP_WEB_SOCKET_CONNECTION_REQUEST;
  }

  @Override
  protected WebSocketCall createCall(ICredential credential) {
    return WebSocketCall.fromUrl(context.getBaseUrl() + "/linear-swap-ws");
  }

  @Override
  protected void checkErrorMessage(AnyWebSocketMessage message) throws WebSocketRuntimeException {}
}
