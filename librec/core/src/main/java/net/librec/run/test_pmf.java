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
public class test_pmf {
    protected final static Log LOG = LogFactory.getLog(test_pmf.class);

    public static void main(String[] args) throws Exception {

        System.out.println("Hello World");
        testRecommender_By_ConfigFile();
    }

    public static void testRecommender_By_ConfigFile() throws ClassNotFoundException, LibrecException, IOException, Exception {
        String[] params = new String[]{"bcos", "cos", "cpc", "msesim", "msd", "pcc", "krcc", "dice", "jaccard", "exjaccard"};

        for (int i = 0; i < params.length; i++){
            LOG.info(StringUtils.repeat("*",80));
            LOG.info("Begin\n");
            Configuration conf = new Configuration();

            Configuration.Resource data_resources = new Configuration.Resource(Paths.get("E:\\Users\\v-fuz\\tools\\recommender_system\\librec_sourcecode\\librec\\core\\src\\main\\java\\net\\librec\\run\\models\\movielens-100k\\data.properties"));

            conf.addResource(data_resources);
            String model_file_config =
                    "E:\\Users\\v-fuz\\tools\\recommender_system\\librec_sourcecode\\librec\\core\\src\\main\\java\\net\\librec\\run\\models\\"
                            + "movielens-100k\\userknn-test.properties";
            Configuration.Resource resource = new Configuration.Resource(
                    Paths.get(model_file_config)
                    //Paths.get("E:\\Users\\v-fuz\\tools\\recommender_system\\librec_sourcecode\\librec\\core\\src\\main\\java\\net\\librec\\run\\models\\test1.properties")
                    //Paths.get("E:\\Users\\v-fuz\\tools\\recommender_system\\librec_sourcecode\\librec\\core\\src\\main\\java\\net\\librec\\run\\models\\test1_arff.properties")
                    //Paths.get("E:\\Users\\v-fuz\\tools\\recommender_system\\librec_sourcecode\\librec\\core\\src\\main\\java\\net\\librec\\run\\models\\fmsgd-test.properties")
                    //Paths.get("E:\\Users\\v-fuz\\tools\\recommender_system\\librec_sourcecode\\librec\\core\\src\\main\\java\\net\\librec\\run\\models\\pmf-test.properties")
            );
            conf.addResource(resource);

            conf.set("rec.similarity.class", params[i]);

            //String model_detail = FileUtil.readAsString(model_file_config);
            LOG.info(conf.get_str_by_startwith("rec."));

            RecommenderJob job = new RecommenderJob(conf);
            job.runJob();
            LOG.info("End\n\n");
        }
    }

}
