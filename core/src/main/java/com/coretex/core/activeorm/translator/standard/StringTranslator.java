
package com.coretex.core.activeorm.translator.standard;


import com.coretex.core.activeorm.translator.TypeTranslator;

import java.sql.ResultSet;

public class StringTranslator implements TypeTranslator<String>
{
    public String read(ResultSet resultSet, int columnIndex) throws Exception
    {
        return resultSet.getString(columnIndex);
    }
}
