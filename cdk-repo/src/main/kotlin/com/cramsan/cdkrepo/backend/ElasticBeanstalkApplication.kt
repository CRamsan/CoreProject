package com.cramsan.cdkrepo.backend

import software.amazon.awscdk.core.Construct
import software.amazon.awscdk.services.elasticbeanstalk.CfnApplication
import software.amazon.awscdk.services.elasticbeanstalk.CfnApplicationProps
import software.amazon.awscdk.services.elasticbeanstalk.CfnEnvironment
import software.amazon.awscdk.services.elasticbeanstalk.CfnEnvironmentProps
import software.amazon.awscdk.services.iam.CfnInstanceProfile
import software.amazon.awscdk.services.iam.CfnInstanceProfileProps
import software.amazon.awscdk.services.iam.ManagedPolicy
import software.amazon.awscdk.services.iam.Role
import software.amazon.awscdk.services.iam.RoleProps
import software.amazon.awscdk.services.iam.ServicePrincipal

/**
 * Construct that creates an Elastic Beanstalk application.
 */
class ElasticBeanstalkApplication(scope: software.constructs.Construct, id: String) : Construct(scope, id) {

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
                        value("t3.small")
                    }.build(),
                    CfnEnvironment.OptionSettingProperty.builder().apply {
                        namespace("aws:autoscaling:launchconfiguration")
                        optionName("IamInstanceProfile")
                        value(instanceProfileName)
                    }.build(),
                ),
            )
        }.build()
        val environment = CfnEnvironment(this, "EB-Env-$id", environmentProps)
        environment.addDependsOn(backEndApp)
    }
}
