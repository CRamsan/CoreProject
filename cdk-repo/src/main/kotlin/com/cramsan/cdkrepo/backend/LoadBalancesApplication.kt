package com.cramsan.cdkrepo.backend

import software.amazon.awscdk.core.CfnOutput
import software.amazon.awscdk.core.CfnOutputProps
import software.amazon.awscdk.core.Construct
import software.amazon.awscdk.core.Duration
import software.amazon.awscdk.services.autoscaling.AutoScalingGroup
import software.amazon.awscdk.services.autoscaling.AutoScalingGroupProps
import software.amazon.awscdk.services.autoscaling.RequestCountScalingProps
import software.amazon.awscdk.services.ec2.AmazonLinuxGeneration
import software.amazon.awscdk.services.ec2.AmazonLinuxImage
import software.amazon.awscdk.services.ec2.AmazonLinuxImageProps
import software.amazon.awscdk.services.ec2.InstanceClass
import software.amazon.awscdk.services.ec2.InstanceSize
import software.amazon.awscdk.services.ec2.InstanceType
import software.amazon.awscdk.services.ec2.UserData
import software.amazon.awscdk.services.ec2.Vpc
import software.amazon.awscdk.services.ec2.VpcProps
import software.amazon.awscdk.services.elasticloadbalancingv2.AddApplicationTargetsProps
import software.amazon.awscdk.services.elasticloadbalancingv2.ApplicationListenerProps
import software.amazon.awscdk.services.elasticloadbalancingv2.ApplicationLoadBalancer
import software.amazon.awscdk.services.elasticloadbalancingv2.ApplicationLoadBalancerProps
import software.amazon.awscdk.services.elasticloadbalancingv2.HealthCheck

/**
 * Construct that creates an Elastic Beanstalk application.
 */
class LoadBalancesApplication(scope: software.constructs.Construct, id: String) : Construct(scope, id) {

    init {
        // Based on the implementation from this article:
        // https://bobbyhadz.com/blog/aws-cdk-application-load-balancer
        val vpcProps = VpcProps.builder().apply {
            natGateways(1)
        }.build()
        val vpc = Vpc(this, "vpc-$id", vpcProps)

        val albProps = ApplicationLoadBalancerProps.builder().apply {
            vpc(vpc)
            internetFacing(true)
        }.build()
        val alb = ApplicationLoadBalancer(this, "alb-$id", albProps)

        val listener = alb.addListener(
            "public-listener-$id",
            ApplicationListenerProps.builder().apply {
                port(80)
                open(true)
            }.build(),
        )

        val userData = UserData.forLinux().apply {
            addCommands(
                "sudo su",
                "yum install -y httpd",
                "systemctl start httpd",
                "systemctl enable httpd",
                "echo \"<h1>Hello World from $(hostname -f)</h1>\" > /var/www/html/index.html",
            )
        }

        val asg = AutoScalingGroup(
            this,
            "adg-$id",
            AutoScalingGroupProps.builder().apply {
                vpc(vpc)
                instanceType(
                    InstanceType.of(InstanceClass.BURSTABLE2, InstanceSize.NANO),
                )
                machineImage(
                    AmazonLinuxImage(
                        AmazonLinuxImageProps.builder().apply {
                            generation(AmazonLinuxGeneration.AMAZON_LINUX_2)
                        }.build(),
                    ),
                )
                userData(userData)
                minCapacity(1)
                maxCapacity(2)
            }.build(),
        )

        listener.addTargets(
            "default-target-$id",
            AddApplicationTargetsProps.builder().apply {
                port(80)
                targets(listOf(asg))
                healthCheck(
                    HealthCheck.builder().apply {
                        path("/")
                        unhealthyThresholdCount(2)
                        healthyThresholdCount(5)
                        interval(Duration.minutes(1))
                    }.build(),
                )
            }.build(),
        )

        asg.scaleOnRequestCount(
            "scale-up-$id",
            RequestCountScalingProps.builder().apply {
                targetRequestsPerMinute(60)
            }.build(),
        )

        CfnOutput(
            this,
            "alb-dns",
            CfnOutputProps.builder().apply {
                value(alb.loadBalancerDnsName)
            }.build(),
        )
    }
}
