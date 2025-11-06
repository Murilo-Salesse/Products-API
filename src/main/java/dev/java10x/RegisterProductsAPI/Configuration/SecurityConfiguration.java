package dev.java10x.RegisterProductsAPI.Configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfiguration implements WebMvcConfigurer {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfiguration(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }


    // E o tipo SecurityFilterChain é o contrato que o Spring Security usa internamente para montar a cadeia de filtros HTTP (security filters)
    // Entao quando sobe a aplicação, o spring ve que to usando o Bean para configura manualmente
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthEntryPoint entryPoint) throws Exception {
        http
                // 	•	Desativa o CSRF (Cross-Site Request Forgery).
                //	•	Explicação rápida: CSRF protege formulários do browser contra requisições forjadas.
                //	•   Em APIs REST que usam tokens JWT e não mantêm sessão, normalmente desativa-se CSRF porque o token já provê proteção
                .csrf(AbstractHttpConfigurer::disable)

                // SessionCreationPolicy.STATELESS significa: não criar ou usar sessão HTTP.
                // Cada requisição é independente — isso é o padrão para APIs REST com JWT.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // sem sessão
                .exceptionHandling(ex -> ex.authenticationEntryPoint(entryPoint))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/user/register",
                                "/api/user/login"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                /*
                    O meu jwtAuthenticationFilter, ele fica em primeiro na cadeia de filtros do java após chamar
                    uma request HTTP, ele que vai validar meu token para poder me mandar para as proximas requests ou não
                    quando eu não uso o JWT, o (JWT Padrao) seria o UsernamePasswordAuthenticationFilter, que ai ele
                    ia ser o 1 filtro, ele ia verificar se existe um name e password na request, pq obrigatoriamente
                    o UsernamePasswordAuthenticationFilter espera por isso, se existir, ele vai chamar
                    o AuthenticationManager → que consulta seu UserDetailsService → que vai ao banco verificar o usuário
                    Se estiver certo, o Spring cria um Authentication e guarda no SecurityContextHolder.

                    AuthenticationManager -> isso sabe qual usuario buscar pq quando nao usa JWT por padrao
                    ele gera por padrao um user e passowrd pela dependencia do validation
                    tipo: adimn 123456
                    e vc começa a substituir ele a partir do momento que usa UsernamePasswordAuthenticationFilter
                    pq ai ele vai pegar o name e password da request do /login e vai setar como esse user
                    e dps so busca no banco
                 */
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .cors(withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public org.springframework.web.cors.CorsConfigurationSource corsConfigurationSource() {
        var configuration = new org.springframework.web.cors.CorsConfiguration();
        configuration.setAllowedOrigins(java.util.List.of("http://localhost:4200"));
        configuration.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(java.util.List.of("*"));
        configuration.setAllowCredentials(true);

        var source = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}