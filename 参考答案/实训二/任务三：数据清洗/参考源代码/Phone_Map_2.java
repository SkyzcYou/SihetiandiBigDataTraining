package Phone_MR;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * @Auther: lp
 * @Date: 2018/10/18 13:09
 * @Description:
 */
public class Phone_Map_2 extends Mapper<LongWritable,Text,Text,NullWritable> {

    Text text = new Text();
    String[] words;
    @Override
    protected void map(LongWritable key, Text value, Mapper.Context context) {

        // 数据预处理
        String rawValue = PreProcessData(value.toString());

        try{

            // 解析JSON格式数据，从数据中获取需要的字段
            // 提取手机品牌名称
            String phoneBrand =  GetStringByPhoneBrand(rawValue, "phone_brand");

            // 解析JSON格式数据，从数据中获取需要的字段
            // 提取手机名称

            String phoneName = GetStringByPhoneName(rawValue);

            if (phoneName.contains("其他") || phoneName.equals("0") || phoneName.contains("官") || phoneName.length() == 1 ) {
               return;
            }

            // 提取用户购买手机时间
            String buy_date = GetPhoneComment(rawValue,"buy_date");

             buy_date= buy_date.split(" ")[0];
            System.out.println(buy_date);
            // 提取用户购买渠道
            String buy_client = GetPhoneComment(rawValue,"buy_client");

            // 提取用户评分
            String score = GetPhoneComment(rawValue,"score");

           // System.out.println(buy_date);
            // 检查获取的数据，若为空，则认为是无效数据
            String[] params = {phoneBrand, phoneName, buy_date,buy_client,score};
            if(CheckData(params)){
                String outputData = GenerateOutputData(phoneBrand, phoneName, buy_date,buy_client,score);

                text.set(outputData);
              // System.out.println(text);
                context.write(text, NullWritable.get());
            }

        } catch (Exception e) {
            return;
        }
    }
    /**
     * 清洗特殊字符|和\r,清洗规则如下：
     * 1. 将|替换为-
     * 2. 将换行符删除
     * @param value
     * @return 处理完成的数据
     */
    private String PreProcessData(String value){

        String retValue = value;

        // 将|替换为-
        retValue = value.toString().replace('|', '-');

        // 将换行符删除
        retValue = retValue.toString().replaceAll("\\r\\t", "");

        return retValue;
    }

    /**
     * 解析JSON格式数据，根据数如参数从JSON中提取目标字符串
     * @param rawJson 原始JSON格式字符串
     * @param keysName 目标数据key名称
     * @return 提取到的字符串
     */
    private String GetStringByPhoneBrand(String rawJson, String keysName){

        JSONObject object = JSONObject.parseObject(rawJson);
        if(object.containsKey(keysName)){
            return object.getString(keysName).trim();
        }
        else{
            return "";
        }
    }

    /**
     *
     * @param rawJson
     * @return
     */
    private String GetStringByPhoneName(String rawJson){

        // 获取parameter参数数据
        String parameter = GetStringByPhoneBrand(rawJson, "parameter");

        // 解析为数组
        JSONArray arrParam = JSONArray.parseArray(parameter);

        String keyName = "型号";

        // 遍历数组，寻找目标数据
        for(int iIndex = 0; iIndex < arrParam.size(); iIndex++){
            String rawValue = GetStringByPhoneBrand(arrParam.get(iIndex).toString(), keyName);
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
     * @param rawValue 原始JSON格式字符串
     * @param KeyName 目标数据key名称
     * @return 目标字段
     */
    private String GetPhoneComment(String rawValue,String KeyName){

        String jsonComments = GetStringByPhoneBrand(rawValue, "comments");
        String buy_date = GetStringByPhoneBrand(jsonComments, KeyName);

        return buy_date;
    }

    /**
     *
     * @param paramArr 所有字段数组
     * @return true false
     */
    private boolean CheckData(String[] paramArr){
        for(int iIndex = 0; iIndex < paramArr.length; iIndex++){
            if("" == paramArr[iIndex].trim()){
                return false;
            }
        }
        return true;
    }
    /**
     * @param phoneBrand 品牌
     * @param phoneName  手机名称
     * @param buy_date 用户购买时间
     * @param buy_client 用户购买渠道
     * @param score 用户评分
     * @return 拼接够的字符串
     */
    private String GenerateOutputData(String phoneBrand,String phoneName,String buy_date,String buy_client,String score ){
        return String.format("%s|%s|%s|%s|%s", phoneBrand, phoneName, buy_date,buy_client,score);
    }


}
