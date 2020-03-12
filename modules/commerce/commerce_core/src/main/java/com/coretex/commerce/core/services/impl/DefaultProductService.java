package com.coretex.commerce.core.services.impl;

import com.coretex.commerce.core.dao.ProductDao;
import com.coretex.commerce.core.services.AbstractGenericItemService;
import com.coretex.commerce.core.services.ProductService;
import com.coretex.core.activeorm.services.PageableSearchResult;
import com.coretex.items.cx_core.ProductImageItem;
import com.coretex.items.cx_core.ProductItem;
import com.coretex.items.cx_core.VariantProductItem;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
public class DefaultProductService extends AbstractGenericItemService<ProductItem> implements ProductService {

	private ProductDao productDao;


	public DefaultProductService(ProductDao repository) {
		super(repository);
		this.productDao = repository;
	}

	@Override
	public ProductItem getByCode(String code) {
		return productDao.getByCode(code);
	}

	@Override
	public PageableSearchResult<VariantProductItem> variants(UUID uuid, long count, long page) {
		return productDao.getVariants(uuid, count, page);
	}

	@Override
	public PageableSearchResult<ProductItem> categoryPage(String code, long count, long page) {
		return productDao.getCategoryPage(code, count, page);
	}

	@Override
	public ProductImageItem getDefaultImage(ProductItem productItem) {
		Objects.requireNonNull(productItem, "Unable to find default image for nullable product item ");
		if(CollectionUtils.isNotEmpty(productItem.getImages())){
			return IteratorUtils.first(productItem.getImages().iterator());
		}

		if(CollectionUtils.isNotEmpty(productItem.getVariants())){
			for (ProductItem variant: productItem.getVariants()) {
				var defaultImage = getDefaultImage(variant);
				if(Objects.nonNull(defaultImage)){
					return defaultImage;
				}
			}
		}

		return null;
	}

	@Override
	public PageableSearchResult<ProductItem> pageableList() {
		return productDao.findPageable(true);
	}

	@Override
	public PageableSearchResult<ProductItem> pageableList(long count) {
		return productDao.findPageable(count, true);
	}

	@Override
	public PageableSearchResult<ProductItem> pageableList(long count, long page) {
		return productDao.findPageable(count, page, true);
	}
}
