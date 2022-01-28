package io.contek.invoker.okx.api.rest.user;

import com.google.common.collect.ImmutableList;
import io.contek.invoker.commons.actor.IActor;
import io.contek.invoker.commons.actor.ratelimit.TypedPermitRequest;
import io.contek.invoker.commons.rest.RestContext;
import io.contek.invoker.commons.rest.RestMethod;
import io.contek.invoker.commons.rest.RestParams;
import io.contek.invoker.okx.api.common._LendingOffer;
import io.contek.invoker.okx.api.rest.common.RestResponse;

import javax.annotation.concurrent.NotThreadSafe;
import java.util.List;

import static io.contek.invoker.okx.api.ApiFactory.RateLimits.ONE_REST_REQUEST;

@NotThreadSafe
public final class GetLendingOffers extends UserRestRequest<GetLendingOffers.Response> {

  GetLendingOffers(IActor actor, RestContext context) {
    super(actor, context);
  }

  @Override
  protected ImmutableList<TypedPermitRequest> getRequiredQuotas() {
    return ONE_REST_REQUEST;
  }

  @Override
  protected Class<Response> getResponseType() {
    return Response.class;
  }

  @Override
  protected RestMethod getMethod() {
    return RestMethod.GET;
  }

  @Override
  protected String getEndpointPath() {
    return "/api/spot_margin/offers";
  }

  @Override
  protected RestParams getParams() {
    return RestParams.empty();
  }

  @NotThreadSafe
  public static final class Response extends RestResponse<List<_LendingOffer>> {}
}