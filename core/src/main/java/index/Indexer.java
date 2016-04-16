package index;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Indexer {
	public static void main(String[] args) throws IOException,
			ClassNotFoundException, InterruptedException {
		Configuration config = new Configuration();

		Job job = Job.getInstance(config, "Indexer");
		job.setJarByClass(Indexer.class);

		job.setMapperClass(IndexMapper.class);
		job.setSortComparatorClass(IndexKeyComparator.class);
		job.setCombinerClass(IndexCombiner.class);
		job.setCombinerKeyGroupingComparatorClass(IndexKeyGroupComparator.class);

		job.setReducerClass(IndexReducer.class);

		job.setMapOutputKeyClass(IndexKey.class);
		job.setMapOutputValueClass(TermFrequencyWritable.class);
		job.setOutputKeyClass(OutputIndexKey.class);
		job.setOutputValueClass(TermFrequencyArrayWritable.class);

		job.setNumReduceTasks(1);

		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
