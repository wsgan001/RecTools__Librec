package net.librec.run;

import net.librec.common.LibrecException;
import net.librec.conf.Configuration;
import net.librec.job.RecommenderJob;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Created by fuzzhang on 3/12/2017.
 */
public class test_all_ranking_pure {
    protected final static Log LOG = LogFactory.getLog(test_all_ranking_pure.class);
    public static final String models_dir = "E:\\Users\\v-fuz\\tools\\recommender_system\\librec_sourcecode\\librec\\core\\src\\main\\java\\net\\librec\\run\\models\\movielens-100k\\ranking\\";

    public static void main(String[] args) throws Exception {
        String pattern = "ranking_pure_";
        String logfilename = "E:\\Users\\v-fuz\\tools\\recommender_system\\librec_sourcecode\\librec\\conf\\"
                + "log4j.properties";
        Properties ps = new Properties();
        ps.load(new FileInputStream(logfilename));
        ps.setProperty("librec.log.file", pattern + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm")) + ".log");
        PropertyConfigurator.configure(ps);
        testRecommender_By_ConfigFile();

    }

    public static String Properties_file_str(Properties props){
        Enumeration e = props.propertyNames();
        String str = "";
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            //System.out.println(key + " -- " + props.getProperty(key));
            str += key + "=" + props.getProperty(key) + "\n";
        }
        return str;
    }

    public static void testRecommender_By_ConfigFile() {

        List<File> filesInFolder = null;
        try {
            filesInFolder = Files.walk(Paths.get(models_dir))
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (File f : filesInFolder){
            try {
                LOG.info(StringUtils.repeat("*",80));
                LOG.info("Begin\n");
                Configuration conf = new Configuration();

                Configuration.Resource data_resources = new Configuration.Resource(
                        Paths.get("E:\\Users\\v-fuz\\tools\\recommender_system\\librec_sourcecode\\librec\\core\\src\\main\\java\\net\\librec\\run\\models\\movielens-100k\\data_pure_ranking.properties"));

                conf.addResource(data_resources);
                //String model_file_config = f.getAbsolutePath();
                Configuration.Resource resource = new Configuration.Resource(
                        Paths.get(f.getAbsolutePath())
                );
                conf.addResource(resource);
                LOG.info(conf.get_str_by_startwith("rec."));

                RecommenderJob job = new RecommenderJob(conf);
                job.runJob();
                LOG.info("End\n\n");
            } catch (Exception e) {
                e.printStackTrace();
                LOG.error(e.getMessage());
            }
        }
    }

}
