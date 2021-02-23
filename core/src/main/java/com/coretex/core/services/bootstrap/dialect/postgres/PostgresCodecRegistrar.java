package com.coretex.core.services.bootstrap.dialect.postgres;

import io.netty.buffer.ByteBufAllocator;
import io.r2dbc.postgresql.api.PostgresqlConnection;
import io.r2dbc.postgresql.codec.CodecRegistry;
import io.r2dbc.postgresql.extension.CodecRegistrar;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

public class PostgresCodecRegistrar implements CodecRegistrar {

	@Override
	public Publisher<Void> register(PostgresqlConnection connection, ByteBufAllocator allocator, CodecRegistry registry) {
		registry.addFirst(new PostgresClassCodec(allocator));
		return Mono.empty();
	}
}
