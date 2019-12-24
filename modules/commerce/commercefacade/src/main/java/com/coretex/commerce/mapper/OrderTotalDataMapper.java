package com.coretex.commerce.mapper;

import com.coretex.commerce.data.OrderTotalData;
import com.coretex.items.commerce_core_model.OrderTotalItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ReferenceMapper.class})
public interface OrderTotalDataMapper extends GenericDataMapper<OrderTotalItem, OrderTotalData>{

}
