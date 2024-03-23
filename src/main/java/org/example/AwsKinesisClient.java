package org.example;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisClientBuilder;


public class AwsKinesisClient {
    public static final String AWS_ACCESS_KEY = "aws.accessKeyId";
    public static final String AWS_SECRET_KEY = "aws.secretKey";

    static {
        //add your AWS account access key and secret key
        System.setProperty(AWS_ACCESS_KEY, "");
        System.setProperty(AWS_SECRET_KEY, "");
    }

    public static AmazonKinesis getKinesisClient(){
        return AmazonKinesisClientBuilder.standard()
                .withRegion(Regions.AP_SOUTH_1)
                .build();
    }
}
