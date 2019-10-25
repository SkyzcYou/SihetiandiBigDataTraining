package testDemo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * @Auther: lp
 * @Date: 2018/10/16 16:04
 * @Description:
 */

public class Phone_Map extends Mapper<LongWritable,Text,Text,NullWritable> {

    Text text = new Text();

    @Override
    protected void map(LongWritable key, Text value, Context context) {

        // 数据预处理
        String rawValue = PreProcessData(value.toString());

        try{

            // 解析JSON格式数据，从数据中获取需要的字段
            // 提取手机品牌名称
            String phoneBrand =  GetStringByName(rawValue, "phone_brand");

            // 提取手机屏幕尺寸
            String phoneSize = GetPhoneSize(rawValue);

            // 提取用户购买手机颜色

            String buyColor = GetPhoneColor(rawValue);

            System.out.println(buyColor);
            // 检查获取的数据，若为空，则认为是无效数据
            String[] params = {phoneBrand, phoneSize, buyColor};
            if(CheckData(params)){
                String outputData = GenerateOutputData(phoneBrand, buyColor, phoneSize);

                text.set(outputData);
                //System.out.println(text);
                context.write(text,NullWritable.get());
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
    private String GetStringByName(String rawJson, String keysName){

        JSONObject object = JSONObject.parseObject(rawJson);
        if(object.containsKey(keysName)){
            return object.getString(keysName).trim();
        }
        else{
            return "";
        }
    }

    /**
     * 获取手机屏幕尺寸数据
     * @param rawJson
     * @return 手机屏幕尺寸数据
     */
    private String GetPhoneSize(String rawJson){

        // 获取parameter参数数据
        String parameter = GetStringByName(rawJson, "parameter");

        // 解析为数组
        JSONArray arrParam = JSONArray.parseArray(parameter);

        String keyName = "主屏幕尺寸（英寸）";

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
     * @param rawValue
     * @return String
     */
    private String GetPhoneColor(String rawValue){

        String jsonComments = GetStringByName(rawValue, "comments");
        String buyColor = GetStringByName(jsonComments, "buy_color");

        // System.out.println(jsonComments);
        String[] arrColor = new String[]{"红","黑","白","金","蓝","黄","紫","绿","粉","银","灰","青"};
        int iIndex = 0;
        for ( ; iIndex < arrColor.length; iIndex++){
            if (buyColor.contains(arrColor[iIndex])){
                buyColor = arrColor[iIndex];
                break;
            }
        }
        if(arrColor.length == iIndex) {
            buyColor = "其他";
        }
        return buyColor;
    }

    /**
     * 检查获取到的数据是否存在空字符串
     * @param paramArr
     * @return 手机屏幕尺寸数据
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
     * 将数据按照要求的格式组装
     * @param phoneBrand
     * @param buyColor
     * @param phoneSize
     * @return 组装好的输出数据
     */
    private String GenerateOutputData(String phoneBrand,String buyColor,String phoneSize){
        return String.format("%s|%s|%s", phoneBrand, buyColor, phoneSize);
    }
}
