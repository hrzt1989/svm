/**
 * Created by zhangtao on 2017/9/8.
 */
public class LinearKernel  implements KernelFunction{
    public float compute(float []v1, float []v2){
        float sum = 0;
        for(int i = 0; i < v1.length; ++i){
            sum += v1[i] * v2[i];
        }
        return sum;
    }
}
