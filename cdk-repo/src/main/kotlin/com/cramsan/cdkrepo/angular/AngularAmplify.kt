package com.cramsan.cdkrepo.angular

import software.constructs.Construct
import software.amazon.awscdk.services.amplify.CfnApp as App
import software.amazon.awscdk.services.amplify.CfnApp.CustomRuleProperty as CustomRule
import software.amazon.awscdk.services.amplify.CfnAppProps as AppProps

/**
 * Construct that creates an Angular app on Amplify.
 */
class AngularAmplify(scope: Construct, id: String) : Construct(scope, id) {

    init {
        // Set the routing needed for the SPA.
        // https://angular.io/guide/deployment#server-configuration
        // https://docs.aws.amazon.com/amplify/latest/userguide/redirects.html#redirects-for-single-page-web-apps-spa
        val frontEndAppProps = AppProps.builder().apply {
            name("Angular-App-$id")
            customRules(
                listOf(
                    CustomRule.builder().apply {
                        source(REDIRECT_REGEX)
                        target("/index.html")
                        status("200")
                    }.build(),
                ),
            )
        }.build()

        // Create the Amplify app.
        App(this, "Angular-$id", frontEndAppProps)
    }

    companion object {
        const val REDIRECT_REGEX = "</^[^.]+\$|\\.(?!(css|gif|ico|jpg|js|png|txt|svg|woff|woff2|ttf|map|" +
            "json|webp)\$)([^.]+\$)/>"
    }
}
