import * as cdk from '@aws-cdk/core';
import * as apigateway from "@aws-cdk/aws-apigateway";
import { Runtime, Function, Code } from '@aws-cdk/aws-lambda';
import { NodejsFunction } from '@aws-cdk/aws-lambda-nodejs';
import * as path from 'path';
import * as storage from '../lib/storage';

export class AwsStack extends cdk.Stack {
  constructor(scope: cdk.Construct, id: string, props?: cdk.StackProps) {
    super(scope, id, props);

    new storage.Storage(this, 'DynamoDBStorage');

    const api = new apigateway.RestApi(this, "widgets-api", {
      restApiName: "Widget Service",
      description: "This service serves widgets."
    });

    // Create the Commands Lambda.
    const handler = new NodejsFunction(this, 'petproject-lambda', {
      runtime: Runtime.NODEJS_14_X,
      entry: path.join(__dirname, '../resources/widgets.ts'),
      handler: 'handler',
      timeout: cdk.Duration.seconds(60),
    });

    const getWidgetsIntegration = new apigateway.LambdaIntegration(handler, {
      requestTemplates: { "application/json": '{ "statusCode": "200" }' }
    });

    api.root.addMethod("GET", getWidgetsIntegration); // GET /
  }
}
