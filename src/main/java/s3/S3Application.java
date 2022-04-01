package s3;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class S3Application {
	private static final AWSCredentials credentials;
	private static String bucketName;

	static {
		// put your accesskey and secretkey here
		credentials = new BasicAWSCredentials("AKIARJYGZ6WNZTID4E2E", "DT8mMmTRTdX/hu3T1iyOKmiuI7Klc64VH53gSviW");
	}

	public static void main(String[] args) throws IOException {
		// set-up the client
		AmazonS3 s3client = AmazonS3ClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(Regions.AP_SOUTHEAST_1)
				.build();

		AWSS3Service awsService = new AWSS3Service(s3client);

		bucketName = "recommend-force-update";
		if (awsService.doesBucketExist(bucketName)) {
			System.out.println("Bucket name is available." + " Try again with a different Bucket name.");
		}

		// awsService.createBucket(bucketName);

		// list all the buckets
		for (Bucket s : awsService.listBuckets()) {
			System.out.println(s.getName());
		}

		// listing objects
		ObjectListing objectListing = awsService.listObjects(bucketName);
		for (S3ObjectSummary os : objectListing.getObjectSummaries()) {
			System.out.println(os.getKey());
		}

		// uploading object
		awsService.putObject(bucketName, "hello.txt", new File("D:/hello.txt"));

		// downloading an object
		S3Object s3object = awsService.getObject(bucketName, "test.json");
		S3ObjectInputStream inputStream = s3object.getObjectContent();
		FileUtils.copyInputStreamToFile(inputStream, new File("D:/test.json"));

	}
}
