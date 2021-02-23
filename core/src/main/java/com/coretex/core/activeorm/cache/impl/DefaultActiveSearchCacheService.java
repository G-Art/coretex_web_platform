package com.coretex.core.activeorm.cache.impl;

import com.coretex.core.activeorm.cache.CacheContext;
import com.coretex.core.activeorm.cache.CacheInvalidationContext;
import com.coretex.core.activeorm.cache.CacheManager;
import com.coretex.core.activeorm.cache.CacheSegment;
import com.coretex.core.activeorm.cache.CacheService;
import com.coretex.core.activeorm.cache.SegmentManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.cache.CacheFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class DefaultActiveSearchCacheService implements CacheService {

	private final Logger LOG = LoggerFactory.getLogger(DefaultActiveSearchCacheService.class);
	private final SegmentManager segmentManager;

	public DefaultActiveSearchCacheService(SegmentManager segmentManager) {
		this.segmentManager = segmentManager;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <R> R get(CacheContext cacheContext, Supplier<R> supplier) {
		CacheSegment<CacheInvalidationContext, CacheContext> segment = (CacheSegment<CacheInvalidationContext, CacheContext>) segmentManager.findSegment(cacheContext);

		if (Objects.isNull(segment)) {
			return supplier.get();
		}

		var key = segment.createKey(cacheContext);
		return (R) CacheFlux.lookup(
				k -> segment.apply(
						cacheManager -> segment.retrieveValue(
								() -> cacheManager.getIfPresent(k)
						)
				),
				key)
				.onCacheMissResume((Supplier<Flux<Object>>) supplier)
				.andWriteWith(
						(k, signals) -> Mono.just(signals)
								.doOnNext(sig -> segment.apply(
										cacheManager -> {
											cacheManager.put(k, () -> segment.storeValue(() -> sig));
											return Mono.empty();
										}
								))
								.then()
				);


	}

	@Override
	public void invalidateFor(CacheInvalidationContext context) {
		segmentManager.findSegments(new SegmentManager.SegmentFilter() {
			@Override
			public Stream<CacheSegment<? extends CacheInvalidationContext, ? extends CacheContext>> doFilter(Map<Class<? extends CacheContext>, Set<CacheSegment<? extends CacheInvalidationContext, ? extends CacheContext>>> segments) {
				return segments
						.entrySet()
						.stream()
						.flatMap(entry -> entry.getValue()
								.stream())
						.filter(context::filter);
			}
		}).forEach(segment -> segment.accept(cacheManager -> {
			var affectedKeys = context.selectAllAffectedKeys(segment);
			cacheManager.invalidate(affectedKeys);
		}));
	}

	public void invalidateAll() {
		segmentManager.allSegments()
				.forEach(segment -> segment.accept(CacheManager::invalidateAll));
	}
}
