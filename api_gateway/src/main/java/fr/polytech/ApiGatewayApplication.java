package fr.polytech;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;

@SpringBootApplication
public class ApiGatewayApplication {

	@Value("${user.service.url}")
	private String userServiceUrl;

	@Value("${location.service.url}")
	private String locationServiceUrl;

	@Value("${reservation.service.url}")
	private String reservationServiceUrl;

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("user_route", r -> r.path("/users/**")
						.filters(f -> f.rewritePath("/users/(?<segment>.*)", "/api/v1/users/${segment}"))
						.uri(userServiceUrl))
				.route("location_route", r -> r.path("/locations/**")
						.filters(f -> f.rewritePath("/locations/(?<segment>.*)", "/api/v1/locations/${segment}"))
						.uri(locationServiceUrl))
				.route("reservation_route", r -> r.path("/reservations/**")
						.filters(f -> f.rewritePath("/reservations/(?<segment>.*)", "/api/v1/reservations/${segment}"))
						.uri(reservationServiceUrl))
				.build();
	}

}
