package testDemo;

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
 * @Date: 2018/10/16 16:04
 * @Description:
 */
public class Phone_Drive {
    public static void main(String[] args) throws Exception {

        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        job.setJarByClass(Phone_Drive.class);
        job.setMapperClass(Phone_Map.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);
        job.setNumReduceTasks(3);

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
        FileInputFormat.addInputPath(job, new Path("hdfs:/Initial_Data/jdn*/"));
        Path path = new Path("hdfs:/Clean_Data/");
        FileOutputFormat.setOutputPath(job,path);
        return path;
    }

}
