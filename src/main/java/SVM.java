import java.util.*;

/**
 * Created by ZhangTao on 2017/9/4.
 */
public class SVM {
    private boolean linear = true;
    private List<Float> suportVectorAlphs = new ArrayList<Float>();
    private List<float[]> suportVectors = new ArrayList<float[]>();
    private List<Float> suportVectorLables = new ArrayList<Float>();
    private KernelFunction kernelFunction;
    private float []errorArray;
    private float c;
    private float b;
    private float []w;
    private float []kernelResultMap;
    private float loss;
    private int dataCount;

    public SVM(boolean linear){
        this.linear = linear;
    }

    public void setKernelFunction(KernelFunction kernelFunction) {
        this.kernelFunction = kernelFunction;
    }

    public void setKernelResult(int i, int j, float result){
        int r,c;
        r = i < j ? i : j;
        c = j > i ? j : i;
        if(i == 0){
            kernelResultMap[c] = result;
            return;
        }
        kernelResultMap[dataCount * r - (r * (r - 1)) / 2 + c - r] = result;
    }

    public float getKernelResult(int i, int j){
        int r,c;
        r = i < j ? i : j;
        c = j > i ? j : i;
        if(i == 0){
            return kernelResultMap[c];
        }
        return kernelResultMap[dataCount * r - (r * (r - 1)) / 2 + c - r];
    }

    public void setC(float c) {
        this.c = c;
    }

    private void initKernelResultMap(int dataCount){
        int length = (dataCount * (dataCount + 1)) / 2;
        kernelResultMap = new float [length];
    }

    public void training(float [][]inputData, float []lables){
        if(linear) {
            w = new float[inputData[0].length];
            Arrays.fill(w, 0);
            kernelFunction = new LinearKernel();
        }else{
            suportVectorAlphs = new LinkedList<Float>();
            suportVectors = new LinkedList<float[]>();
            suportVectorLables = new LinkedList<Float>();
        }
        int length = inputData.length;
        dataCount = length;
        initKernelResultMap(length);
        for(int i = 0; i < length; ++i){
            for(int j = i; j < length; ++j){
                setKernelResult(i, j, kernelFunction.compute(inputData[i], inputData[j]));
            }
        }
        errorArray = new float[length];
        SMO(inputData, lables);
    }
    private float computeResult(int index, float[][] data, float []lable, float []alpha) {
        if(linear){
            return kernelFunction.compute(w, data[index]) + b;
        }
        float sum = 0;
        for(int i = 0; i < data.length; ++i){
            sum += lable[i] * getKernelResult(index, i) * alpha[i];
        }
        return sum + b;
    }
    private int updateParameter(int index1, int index2, float [][]data, float []alpha, float []lable){
        float E1, E2, k11, k22, k12, eta, L, H, S;
        E1 = errorArray[index1];
        E2 = errorArray[index2];
        k11 = getKernelResult(index1, index1);
        k22 = getKernelResult(index2, index2);
        k12 = getKernelResult(index1, index2);
        eta = k11 + k22 - 2 * k12;
        S = lable[index1] * lable[index2];
        if(S > 0){
            L = max(0, alpha[index1] + alpha[index2] - c);
            H = min(c, alpha[index1] + alpha[index2]);
        } else {
            //有问题
            L = max(0, alpha[index1] - alpha[index2]);
            H = min(c, c + alpha[index1] - alpha[index2]);
        }
        float tempAlpha1, tempAlpha2;
        if(eta > 0.001){
            tempAlpha1 = (E2 - E1) * lable[index1] / eta + alpha[index1];
            if(tempAlpha1 < L){
                tempAlpha1 = L;
            } else if(tempAlpha1 > H){
                tempAlpha1 = H;
            }
            tempAlpha2 = computeAlpha2(tempAlpha1 - alpha[index1], alpha[index2], S);
            loss = newLossFunction(index1, index2, alpha, lable, tempAlpha1, tempAlpha2);
        } else {
            float alpha1L, alpha1H, alpha2L, alpha2H;
            alpha1L = L;
            alpha2L = computeAlpha2(alpha1L - alpha[index1], alpha[index2], S);
            alpha1H = H;
            alpha2H = computeAlpha2(alpha1H - alpha[index1], alpha[index2], S);
            float tempLossL, tempLossH;
            tempLossL = newLossFunction(index1, index2, alpha, lable, alpha1L, alpha2L);
            tempLossH = newLossFunction(index1, index2, alpha, lable, alpha1H, alpha2H);
            if(tempLossL > tempLossH){
                tempAlpha1 = alpha1L;
                tempAlpha2 = alpha2L;
                loss = tempLossH;
            }else {
                tempAlpha1 = alpha1H;
                tempAlpha2 = alpha2H;
                loss = tempLossH;
            }
        }
        if(Math.abs(tempAlpha1 - alpha[index1]) < 0.001){
            return 0;
        }
        float d1 = (tempAlpha1 - alpha[index1]) * lable[index1],
                d2 = (tempAlpha2 - alpha[index2]) * lable[index2];
        if(linear){
            //更新w
            for(int i = 0; i < w.length; ++i){
                w[i] += d1 * data[index1][i] + d2 * data[index2][i];
            }
        }
        float b1, b2;
        b1 = b - E1 - d1 * getKernelResult(index1, index1) -  d2 * getKernelResult(index1, index2);
        b2 = b - E1 - d1 * getKernelResult(index2, index1) -  d2 * getKernelResult(index2, index2);
        b = (b1 + b2) / 2;
        alpha[index1] = tempAlpha1;
        alpha[index2] = tempAlpha2;
        return 1;
    }

    private float computeAlpha2(float delta, float alph2Old, float s){
        return alph2Old - s * delta;
    }

    private float lossFunction(float [][]data, float []lable, float []alpha){
        float alphaSum = 0;
        for(int i = 0; i < lable.length; ++i){
            alphaSum += alpha[i];
        }
        float kSum = 0;
        for(int i = 0; i < data.length; ++i){
            kSum += getKernelResult(i, i) * alpha[i] * alpha[i];
        }
        for(int i = 1; i < data.length; ++i){
            for(int j = i + 1; j < data.length; ++j){
                kSum += getKernelResult(i, j) * alpha[i] * alpha[j] * lable[i] * lable[j] * 2;
            }
        }
        return alphaSum - kSum / 2;
    }

    private float newLossFunction(int index1, int index2,
                                     float []alpha, float []lable,
                                     float alpha1, float alpha2){
        float delta1, delta2, temSum = 0;
        delta1 = alpha1 - alpha[index1];
        delta2 = alpha2 - alpha[index2];
        temSum += (alpha1 * alpha1 - alpha[index1] * alpha[index1]) * getKernelResult(index1, index1);
        temSum += (alpha2 * alpha2 - alpha[index2] * alpha[index2]) * getKernelResult(index2, index2);
        for(int i = 0; i < lable.length; ++i){
            if(i != index1 && i != index2){
                temSum += delta1 * lable[i] * lable[index1] * alpha[i] * getKernelResult(index1, i) * 2;
            }
        }
        for(int i = 0; i < lable.length; ++i){
            if(i != index2 && i != index1){
                temSum += delta2 * lable[i] * lable[index2] * alpha[i] * getKernelResult(index2, i) * 2;
            }
        }
        temSum += lable[index1] * lable[index2] * getKernelResult(index1, index2) * (alpha1 * alpha2 *  -
                alpha[index1] * alpha[index2] ) * 2;
        temSum /= 2;
        temSum += delta1 + delta2;
        return loss + temSum;
    }


    private float min(float d1, float d2){
        return d1 < d2 ? d1 : d2;
    }
    private float max(float d1, float d2){
        return d1 > d2 ? d1 : d2;
    }

    public float classfy(float []data){
        float result = 0;
        if(linear){
            for(int i = 0; i < data.length; ++i){
               result = kernelFunction.compute(w, data);
            }
        } else {
            for(int i = 0; i < suportVectors.size(); ++i){
                result += kernelFunction.compute(suportVectors.get(i), data)
                        * suportVectorAlphs.get(i)
                        * suportVectorLables.get(i);
            }
        }
        return result + b;
    }

    private boolean violateKKT(int index, float [][]data, float []alpha, float []lable){
        float r =  (computeResult(index, data, lable, alpha) - lable[index]) * lable[index];
        //kkt 条件
        if((r < -0.001 && alpha[index] < c - 0.001) || (r > 0.001 && alpha[index] > 0.001)){
            return true;
        }
        return false;
    }

    private void computeError(float [][]data, float []lable, float []alpha){
        for(int i = 0; i < data.length; ++i){
            errorArray[i] = computeResult(i, data,lable,alpha) - lable[i];
        }
    }
    private int chooseAlpha2(int index1){
        int i =  (int)(System.currentTimeMillis() % errorArray.length);
        float maxError = Math.abs(errorArray[index1] - errorArray[i]);
        int index2 = i;
        int count = 0;
        while(count < errorArray.length - 1){
            i = (i + 1) / errorArray.length;
            float tempError = Math.abs(errorArray[index1] - errorArray[i]);
            if(index1 != i && maxError < tempError){
                maxError = tempError;
                index2 = i;
            }
            ++count;
        }
        return index2;
    }
    private void SMO(float [][]inputData, float []lables){
        b = 0;
        float []alpha = new float[lables.length];
        Arrays.fill(alpha, 0);
        boolean stop = false;
        List<Integer> workList = new LinkedList<Integer>();
        int count = 0;
        computeError(inputData, lables, alpha);
        while(!stop){
            stop = true;
            for(int i = 0; i < inputData.length; ++i){
//                System.out.println(count);
                //违反kkt条件
                if(violateKKT(i,inputData,alpha,lables)){
                    ++count;
                    int index2 = chooseAlpha2(i);
                    if(updateParameter(i, index2, inputData, alpha, lables) == 0){
                        LinkedList<Integer> list =  new LinkedList<Integer>();
                        int j = (int)(System.currentTimeMillis() % alpha.length);
                        int k = 0;
                        while(k < alpha.length){
                            if(j != i){
                                if(alpha[j] > 0 && alpha[j] < c) {
                                    list.add(0,j);
                                } else {
                                    list.add(j);
                                }
                            }//if
                            ++k;
                            j = (j + 1) % alpha.length;
                        }//while
                        Queue<Integer> q = list;
                        while(!q.isEmpty()){
                            index2 = q.poll();
                            if(updateParameter(i, index2,inputData,alpha,lables) == 1){
                                stop = false;
                                computeError(inputData, lables, alpha);
                                break;
                            }
                        }
                    } else {
                        stop = false;
                        computeError(inputData, lables, alpha);
                    }
                }//if
            }//for
            if(count > 4000000){
                stop = true;
            }
        }//while
        if(!linear){
            for(int i = 0; i < alpha.length; ++i){
                if(alpha[i] > 0.001){
                    suportVectorAlphs.add(alpha[i]);
                    suportVectorLables.add(lables[i]);
                    suportVectors.add(inputData[i]);
                }
            }
        }
    }
}
