package net.librec.run;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.util.*;

/**
 * Created by fuzzhang on 3/21/2017.
 */
public class GridSearch {
    public static void main(String[] args) {
//        List<List<String>> param = new ArrayList<List<String>>() {{
//            add(Arrays.asList("A", "B"));
//            add(Arrays.asList("C", "D"));
//            add(Arrays.asList("E", "F"));
//        }};
//        for (List<String> l : getCross(param)) {
//            System.out.println(l);
//        }
        Multimap<String, String> multiMap = HashMultimap.create();
        multiMap.putAll("rec.iterator.learnrate", Arrays.asList(new String[]{"0.1", "0.01"}));
        multiMap.putAll("rec.user.regularization", Arrays.asList(new String[]{"0.2", "0.02"}));
        multiMap.putAll("rec.user.ll", Arrays.asList(new String[]{"a", "b", "c"}));
        List<HashMap<String,String>> grids = grid_search(multiMap);
    }

    public static List<HashMap<String,String>> grid_search(Multimap<String, String> multiMap){
        List<List<String>> param = new ArrayList<List<String>>();

        Set<String> keys = multiMap.keySet();
        for (String key : keys) {
            //System.out.println("Key = " + key);
            Collection<String> values = multiMap.get(key);
            param.add(new ArrayList<>(values));
        }

        List<HashMap<String,String>> grids = new ArrayList<>();

        for (List<String> l: getCross(param)){
            HashMap<String,String> map = new HashMap<>();
            int index = 0;
            for (String key : keys){
                map.put(key, l.get(index));
                index++;
            }
            grids.add(map);
        }
        return grids;
    }


    public static <T> List<List<T>> getCross(List<List<T>> values) {
        List<List<T>> accumulator = new ArrayList<List<T>>();
        if (values.size() != 0) {
            List<T> comb = new ArrayList<T>();
            comb.addAll(Collections.<T>nCopies(values.size(), null));
            getCross(accumulator, 0, comb, values);
        }
        return accumulator;
    }

    private static <T> void getCross(List<List<T>> accumulator, int idx, List<T> combination, List<List<T>> param) {
        if (idx == combination.size()) {
            accumulator.add(new ArrayList<T>(combination));
        } else {
            for(T t : param.get(idx)) {
                combination.set(idx, t);
                getCross(accumulator, idx + 1, combination, param);
            }
        }
    }
}
