import java.util.Arrays;
import java.util.Map;

/**
 * Created by ZhangTao on 2017/9/4.
 */
public class main {
    public static void main(String []arg){
        Map<String, Object> inputDataMap = DataUtil.readerData("D:\\adult\\adult.data",true);
        float [][]inputData = (float[][]) inputDataMap.get("inputData");
        float []inputLable = (float[]) inputDataMap.get("lables");
        Map<String, Object> testInputDataMap = DataUtil.readerData("D:\\adult\\adult.test", false);
        float [][]testInputData = (float [][]) testInputDataMap.get("inputData");
        float []testInputLable = (float[]) testInputDataMap.get("lables");
        SVM svm = new SVM(true);
        svm.setC(1);
        svm.training(inputData, inputLable);
        int k  =0;
        for(int i = 0; i < testInputLable.length; ++i){
            if(svm.classfy(testInputData[i]) * testInputLable[i] > 0){
                ++k;
            }
        }
        System.out.println(k);
    }
}
