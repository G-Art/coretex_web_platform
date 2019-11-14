package com.coretex.core.business.repositories.user;

import java.util.List;

import com.coretex.items.commerce_core_model.UserItem;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.model.common.Criteria;
import com.coretex.core.model.common.GenericEntityList;

public class UserRepositoryImpl implements UserRepositoryCustom {


	private static final Logger LOGGER = LoggerFactory.getLogger(UserRepositoryImpl.class);

	@SuppressWarnings("unchecked")
	@Override
	public GenericEntityList<UserItem> listByCriteria(Criteria criteria) throws ServiceException {
		try {
			StringBuilder req = new StringBuilder();
			req.append(
					"select distinct u from UserItem as u left join fetch u.groups ug left join fetch u.defaultLanguage ud join fetch u.merchantStore um left join fetch ug.permissions ugp");
			StringBuilder countBuilder = new StringBuilder();
			countBuilder.append("select count(distinct u) from UserItem as u join u.merchantStore um");
			if (!StringUtils.isBlank(criteria.getStoreCode())) {
				req.append("  where um.code=:storeCode");
				countBuilder.append(" where um.code=:storeCode");
			}

			if (!StringUtils.isBlank(criteria.getCriteriaOrderByField())) {
				req.append(" order by u." + criteria.getCriteriaOrderByField() + " "
						+ criteria.getOrderBy().name().toLowerCase());
			}

//      Query countQ = this.em.createQuery(countBuilder.toString());

//      String hql = req.toString();
//      Query q = this.em.createQuery(hql);

			if (!StringUtils.isBlank(criteria.getSearch())) {

			} else {
//        if (criteria.getStoreCode() != null) {
//          countQ.setParameter("storeCode", criteria.getStoreCode());
//          q.setParameter("storeCode", criteria.getStoreCode());
//        }
			}

//      Number count = (Number) countQ.getSingleResult();

			@SuppressWarnings("rawtypes")
			GenericEntityList entityList = new GenericEntityList();
//      entityList.setTotalCount(count.intValue());

			if (criteria.getMaxCount() > 0) {

//        q.setFirstResult(criteria.getStartIndex());
//        if (criteria.getMaxCount() < count.intValue()) {
//          q.setMaxResults(criteria.getMaxCount());
//        } else {
//          q.setMaxResults(count.intValue());
//        }
			}

//      List<UserItem> users = q.getResultList();
//      entityList.setList(users);

			return entityList;


		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new ServiceException(e);
		}
//    return null;
	}

}
