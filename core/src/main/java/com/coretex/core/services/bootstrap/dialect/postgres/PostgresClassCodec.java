package com.coretex.core.services.bootstrap.dialect.postgres;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.r2dbc.postgresql.client.Parameter;
import io.r2dbc.postgresql.message.Format;
import io.r2dbc.postgresql.type.PostgresqlObjectId;
import io.r2dbc.postgresql.util.Assert;
import io.r2dbc.postgresql.util.ByteBufUtils;

import static io.r2dbc.postgresql.message.Format.FORMAT_TEXT;
import static io.r2dbc.postgresql.type.PostgresqlObjectId.TEXT;
import static io.r2dbc.postgresql.type.PostgresqlObjectId.VARCHAR;

public class PostgresClassCodec extends PostgresAbstractCodec<Class> {


	private ByteBufAllocator byteBufAllocator;

	public PostgresClassCodec(ByteBufAllocator byteBufAllocator) {
		super(Class.class);
		this.byteBufAllocator = byteBufAllocator;
	}

	@Override
	boolean doCanDecode(PostgresqlObjectId type, Format format) {
		Assert.requireNonNull(format, "format must not be null");
		Assert.requireNonNull(type, "type must not be null");

		return VARCHAR == type || TEXT == type;
	}

	@Override
	Class doDecode(ByteBuf buffer, PostgresqlObjectId dataType, Format format, Class<? extends Class> type) {
		Assert.requireNonNull(buffer, "byteBuf must not be null");

		String str = ByteBufUtils.decode(buffer);
		try {
			return Class.forName(str);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	Parameter doEncode(Class value) {
		Assert.requireNonNull(value, "value must not be null");

		return create(PostgresqlObjectId.VARCHAR, FORMAT_TEXT, () -> ByteBufUtils.encode(this.byteBufAllocator, value.getName()));
	}

	@Override
	public Parameter encodeNull() {
		return createNull(PostgresqlObjectId.VARCHAR, FORMAT_TEXT);
	}
}
