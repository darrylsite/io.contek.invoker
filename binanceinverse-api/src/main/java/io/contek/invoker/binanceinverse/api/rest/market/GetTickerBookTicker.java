package io.contek.invoker.binanceinverse.api.rest.market;

import com.google.common.collect.ImmutableList;
import io.contek.invoker.binanceinverse.api.common._BookTicker;
import io.contek.invoker.binanceinverse.api.rest.market.GetTickerBookTicker.Response;
import io.contek.invoker.commons.actor.IActor;
import io.contek.invoker.commons.actor.ratelimit.TypedPermitRequest;
import io.contek.invoker.commons.rest.RestContext;
import io.contek.invoker.commons.rest.RestParams;

import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;
import java.util.ArrayList;

import static io.contek.invoker.binanceinverse.api.ApiFactory.RateLimits.IP_REST_REQUEST_RULE;
import static io.contek.invoker.binanceinverse.api.ApiFactory.RateLimits.ONE_REST_REQUEST;

@NotThreadSafe
public final class GetTickerBookTicker extends MarketRestRequest<Response> {

  private static final ImmutableList<TypedPermitRequest> MULTI_SYMBOLS_REQUIRED_QUOTA =
      ImmutableList.of(IP_REST_REQUEST_RULE.forPermits(2));

  private String symbol;
  private String pair;

  GetTickerBookTicker(IActor actor, RestContext context) {
    super(actor, context);
  }

  public GetTickerBookTicker setSymbol(@Nullable String symbol) {
    this.symbol = symbol;
    return this;
  }

  public GetTickerBookTicker setPair(@Nullable String pair) {
    this.pair = pair;
    return this;
  }

  @Override
  protected Class<Response> getResponseType() {
    return Response.class;
  }

  @Override
  protected String getEndpointPath() {
    return "/dapi/v1/ticker/bookTicker";
  }

  @Override
  protected RestParams getParams() {
    RestParams.Builder builder = RestParams.newBuilder();

    if (symbol != null) {
      builder.add("symbol", symbol);
    }

    if (pair != null) {
      builder.add("pair", pair);
    }

    return builder.build();
  }

  @Override
  protected ImmutableList<TypedPermitRequest> getRequiredQuotas() {
    if (symbol != null) {
      return ONE_REST_REQUEST;
    }
    return MULTI_SYMBOLS_REQUIRED_QUOTA;
  }

  @NotThreadSafe
  public static final class Response extends ArrayList<_BookTicker> {}
}