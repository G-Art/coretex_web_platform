package com.coretex.core.business.repositories.merchant;

import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coretex.core.model.common.GenericEntityList;
import com.coretex.core.model.merchant.MerchantStoreCriteria;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
public class MerchantDaoImpl extends DefaultGenericDao<MerchantStoreItem> implements MerchantDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(MerchantDaoImpl.class);

	public MerchantDaoImpl() {
		super(MerchantStoreItem.ITEM_TYPE);
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public GenericEntityList listByCriteria(MerchantStoreCriteria criteria)  {
		try {
			StringBuilder req = new StringBuilder();
			req.append(
					"select distinct m from MerchantStoreItem m left join fetch m.country mc left join fetch m.currency mc left join fetch m.zone mz left join fetch m.defaultLanguage md left join fetch m.languages mls");
			StringBuilder countBuilder = new StringBuilder();
			countBuilder.append("select count(distinct m) from MerchantStoreItem m");
			if (criteria.getCode() != null) {
				req.append("  where lower(m.code) like:code");
				countBuilder.append(" where lower(m.code) like:code");
			}
			if (criteria.getName() != null) {
				if (criteria.getCode() == null) {
					req.append(" where");
					countBuilder.append(" where ");
				} else {
					req.append(" or");
					countBuilder.append(" or ");
				}
				req.append(" lower(m.storename) like:name");
				countBuilder.append(" lower(m.storename) like:name");
			}

			if (!StringUtils.isBlank(criteria.getCriteriaOrderByField())) {
				req.append(" order by m." + criteria.getCriteriaOrderByField() + " "
						+ criteria.getOrderBy().name().toLowerCase());
			}

//      Query countQ = this.em.createQuery(countBuilder.toString());

			String hql = req.toString();
//      Query q = this.em.createQuery(hql);
//
//      if (criteria.getCode() != null) {
//        countQ.setParameter("code", "%" + criteria.getCode().toLowerCase() + "%");
//        q.setParameter("code", "%" + criteria.getCode().toLowerCase() + "%");
//      }
//      if (criteria.getName() != null) {
//        countQ.setParameter("name", "%" + criteria.getCode().toLowerCase() + "%");
//        q.setParameter("name", "%" + criteria.getCode().toLowerCase() + "%");
//      }
//      if (criteria.getUser() != null) {
//      }


//      Number count = (Number) countQ.getSingleResult();

			GenericEntityList entityList = new GenericEntityList();
//      entityList.setTotalCount(count.intValue());

//      if (criteria.getMaxCount() > 0) {
//
//        q.setFirstResult(criteria.getStartIndex());
//        if (criteria.getMaxCount() < count.intValue()) {
//          q.setMaxResults(criteria.getMaxCount());
//        } else {
//          q.setMaxResults(count.intValue());
//        }
//      }

//      List<MerchantStoreItem> stores = q.getResultList();
//      entityList.setList(stores);

			return entityList;


		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new RuntimeException(e);
		}
//    return null;
	}

	@Override
	public MerchantStoreItem findByCode(String code) {
		return findSingle(Map.of(MerchantStoreItem.CODE, code), true);
	}

	@Override
	public MerchantStoreItem getById(UUID id) {
		return find(id);
	}

	@Override
	public boolean existsByCode(String code) {
		return findSingle(Map.of(MerchantStoreItem.CODE, code), false) != null;
	}
}
