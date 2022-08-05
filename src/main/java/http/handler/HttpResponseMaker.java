package http.handler;

import io.netty.handler.codec.http.FullHttpResponse;

public interface HttpResponseMaker<T> extends Maker<T, FullHttpResponse> {
}
