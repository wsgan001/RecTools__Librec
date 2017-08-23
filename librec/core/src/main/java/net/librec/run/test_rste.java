package net.librec.run;

import net.librec.common.LibrecException;
import net.librec.conf.Configuration;
import net.librec.job.RecommenderJob;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * Created by fuzzhang on 3/12/2017.
 */
public class test_rste {
    protected final static Log LOG = LogFactory.getLog(test_rste.class);

    public static void main(String[] args) throws Exception {

        System.out.println("Hello World");
        testRecommender_By_ConfigFile();
    }

    public static void testRecommender_By_ConfigFile() throws ClassNotFoundException, LibrecException, IOException, Exception {
        LOG.info(StringUtils.repeat("*",80));
        LOG.info("Begin\n");
        Configuration conf = new Configuration();

        String model_file_config =
                "E:\\Users\\v-fuz\\tools\\recommender_system\\librec_sourcecode\\librec\\core\\src\\main\\java\\net\\librec\\run\\models\\movielens-100k\\test\\"
                        + "rste-test.properties";
        Configuration.Resource resource = new Configuration.Resource(
                Paths.get(model_file_config)
        );
        conf.addResource(resource);

        LOG.info(conf.get_str_by_startwith("rec."));

        RecommenderJob job = new RecommenderJob(conf);
        job.runJob();
        LOG.info("End\n\n");
    }
}
