package com.smartsure.api_gateway.routes;

import com.smartsure.api_gateway.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class ServiceRoutes {

    private final JwtAuthenticationFilter jwtFilter;

    @Bean
    public RouteLocator authMicroServiceManualRouting(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-service", r -> r.path("/api/auth/**")
                        .uri("lb://AUTH-SERVICE"))
                .build();
    }

    @Bean
    public RouteLocator policyMicroServiceManualRouting(RouteLocatorBuilder builder) {
        return builder.routes()

                .route("policy-service-user", r -> r
                        .path("/api/policies/**")
                        .filters(f -> f.filter(jwtFilter))
                        .uri("lb://POLICY-SERVICE"))

                .route("admin-service", r -> r
                        .path("/api/admin/**")
                        .filters(f -> f.filter(jwtFilter))
                        .uri("lb://ADMIN-SERVICE"))

                .build();
    }


    @Bean
    public RouteLocator claimsMicroServiceManualRouting(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("claims-service", r -> r
                        .path("/api/claims/**", "/api/admin/claims/**", "/api/documents/**")
                        .filters(f -> f.filter(jwtFilter))
                        .uri("lb://CLAIMS-SERVICE"))
                .build();
    }

    @Bean
    public RouteLocator adminMicroServiceManualRouting(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("admin-service", r -> r
                .path("/api/admin/**")
                .filters(f -> f.filter(jwtFilter))
                .uri("lb://ADMIN-SERVICE"))
                .build();
    }


}
