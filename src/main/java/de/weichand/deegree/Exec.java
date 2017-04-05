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

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.deegree.commons.xml.stax.IndentingXMLStreamWriter;
import org.deegree.cs.persistence.CRSManager;
import org.deegree.cs.refs.coordinatesystem.CRSRef;
import org.deegree.feature.persistence.sql.GeometryStorageParams;
import org.deegree.feature.persistence.sql.MappedAppSchema;
import org.deegree.feature.persistence.sql.config.SQLFeatureStoreConfigWriter;
import org.deegree.feature.persistence.sql.ddl.DDLCreator;
import org.deegree.feature.persistence.sql.mapper.AppSchemaMapper;
import org.deegree.feature.types.AppSchema;
import static org.deegree.feature.types.property.GeometryPropertyType.CoordinateDimension.DIM_2;
import org.deegree.gml.schema.GMLAppSchemaReader;
import org.deegree.sqldialect.SQLDialect;
import org.deegree.sqldialect.postgis.PostGISDialect;

/**
 * CLI utility
 * 
 * @author Juergen Weichand
 */
public class Exec {
    
    private static String format = "deegree";
    private static int srid = 4258;
    private static boolean useIntegerFids = true;

    public static void main(String[] args) throws Exception 
    {
        
        if (args.length == 0) 
        {
            System.out.println("Usage: java -jar deegree-cli-utility.jar [options] schema_url");
            System.out.println("");
            System.out.println("options:");
            System.out.println(" --format={deegree|ddl|all}");
            System.out.println(" --srid=<epsg_code>");
            System.out.println(" --idtype={int|uuid}");
            return;
        }
        
        String schemaUrl = "";
        for (String arg : args) 
        {
            if (arg.startsWith("--format")) 
            {
                format = arg.split("=")[1];
            }
            else if (arg.startsWith("--srid")) 
            {
                srid = Integer.valueOf(arg.split("=")[1]);
            }
            else if (arg.startsWith("--idtype"))
            {
                String idMappingArg = arg.split("=")[1];
                useIntegerFids = idMappingArg.equals( "uuid" ) ? false : true;
            }
            else 
            {
                schemaUrl = arg;
            }
        }
        
        String[] schemaUrls = { schemaUrl };
        GMLAppSchemaReader xsdDecoder = new GMLAppSchemaReader(null, null, schemaUrls);
        AppSchema appSchema = xsdDecoder.extractAppSchema();
        
        CRSRef storageCrs = CRSManager.getCRSRef( "EPSG:" + String.valueOf(srid) );
        GeometryStorageParams geometryParams = new GeometryStorageParams( storageCrs, String.valueOf(srid), DIM_2 );
        AppSchemaMapper mapper = new AppSchemaMapper( appSchema, false, true, geometryParams, 64, true, useIntegerFids );
        MappedAppSchema mappedSchema = mapper.getMappedSchema();
        SQLFeatureStoreConfigWriter configWriter = new SQLFeatureStoreConfigWriter( mappedSchema );
        String uriPathToSchema = new URI(schemaUrl).getPath();
        String schemaFileName = uriPathToSchema.substring(uriPathToSchema.lastIndexOf('/') + 1);
        String fileName = schemaFileName.replaceFirst("[.][^.]+$", "");

        if (format.equals("all"))
        {
            writeSqlDdlFile(mappedSchema, fileName);
            writeXmlConfigFile(schemaUrls, configWriter, fileName);
        }
        else if (format.equals("deegree"))
        {
            writeXmlConfigFile(schemaUrls, configWriter, fileName);
        }
        else if (format.equals("ddl"))
        {
            writeSqlDdlFile(mappedSchema, fileName);
        }


  }

    private static void writeSqlDdlFile(MappedAppSchema mappedSchema, String fileName) throws IOException {
        SQLDialect dialect = new PostGISDialect("2.0.0");
        String[] createStmts = DDLCreator.newInstance( mappedSchema, dialect).getDDL();
        String sqlOutputFilename = "./"+fileName+".sql";
        System.out.println( "Writing SQL DDL into file: " + sqlOutputFilename);
        Path pathToSqlOutputFile = Paths.get(sqlOutputFilename);
        try (BufferedWriter writer = Files.newBufferedWriter(pathToSqlOutputFile)) {
            for (String sqlStatement: createStmts) {
                writer.write(sqlStatement+";"+System.getProperty("line.separator"));
            }
        }
    }

    private static void writeXmlConfigFile(String[] schemaUrls, SQLFeatureStoreConfigWriter configWriter, String fileName) throws XMLStreamException, IOException {
        List<String> configUrls = Arrays.asList(schemaUrls);
        String xmlOutputFilename = "./"+fileName+".xml";
        System.out.println( "Writing deegree FeatureStore config into file: " + xmlOutputFilename);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        XMLStreamWriter xmlWriter = XMLOutputFactory.newInstance().createXMLStreamWriter( bos );
        xmlWriter = new IndentingXMLStreamWriter( xmlWriter );
        configWriter.writeConfig( xmlWriter, fileName+"DS", configUrls );
        xmlWriter.close();
        Files.write(Paths.get(xmlOutputFilename), bos.toString().getBytes(StandardCharsets.UTF_8) );
    }
}
