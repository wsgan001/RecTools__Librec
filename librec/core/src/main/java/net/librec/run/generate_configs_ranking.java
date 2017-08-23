package net.librec.run;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.librec.util.FileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by fuzzhang on 3/20/2017.
 */
public class generate_configs_ranking {



    public static void main(String[] args) throws Exception {
        Files.walk(Paths.get(Params.configOutputDir))
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
//        associationrule();
//        prankd();
//        personalitydiagnosis();
//        slopeone();



        /***
         * Non-Bayesian Methods
         */
//        user_knn();
//        item_knn();
        bpr();
//        aobpr();
//        climf();
//        eals();
//        fismauc();
//        fismrmse();
//        gbpr();
//        listwisemf();
//        rankals();
//        ranksgd();
//        slim();
//        wbpr();
//        wrmf();

        /***
         * Bayesian Methods
         */
//        usercluster();
//        itemcluster();
//        bucm();
//        bhfree();
//        aspectmodelranking();
//        itembigram();
//        lda();
//        plsa();

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
        ps.load(new FileInputStream(Params.modelTemplateDir
                + "cf\\userknn-testranking.properties"));
        int i = 1;
        for (HashMap<String,String> grid: grids){
            Properties ps_tmp = (Properties) ps.clone();
            for (Map.Entry<String,String> entry: grid.entrySet()){
                ps_tmp.setProperty(entry.getKey(), entry.getValue());
            }
            FileUtil.writeString(Params.configOutputDir + "userknn_" + Integer.toString(i) + ".properties", Properties_file_str(ps_tmp));
            i++;
        }
    }

    public static void item_knn() throws Exception {
        Multimap<String, String> multiMap = HashMultimap.create();
        multiMap.putAll("rec.similarity.class", Arrays.asList(new String[]{"bcos", "cos", "cpc", "msesim", "msd", "pcc", "krcc", "dice", "jaccard", "exjaccard"}));
        List<HashMap<String,String>> grids = GridSearch.grid_search(multiMap);
        Properties ps = new Properties();
        ps.load(new FileInputStream(Params.modelTemplateDir
                + "cf\\itemknn-testranking.properties"));
        int i = 1;
        for (HashMap<String,String> grid: grids){
            Properties ps_tmp = (Properties) ps.clone();
            for (Map.Entry<String,String> entry: grid.entrySet()){
                ps_tmp.setProperty(entry.getKey(), entry.getValue());
            }
            FileUtil.writeString(Params.configOutputDir + "itemknn_" + Integer.toString(i) + ".properties", Properties_file_str(ps_tmp));
            i++;
        }
    }

    public static void bpr() throws Exception {
        Multimap<String, String> multiMap = HashMultimap.create();
        multiMap.putAll("rec.iterator.learnrate", Arrays.asList(new String[]{"0.01"}));
        multiMap.putAll("rec.iterator.maximum", Arrays.asList(new String[]{"100"}));
        multiMap.putAll("regularization", Arrays.asList(new String[]{"0.00"}));
        multiMap.putAll("rec.factor.number", Arrays.asList(new String[]{"100"}));
        multiMap.putAll("rec.learningrate.bolddriver", Arrays.asList(new String[]{"false"}));
        multiMap.putAll("rec.learningrate.decay", Arrays.asList(new String[]{"1.0"}));

        List<HashMap<String,String>> grids = GridSearch.grid_search(multiMap);
        Properties ps = new Properties();
        ps.load(new FileInputStream(Params.modelTemplateDir
                + "cf\\ranking\\bpr-test.properties"));
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
            FileUtil.writeString(Params.configOutputDir + "bpr_" + Integer.toString(i) + ".properties", Properties_file_str(ps_tmp));
            i++;
        }
    }

    public static void aobpr() throws Exception {
        Multimap<String, String> multiMap = HashMultimap.create();
        multiMap.putAll("rec.iterator.learnrate", Arrays.asList(new String[]{"0.01"}));
        multiMap.putAll("rec.iterator.maximum", Arrays.asList(new String[]{"200"}));
        multiMap.putAll("regularization", Arrays.asList(new String[]{"0.01"}));
        multiMap.putAll("rec.factor.number", Arrays.asList(new String[]{"10"}));
        multiMap.putAll("rec.learnRate.bolddriver", Arrays.asList(new String[]{"false"}));

        List<HashMap<String,String>> grids = GridSearch.grid_search(multiMap);
        Properties ps = new Properties();
        ps.load(new FileInputStream(Params.modelTemplateDir
                + "cf\\ranking\\aobpr-test.properties"));
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
            FileUtil.writeString(Params.configOutputDir + "aobpr_" + Integer.toString(i) + ".properties", Properties_file_str(ps_tmp));
            i++;
        }
    }

    public static void climf() throws Exception {
        Multimap<String, String> multiMap = HashMultimap.create();
        multiMap.putAll("rec.iterator.learnrate", Arrays.asList(new String[]{"0.01"}));
        multiMap.putAll("rec.iterator.maximum", Arrays.asList(new String[]{"200"}));
        multiMap.putAll("regularization", Arrays.asList(new String[]{"0.1"}));
        multiMap.putAll("rec.factor.number", Arrays.asList(new String[]{"10"}));
        multiMap.putAll("rec.learnRate.bolddriver", Arrays.asList(new String[]{"false"}));

        List<HashMap<String,String>> grids = GridSearch.grid_search(multiMap);
        Properties ps = new Properties();
        ps.load(new FileInputStream(Params.modelTemplateDir
                + "cf\\ranking\\climf-test.properties"));
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
            FileUtil.writeString(Params.configOutputDir + "climf_" + Integer.toString(i) + ".properties", Properties_file_str(ps_tmp));
            i++;
        }
    }

    public static void eals() throws Exception {
        Multimap<String, String> multiMap = HashMultimap.create();

        multiMap.putAll("rec.iterator.maximum", Arrays.asList(new String[]{"200"}));
        multiMap.putAll("regularization", Arrays.asList(new String[]{"0.1"}));
        multiMap.putAll("rec.factor.number", Arrays.asList(new String[]{"10"}));
        multiMap.putAll("rec.eals.wrmf.judge", Arrays.asList(new String[]{"1"}));
        multiMap.putAll("rec.eals.ratio", Arrays.asList(new String[]{"0.4","0.6"}));

        List<HashMap<String,String>> grids = GridSearch.grid_search(multiMap);
        Properties ps = new Properties();
        ps.load(new FileInputStream(Params.modelTemplateDir
                + "cf\\ranking\\eals-test.properties"));
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
            FileUtil.writeString(Params.configOutputDir + "eals_" + Integer.toString(i) + ".properties", Properties_file_str(ps_tmp));
            i++;
        }
    }

    public static void fismauc() throws Exception {
        Multimap<String, String> multiMap = HashMultimap.create();
        multiMap.putAll("rec.iterator.learnrate", Arrays.asList(new String[]{"0.01"}));
        multiMap.putAll("rec.iterator.maximum", Arrays.asList(new String[]{"100"}));
        multiMap.putAll("regularization", Arrays.asList(new String[]{"0.1"}));
        multiMap.putAll("rec.factor.number", Arrays.asList(new String[]{"10"}));
        multiMap.putAll("rec.learnRate.bolddriver", Arrays.asList(new String[]{"false"}));


        List<HashMap<String,String>> grids = GridSearch.grid_search(multiMap);
        Properties ps = new Properties();
        ps.load(new FileInputStream(Params.modelTemplateDir
                + "cf\\ranking\\fismauc-test.properties"));
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
            FileUtil.writeString(Params.configOutputDir + "fismauc_" + Integer.toString(i) + ".properties", Properties_file_str(ps_tmp));
            i++;
        }
    }

    public static void fismrmse() throws Exception {
        Multimap<String, String> multiMap = HashMultimap.create();
        multiMap.putAll("rec.iterator.learnrate", Arrays.asList(new String[]{"0.01"}));
        multiMap.putAll("rec.iterator.maximum", Arrays.asList(new String[]{"200"}));
        multiMap.putAll("regularization", Arrays.asList(new String[]{"0.1"}));
        multiMap.putAll("rec.factor.number", Arrays.asList(new String[]{"10"}));
        multiMap.putAll("rec.learnRate.bolddriver", Arrays.asList(new String[]{"false"}));


        List<HashMap<String,String>> grids = GridSearch.grid_search(multiMap);
        Properties ps = new Properties();
        ps.load(new FileInputStream(Params.modelTemplateDir
                + "cf\\ranking\\fismrmse-test.properties"));
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
            FileUtil.writeString(Params.configOutputDir + "fismrmse_" + Integer.toString(i) + ".properties", Properties_file_str(ps_tmp));
            i++;
        }
    }

    public static void gbpr() throws Exception {
        Multimap<String, String> multiMap = HashMultimap.create();
        multiMap.putAll("rec.iterator.learnrate", Arrays.asList(new String[]{"0.1"}));
        multiMap.putAll("rec.iterator.maximum", Arrays.asList(new String[]{"200"}));
        multiMap.putAll("regularization", Arrays.asList(new String[]{"0.1"}));
        multiMap.putAll("rec.factor.number", Arrays.asList(new String[]{"10"}));
        multiMap.putAll("rec.learnRate.bolddriver", Arrays.asList(new String[]{"false"}));


        List<HashMap<String,String>> grids = GridSearch.grid_search(multiMap);
        Properties ps = new Properties();
        ps.load(new FileInputStream(Params.modelTemplateDir
                + "cf\\ranking\\gbpr-test.properties"));
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
            FileUtil.writeString(Params.configOutputDir + "gbpr_" + Integer.toString(i) + ".properties", Properties_file_str(ps_tmp));
            i++;
        }
    }

    public static void listwisemf() throws Exception {
        Multimap<String, String> multiMap = HashMultimap.create();
        multiMap.putAll("rec.iterator.learnrate", Arrays.asList(new String[]{"0.1"}));
        multiMap.putAll("rec.iterator.maximum", Arrays.asList(new String[]{"10"}));
        multiMap.putAll("regularization", Arrays.asList(new String[]{"0.1"}));
        multiMap.putAll("rec.factor.number", Arrays.asList(new String[]{"10"}));
        multiMap.putAll("rec.learnrate.bolddriver", Arrays.asList(new String[]{"false"}));


        List<HashMap<String,String>> grids = GridSearch.grid_search(multiMap);
        Properties ps = new Properties();
        ps.load(new FileInputStream(Params.modelTemplateDir
                + "cf\\ranking\\listwisemf-test.properties"));
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
            FileUtil.writeString(Params.configOutputDir + "listwisemf_" + Integer.toString(i) + ".properties", Properties_file_str(ps_tmp));
            i++;
        }
    }

    public static void rankals() throws Exception {
        Multimap<String, String> multiMap = HashMultimap.create();
        multiMap.putAll("rec.iterator.maximum", Arrays.asList(new String[]{"15"}));
        multiMap.putAll("regularization", Arrays.asList(new String[]{"0.1"}));
        multiMap.putAll("rec.factor.number", Arrays.asList(new String[]{"10"}));


        List<HashMap<String,String>> grids = GridSearch.grid_search(multiMap);
        Properties ps = new Properties();
        ps.load(new FileInputStream(Params.modelTemplateDir
                + "cf\\ranking\\rankals-test.properties"));
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
            FileUtil.writeString(Params.configOutputDir + "rankals_" + Integer.toString(i) + ".properties", Properties_file_str(ps_tmp));
            i++;
        }
    }

    public static void ranksgd() throws Exception {
        Multimap<String, String> multiMap = HashMultimap.create();
        multiMap.putAll("rec.iterator.learnrate", Arrays.asList(new String[]{"0.01"}));
        multiMap.putAll("rec.iterator.maximum", Arrays.asList(new String[]{"200"}));
        multiMap.putAll("regularization", Arrays.asList(new String[]{"0.1"}));
        multiMap.putAll("rec.factor.number", Arrays.asList(new String[]{"10"}));
        multiMap.putAll("rec.learnRate.bolddriver", Arrays.asList(new String[]{"true"}));


        List<HashMap<String,String>> grids = GridSearch.grid_search(multiMap);
        Properties ps = new Properties();
        ps.load(new FileInputStream(Params.modelTemplateDir
                + "cf\\ranking\\ranksgd-test.properties"));
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
            FileUtil.writeString(Params.configOutputDir + "ranksgd_" + Integer.toString(i) + ".properties", Properties_file_str(ps_tmp));
            i++;
        }
    }

    public static void slim() throws Exception {
        Multimap<String, String> multiMap = HashMultimap.create();
        multiMap.putAll("rec.neighbors.knn.number", Arrays.asList(new String[]{"50", "80"}));
        multiMap.putAll("rec.iterator.maximum", Arrays.asList(new String[]{"100"}));


        List<HashMap<String,String>> grids = GridSearch.grid_search(multiMap);
        Properties ps = new Properties();
        ps.load(new FileInputStream(Params.modelTemplateDir
                + "cf\\ranking\\slim-test.properties"));
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
            FileUtil.writeString(Params.configOutputDir + "slim_" + Integer.toString(i) + ".properties", Properties_file_str(ps_tmp));
            i++;
        }
    }

    public static void wbpr() throws Exception {
        Multimap<String, String> multiMap = HashMultimap.create();
        multiMap.putAll("rec.iterator.learnrate", Arrays.asList(new String[]{"0.1"}));
        multiMap.putAll("rec.iterator.maximum", Arrays.asList(new String[]{"100"}));
        multiMap.putAll("regularization", Arrays.asList(new String[]{"0.1"}));
        multiMap.putAll("rec.factor.number", Arrays.asList(new String[]{"10"}));
        multiMap.putAll("rec.learnRate.bolddriver", Arrays.asList(new String[]{"true"}));


        List<HashMap<String,String>> grids = GridSearch.grid_search(multiMap);
        Properties ps = new Properties();
        ps.load(new FileInputStream(Params.modelTemplateDir
                + "cf\\ranking\\wbpr-test.properties"));
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
            FileUtil.writeString(Params.configOutputDir + "wbpr_" + Integer.toString(i) + ".properties", Properties_file_str(ps_tmp));
            i++;
        }
    }

    public static void wrmf() throws Exception {
        Multimap<String, String> multiMap = HashMultimap.create();
        multiMap.putAll("rec.iterator.learnrate", Arrays.asList(new String[]{"0.01"}));
        multiMap.putAll("rec.iterator.maximum", Arrays.asList(new String[]{"100"}));
        multiMap.putAll("regularization", Arrays.asList(new String[]{"0.1"}));
        multiMap.putAll("rec.factor.number", Arrays.asList(new String[]{"10"}));
        multiMap.putAll("rec.wrmf.weight.coefficient", Arrays.asList(new String[]{"4"}));


        List<HashMap<String,String>> grids = GridSearch.grid_search(multiMap);
        Properties ps = new Properties();
        ps.load(new FileInputStream(Params.modelTemplateDir
                + "cf\\ranking\\wrmf-test.properties"));
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
            FileUtil.writeString(Params.configOutputDir + "wrmf_" + Integer.toString(i) + ".properties", Properties_file_str(ps_tmp));
            i++;
        }
    }


    public static void constantguess() throws Exception {
        Properties ps = new Properties();
        ps.load(new FileInputStream(Params.modelTemplateDir
                + "baseline\\constantguess-test.properties"));
        FileUtil.writeString(Params.configOutputDir + "constantguess.properties", Properties_file_str(ps));
    }

    public static void globalaverage() throws Exception {
        Properties ps = new Properties();
        ps.load(new FileInputStream(Params.modelTemplateDir
                + "baseline\\globalaverage-test.properties"));
        FileUtil.writeString(Params.configOutputDir + "globalaverage.properties", Properties_file_str(ps));
    }

    public static void itemaverage() throws Exception {
        Properties ps = new Properties();
        ps.load(new FileInputStream(Params.modelTemplateDir
                + "baseline\\itemaverage-test.properties"));
        FileUtil.writeString(Params.configOutputDir + "itemaverage.properties", Properties_file_str(ps));
    }

    public static void mostpopular() throws Exception {
        Properties ps = new Properties();
        ps.load(new FileInputStream(Params.modelTemplateDir
                + "baseline\\mostpopular-test.properties"));
        FileUtil.writeString(Params.configOutputDir + "mostpopular.properties", Properties_file_str(ps));
    }

    public static void randomguess() throws Exception {
        Properties ps = new Properties();
        ps.load(new FileInputStream(Params.modelTemplateDir
                + "baseline\\randomguess-test.properties"));
        FileUtil.writeString(Params.configOutputDir + "randomguess.properties", Properties_file_str(ps));
    }

    public static void useraverage() throws Exception {
        Properties ps = new Properties();
        ps.load(new FileInputStream(Params.modelTemplateDir
                + "baseline\\useraverage-test.properties"));
        FileUtil.writeString(Params.configOutputDir + "useraverage.properties", Properties_file_str(ps));
    }

    public static void associationrule() throws Exception {
        Properties ps = new Properties();
        ps.load(new FileInputStream(Params.modelTemplateDir
                + "ext\\associationrule-test.properties"));
        FileUtil.writeString(Params.configOutputDir + "associationrule.properties", Properties_file_str(ps));
    }

    public static void personalitydiagnosis() throws Exception {
        Multimap<String, String> multiMap = HashMultimap.create();
        multiMap.putAll("rec.PersonalityDiagnosis.sigma", Arrays.asList(new String[]{"2.0"}));

        List<HashMap<String,String>> grids = GridSearch.grid_search(multiMap);
        Properties ps = new Properties();
        ps.load(new FileInputStream(Params.modelTemplateDir
                + "ext\\personalitydiagnosis-test.properties"));
        int i = 1;
        for (HashMap<String,String> grid: grids){
            Properties ps_tmp = (Properties) ps.clone();
            for (Map.Entry<String,String> entry: grid.entrySet()){
                ps_tmp.setProperty(entry.getKey(), entry.getValue());
            }
            FileUtil.writeString(Params.configOutputDir + "personalitydiagnosis_" + Integer.toString(i) + ".properties", Properties_file_str(ps_tmp));
            i++;
        }
    }

    public static void prankd() throws Exception {
        Multimap<String, String> multiMap = HashMultimap.create();
        multiMap.putAll("rec.similarity.class", Arrays.asList(new String[]{"cos"}));

        List<HashMap<String,String>> grids = GridSearch.grid_search(multiMap);
        Properties ps = new Properties();
        ps.load(new FileInputStream(Params.modelTemplateDir
                + "ext\\prankd-test.properties"));
        int i = 1;
        for (HashMap<String,String> grid: grids){
            Properties ps_tmp = (Properties) ps.clone();
            for (Map.Entry<String,String> entry: grid.entrySet()){
                ps_tmp.setProperty(entry.getKey(), entry.getValue());
            }
            FileUtil.writeString(Params.configOutputDir + "prankd_" + Integer.toString(i) + ".properties", Properties_file_str(ps_tmp));
            i++;
        }
    }

    public static void slopeone() throws Exception {
        Multimap<String, String> multiMap = HashMultimap.create();
        multiMap.putAll("rec.eval.enable", Arrays.asList(new String[]{"true"}));

        List<HashMap<String,String>> grids = GridSearch.grid_search(multiMap);
        Properties ps = new Properties();
        ps.load(new FileInputStream(Params.modelTemplateDir
                + "ext\\slopeone-test.properties"));
        int i = 1;
        for (HashMap<String,String> grid: grids){
            Properties ps_tmp = (Properties) ps.clone();
            for (Map.Entry<String,String> entry: grid.entrySet()){
                ps_tmp.setProperty(entry.getKey(), entry.getValue());
            }
            FileUtil.writeString(Params.configOutputDir + "slopeone_" + Integer.toString(i) + ".properties", Properties_file_str(ps_tmp));
            i++;
        }
    }



    public static void usercluster() throws Exception {
        Multimap<String, String> multiMap = HashMultimap.create();
        multiMap.putAll("rec.factory.number", Arrays.asList(new String[]{"10", "20"}));
        multiMap.putAll("rec.iterator.maximum", Arrays.asList(new String[]{"20", "30"}));

        List<HashMap<String,String>> grids = GridSearch.grid_search(multiMap);
        Properties ps = new Properties();
        ps.load(new FileInputStream(Params.modelTemplateDir
                + "baseline\\usercluster-test.properties"));
        int i = 1;
        for (HashMap<String,String> grid: grids){
            Properties ps_tmp = (Properties) ps.clone();
            for (Map.Entry<String,String> entry: grid.entrySet()){
                ps_tmp.setProperty(entry.getKey(), entry.getValue());
            }
            FileUtil.writeString(Params.configOutputDir + "usercluster_" + Integer.toString(i) + ".properties", Properties_file_str(ps_tmp));
            i++;
        }
    }

    public static void itemcluster() throws Exception {
        Multimap<String, String> multiMap = HashMultimap.create();
        multiMap.putAll("rec.pgm.number", Arrays.asList(new String[]{"10", "20"}));
        multiMap.putAll("rec.iterator.maximum", Arrays.asList(new String[]{"20", "30"}));

        List<HashMap<String,String>> grids = GridSearch.grid_search(multiMap);
        Properties ps = new Properties();
        ps.load(new FileInputStream(Params.modelTemplateDir
                + "baseline\\itemcluster-test.properties"));
        int i = 1;
        for (HashMap<String,String> grid: grids){
            Properties ps_tmp = (Properties) ps.clone();
            for (Map.Entry<String,String> entry: grid.entrySet()){
                ps_tmp.setProperty(entry.getKey(), entry.getValue());
            }
            FileUtil.writeString(Params.configOutputDir + "itemcluster_" + Integer.toString(i) + ".properties", Properties_file_str(ps_tmp));
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
        ps.load(new FileInputStream(Params.modelTemplateDir
                + "cf\\bucm-test.properties"));
        int i = 1;
        for (HashMap<String,String> grid: grids){
            Properties ps_tmp = (Properties) ps.clone();
            for (Map.Entry<String,String> entry: grid.entrySet()){
                ps_tmp.setProperty(entry.getKey(), entry.getValue());
            }
            FileUtil.writeString(Params.configOutputDir + "bucm_" + Integer.toString(i) + ".properties", Properties_file_str(ps_tmp));
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
        ps.load(new FileInputStream(Params.modelTemplateDir
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
            FileUtil.writeString(Params.configOutputDir + "bhfree_" + Integer.toString(i) + ".properties", Properties_file_str(ps_tmp));
            i++;
        }
    }

    public static void aspectmodelranking() throws Exception {
        Multimap<String, String> multiMap = HashMultimap.create();

        multiMap.putAll("rec.iterator.maximum", Arrays.asList(new String[]{"200"}));
        multiMap.putAll("rec.pgm.burnin", Arrays.asList(new String[]{"10", "20", "30"}));
        multiMap.putAll("rec.topic.number", Arrays.asList(new String[]{"10", "20"}));


        List<HashMap<String,String>> grids = GridSearch.grid_search(multiMap);
        Properties ps = new Properties();
        ps.load(new FileInputStream(Params.modelTemplateDir
                + "cf\\ranking\\aspectmodelranking-test.properties"));
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
            FileUtil.writeString(Params.configOutputDir + "aspectmodelranking_" + Integer.toString(i) + ".properties", Properties_file_str(ps_tmp));
            i++;
        }
    }

    public static void itembigram() throws Exception {
        Multimap<String, String> multiMap = HashMultimap.create();

        multiMap.putAll("rec.iterator.maximum", Arrays.asList(new String[]{"200"}));
        multiMap.putAll("rec.pgm.burnin", Arrays.asList(new String[]{"10", "20", "30"}));
        multiMap.putAll("rec.topic.number", Arrays.asList(new String[]{"10", "20"}));


        List<HashMap<String,String>> grids = GridSearch.grid_search(multiMap);
        Properties ps = new Properties();
        ps.load(new FileInputStream(Params.modelTemplateDir
                + "cf\\ranking\\itembigram-test.properties"));
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
            FileUtil.writeString(Params.configOutputDir + "itembigram_" + Integer.toString(i) + ".properties", Properties_file_str(ps_tmp));
            i++;
        }
    }

    public static void lda() throws Exception {
        Multimap<String, String> multiMap = HashMultimap.create();

        multiMap.putAll("rec.iterator.maximum", Arrays.asList(new String[]{"200"}));
        multiMap.putAll("rec.pgm.burnin", Arrays.asList(new String[]{"10", "20", "30"}));
        multiMap.putAll("rec.topic.number", Arrays.asList(new String[]{"10", "20", "50"}));


        List<HashMap<String,String>> grids = GridSearch.grid_search(multiMap);
        Properties ps = new Properties();
        ps.load(new FileInputStream(Params.modelTemplateDir
                + "cf\\ranking\\lda-test.properties"));
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
            FileUtil.writeString(Params.configOutputDir + "lda_" + Integer.toString(i) + ".properties", Properties_file_str(ps_tmp));
            i++;
        }
    }

    public static void plsa() throws Exception {
        Multimap<String, String> multiMap = HashMultimap.create();

        multiMap.putAll("rec.iteration.learnrate", Arrays.asList(new String[]{"0.01","0.001"}));
        multiMap.putAll("rec.iterator.maximum", Arrays.asList(new String[]{"200"}));
        multiMap.putAll("rec.topic.number", Arrays.asList(new String[]{"10", "20", "50"}));


        List<HashMap<String,String>> grids = GridSearch.grid_search(multiMap);
        Properties ps = new Properties();
        ps.load(new FileInputStream(Params.modelTemplateDir
                + "cf\\ranking\\plsa-test.properties"));
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
            FileUtil.writeString(Params.configOutputDir + "plsa_" + Integer.toString(i) + ".properties", Properties_file_str(ps_tmp));
            i++;
        }
    }
}
