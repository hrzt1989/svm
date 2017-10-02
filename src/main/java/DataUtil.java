import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

/**
 * Created by ZhangTao on 2017/9/4.
 */
public class DataUtil {
    public static Map<Integer,Map<String, Integer>> dispersedMap = new HashMap<Integer,Map<String, Integer>>();
    public static Map<Integer, List<Float>> continueMap = new HashMap<Integer, List<Float>>();
    static {
        Map<String, Integer> workclassMap = new HashMap<String, Integer>();
        Map<String, Integer> educationMap = new HashMap<String, Integer>();
        Map<String, Integer> marital_statusMap = new HashMap<String, Integer>();
        Map<String, Integer> occupationMap = new HashMap<String, Integer>();
        Map<String, Integer> relationshipMap = new HashMap<String, Integer>();
        Map<String, Integer> raceMap = new HashMap<String, Integer>();
        Map<String, Integer> sexMap = new HashMap<String, Integer>();
        Map<String, Integer> native_countryMap = new HashMap<String, Integer>();

        workclassMap.put("Private", 0);
        workclassMap.put("Self-emp-not-inc", 1);
        workclassMap.put("Self-emp-inc", 2);
        workclassMap.put("Federal-gov", 3);
        workclassMap.put("Local-gov", 4);
        workclassMap.put("State-gov", 5);
        workclassMap.put("Without-pay", 6);
        workclassMap.put("Never-worked", 7);
        dispersedMap.put(1, workclassMap);

        educationMap.put("Bachelors", 0);
        educationMap.put("Some-college", 1);
        educationMap.put("11th", 2);
        educationMap.put("HS-grad", 3);
        educationMap.put("Prof-school", 4);
        educationMap.put("Assoc-acdm", 5);
        educationMap.put("Assoc-voc", 6);
        educationMap.put("9th", 7);
        educationMap.put("7th-8th", 8);
        educationMap.put("12th", 9);
        educationMap.put("Masters", 10);
        educationMap.put("1st-4th", 11);
        educationMap.put("10th", 12);
        educationMap.put("Doctorate", 13);
        educationMap.put("5th-6th", 14);
        educationMap.put("Preschool", 15);
        dispersedMap.put(3, educationMap);

        marital_statusMap.put("Married-civ-spouse", 0);
        marital_statusMap.put("Divorced", 1);
        marital_statusMap.put("Never-married", 2);
        marital_statusMap.put("Separated", 3);
        marital_statusMap.put("Widowed", 4);
        marital_statusMap.put("Married-spouse-absent", 5);
        marital_statusMap.put("Married-AF-spouse", 6);
        dispersedMap.put(5, marital_statusMap);

        occupationMap.put("Tech-support", 0);
        occupationMap.put("Craft-repair", 1);
        occupationMap.put("Other-service", 2);
        occupationMap.put("Sales", 3);
        occupationMap.put("Exec-managerial", 4);
        occupationMap.put("Prof-specialty", 5);
        occupationMap.put("Handlers-cleaners", 6);
        occupationMap.put("Machine-op-inspct", 7);
        occupationMap.put("Adm-clerical", 8);
        occupationMap.put("Farming-fishing", 9);
        occupationMap.put("Transport-moving", 10);
        occupationMap.put("Priv-house-serv", 11);
        occupationMap.put("Protective-serv", 12);
        occupationMap.put("Armed-Forces", 13);
        dispersedMap.put(6, occupationMap);

        relationshipMap.put("Wife", 0);
        relationshipMap.put("Own-child", 1);
        relationshipMap.put("Husband", 2);
        relationshipMap.put("Not-in-family", 3);
        relationshipMap.put("Other-relative", 4);
        relationshipMap.put("Unmarried", 5);
        dispersedMap.put(7, relationshipMap);

        raceMap.put("White", 0);
        raceMap.put("Asian-Pac-Islander", 1);
        raceMap.put("Amer-Indian-Eskimo", 2);
        raceMap.put("Other", 3);
        raceMap.put("Black", 4);
        dispersedMap.put(8, raceMap);

        sexMap.put("Female", 0);
        sexMap.put("Male", 1);
        dispersedMap.put(9, sexMap);

        native_countryMap.put("United-States", 0);
        native_countryMap.put("Cambodia", 1);
        native_countryMap.put("England", 2);
        native_countryMap.put("Puerto-Rico", 3);
        native_countryMap.put("Canada", 4);
        native_countryMap.put("Germany", 5);
        native_countryMap.put("Outlying-US(Guam-USVI-etc)", 6);
        native_countryMap.put("India", 7);
        native_countryMap.put("Japan", 8);
        native_countryMap.put("Greece", 9);
        native_countryMap.put("South", 10);
        native_countryMap.put("China", 11);
        native_countryMap.put("Cuba", 12);
        native_countryMap.put("Iran", 13);
        native_countryMap.put("Honduras", 14);
        native_countryMap.put("Philippines", 15);
        native_countryMap.put("Italy", 16);
        native_countryMap.put("Poland", 17);
        native_countryMap.put("Jamaica", 18);
        native_countryMap.put("Vietnam", 19);
        native_countryMap.put("Mexico", 20);
        native_countryMap.put("Portugal", 21);
        native_countryMap.put("Ireland", 22);
        native_countryMap.put("France", 23);
        native_countryMap.put("Dominican-Republic", 24);
        native_countryMap.put("Laos", 25);
        native_countryMap.put("Ecuador", 26);
        native_countryMap.put("Taiwan", 27);
        native_countryMap.put("Haiti", 28);
        native_countryMap.put("Columbia", 29);
        native_countryMap.put("Hungary", 30);
        native_countryMap.put("Guatemala", 31);
        native_countryMap.put("Nicaragua", 32);
        native_countryMap.put("Scotland", 33);
        native_countryMap.put("Thailand", 34);
        native_countryMap.put("Yugoslavia", 35);
        native_countryMap.put("El-Salvador", 36);
        native_countryMap.put("Trinadad&Tobago", 37);
        native_countryMap.put("Peru", 38);
        native_countryMap.put("Hong", 39);
        native_countryMap.put("Holand-Netherlands", 40);
        dispersedMap.put(13, native_countryMap);
    }
    public static Map<String, Object> readerData(String filePath, boolean build){
        File file = new File(filePath);
        BufferedReader reader = null;
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            reader = new BufferedReader(new FileReader(file));
            String strBuf = null;
            List<String[]> strList = new LinkedList<String[]>();
            List<String> lableList = new LinkedList<String>();
            while((strBuf = reader.readLine()) != null){
                String []strArray = strBuf.split(", ");
                if(strArray.length == 15){
                    strList.add(strArray);
                    lableList.add(strArray[14]);
                }
            }
            reader.close();
            if(build) {
                statsContinueMaxAndMini(strList);
            }
            float [][]inputData = new float[strList.size()][135];
            float []lables = new float[strList.size()];
            Arrays.fill(lables, 0f);
            int i = 0;
            for(String []strArray : strList){
                Arrays.fill(inputData[i], 0f);
                int d = 0;
                int k = 0;
                for(int j = 0; j < 14; ++j){
  //                  System.out.println(strArray[j]);
                    if(inContinue(j)){
                        d = 6;
                        if(!"?".equals(strArray[j])){
                            Float df = Float.parseFloat(strArray[j]);
                            inputData[i][k + discretization(df, j)] = 1f;
                        }
                    } else {
                        Map<String, Integer> dMap = dispersedMap.get(j);
                        d = dMap.size();
                        if(!"?".equals(strArray[j])){
                            inputData[i][k + dMap.get(strArray[j])] = 1f;
                        }
                    }
                    k += d;
                }
                if(!"?".equals(strArray[14])){
                    if(">50K".equals(strArray[14]) || ">50K.".equals(strArray[14])){
                        lables[i] = 1f;
                    } else if("<=50K".equals(strArray[14]) || "<=50K.".equals(strArray[14])){
                        lables[i] = -1f;
                    }
                }
                ++i;
            }
            dataMap.put("inputData", inputData);
            dataMap.put("lables", lables);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataMap;
    }
    private static void statsContinueMaxAndMini(List<String[]> dataList){
        Map<Integer, Float> maxMap = new HashMap<Integer, Float>();
        Map<Integer, Float> minMap = new HashMap<Integer, Float>();
        for(String []strArray : dataList){
            for(int i = 0; i < 14; ++i){
                if(inContinue(i)){
                    String str = strArray[i];
                    if(!"?".equals(str)){
                        Float f = Float.parseFloat(str);
                        if(null == maxMap.get(i) || maxMap.get(i) < f){
                            maxMap.put(i, f);
                        }
                        if(null == minMap.get(i) || minMap.get(i) > f){
                            minMap.put(i, f);
                        }
                    }
                }
            }
        }
        for(Map.Entry<Integer, Float> entry : maxMap.entrySet()){
            Integer index = entry.getKey();
            Float max = entry.getValue();
            Float min = minMap.get(index);
            Float d = (max - min) / 4;
            List<Float> dArray = new ArrayList<Float>();
            dArray.add(Float.MIN_VALUE);
            float temp = min;
            for(int i = 0; i < 5; ++i){
                dArray.add(temp);
                temp += d;
            }
            dArray.add(Float.MAX_VALUE);
            continueMap.put(entry.getKey(), dArray);
        }
    }
    public static int discretization(float d, int i){
        List<Float> dList = continueMap.get(i);
        int f = 0, l = 1;
        while(l < dList.size()){
            if(d >= dList.get(f) && d < dList.get(l)){
                return f;
            }
            ++f;
            ++l;
        }
        return -1;
    }
    public static boolean inContinue(int index){
        return index == 0 || index == 2 || index == 4 || index == 10 || index == 11 || index == 12;
    }
}
