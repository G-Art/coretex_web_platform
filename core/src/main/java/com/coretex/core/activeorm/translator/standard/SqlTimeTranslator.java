
package com.coretex.core.activeorm.translator.standard;


import com.coretex.core.activeorm.translator.TypeTranslator;

import java.sql.ResultSet;

public class SqlTimeTranslator implements TypeTranslator<java.sql.Time>
{
    public java.sql.Time read(ResultSet resultSet, int columnIndex) throws Exception
    {
        return resultSet.getTime(columnIndex);
    }
}
