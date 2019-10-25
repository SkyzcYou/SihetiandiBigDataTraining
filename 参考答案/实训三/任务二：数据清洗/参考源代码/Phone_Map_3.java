package Phone_MR_3;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;


/**
 * @Auther: lp
 * @Date: 2018/10/23 09:37
 * @Description:
 */
public class Phone_Map_3 extends Mapper<LongWritable,Text,Text,NullWritable> {

    Text text = new Text();
    String[] words;

    @Override
    protected void map(LongWritable key, Text value, Mapper.Context context) {

        // 数据预处理
        String rawValue = PreProcessData(value.toString());

        try {
            // 提取手机操作系统
            String PhoneOS = GetPhoneParameter(rawValue,"操作系统");
            if (PhoneOS.equals("其他") || PhoneOS.contains("官网") || PhoneOS.equals("")){
                PhoneOS="非智能机";
            }
            //提取手机CPU型号
            String CPU = GetPhoneParameter(rawValue,"CPU型号");
            //System.out.println(CPU);
            //获取RAM
            String RAM = GetPhoneParameter(rawValue,"RAM");
            //System.out.println(RAM);
            //获取ROM
            String ROM = GetPhoneParameter(rawValue,"ROM");
            //System.out.println(ROM);
            // 检查获取的数据，若为空，则认为是无效数据
            String[] params = {PhoneOS, CPU,RAM,ROM};
            if (CheckData(params)) {
                if (FilterData(params)) {
                    String outputData = GenerateOutputData(PhoneOS, CPU, RAM, ROM);

                    text.set(outputData);
                   System.out.println(text);
                    context.write(text, NullWritable.get());
                }
            }
        } catch (Exception e) {
            return;
        }
    }

    /**
     * 清洗特殊字符|和\r,清洗规则如下：
     * 1. 将|替换为-
     * 2. 将换行符删除
     *
     * @param value
     * @return 处理完成的数据
     */
    private String PreProcessData(String value) {

        String retValue = value;

        // 将|替换为-
        retValue = value.toString().replace('|', '-');

        // 将换行符删除
        retValue = retValue.toString().replaceAll("\\r\\t", "");

        return retValue;
    }

    /**
     * 解析JSON格式数据，根据数如参数从JSON中提取目标字符串
     *
     * @param rawJson  原始JSON格式字符串
     * @param keysName 目标数据key名称
     * @return 提取到的字符串
     */
    private String GetStringByName(String rawJson, String keysName) {

        JSONObject object = JSONObject.parseObject(rawJson);
        if (object.containsKey(keysName)) {
            return object.getString(keysName).trim();
        } else {
            return "";
        }
    }
    /**
     * 获取手机操作系统
     * @param rawJson
     * @return 手机操作系统
     */
    private String GetPhoneParameter(String rawJson,String keyName){

        // 获取parameter参数数据
        String parameter = GetStringByName(rawJson, "parameter");

        // 解析为数组
        JSONArray arrParam = JSONArray.parseArray(parameter);

        // 遍历数组，寻找目标数据
        for(int iIndex = 0; iIndex < arrParam.size(); iIndex++){


                String rawValue = GetStringByName(arrParam.get(iIndex).toString(), keyName);
                if("" == rawValue.trim()){

                    continue;
                }
                else{
                    return rawValue;
                }
            }
        return "";
    }

    /**
     *
     * @param paramArr
     * @return
     */
    private boolean CheckData(String[] paramArr){
        for(int iIndex = 0; iIndex < paramArr.length; iIndex++){
            if("" == paramArr[iIndex].trim()){
                return false;
            }
        }
        return true;
    }

    private boolean FilterData (String [] parparamArr){

        for (int iIndex = 0; iIndex < parparamArr.length; iIndex++){

            if ( parparamArr[iIndex].equals("官网") || parparamArr[iIndex].equals("") || parparamArr[iIndex].length()>10 || parparamArr[iIndex].equals("--")){
                return false;
            }
        }
        return true;
    }
    /**
     *
     * @param OS
     * @param CPU
     * @param RAM
     * @param ROM
     * @return
     */
    private String GenerateOutputData(String OS,String CPU,String RAM,String ROM){
        return String.format("%s|%s|%s|%s", OS, CPU,RAM,ROM);
    }
}
