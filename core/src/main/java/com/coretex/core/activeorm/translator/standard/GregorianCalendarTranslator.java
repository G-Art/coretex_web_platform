
package com.coretex.core.activeorm.translator.standard;

import com.coretex.core.activeorm.translator.TypeTranslator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.GregorianCalendar;

public class GregorianCalendarTranslator implements TypeTranslator<GregorianCalendar>
{
    public GregorianCalendar read(ResultSet resultSet, int columnIndex) throws Exception
    {
        GregorianCalendar gc;
        
        Timestamp t = resultSet.getTimestamp(columnIndex);
        if (t == null)
        {
            gc = null;
        }
        else
        {
            gc = new GregorianCalendar();
            gc.setTimeInMillis(t.getTime());
        }
        
        return gc;
    }
}
