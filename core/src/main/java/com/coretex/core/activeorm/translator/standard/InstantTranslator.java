/* sormula - Simple object relational mapping
 * Copyright (C) 2011-2012 Jeff Miller
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.coretex.core.activeorm.translator.standard;


import com.coretex.core.activeorm.translator.TypeTranslator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.Instant;

public class InstantTranslator implements TypeTranslator<Instant>
{
    public Instant read(ResultSet resultSet, int columnIndex) throws Exception
    {
        Timestamp timestamp = resultSet.getTimestamp(columnIndex);
        if (timestamp == null) return null;
        else                   return timestamp.toInstant();
    }
}
