# Welcome to your CDK Java project!

This is a blank project for Java development with CDK.

The `cdk.json` file tells the CDK Toolkit how to execute your app.

It is a Gradle based project, so you can open this project with any Gradle compatible Java IDE to build and run tests.

# References
- https://medium.com/geekculture/serverless-aws-with-kotlin-gradle-and-cdk-d6bfe820b85

## Useful commands

 * `cdk ls`          list all stacks in the app
 * `cdk synth`       emits the synthesized CloudFormation template
 * `cdk deploy`      deploy this stack to your default AWS account/region
 * `cdk diff`        compare deployed stack with current state
 * `cdk docs`        open CDK documentation

## This CDK pipeline takes some env arguments:

SCHEDULE_EXPRESSION: In [this format](https://docs.aws.amazon.com/AmazonCloudWatch/latest/events/ScheduledEvents.html). Use it to define the execution rate of this lambda.