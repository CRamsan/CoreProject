package com.cramsan.cdkrepo.backend

import software.amazon.awscdk.services.elasticbeanstalk.CfnApplication
import software.amazon.awscdk.services.elasticbeanstalk.CfnApplicationProps
import software.amazon.awscdk.services.elasticbeanstalk.CfnApplicationVersion
import software.amazon.awscdk.services.elasticbeanstalk.CfnApplicationVersionProps
import software.amazon.awscdk.services.elasticbeanstalk.CfnEnvironment
import software.amazon.awscdk.services.elasticbeanstalk.CfnEnvironmentProps
import software.amazon.awscdk.services.iam.CfnInstanceProfile
import software.amazon.awscdk.services.iam.CfnInstanceProfileProps
import software.amazon.awscdk.services.iam.ManagedPolicy
import software.amazon.awscdk.services.iam.Role
import software.amazon.awscdk.services.iam.RoleProps
import software.amazon.awscdk.services.iam.ServicePrincipal
import software.amazon.awscdk.services.s3.assets.Asset
import software.amazon.awscdk.services.s3.assets.AssetProps
import software.constructs.Construct

/**
 * Construct that creates an Elastic Beanstalk application.
 */
class ElasticBeanstalkApplication(
    scope: Construct,
    id: String,
    jarPath: String,
    applyBlock: (ElasticBeanstalkApplication.() -> Unit)? = null,
) : Construct(scope, id) {

    init {
        val backendAppProps = CfnApplicationProps.builder().apply {
            applicationName(id)
        }.build()
        val backEndApp = CfnApplication(this, "EB-App-$id", backendAppProps)

        val instanceRole = Role(
            this,
            "EB-ec2-role",
            RoleProps.builder().apply {
                assumedBy(ServicePrincipal("ec2.amazonaws.com"))
            }.build(),
        )

        val managedPolicy = ManagedPolicy.fromAwsManagedPolicyName("AWSElasticBeanstalkWebTier")
        instanceRole.addManagedPolicy(managedPolicy)

        val instanceProfileName = "EB-InstanceProfile-$id"
        CfnInstanceProfile(
            this,
            instanceProfileName,
            CfnInstanceProfileProps.builder().apply {
                instanceProfileName(instanceProfileName)
                roles(listOf(instanceRole.roleName))
            }.build(),
        )

        val environmentProps = CfnEnvironmentProps.builder().apply {
            applicationName(backEndApp.applicationName)
            solutionStackName("64bit Amazon Linux 2 v3.2.15 running Corretto 8")
            optionSettings(
                listOf(
                    CfnEnvironment.OptionSettingProperty.builder().apply {
                        namespace("aws:autoscaling:launchconfiguration")
                        optionName("InstanceType")
                        value("t2.micro")
                    }.build(),
                    CfnEnvironment.OptionSettingProperty.builder().apply {
                        namespace("aws:autoscaling:launchconfiguration")
                        optionName("IamInstanceProfile")
                        value(instanceProfileName)
                    }.build(),
                    CfnEnvironment.OptionSettingProperty.builder().apply {
                        namespace("aws:autoscaling:asg")
                        optionName("MinSize")
                        value("0")
                    }.build(),
                    CfnEnvironment.OptionSettingProperty.builder().apply {
                        namespace("aws:autoscaling:asg")
                        optionName("MaxSize")
                        value("1")
                    }.build(),
                ),
            )
        }.build()
        val environment = CfnEnvironment(this, "EB-Env-$id", environmentProps)
        environment.addDependsOn(backEndApp)

        val wepAppZipArchive = Asset(
            this,
            "app-zip",
            AssetProps.builder().apply {
                path(jarPath)
            }.build(),
        )

        val appVersion = CfnApplicationVersion(
            this,
            "app-version",
            CfnApplicationVersionProps.builder().apply {
                applicationName(backEndApp.applicationName)
                sourceBundle(
                    CfnApplicationVersion.SourceBundleProperty.builder().apply {
                        s3Bucket(wepAppZipArchive.s3BucketName)
                        s3Key(wepAppZipArchive.s3ObjectKey)
                    }.build(),
                )
            }.build(),
        )
        appVersion.addDependsOn(backEndApp)

        applyBlock?.let { it(this) }
    }
}
