import * as core from "@aws-cdk/core";
import * as ddb from "@aws-cdk/aws-dynamodb";

export class Storage extends core.Construct {
  constructor(scope: core.Construct, id: string) {
    super(scope, id);

    const descriptions = new ddb.Table(this, 'Description', {
      partitionKey: { name: 'id', type: ddb.AttributeType.NUMBER }
    });

    const plants = new ddb.Table(this, 'Plants', {
      partitionKey: { name: 'id', type: ddb.AttributeType.NUMBER }
    });

    const commonNames = new ddb.Table(this, 'CommonNames', {
      partitionKey: { name: 'id', type: ddb.AttributeType.NUMBER }
    });

    const families = new ddb.Table(this, 'Families', {
      partitionKey: { name: 'id', type: ddb.AttributeType.NUMBER }
    });

    const mainNames = new ddb.Table(this, 'MainNames', {
      partitionKey: { name: 'id', type: ddb.AttributeType.NUMBER }
    });

    const toxicities = new ddb.Table(this, 'Toxicities', {
      partitionKey: { name: 'id', type: ddb.AttributeType.NUMBER }
    });
  }
}