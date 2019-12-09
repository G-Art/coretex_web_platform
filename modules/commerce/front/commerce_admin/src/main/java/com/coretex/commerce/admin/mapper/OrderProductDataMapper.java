package com.coretex.commerce.admin.mapper;

import com.coretex.commerce.admin.data.OrderProductData;
import com.coretex.items.commerce_core_model.OrderProductItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ReferenceMapper.class})
public interface OrderProductDataMapper extends GenericDataMapper<OrderProductItem, OrderProductData>{

}
