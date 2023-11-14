package samples.client.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.server.DefaultServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebFluxSecurity
@Configuration(proxyBeanMethods = false)
public class ClientServerConfig {

    // @formatter:off
	@Bean
	public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http,
														 ReactiveClientRegistrationRepository clientRegistrationRepository) throws Exception {
		http
			.authorizeExchange(authorize ->
				authorize.anyExchange().authenticated()
			)
			.oauth2Login(oauth2Login ->
					oauth2Login.authorizationRequestResolver(this.authorizationRequestResolver(clientRegistrationRepository))
			)
			.oauth2Client(withDefaults());
		return http.build();
	}
	// @formatter:on

    private ServerOAuth2AuthorizationRequestResolver authorizationRequestResolver(ReactiveClientRegistrationRepository clientRegistrationRepository) {
        ServerWebExchangeMatcher authorizationRequestMatcher = new PathPatternParserServerWebExchangeMatcher("/login/oauth2/authorization/{registrationId}");
        return new DefaultServerOAuth2AuthorizationRequestResolver(clientRegistrationRepository, authorizationRequestMatcher);
    }

}
