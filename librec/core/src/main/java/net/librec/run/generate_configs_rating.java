package net.librec.run;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.librec.util.FileUtil;
import net.librec.util.StringUtil;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by fuzzhang on 3/20/2017.
 */
public class generate_configs_rating {

    public static final String output_dir = "E:\\Users\\v-fuz\\tools\\recommender_system\\librec_sourcecode\\librec\\core\\src\\main\\java\\net\\librec\\run\\models\\movielens-100k\\rating\\";

    public static void main(String[] args) throws Exception {
        Files.walk(Paths.get(output_dir))
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .forEach(File::delete);
        /***
         * Poor Baselines
         */
//        constantguess();
//        globalaverage();
//        itemaverage();
//        mostpopular();
//        randomguess();
//        useraverage();
        associationrule();
        personalitydiagnosis();
        prankd();
        slopeone();

        /***
         * Non-Bayesian Methods
         */
//        user_knn();
//        item_knn();
//        pmf();
//        biasedmf();
//        svdpp();
//        asvdpp();
//        rfrec();
//        nmf();
//        mfals();
//        llorma();
//        bpoissmf();
//        rbm();

        /***
         * Bayesian Methods
         */

//        usercluster();
//        itemcluster();
//        bucm();
//        bhfree();
//        urp();
//        ldcc();
//        gplsa();
//        bpmf();
//        aspectmodelrating();

        System.out.println("Finished");

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

    public static void user_knn() throws Exception {
        Multimap<String, String> multiMap = HashMultimap.create();
        multiMap.putAll("rec.similarity.class", Arrays.asList(new String[]{"bcos", "cos", "cpc", "msesim", "msd", "pcc", "krcc", "dice", "jaccard", "exjaccard"}));
        List<HashMap<String,String>> grids = GridSearch.grid_search(multiMap);
        Properties ps = new Properties();
        ps.load(new FileInputStream("E:\\Users\\v-fuz\\tools\\recommender_system\\librec_sourcecode\\librec\\core\\src\\main\\resources\\rec\\"
                + "cf\\userknn-test.properties"));
        int i = 1;
        for (HashMap<String,String> grid: grids){
            Properties ps_tmp = (Properties) ps.clone();
            for (Map.Entry<String,String> entry: grid.entrySet()){
                ps_tmp.setProperty(entry.getKey(), entry.getValue());
            }
            FileUtil.writeString(output_dir + "userknn_" + Integer.toString(i) + ".properties", Properties_file_str(ps_tmp));
            i++;
        }
    }

    public static void item_knn() throws Exception {
        Multimap<String, String> multiMap = HashMultimap.create();
        multiMap.putAll("rec.similarity.class", Arrays.asList(new String[]{"bcos", "cos", "cpc", "msesim", "msd", "pcc", "krcc", "dice", "jaccard", "exjaccard"}));
        List<HashMap<String,String>> grids = GridSearch.grid_search(multiMap);
        Properties ps = new Properties();
        ps.load(new FileInputStream("E:\\Users\\v-fuz\\tools\\recommender_system\\librec_sourcecode\\librec\\core\\src\\main\\resources\\rec\\"
                + "cf\\itemknn-test.properties"));
        int i = 1;
        for (HashMap<String,String> grid: grids){
            Properties ps_tmp = (Properties) ps.clone();
            for (Map.Entry<String,String> entry: grid.entrySet()){
                ps_tmp.setProperty(entry.getKey(), entry.getValue());
            }
            FileUtil.writeString(output_dir + "itemknn_" + Integer.toString(i) + ".properties", Properties_file_str(ps_tmp));
            i++;
        }
    }

    public static void pmf() throws Exception {
        Multimap<String, String> multiMap = HashMultimap.create();
        multiMap.putAll("rec.iterator.learnrate", Arrays.asList(new String[]{"0.01", "0.001"}));
        multiMap.putAll("rec.iterator.maximum", Arrays.asList(new String[]{"200"}));
        multiMap.putAll("regularization", Arrays.asList(new String[]{"0.1", "0.05", "0.01"}));
        multiMap.putAll("rec.factor.number", Arrays.asList(new String[]{"100"}));
        multiMap.putAll("rec.learnrate.bolddriver", Arrays.asList(new String[]{"false","true"}));

        List<HashMap<String,String>> grids = GridSearch.grid_search(multiMap);
        Properties ps = new Properties();
        ps.load(new FileInputStream("E:\\Users\\v-fuz\\tools\\recommender_system\\librec_sourcecode\\librec\\core\\src\\main\\resources\\rec\\"
                + "cf\\rating\\pmf-test.properties"));
        int i = 1;
        for (HashMap<String,String> grid: grids){
            Properties ps_tmp = (Properties) ps.clone();
            for (Map.Entry<String,String> entry: grid.entrySet()){
                if (entry.getKey().equals("regularization")){
                    ps_tmp.setProperty("rec.user.regularization", entry.getValue());
                    ps_tmp.setProperty("rec.item.regularization", entry.getValue());
                }
                else
                    ps_tmp.setProperty(entry.getKey(), entry.getValue());
            }
            FileUtil.writeString(output_dir + "pmf_" + Integer.toString(i) + ".properties", Properties_file_str(ps_tmp));
            i++;
        }
    }

    public static void biasedmf() throws Exception {
        Multimap<String, String> multiMap = HashMultimap.create();
        multiMap.putAll("rec.iterator.learnrate", Arrays.asList(new String[]{"0.01", "0.001"}));
        multiMap.putAll("rec.iterator.maximum", Arrays.asList(new String[]{"20"}));
        multiMap.putAll("regularization", Arrays.asList(new String[]{"0.1", "0.05", "0.01"}));
        multiMap.putAll("rec.factor.number", Arrays.asList(new String[]{"100"}));
        multiMap.putAll("rec.learnrate.bolddriver", Arrays.asList(new String[]{"false","true"}));

        List<HashMap<String,String>> grids = GridSearch.grid_search(multiMap);
        Properties ps = new Properties();
        ps.load(new FileInputStream("E:\\Users\\v-fuz\\tools\\recommender_system\\librec_sourcecode\\librec\\core\\src\\main\\resources\\rec\\"
                + "cf\\rating\\biasedmf-test.properties"));
        int i = 1;
        for (HashMap<String,String> grid: grids){
            Properties ps_tmp = (Properties) ps.clone();
            for (Map.Entry<String,String> entry: grid.entrySet()){
                if (entry.getKey().equals("regularization")){
                    ps_tmp.setProperty("rec.user.regularization", entry.getValue());
                    ps_tmp.setProperty("rec.item.regularization", entry.getValue());
                    ps_tmp.setProperty("rec.bias.regularization", entry.getValue());
                }
                else
                    ps_tmp.setProperty(entry.getKey(), entry.getValue());
            }
            FileUtil.writeString(output_dir + "biasedmf_" + Integer.toString(i) + ".properties", Properties_file_str(ps_tmp));
            i++;
        }
    }

    public static void svdpp() throws Exception {
        Multimap<String, String> multiMap = HashMultimap.create();
        multiMap.putAll("rec.iterator.learnrate", Arrays.asList(new String[]{"0.01", "0.001"}));
        multiMap.putAll("rec.iterator.maximum", Arrays.asList(new String[]{"20"}));
        multiMap.putAll("regularization", Arrays.asList(new String[]{"0.1", "0.05", "0.01"}));
        multiMap.putAll("rec.factor.number", Arrays.asList(new String[]{"100"}));
        multiMap.putAll("rec.learnrate.bolddriver", Arrays.asList(new String[]{"false","true"}));

        List<HashMap<String,String>> grids = GridSearch.grid_search(multiMap);
        Properties ps = new Properties();
        ps.load(new FileInputStream("E:\\Users\\v-fuz\\tools\\recommender_system\\librec_sourcecode\\librec\\core\\src\\main\\resources\\rec\\"
                + "cf\\rating\\svdpp-test.properties"));
        int i = 1;
        for (HashMap<String,String> grid: grids){
            Properties ps_tmp = (Properties) ps.clone();
            for (Map.Entry<String,String> entry: grid.entrySet()){
                if (entry.getKey().equals("regularization")){
                    ps_tmp.setProperty("rec.user.regularization", entry.getValue());
                    ps_tmp.setProperty("rec.item.regularization", entry.getValue());
                }
                else
                    ps_tmp.setProperty(entry.getKey(), entry.getValue());
            }
            FileUtil.writeString(output_dir + "svdpp_" + Integer.toString(i) + ".properties", Properties_file_str(ps_tmp));
            i++;
        }
    }

    public static void asvdpp() throws Exception {
        Multimap<String, String> multiMap = HashMultimap.create();
        multiMap.putAll("rec.iterator.learnrate", Arrays.asList(new String[]{"0.01", "0.001"}));
        multiMap.putAll("rec.iterator.maximum", Arrays.asList(new String[]{"20"}));
        multiMap.putAll("regularization", Arrays.asList(new String[]{"0.1", "0.05", "0.01"}));
        multiMap.putAll("rec.factor.number", Arrays.asList(new String[]{"100"}));
        multiMap.putAll("rec.learnrate.bolddriver", Arrays.asList(new String[]{"false","true"}));

        List<HashMap<String,String>> grids = GridSearch.grid_search(multiMap);
        Properties ps = new Properties();
        ps.load(new FileInputStream("E:\\Users\\v-fuz\\tools\\recommender_system\\librec_sourcecode\\librec\\core\\src\\main\\resources\\rec\\"
                + "cf\\rating\\asvdpp-test.properties"));
        int i = 1;
        for (HashMap<String,String> grid: grids){
            Properties ps_tmp = (Properties) ps.clone();
            for (Map.Entry<String,String> entry: grid.entrySet()){
                if (entry.getKey().equals("regularization")){
                    ps_tmp.setProperty("rec.user.regularization", entry.getValue());
                    ps_tmp.setProperty("rec.item.regularization", entry.getValue());
                }
                else
                    ps_tmp.setProperty(entry.getKey(), entry.getValue());
            }
            FileUtil.writeString(output_dir + "svdpp_" + Integer.toString(i) + ".properties", Properties_file_str(ps_tmp));
            i++;
        }
    }

    public static void rfrec() throws Exception {
        Multimap<String, String> multiMap = HashMultimap.create();
        multiMap.putAll("rec.iterator.learnrate", Arrays.asList(new String[]{"0.01", "0.001"}));
        multiMap.putAll("rec.iterator.maximum", Arrays.asList(new String[]{"20"}));
        multiMap.putAll("regularization", Arrays.asList(new String[]{"0.1", "0.05", "0.01"}));
        multiMap.putAll("rec.factor.number", Arrays.asList(new String[]{"100"}));
        multiMap.putAll("rec.learnrate.bolddriver", Arrays.asList(new String[]{"false","true"}));

        List<HashMap<String,String>> grids = GridSearch.grid_search(multiMap);
        Properties ps = new Properties();
        ps.load(new FileInputStream("E:\\Users\\v-fuz\\tools\\recommender_system\\librec_sourcecode\\librec\\core\\src\\main\\resources\\rec\\"
                + "cf\\rating\\rfrec-test.properties"));
        int i = 1;
        for (HashMap<String,String> grid: grids){
            Properties ps_tmp = (Properties) ps.clone();
            for (Map.Entry<String,String> entry: grid.entrySet()){
                if (entry.getKey().equals("regularization")){
                    ps_tmp.setProperty("rec.user.regularization", entry.getValue());
                    ps_tmp.setProperty("rec.item.regularization", entry.getValue());
                }
                else
                    ps_tmp.setProperty(entry.getKey(), entry.getValue());
            }
            FileUtil.writeString(output_dir + "rfrec_" + Integer.toString(i) + ".properties", Properties_file_str(ps_tmp));
            i++;
        }
    }

    public static void nmf() throws Exception {
        Multimap<String, String> multiMap = HashMultimap.create();
        multiMap.putAll("rec.iterator.maximum", Arrays.asList(new String[]{"20"}));
        multiMap.putAll("rec.factor.number", Arrays.asList(new String[]{"100"}));
        multiMap.putAll("rec.learnrate.bolddriver", Arrays.asList(new String[]{"false","true"}));

        List<HashMap<String,String>> grids = GridSearch.grid_search(multiMap);
        Properties ps = new Properties();
        ps.load(new FileInputStream("E:\\Users\\v-fuz\\tools\\recommender_system\\librec_sourcecode\\librec\\core\\src\\main\\resources\\rec\\"
                + "cf\\rating\\nmf-test.properties"));
        int i = 1;
        for (HashMap<String,String> grid: grids){
            Properties ps_tmp = (Properties) ps.clone();
            for (Map.Entry<String,String> entry: grid.entrySet()){
                if (entry.getKey().equals("regularization")){
                    ps_tmp.setProperty("rec.user.regularization", entry.getValue());
                    ps_tmp.setProperty("rec.item.regularization", entry.getValue());
                }
                else
                    ps_tmp.setProperty(entry.getKey(), entry.getValue());
            }
            FileUtil.writeString(output_dir + "nmf_" + Integer.toString(i) + ".properties", Properties_file_str(ps_tmp));
            i++;
        }
    }

    public static void mfals() throws Exception {
        Multimap<String, String> multiMap = HashMultimap.create();
        multiMap.putAll("rec.iterator.maximum", Arrays.asList(new String[]{"200"}));
        multiMap.putAll("rec.factor.number", Arrays.asList(new String[]{"100"}));
        multiMap.putAll("regularization", Arrays.asList(new String[]{"0.1", "0.05", "0.01"}));

        List<HashMap<String,String>> grids = GridSearch.grid_search(multiMap);
        Properties ps = new Properties();
        ps.load(new FileInputStream("E:\\Users\\v-fuz\\tools\\recommender_system\\librec_sourcecode\\librec\\core\\src\\main\\resources\\rec\\"
                + "cf\\rating\\mfals-test.properties"));
        int i = 1;
        for (HashMap<String,String> grid: grids){
            Properties ps_tmp = (Properties) ps.clone();
            for (Map.Entry<String,String> entry: grid.entrySet()){
                if (entry.getKey().equals("regularization")){
                    ps_tmp.setProperty("rec.user.regularization", entry.getValue());
                    ps_tmp.setProperty("rec.item.regularization", entry.getValue());
                }
                else
                    ps_tmp.setProperty(entry.getKey(), entry.getValue());
            }
            FileUtil.writeString(output_dir + "mfals_" + Integer.toString(i) + ".properties", Properties_file_str(ps_tmp));
            i++;
        }
    }

    public static void llorma() throws Exception {
        Multimap<String, String> multiMap = HashMultimap.create();
        multiMap.putAll("rec.iterator.learnrate", Arrays.asList(new String[]{"0.01", "0.001"}));
        multiMap.putAll("rec.iterator.maximum", Arrays.asList(new String[]{"200"}));
        multiMap.putAll("rec.factor.number", Arrays.asList(new String[]{"100"}));
        multiMap.putAll("rec.llorma.global.factors.num", Arrays.asList(new String[]{"100"}));
        multiMap.putAll("regularization", Arrays.asList(new String[]{"0.1", "0.05", "0.01"}));
        multiMap.putAll("rec.learnrate.bolddriver", Arrays.asList(new String[]{"false","true"}));

        List<HashMap<String,String>> grids = GridSearch.grid_search(multiMap);
        Properties ps = new Properties();
        ps.load(new FileInputStream("E:\\Users\\v-fuz\\tools\\recommender_system\\librec_sourcecode\\librec\\core\\src\\main\\resources\\rec\\"
                + "cf\\rating\\llorma-test.properties"));
        int i = 1;
        for (HashMap<String,String> grid: grids){
            Properties ps_tmp = (Properties) ps.clone();
            for (Map.Entry<String,String> entry: grid.entrySet()){
                if (entry.getKey().equals("regularization")){
                    ps_tmp.setProperty("rec.user.regularization", entry.getValue());
                    ps_tmp.setProperty("rec.item.regularization", entry.getValue());
                }
                else
                    ps_tmp.setProperty(entry.getKey(), entry.getValue());
            }
            FileUtil.writeString(output_dir + "llorma_" + Integer.toString(i) + ".properties", Properties_file_str(ps_tmp));
            i++;
        }
    }

    public static void bpoissmf() throws Exception {
        Multimap<String, String> multiMap = HashMultimap.create();
        multiMap.putAll("rec.iterator.learnrate", Arrays.asList(new String[]{"0.01", "0.001"}));
        multiMap.putAll("rec.iterator.maximum", Arrays.asList(new String[]{"200"}));
        multiMap.putAll("rec.factor.number", Arrays.asList(new String[]{"100"}));
        multiMap.putAll("rec.llorma.global.factors.num", Arrays.asList(new String[]{"100"}));
        multiMap.putAll("regularization", Arrays.asList(new String[]{"0.1", "0.05", "0.01"}));
        multiMap.putAll("rec.learnrate.bolddriver", Arrays.asList(new String[]{"false","true"}));

        List<HashMap<String,String>> grids = GridSearch.grid_search(multiMap);
        Properties ps = new Properties();
        ps.load(new FileInputStream("E:\\Users\\v-fuz\\tools\\recommender_system\\librec_sourcecode\\librec\\core\\src\\main\\resources\\rec\\"
                + "cf\\rating\\bpoissmf-test.properties"));
        int i = 1;
        for (HashMap<String,String> grid: grids){
            Properties ps_tmp = (Properties) ps.clone();
            for (Map.Entry<String,String> entry: grid.entrySet()){
                if (entry.getKey().equals("regularization")){
                    ps_tmp.setProperty("rec.user.regularization", entry.getValue());
                    ps_tmp.setProperty("rec.item.regularization", entry.getValue());
                }
                else
                    ps_tmp.setProperty(entry.getKey(), entry.getValue());
            }
            FileUtil.writeString(output_dir + "bpoissmf_" + Integer.toString(i) + ".properties", Properties_file_str(ps_tmp));
            i++;
        }
    }

    public static void rbm() throws Exception {
        Multimap<String, String> multiMap = HashMultimap.create();
        multiMap.putAll("rec.iterator.maximum", Arrays.asList(new String[]{"50"}));
        multiMap.putAll("rec.factor.number", Arrays.asList(new String[]{"100"}));


        List<HashMap<String,String>> grids = GridSearch.grid_search(multiMap);
        Properties ps = new Properties();
        ps.load(new FileInputStream("E:\\Users\\v-fuz\\tools\\recommender_system\\librec_sourcecode\\librec\\core\\src\\main\\resources\\rec\\"
                + "cf\\rating\\rbm-test.properties"));
        int i = 1;
        for (HashMap<String,String> grid: grids){
            Properties ps_tmp = (Properties) ps.clone();
            for (Map.Entry<String,String> entry: grid.entrySet()){
                if (entry.getKey().equals("regularization")){
                    ps_tmp.setProperty("rec.user.regularization", entry.getValue());
                    ps_tmp.setProperty("rec.item.regularization", entry.getValue());
                }
                else
                    ps_tmp.setProperty(entry.getKey(), entry.getValue());
            }
            FileUtil.writeString(output_dir + "rbm_" + Integer.toString(i) + ".properties", Properties_file_str(ps_tmp));
            i++;
        }
    }



    public static void constantguess() throws Exception {
        Properties ps = new Properties();
        ps.load(new FileInputStream("E:\\Users\\v-fuz\\tools\\recommender_system\\librec_sourcecode\\librec\\core\\src\\main\\resources\\rec\\"
                + "baseline\\constantguess-test.properties"));
        FileUtil.writeString(output_dir + "constantguess.properties", Properties_file_str(ps));
    }

    public static void globalaverage() throws Exception {
        Properties ps = new Properties();
        ps.load(new FileInputStream("E:\\Users\\v-fuz\\tools\\recommender_system\\librec_sourcecode\\librec\\core\\src\\main\\resources\\rec\\"
                + "baseline\\globalaverage-test.properties"));
        FileUtil.writeString(output_dir + "globalaverage.properties", Properties_file_str(ps));
    }

    public static void itemaverage() throws Exception {
        Properties ps = new Properties();
        ps.load(new FileInputStream("E:\\Users\\v-fuz\\tools\\recommender_system\\librec_sourcecode\\librec\\core\\src\\main\\resources\\rec\\"
                + "baseline\\itemaverage-test.properties"));
        FileUtil.writeString(output_dir + "itemaverage.properties", Properties_file_str(ps));
    }

    public static void mostpopular() throws Exception {
        Properties ps = new Properties();
        ps.load(new FileInputStream("E:\\Users\\v-fuz\\tools\\recommender_system\\librec_sourcecode\\librec\\core\\src\\main\\resources\\rec\\"
                + "baseline\\mostpopular-test.properties"));
        FileUtil.writeString(output_dir + "mostpopular.properties", Properties_file_str(ps));
    }

    public static void randomguess() throws Exception {
        Properties ps = new Properties();
        ps.load(new FileInputStream("E:\\Users\\v-fuz\\tools\\recommender_system\\librec_sourcecode\\librec\\core\\src\\main\\resources\\rec\\"
                + "baseline\\randomguess-test.properties"));
        FileUtil.writeString(output_dir + "randomguess.properties", Properties_file_str(ps));
    }

    public static void useraverage() throws Exception {
        Properties ps = new Properties();
        ps.load(new FileInputStream("E:\\Users\\v-fuz\\tools\\recommender_system\\librec_sourcecode\\librec\\core\\src\\main\\resources\\rec\\"
                + "baseline\\useraverage-test.properties"));
        FileUtil.writeString(output_dir + "useraverage.properties", Properties_file_str(ps));
    }

    public static void associationrule() throws Exception {
        Properties ps = new Properties();
        ps.load(new FileInputStream("E:\\Users\\v-fuz\\tools\\recommender_system\\librec_sourcecode\\librec\\core\\src\\main\\resources\\rec\\"
                + "ext\\associationrule-test.properties"));
        FileUtil.writeString(output_dir + "associationrule.properties", Properties_file_str(ps));
    }

    public static void personalitydiagnosis() throws Exception {
        Multimap<String, String> multiMap = HashMultimap.create();
        multiMap.putAll("rec.PersonalityDiagnosis.sigma", Arrays.asList(new String[]{"2.0"}));

        List<HashMap<String,String>> grids = GridSearch.grid_search(multiMap);
        Properties ps = new Properties();
        ps.load(new FileInputStream("E:\\Users\\v-fuz\\tools\\recommender_system\\librec_sourcecode\\librec\\core\\src\\main\\resources\\rec\\"
                + "ext\\personalitydiagnosis-test.properties"));
        int i = 1;
        for (HashMap<String,String> grid: grids){
            Properties ps_tmp = (Properties) ps.clone();
            for (Map.Entry<String,String> entry: grid.entrySet()){
                ps_tmp.setProperty(entry.getKey(), entry.getValue());
            }
            FileUtil.writeString(output_dir + "personalitydiagnosis_" + Integer.toString(i) + ".properties", Properties_file_str(ps_tmp));
            i++;
        }
    }

    public static void prankd() throws Exception {
        Multimap<String, String> multiMap = HashMultimap.create();
        multiMap.putAll("rec.similarity.class", Arrays.asList(new String[]{"cos"}));

        List<HashMap<String,String>> grids = GridSearch.grid_search(multiMap);
        Properties ps = new Properties();
        ps.load(new FileInputStream("E:\\Users\\v-fuz\\tools\\recommender_system\\librec_sourcecode\\librec\\core\\src\\main\\resources\\rec\\"
                + "ext\\prankd-test.properties"));
        int i = 1;
        for (HashMap<String,String> grid: grids){
            Properties ps_tmp = (Properties) ps.clone();
            for (Map.Entry<String,String> entry: grid.entrySet()){
                ps_tmp.setProperty(entry.getKey(), entry.getValue());
            }
            FileUtil.writeString(output_dir + "prankd_" + Integer.toString(i) + ".properties", Properties_file_str(ps_tmp));
            i++;
        }
    }

    public static void slopeone() throws Exception {
        Multimap<String, String> multiMap = HashMultimap.create();
        multiMap.putAll("rec.eval.enable", Arrays.asList(new String[]{"true"}));

        List<HashMap<String,String>> grids = GridSearch.grid_search(multiMap);
        Properties ps = new Properties();
        ps.load(new FileInputStream("E:\\Users\\v-fuz\\tools\\recommender_system\\librec_sourcecode\\librec\\core\\src\\main\\resources\\rec\\"
                + "ext\\slopeone-test.properties"));
        int i = 1;
        for (HashMap<String,String> grid: grids){
            Properties ps_tmp = (Properties) ps.clone();
            for (Map.Entry<String,String> entry: grid.entrySet()){
                ps_tmp.setProperty(entry.getKey(), entry.getValue());
            }
            FileUtil.writeString(output_dir + "slopeone_" + Integer.toString(i) + ".properties", Properties_file_str(ps_tmp));
            i++;
        }
    }




    public static void usercluster() throws Exception {
        Multimap<String, String> multiMap = HashMultimap.create();
        multiMap.putAll("rec.factory.number", Arrays.asList(new String[]{"10", "20"}));
        multiMap.putAll("rec.iterator.maximum", Arrays.asList(new String[]{"20", "30"}));

        List<HashMap<String,String>> grids = GridSearch.grid_search(multiMap);
        Properties ps = new Properties();
        ps.load(new FileInputStream("E:\\Users\\v-fuz\\tools\\recommender_system\\librec_sourcecode\\librec\\core\\src\\main\\resources\\rec\\"
                + "baseline\\usercluster-test.properties"));
        int i = 1;
        for (HashMap<String,String> grid: grids){
            Properties ps_tmp = (Properties) ps.clone();
            for (Map.Entry<String,String> entry: grid.entrySet()){
                ps_tmp.setProperty(entry.getKey(), entry.getValue());
            }
            FileUtil.writeString(output_dir + "usercluster_" + Integer.toString(i) + ".properties", Properties_file_str(ps_tmp));
            i++;
        }
    }

    public static void itemcluster() throws Exception {
        Multimap<String, String> multiMap = HashMultimap.create();
        multiMap.putAll("rec.pgm.number", Arrays.asList(new String[]{"10", "20"}));
        multiMap.putAll("rec.iterator.maximum", Arrays.asList(new String[]{"20", "30"}));

        List<HashMap<String,String>> grids = GridSearch.grid_search(multiMap);
        Properties ps = new Properties();
        ps.load(new FileInputStream("E:\\Users\\v-fuz\\tools\\recommender_system\\librec_sourcecode\\librec\\core\\src\\main\\resources\\rec\\"
                + "baseline\\itemcluster-test.properties"));
        int i = 1;
        for (HashMap<String,String> grid: grids){
            Properties ps_tmp = (Properties) ps.clone();
            for (Map.Entry<String,String> entry: grid.entrySet()){
                ps_tmp.setProperty(entry.getKey(), entry.getValue());
            }
            FileUtil.writeString(output_dir + "itemcluster_" + Integer.toString(i) + ".properties", Properties_file_str(ps_tmp));
            i++;
        }
    }

    public static void bucm() throws Exception {
        Multimap<String, String> multiMap = HashMultimap.create();
        multiMap.putAll("rec.pgm.burnin", Arrays.asList(new String[]{"10", "20"}));
        multiMap.putAll("rec.iterator.maximum", Arrays.asList(new String[]{"200"}));
        multiMap.putAll("rec.pgm.topic.number", Arrays.asList(new String[]{"10","20","50"}));

        List<HashMap<String,String>> grids = GridSearch.grid_search(multiMap);
        Properties ps = new Properties();
        ps.load(new FileInputStream("E:\\Users\\v-fuz\\tools\\recommender_system\\librec_sourcecode\\librec\\core\\src\\main\\resources\\rec\\"
                + "cf\\bucm-test.properties"));
        int i = 1;
        for (HashMap<String,String> grid: grids){
            Properties ps_tmp = (Properties) ps.clone();
            for (Map.Entry<String,String> entry: grid.entrySet()){
                ps_tmp.setProperty(entry.getKey(), entry.getValue());
            }
            FileUtil.writeString(output_dir + "bucm_" + Integer.toString(i) + ".properties", Properties_file_str(ps_tmp));
            i++;
        }
    }

    public static void bhfree() throws Exception {
        Multimap<String, String> multiMap = HashMultimap.create();
        multiMap.putAll("rec.pgm.burnin", Arrays.asList(new String[]{"10", "20"}));
        multiMap.putAll("rec.iterator.maximum", Arrays.asList(new String[]{"200"}));
        multiMap.putAll("topic.number", Arrays.asList(new String[]{"10","20","50"}));

        List<HashMap<String,String>> grids = GridSearch.grid_search(multiMap);
        Properties ps = new Properties();
        ps.load(new FileInputStream("E:\\Users\\v-fuz\\tools\\recommender_system\\librec_sourcecode\\librec\\core\\src\\main\\resources\\rec\\"
                + "cf\\bhfree-test.properties"));
        int i = 1;
        for (HashMap<String,String> grid: grids){
            Properties ps_tmp = (Properties) ps.clone();
            for (Map.Entry<String,String> entry: grid.entrySet()){
                if (entry.getKey().equals("topic.number")){
                    ps_tmp.setProperty("rec.bhfree.user.topic.number", entry.getValue());
                    ps_tmp.setProperty("rec.bhfree.item.topic.number", entry.getValue());
                }
                else
                    ps_tmp.setProperty(entry.getKey(), entry.getValue());
            }
            FileUtil.writeString(output_dir + "bhfree_" + Integer.toString(i) + ".properties", Properties_file_str(ps_tmp));
            i++;
        }
    }

    public static void urp() throws Exception {
        Multimap<String, String> multiMap = HashMultimap.create();
        multiMap.putAll("rec.iteration.learnrate", Arrays.asList(new String[]{"0.01", "0.001"}));
        multiMap.putAll("rec.iterator.maximum", Arrays.asList(new String[]{"20"}));


        List<HashMap<String,String>> grids = GridSearch.grid_search(multiMap);
        Properties ps = new Properties();
        ps.load(new FileInputStream("E:\\Users\\v-fuz\\tools\\recommender_system\\librec_sourcecode\\librec\\core\\src\\main\\resources\\rec\\"
                + "cf\\rating\\urp-test.properties"));
        int i = 1;
        for (HashMap<String,String> grid: grids){
            Properties ps_tmp = (Properties) ps.clone();
            for (Map.Entry<String,String> entry: grid.entrySet()){
                if (entry.getKey().equals("regularization")){
                    ps_tmp.setProperty("rec.user.regularization", entry.getValue());
                    ps_tmp.setProperty("rec.item.regularization", entry.getValue());
                }
                else
                    ps_tmp.setProperty(entry.getKey(), entry.getValue());
            }
            FileUtil.writeString(output_dir + "urp_" + Integer.toString(i) + ".properties", Properties_file_str(ps_tmp));
            i++;
        }
    }

    public static void ldcc() throws Exception {
        Multimap<String, String> multiMap = HashMultimap.create();
        multiMap.putAll("rec.iteration.learnrate", Arrays.asList(new String[]{"0.01", "0.001"}));
        multiMap.putAll("rec.iterator.maximum", Arrays.asList(new String[]{"200"}));


        List<HashMap<String,String>> grids = GridSearch.grid_search(multiMap);
        Properties ps = new Properties();
        ps.load(new FileInputStream("E:\\Users\\v-fuz\\tools\\recommender_system\\librec_sourcecode\\librec\\core\\src\\main\\resources\\rec\\"
                + "cf\\rating\\ldcc-test.properties"));
        int i = 1;
        for (HashMap<String,String> grid: grids){
            Properties ps_tmp = (Properties) ps.clone();
            for (Map.Entry<String,String> entry: grid.entrySet()){
                if (entry.getKey().equals("regularization")){
                    ps_tmp.setProperty("rec.user.regularization", entry.getValue());
                    ps_tmp.setProperty("rec.item.regularization", entry.getValue());
                }
                else
                    ps_tmp.setProperty(entry.getKey(), entry.getValue());
            }
            FileUtil.writeString(output_dir + "ldcc_" + Integer.toString(i) + ".properties", Properties_file_str(ps_tmp));
            i++;
        }
    }

    public static void gplsa() throws Exception {
        Multimap<String, String> multiMap = HashMultimap.create();
        multiMap.putAll("rec.iteration.learnrate", Arrays.asList(new String[]{"0.01", "0.001"}));
        multiMap.putAll("rec.iterator.maximum", Arrays.asList(new String[]{"200"}));
        multiMap.putAll("rec.topic.number", Arrays.asList(new String[]{"10", "50", "100"}));


        List<HashMap<String,String>> grids = GridSearch.grid_search(multiMap);
        Properties ps = new Properties();
        ps.load(new FileInputStream("E:\\Users\\v-fuz\\tools\\recommender_system\\librec_sourcecode\\librec\\core\\src\\main\\resources\\rec\\"
                + "cf\\rating\\gplsa-test.properties"));
        int i = 1;
        for (HashMap<String,String> grid: grids){
            Properties ps_tmp = (Properties) ps.clone();
            for (Map.Entry<String,String> entry: grid.entrySet()){
                if (entry.getKey().equals("regularization")){
                    ps_tmp.setProperty("rec.user.regularization", entry.getValue());
                    ps_tmp.setProperty("rec.item.regularization", entry.getValue());
                }
                else
                    ps_tmp.setProperty(entry.getKey(), entry.getValue());
            }
            FileUtil.writeString(output_dir + "gplsa_" + Integer.toString(i) + ".properties", Properties_file_str(ps_tmp));
            i++;
        }
    }

    public static void bpmf() throws Exception {
        Multimap<String, String> multiMap = HashMultimap.create();
        multiMap.putAll("rec.factor.number", Arrays.asList(new String[]{"100"}));
        multiMap.putAll("rec.iterator.maximum", Arrays.asList(new String[]{"200"}));
        multiMap.putAll("regularization", Arrays.asList(new String[]{"0.01", "0.05", "0.1"}));


        List<HashMap<String,String>> grids = GridSearch.grid_search(multiMap);
        Properties ps = new Properties();
        ps.load(new FileInputStream("E:\\Users\\v-fuz\\tools\\recommender_system\\librec_sourcecode\\librec\\core\\src\\main\\resources\\rec\\"
                + "cf\\rating\\bpmf-test.properties"));
        int i = 1;
        for (HashMap<String,String> grid: grids){
            Properties ps_tmp = (Properties) ps.clone();
            for (Map.Entry<String,String> entry: grid.entrySet()){
                if (entry.getKey().equals("regularization")){
                    ps_tmp.setProperty("rec.user.regularization", entry.getValue());
                    ps_tmp.setProperty("rec.item.regularization", entry.getValue());
                }
                else
                    ps_tmp.setProperty(entry.getKey(), entry.getValue());
            }
            FileUtil.writeString(output_dir + "bpmf_" + Integer.toString(i) + ".properties", Properties_file_str(ps_tmp));
            i++;
        }
    }

    public static void aspectmodelrating() throws Exception {
        Multimap<String, String> multiMap = HashMultimap.create();
        multiMap.putAll("rec.iteration.learnrate", Arrays.asList(new String[]{"0.01", "0.001"}));
        multiMap.putAll("rec.iterator.maximum", Arrays.asList(new String[]{"200"}));

        List<HashMap<String,String>> grids = GridSearch.grid_search(multiMap);
        Properties ps = new Properties();
        ps.load(new FileInputStream("E:\\Users\\v-fuz\\tools\\recommender_system\\librec_sourcecode\\librec\\core\\src\\main\\resources\\rec\\"
                + "cf\\rating\\aspectmodelrating-test.properties"));
        int i = 1;
        for (HashMap<String,String> grid: grids){
            Properties ps_tmp = (Properties) ps.clone();
            for (Map.Entry<String,String> entry: grid.entrySet()){
                if (entry.getKey().equals("regularization")){
                    ps_tmp.setProperty("rec.user.regularization", entry.getValue());
                    ps_tmp.setProperty("rec.item.regularization", entry.getValue());
                }
                else
                    ps_tmp.setProperty(entry.getKey(), entry.getValue());
            }
            FileUtil.writeString(output_dir + "aspectmodelrating_" + Integer.toString(i) + ".properties", Properties_file_str(ps_tmp));
            i++;
        }
    }
}
