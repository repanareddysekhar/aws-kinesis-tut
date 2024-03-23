package org.example;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.IRecordProcessorFactory;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.InitialPositionInStream;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.KinesisClientLibConfiguration;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.Worker;

import java.net.InetAddress;
import java.util.UUID;

/**
 * Sample Amazon Kinesis Application.
 */
public final class AmazonKinesisApplicationSample {

    public static final String SAMPLE_APPLICATION_STREAM_NAME = "repana-poc-kinesis-stream";

    private static final String SAMPLE_APPLICATION_NAME = "SampleKinesisApplication";

    // Initial position in the stream when the application starts up for the first time.
    // Position can be one of LATEST (most recent data) or TRIM_HORIZON (oldest available data)
    private static final InitialPositionInStream SAMPLE_APPLICATION_INITIAL_POSITION_IN_STREAM =
            InitialPositionInStream.LATEST;

    private static AWSCredentialsProvider credentialsProvider;

    private static void init() {
        // Ensure the JVM will refresh the cached IP values of AWS resources (e.g. service endpoints).
        java.security.Security.setProperty("networkaddress.cache.ttl", "60");

        BasicAWSCredentials awsCredentials = new BasicAWSCredentials("", "");

        credentialsProvider = new AWSStaticCredentialsProvider(awsCredentials);
    }

    public static void main(String[] args) throws Exception {
        init();

        String workerId = InetAddress.getLocalHost().getCanonicalHostName() + ":" + UUID.randomUUID();
        KinesisClientLibConfiguration kinesisClientLibConfiguration =
                new KinesisClientLibConfiguration(SAMPLE_APPLICATION_NAME,
                        SAMPLE_APPLICATION_STREAM_NAME,
                        credentialsProvider,
                        workerId);
        kinesisClientLibConfiguration.withInitialPositionInStream(SAMPLE_APPLICATION_INITIAL_POSITION_IN_STREAM);
        kinesisClientLibConfiguration.withRegionName(Regions.AP_SOUTH_1.getName());

        IRecordProcessorFactory recordProcessorFactory = new AmazonKinesisApplicationRecordProcessorFactory();
        Worker worker = new Worker.Builder()
                .recordProcessorFactory(recordProcessorFactory)
                .config(kinesisClientLibConfiguration)
                .build();

        System.out.printf("Running %s to process stream %s as worker %s...\n",
                SAMPLE_APPLICATION_NAME,
                SAMPLE_APPLICATION_STREAM_NAME,
                workerId);

        int exitCode = 0;
        try {
            worker.run();
        } catch (Throwable t) {
            System.err.println("Caught throwable while processing data.");
            t.printStackTrace();
            exitCode = 1;
        }
        System.exit(exitCode);
    }
}
