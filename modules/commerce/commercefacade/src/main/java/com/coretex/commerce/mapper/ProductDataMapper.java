package com.coretex.commerce.mapper;

import com.coretex.commerce.data.ImageData;
import com.coretex.commerce.data.ProductData;
import com.coretex.items.cx_core.ProductItem;
import com.coretex.items.cx_core.VariantProductItem;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import static com.coretex.commerce.core.utils.ProductUtils.buildProductSmallImageUtils;

@Mapper(componentModel = "spring", uses = {
		ReferenceMapper.class,
		VariantProductDataMapper.class})
public interface ProductDataMapper extends GenericDataMapper<ProductItem, ProductData>{

	@Override
	@Mappings({
			@Mapping(target = "images", expression = "java(mapImageUrl(productItem))"),
			@Mapping(target = "store", source = "store")
	})
	ProductData fromItem(ProductItem productItem);

	@Override
	@InheritConfiguration(name = "fromItem")
	void updateFromItem(ProductItem productItem, @MappingTarget ProductData productData);

	default ImageData[] mapImageUrl(ProductItem productItem){
		var images = Lists.<ImageData>newArrayList();
		if(CollectionUtils.isEmpty(productItem.getImages())){
			if(productItem instanceof VariantProductItem){
				var baseProduct = ((VariantProductItem) productItem).getBaseProduct();
				if(baseProduct instanceof VariantProductItem){
					images.addAll(Lists.newArrayList(mapImageUrl((VariantProductItem) baseProduct)));
				}
			}
		}else {
			productItem.getImages()
					.forEach(image -> images.add(new ImageData(buildProductSmallImageUtils(image.getProduct().getStore(), image.getProduct().getCode(), image.getProductImage()))));
		}

		return images.toArray(new ImageData[0]);
	}

}
