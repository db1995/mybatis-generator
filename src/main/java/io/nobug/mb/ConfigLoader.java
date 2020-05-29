package io.nobug.mb;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Read the config file(config.yml).
 *
 * @author db1995
 */
public class ConfigLoader {
    private static final String CONFIG_FILE_NAME = "config.yml";

    /**
     * Read the configurations.
     */
    public static void load() throws FileNotFoundException {
        System.out.println("Start to load the \"config.yml\"...");
        Yaml yaml = new Yaml();
        URL resource = ConfigLoader.class.getClassLoader().getResource(CONFIG_FILE_NAME);
        Map<String, Map> map = yaml.load(new FileInputStream(resource.getFile()));
        Map<String, Object> mysqlMap = map.get("mysql");
        DevelopInfo.host = (String) mysqlMap.get("host");
        DevelopInfo.database = (String) mysqlMap.get("database");
        DevelopInfo.user = (String) mysqlMap.get("user");
        DevelopInfo.password = (String) mysqlMap.get("password");
        Object table = mysqlMap.get("table");
        if (table instanceof String) {
            DevelopInfo.tableList.add((String) table);
        } else if (table instanceof List) {
            List<String> tableList = (List<String>) table;
            DevelopInfo.tableList.addAll(tableList);
        }
        Map<String, Object> projectMap = map.get("project");
        DevelopInfo.author = (String) projectMap.get("author");
        FileUtil.ENTITY_OUTPUT_PATH = (String) projectMap.get("entity-output-path");
        FileUtil.SERVICE_OUTPUT_PATH = (String) projectMap.get("service-output-location");
        FileUtil.MAPPER_OUTPUT_PATH = (String) projectMap.get("mapper-output-location");
        FileUtil.XML_OUTPUT_PATH = (String) projectMap.get("xml-output-location");
        System.out.println("OK.\n");
    }
}