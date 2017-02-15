/*-
 * #%L
 * deegree-cli-utility
 * %%
 * Copyright (C) 2016 - 2017 weichand.de, lat/lon GmbH
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 2.1 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-2.1.html>.
 * #L%
 */
package de.weichand.deegree;


import de.weichand.deegree.Exec;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author weich_ju
 */
public class InspireTest {
    
    final String SCHEMA_URL_CP = "http://inspire.ec.europa.eu/schemas/cp/4.0/CadastralParcels.xsd";
    
    public InspireTest() {
    }
    
  

    @Test
    public void schemaOnly() throws Exception 
    {
        String[] args = { SCHEMA_URL_CP };
        Exec.main(args);
    }
    
    @Test
    public void schemaToDeegreeConfig() throws Exception 
    {
        String[] args = { SCHEMA_URL_CP, "--format=deegree" };
        Exec.main(args);
    }
    
    @Test
    public void schemaToDeegreeConfigWithSrid() throws Exception 
    {
        String[] args = { SCHEMA_URL_CP, "--format=deegree","--srid=31468" };
        Exec.main(args);
    }
}
