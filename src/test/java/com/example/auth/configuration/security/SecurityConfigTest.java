package com.example.auth.configuration.security;


import com.fleetguard360.configuration.security.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SecurityConfigTest {

    @Autowired
    private ApplicationContext context;

    @Test
    void contextLoadsSecurityConfig() {
        // Tener en cuenta, esta clase solo verifica que la configuración principal de seguridad esté cargada
        assertThat(context.getBean(SecurityConfig.class)).isNotNull();
        assertThat(context.getBean("securityFilterChain")).isNotNull();
        assertThat(context.getBean("authenticationManager")).isNotNull();
        assertThat(context.getBean("passwordEncoder")).isNotNull();
    }
}

