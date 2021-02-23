package com.coretex.core.services.bootstrap.meta.resolvers;

import com.coretex.core.services.bootstrap.meta.MetaDataContext;
import com.coretex.items.core.GenericItem;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static com.coretex.core.general.constants.GeneralConstants.SYSTEM_TIME_ZONE;
import static org.apache.commons.collections4.MapUtils.isNotEmpty;
import static org.apache.commons.lang3.ObjectUtils.NULL;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.validState;

public class GenericItemDataResolver extends DataResolver {

    public GenericItemDataResolver(MetaDataContext dataContext) {
        super(dataContext);
    }

    @Override
    public void resolve(UUID itemUUID) {
        Map<String, Object> itemData = dataContext.getItemData(itemUUID);
        isTrue(isNotEmpty(itemData), "Can't find item date by %s uuid", Objects.toString(itemData));

        Object createDate = itemData.get(GenericItem.CREATE_DATE);
        Object updateDate = itemData.get(GenericItem.UPDATE_DATE);
        validState(createDate instanceof LocalDateTime && updateDate instanceof LocalDateTime,
                "Create and Update date are expected to be instances of Date, but create date is %s, update date is %s",
                defaultIfNull(createDate, NULL).getClass().getName(), defaultIfNull(updateDate, NULL).getClass().getName());

        itemData.put(GenericItem.CREATE_DATE, ((LocalDateTime) createDate).atZone(SYSTEM_TIME_ZONE).toLocalDateTime());
        itemData.put(GenericItem.UPDATE_DATE, ((LocalDateTime) updateDate).atZone(SYSTEM_TIME_ZONE).toLocalDateTime());
    }
}
