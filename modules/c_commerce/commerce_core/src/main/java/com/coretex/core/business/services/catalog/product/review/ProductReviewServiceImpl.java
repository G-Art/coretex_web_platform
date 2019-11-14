package com.coretex.core.business.services.catalog.product.review;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import com.coretex.items.commerce_core_model.ProductItem;
import com.coretex.items.commerce_core_model.ProductReviewItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Service;

import com.coretex.core.business.repositories.catalog.product.review.ProductReviewDao;
import com.coretex.core.business.services.catalog.product.ProductService;
import com.coretex.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.coretex.items.commerce_core_model.CustomerItem;

@Service("productReviewService")
public class ProductReviewServiceImpl extends
		SalesManagerEntityServiceImpl<ProductReviewItem> implements
		ProductReviewService {


	private ProductReviewDao productReviewDao;

	@Resource
	private ProductService productService;

	public ProductReviewServiceImpl(
			ProductReviewDao productReviewDao) {
		super(productReviewDao);
		this.productReviewDao = productReviewDao;
	}

	@Override
	public List<ProductReviewItem> getByCustomer(CustomerItem customer) {
		return productReviewDao.findByCustomer(customer.getUuid());
	}

	@Override
	public List<ProductReviewItem> getByProduct(ProductItem product) {
		return productReviewDao.findByProduct(product.getUuid());
	}

	@Override
	public ProductReviewItem getByProductAndCustomer(UUID productId, UUID customerId) {
		return productReviewDao.findByProductAndCustomer(productId, customerId);
	}

	@Override
	public List<ProductReviewItem> getByProduct(ProductItem product, LanguageItem language) {
		return productReviewDao.findByProduct(product.getUuid(), language.getUuid());
	}

	private void saveOrUpdate(ProductReviewItem review) {


		Validate.notNull(review, "ProductReviewItem cannot be null");
		Validate.notNull(review.getProduct(), "ProductReviewItem.product cannot be null");
		Validate.notNull(review.getCustomer(), "ProductReviewItem.customer cannot be null");


		//refresh product
		ProductItem product = productService.getById(review.getProduct().getUuid());

		//ajust product rating
		Integer count = 0;
		if (product.getProductReviewCount() != null) {
			count = product.getProductReviewCount();
		}


		BigDecimal averageRating = product.getProductReviewAvg();
		if (averageRating == null) {
			averageRating = new BigDecimal(0);
		}
		//get reviews


		BigDecimal totalRating = averageRating.multiply(new BigDecimal(count));
		totalRating = totalRating.add(new BigDecimal(review.getReviewRating()));

		count = count + 1;
		double avg = totalRating.doubleValue() / count.intValue();

		product.setProductReviewAvg(new BigDecimal(avg));
		product.setProductReviewCount(count);
		super.save(review);

		productService.update(product);

		review.setProduct(product);

	}

	public void update(ProductReviewItem review) {
		this.saveOrUpdate(review);
	}

	public void create(ProductReviewItem review) {
		this.saveOrUpdate(review);
	}

	/* (non-Javadoc)
	 * @see com.coretex.core.business.services.catalog.product.review.ProductReviewService#getByProductNoObjects(com.coretex.core.model.catalog.product.ProductItem)
	 */
	@Override
	public List<ProductReviewItem> getByProductNoCustomers(ProductItem product) {
		return productReviewDao.findByProductNoCustomers(product.getUuid());
	}


}
