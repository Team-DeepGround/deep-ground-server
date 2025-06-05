package com.samsamhajo.deepground.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequiredArgsConstructor
public class OAuthController {

    private static final String OAUTH2_AUTHORIZATION_BASE_URI = "/oauth2/authorization";
    private final ClientRegistrationRepository clientRegistrationRepository;

    @GetMapping("/auth/oauth/{provider}/login")
    public RedirectView redirectToProvider(@PathVariable String provider) {

        ClientRegistration registration = getClientRegistration(provider);
        if (registration == null) {
            return new RedirectView("/error?message=Unknown+provider");
        }
        String redirectUrl = OAUTH2_AUTHORIZATION_BASE_URI + "/" + provider;

        return new RedirectView(redirectUrl);
    }

    private ClientRegistration getClientRegistration(String provider) {
        if (clientRegistrationRepository instanceof Iterable) {
            for (ClientRegistration registration : (Iterable<ClientRegistration>) clientRegistrationRepository) {
                if (registration.getRegistrationId().equals(provider)) {
                    return registration;
                }
            }
        }
        return null;
    }
}
