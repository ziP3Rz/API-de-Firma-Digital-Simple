package net.zip3rz.firma_digital.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import net.zip3rz.firma_digital.api.service.CustomUserDetailsService;

/**
 * Configuración de la seguridad de la aplicación.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    /**
     * Configuración de la seguridad de la aplicación.
     * Se deshabilita la protección CSRF.
     * Se permite el acceso a los endpoints de registro y autenticación de usuarios sin autenticación.
     * El resto de los endpoints requieren autenticación.
     * Se configura el UserDetailsService para gestionar los usuarios.
     * Se utiliza autenticación HTTP básica.
     * @param http objeto para configurar la seguridad.
     * @return filtro de seguridad.
     * @throws Exception si ocurre un error al configurar la seguridad.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
            	.requestMatchers("/usuarios/registrar", "/usuarios/autenticar", "firma-digital/verificar-documento").permitAll()
                .requestMatchers("/usuarios/**", "firma-digital/generar-claves").authenticated()
                .anyRequest().permitAll()
            )
            .userDetailsService(userDetailsService)
            .httpBasic();

        return http.build();
    }

    /**
     * Configuración del administrador de autenticación.
     * @param authenticationConfiguration configuración de autenticación.
     * @return administrador de autenticación.
     * @throws Exception si ocurre un error al obtener el administrador de autenticación.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Configuración del codificador de contraseñas.
     * @return codificador de contraseñas BCrypt.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
