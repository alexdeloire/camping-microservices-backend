package fr.polytech;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
public class JWTAuthorizationFilter implements GlobalFilter {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        System.out.println("\n\n\n");
        System.out.println(path);

        System.out.println("\n\n\nFILTER STARTED");

        // Exclude login and register paths
        if (path.startsWith("/api/v1/users/login") || path.startsWith("/api/v1/users/register")) {
            System.out.println("\n\n\nLOGIN OR REGISTER");
            return chain.filter(exchange);
        }
        System.out.println("\n\n\nOTHER ROUTE");
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("\n\n\nNOT IN HEADER");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.replace("Bearer ", "");
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtSecret.getBytes())
                    .parseClaimsJws(token)
                    .getBody();

            // Optionally, you can add user details from claims to headers if needed
            exchange.getRequest().mutate()
                    .header("X-User-Name", claims.getSubject())
                    .header("X-User-Id", String.valueOf(claims.get("userId")))
                    .header("X-Is-Admin", String.valueOf(claims.get("isAdmin")))
                    .build();

            // Check admin privilege
            if (path.startsWith("/api/v1/users/delete-by-admin/") ||
                    path.startsWith("/api/v1/locations/assets/delete-admin") ||
                    path.startsWith("/api/v1/locations/assets/add-admin")){
                boolean isAdmin = Boolean.parseBoolean(claims.get("isAdmin").toString());
                if (!isAdmin) {
                    System.out.println("Unauthorized: Admin privileges required");
                    exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                    return exchange.getResponse().setComplete();
                }
            }

        } catch (Exception e) {
            System.out.println("Token parsing failed: " + e.getMessage());
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }
}
