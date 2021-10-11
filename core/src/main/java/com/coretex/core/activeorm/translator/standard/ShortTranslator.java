
package com.coretex.core.activeorm.translator.standard;


import com.coretex.core.activeorm.translator.TypeTranslator;

import java.sql.ResultSet;

public class ShortTranslator implements TypeTranslator<Short>
{
    public Short read(ResultSet resultSet, int columnIndex) throws Exception
    {
        Short s = resultSet.getShort(columnIndex);
        if (resultSet.wasNull()) s = null;
        return s;
    }
}
