package com.coretex.core.activeorm.factories;

import com.coretex.core.activeorm.query.QueryTransformationProcessor;
import com.coretex.core.activeorm.query.operations.ModificationOperation;
import com.coretex.core.activeorm.query.specs.CascadeInsertOperationSpec;
import com.coretex.core.activeorm.query.specs.CascadeRemoveOperationSpec;
import com.coretex.core.activeorm.query.specs.CascadeUpdateOperationSpec;
import com.coretex.core.activeorm.query.specs.InsertOperationSpec;
import com.coretex.core.activeorm.query.specs.LocalizedDataRemoveOperationSpec;
import com.coretex.core.activeorm.query.specs.LocalizedDataSaveOperationSpec;
import com.coretex.core.activeorm.query.specs.ModificationOperationSpec;
import com.coretex.core.activeorm.query.specs.RemoveOperationSpec;
import com.coretex.core.activeorm.query.specs.UpdateOperationSpec;
import com.coretex.core.activeorm.services.AbstractJdbcService;
import com.coretex.core.activeorm.services.ItemService;
import com.coretex.core.general.utils.OperationUtils;
import com.coretex.items.core.GenericItem;
import com.coretex.items.core.MetaAttributeTypeItem;
import com.coretex.items.core.MetaRelationTypeItem;
import com.coretex.meta.AbstractGenericItem;
import net.sf.jsqlparser.statement.Statement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import static com.coretex.core.general.utils.OperationUtils.isLoopSafe;

public class OperationFactoryImpl extends AbstractJdbcService implements OperationFactory {

	@Autowired
	@Qualifier("idleQueryTransformationProcessor")
	private QueryTransformationProcessor queryTransformationProcessor;


	@Autowired
	private ItemService itemService;

	public <T extends GenericItem> ModificationOperation<? extends Statement, ? extends ModificationOperationSpec>
	createSaveOperation(T item) {

		ModificationOperationSpec<? extends Statement, ? extends ModificationOperation> modificationOperationSpec;
		if (isNew(item)) {
			modificationOperationSpec = new InsertOperationSpec(item);
		} else {
			modificationOperationSpec = new UpdateOperationSpec(item);
		}
		return createModificationOperation(modificationOperationSpec, this::getJdbcTemplate);
	}

	@Override
	public <T extends GenericItem> ModificationOperation<? extends Statement, ? extends ModificationOperationSpec>
	createSaveOperation(T item,
						MetaAttributeTypeItem attributeTypeItem,
						ModificationOperation<? extends Statement, ? extends ModificationOperationSpec> initiator) {

		ModificationOperationSpec<? extends Statement, ? extends ModificationOperation> modificationOperationSpec;
		if (attributeTypeItem.getLocalized()) {
			modificationOperationSpec = new LocalizedDataSaveOperationSpec(initiator, attributeTypeItem);
		} else {
			if (isNew(item)) {
				modificationOperationSpec = new CascadeInsertOperationSpec(initiator, item, attributeTypeItem);
			} else {
				modificationOperationSpec = new CascadeUpdateOperationSpec(initiator, item, attributeTypeItem);
			}
		}
		return createModificationOperation(modificationOperationSpec, initiator::getJdbcTemplate);
	}

	@Override
	public <T extends GenericItem> List<ModificationOperation<? extends Statement, ? extends ModificationOperationSpec>>
	createRelationSaveOperations(Collection<T> items,
								 MetaAttributeTypeItem attributeTypeItem,
								 ModificationOperation<? extends Statement, ? extends ModificationOperationSpec> initiator) {

		LinkedList<ModificationOperation<? extends Statement, ? extends ModificationOperationSpec>> operations = new LinkedList<>();
		GenericItem ownerItem = initiator.getOperationSpec().getItem();
		items.stream()
				.filter(Objects::nonNull)
				.filter(item -> OperationUtils.isLoopSafe(initiator.getOperationSpec(), item))
				.forEach(item -> {
					if (item.getItemContext().isDirty()) {
						ModificationOperation<? extends Statement, ? extends ModificationOperationSpec> modificationOperation = createSaveOperation(item, attributeTypeItem, initiator);
						operations.add(modificationOperation);
					}
					if (isNew(item) || !OperationUtils.haveRelation(item, ownerItem, attributeTypeItem)) {
						ModificationOperation<? extends Statement, ? extends ModificationOperationSpec> relationModificationOperation = createRelationSaveOperation(item, ownerItem, attributeTypeItem, initiator);
						operations.add(relationModificationOperation);
					}
				});
		return operations;
	}

	@Override
	public <T extends GenericItem> List<ModificationOperation<? extends Statement, ? extends ModificationOperationSpec>>
	createRelationSaveOperations(T item,
								 MetaAttributeTypeItem attributeTypeItem,
								 ModificationOperation<? extends Statement, ? extends ModificationOperationSpec> initiator) {

		return createRelationSaveOperations(List.of(item), attributeTypeItem, initiator);
	}

	@Override
	public <T extends GenericItem> ModificationOperation<? extends Statement, ? extends ModificationOperationSpec>
	createDeleteOperation(T item) {
		ModificationOperationSpec<? extends Statement, ? extends ModificationOperation> modificationOperationSpec = new RemoveOperationSpec(item);
		return createModificationOperation(modificationOperationSpec, this::getJdbcTemplate);
	}

	@Override
	public <T extends GenericItem> ModificationOperation<? extends Statement, ? extends ModificationOperationSpec>
	createDeleteOperation(T item,
						  MetaAttributeTypeItem attributeTypeItem,
						  ModificationOperation<? extends Statement, ? extends ModificationOperationSpec> initiator) {

		ModificationOperationSpec<? extends Statement, ? extends ModificationOperation> modificationOperationSpec;
		if (attributeTypeItem.getLocalized()) {
			modificationOperationSpec = new LocalizedDataRemoveOperationSpec(initiator, attributeTypeItem);
		} else {
			modificationOperationSpec = new CascadeRemoveOperationSpec(initiator, item, attributeTypeItem);
		}
		return createModificationOperation(modificationOperationSpec, initiator::getJdbcTemplate);
	}

	@Override
	public <T extends GenericItem> List<ModificationOperation<? extends Statement, ? extends ModificationOperationSpec>>
	createRelationDeleteOperations(Collection<T> items,
								   MetaAttributeTypeItem attributeTypeItem,
								   ModificationOperation<? extends Statement, ? extends ModificationOperationSpec> initiator) {

		List<ModificationOperation<? extends Statement, ? extends ModificationOperationSpec>> opperations = new LinkedList<>();
		GenericItem ownerItem = initiator.getOperationSpec().getItem();
		items.stream().filter(Objects::nonNull).forEach(item -> {
			boolean loopSave = isLoopSafe(initiator.getOperationSpec(), item);
			if (attributeTypeItem.getAssociated() && loopSave) {
				opperations.add(createDeleteOperation(item, attributeTypeItem, initiator));
			}
			if (loopSave) {
				opperations.add(createRelationDeleteOperation(item, ownerItem, attributeTypeItem, initiator));
			}
		});
		return opperations;
	}

	@Override
	public <T extends GenericItem> List<ModificationOperation<? extends Statement, ? extends ModificationOperationSpec>>
	createRelationDeleteOperations(T item,
								   MetaAttributeTypeItem attributeTypeItem,
								   ModificationOperation<? extends Statement, ? extends ModificationOperationSpec> initiator) {

		return createRelationDeleteOperations(List.of(item), attributeTypeItem, initiator);
	}

	private ModificationOperation<? extends Statement, ? extends ModificationOperationSpec>
	createModificationOperation(ModificationOperationSpec<? extends Statement, ? extends ModificationOperation> modificationOperationSpec,
								Supplier<NamedParameterJdbcTemplate> jdbcTemplateSupplier) {

		modificationOperationSpec.setOperationFactory(this);
		modificationOperationSpec.setJdbcService(this);
		ModificationOperation<? extends Statement, ? extends ModificationOperationSpec> modificationOperation = modificationOperationSpec.createOperation(queryTransformationProcessor);
		modificationOperation.setJdbcTemplateSupplier(jdbcTemplateSupplier);

		return modificationOperation;
	}

	private <T extends GenericItem> ModificationOperation<? extends Statement, ? extends ModificationOperationSpec>
	createRelationDeleteOperation(T item,
								  GenericItem ownerItem,
								  MetaAttributeTypeItem attributeTypeItem,
								  ModificationOperation<? extends Statement, ? extends ModificationOperationSpec> initiator) {

		GenericItem relation = (GenericItem) itemService.create(((MetaRelationTypeItem) attributeTypeItem.getAttributeType()).getItemClass());

		if (attributeTypeItem.getSource()) {
			setRelations(ownerItem, item, relation);
		} else {
			setRelations(item, ownerItem, relation);
		}

		return createDeleteOperation(relation, attributeTypeItem, initiator);
	}

	private <T extends GenericItem> ModificationOperation<? extends Statement, ? extends ModificationOperationSpec>
	createRelationSaveOperation(T item,
								GenericItem ownerItem,
								MetaAttributeTypeItem attributeTypeItem,
								ModificationOperation<? extends Statement, ? extends ModificationOperationSpec> initiator) {

		GenericItem relation = (GenericItem) itemService.create(((MetaRelationTypeItem) attributeTypeItem.getAttributeType()).getItemClass());

		LocalDateTime now = LocalDateTime.now();
		relation.setAttributeValue(GenericItem.CREATE_DATE, now);
		relation.setAttributeValue(GenericItem.UPDATE_DATE, now);

		if (attributeTypeItem.getSource()) {
			setRelations(ownerItem, item, relation);
		} else {
			setRelations(item, ownerItem, relation);
		}

		return createSaveOperation(relation, attributeTypeItem, initiator);
	}

	private void setRelations(GenericItem source, GenericItem target, AbstractGenericItem relation) {
		relation.setAttributeValue("source", source);
		relation.setAttributeValue("sourceType", source.getMetaType());
		relation.setAttributeValue("target", target);
		relation.setAttributeValue("targetType", target.getMetaType());
	}

	private <T extends GenericItem> boolean isNew(T item) {
		return item.getItemContext().isNew() || !item.getItemContext().isExist();
	}
}
