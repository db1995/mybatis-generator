package io.nobug.mb;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Stream;

/**
 * Generate the files.
 *
 * @author db1995
 */
public class FileUtil {
    private static final String OUTPUT_DIRECTORY_PATH = "." + File.separator + "out" + File.separator + "mbg";
    public static String ENTITY_OUTPUT_PATH;
    public static String SERVICE_OUTPUT_PATH;
    public static String MAPPER_OUTPUT_PATH;
    public static String XML_OUTPUT_PATH;
    private static String baseName;
    private static final String UNDERSCORE = "_";

    /**
     * Create the directories and files depend on your configuration(config.yml).
     *
     * @param list  the data of type and columns.
     * @param table the table you need to generate.
     * @throws IOException
     */
    public static void createDirectoryAndFiles(List<String> list, String table) throws IOException {
        File outputDirectory = new File(OUTPUT_DIRECTORY_PATH);
        if (!outputDirectory.exists()) {
            outputDirectory.mkdirs();
        }
        File entityDirectory = new File(ENTITY_OUTPUT_PATH);
        if (!entityDirectory.exists()) {
            entityDirectory.mkdirs();
        }
        File serviceDirectory = new File(SERVICE_OUTPUT_PATH);
        if (!serviceDirectory.exists()) {
            serviceDirectory.mkdirs();
        }
        File mapperDirectory = new File(MAPPER_OUTPUT_PATH);
        if (!mapperDirectory.exists()) {
            mapperDirectory.mkdirs();
        }
        File XMLDirectory = new File(XML_OUTPUT_PATH);
        if (!XMLDirectory.exists()) {
            XMLDirectory.mkdirs();
        }
        baseName = mapUnderscoreToCamelCase(table);
        baseName = baseName.substring(0, 1).toUpperCase() + baseName.substring(1);
        File entityFile = new File(ENTITY_OUTPUT_PATH + File.separator + baseName + ".java");
        if (!entityFile.exists()) {
            entityFile.createNewFile();
            writeEntityFileContent(entityFile, list);
        }
        File mapperFile = new File(SERVICE_OUTPUT_PATH + File.separator + baseName + "Mapper.java");
        if (!mapperFile.exists()) {
            mapperFile.createNewFile();
            writeMapperFileContent(mapperFile);
        }
        File XMLFile = new File(XML_OUTPUT_PATH + File.separator + baseName + "Mapper.xml");
        if (!XMLFile.exists()) {
            XMLFile.createNewFile();
            writeXMLFileContent(XMLFile);
        }
    }

    private static void writeEntityFileContent(File entityFile, List<String> list) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(entityFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //Write the file.
        for (int i = 0; i < list.size() / 2; i++) {
            String type = list.get(2 * i + 1);
            if (type.contains("time")) {
                writer.println("import java.util.Date;" + "\n");
            }
            if (type.contains("decimal")) {
                writer.println("import java.math.BigDecimal;" + "\n");
            }
        }
        writer.println("/**\n * @author " + DevelopInfo.author + "\n * @date 2020/5/28\n */");
        writer.println("public class " + baseName + " {");
        String field;
        for (int i = 0; i < list.size() / 2; i++) {
            String type = list.get(2 * i + 1);
            type = convertType(type);
            field = list.get(2 * i);
            field = mapUnderscoreToCamelCase(field);
            writer.println("\tprivate " + type + " " + field + ";");
        }
        writer.println();
        for (int i = 0; i < list.size() / 2; i++) {
            String type = list.get(2 * i + 1);
            type = convertType(type);
            field = list.get(2 * i);
            field = mapUnderscoreToCamelCase(field);
            String upperField = field.substring(0, 1).toUpperCase() + field.substring(1);
            writer.println("\tpublic " + type + " get" + upperField + "() {");
            writer.println("\t\treturn " + field + ";");
            writer.println("\t}" + "\n");
            writer.println("\tpublic void set" + upperField + "(" + type + " " + field + ") {");
            writer.println("\t\tthis." + field + " = " + field + ";");
            writer.println("\t}\n");
        }
        writer.print("}");
        writer.close();
    }

    private static void writeMapperFileContent(File mapperFile) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(mapperFile);
        writer.println("/**\n * @author " + DevelopInfo.author + "\n * @date 2020/5/28\n */");
        writer.println("public interface " + mapperFile.getName().replace(".java", "") + " {");
        writer.println();
        writer.println("}");
        writer.close();
    }

    private static void writeXMLFileContent(File XMLFile) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(XMLFile);
        writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
        writer.println("<!DOCTYPE mapper");
        writer.println("        PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\"");
        writer.println("        \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">");
        writer.println("<mapper namespace=\"\">\n\t");
        writer.println("</mapper>");
        writer.close();
    }

    private static String convertType(String column) {
        String type = null;
        if (column.contains("int")) {
            type = "Integer";
        } else if (column.contains("char") || column.contains("text")) {
            type = "String";
        } else if (column.contains("time")) {
            type = "Date";
        } else if (column.contains("decimal")) {
            type = "BigDecimal";
        }
        return type;
    }

    private static String mapUnderscoreToCamelCase(String varName) {
        String[] split = varName.split(UNDERSCORE);
        return Stream.of(split).skip(1).reduce(split[0], (x, y) -> x + y.substring(0, 1).toUpperCase().concat(y.substring(1)));
    }
}