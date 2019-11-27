package com.coretex.commerce.admin.facades.impl;

import com.coretex.commerce.admin.data.GroupData;
import com.coretex.commerce.admin.facades.GroupFacade;
import com.coretex.commerce.admin.mapper.GroupDataMapper;
import com.coretex.core.business.services.user.GroupService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DefaultGroupFacade implements GroupFacade {

	@Resource
	private GroupService groupService;

	@Resource
	private GroupDataMapper groupDataMapper;

	@Override
	public GroupData getByName(String name){
		return groupDataMapper.fromItem(groupService.findByName(name));
	}

	@Override
	public List<GroupData> getAll(){
		return groupService.list()
				.stream()
				.map(groupDataMapper::fromItem)
				.collect(Collectors.toList());
	}
}
