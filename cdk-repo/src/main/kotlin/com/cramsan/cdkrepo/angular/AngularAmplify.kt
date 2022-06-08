package com.cramsan.cdkrepo.angular

import software.amazon.awscdk.core.Construct
import software.amazon.awscdk.services.amplify.App
import software.amazon.awscdk.services.amplify.AppProps
import software.amazon.awscdk.services.amplify.CustomRule
import software.amazon.awscdk.services.amplify.CustomRuleOptions
import software.amazon.awscdk.services.amplify.RedirectStatus

/**
 * Construct that creates an Angular app on Amplify.
 */
class AngularAmplify(scope: software.constructs.Construct, id: String) : Construct(scope, id) {

    init {
        // Set the routing needed for the SPA.
        // https://angular.io/guide/deployment#server-configuration
        // https://docs.aws.amazon.com/amplify/latest/userguide/redirects.html#redirects-for-single-page-web-apps-spa
        val frontEndAppProps = AppProps.builder().apply {
            customRules(
                listOf(
                    CustomRule(
                        CustomRuleOptions.builder().apply {
                            source(REDIRECT_REGEX)
                            target("/index.html")
                            status(RedirectStatus.REWRITE)
                        }.build(),
                    ),
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
