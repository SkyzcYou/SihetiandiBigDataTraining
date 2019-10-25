package Phone_MR;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * @Auther: lp
 * @Date: 2018/10/18 13:09
 * @Description:
 */
public class Phone_Drive_2 {

    public static void main(String[] args) throws Exception {

        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        job.setJarByClass(Phone_Drive_2.class);
        job.setMapperClass(Phone_Map_2.class);
        //设置输出的Key,Value的类型
        Jobs(job);

        job.setNumReduceTasks(3);
        //指定输入输出；路径
        Path path = setPath(job);

        path.getFileSystem(conf).delete(path,true);//删除你的输出文件夹,可以不用写，但测试时候每次都要删除输出路径文件
        System.exit(job.waitForCompletion(true) ? 0 : 1);

    }

    /**
     * 这个方法为定义数据的输入输出路径
     * @param job
     * @return Path
     * @throws IOException
     */
    private static Path setPath(Job job) throws IOException {

        FileInputFormat.addInputPath(job, new Path("hdfs:/Initial_Data/jd*"));
        Path path = new Path("hdfs:/Clean_Data/kk");
        FileOutputFormat.setOutputPath(job,path);
        return path;
    }
    private static void Jobs (Job job){

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);
    }
}
