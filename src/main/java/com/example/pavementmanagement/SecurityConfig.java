
package com.example.pavementmanagement;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // パスワードをハッシュ化する
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ログインユーザーの設定
    @Bean
    public UserDetailsManager users(PasswordEncoder passwordEncoder) {

        // 環境変数からパスワードを取得
        String adminPassword = System.getenv("PAVEMENT_ADMIN_PASSWORD");
        String userPassword = System.getenv("PAVEMENT_USER_PASSWORD");

        // パスワードが設定されていなければ起動を止める
        if (adminPassword == null || adminPassword.isBlank()
                || userPassword == null || userPassword.isBlank()) {

            throw new IllegalStateException(
                "ログイン用の環境変数が設定されていません。"
                + " PAVEMENT_ADMIN_PASSWORD と"
                + " PAVEMENT_USER_PASSWORD を設定してください。"
            );
        }

        // 管理者ユーザー
        UserDetails admin = User
                .withUsername("admin")
                .password(passwordEncoder.encode(adminPassword))
                .roles("ADMIN")
                .build();

        // 一般ユーザー
        UserDetails user = User
                .withUsername("user")
                .password(passwordEncoder.encode(userPassword))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(admin, user);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
            .authorizeHttpRequests(auth -> auth

                // 工事・売上・経費の登録処理は管理者だけ
                .requestMatchers(
                    HttpMethod.POST,
                    "/projects",
                    "/sales",
                    "/expenses"
                )
                .hasRole("ADMIN")

                // 登録・編集・削除は管理者だけ
                .requestMatchers(
                    "/projects/new",
                    "/projects/edit/**",
                    "/projects/delete/**",
                    "/sales/new",
                    "/sales/edit/**",
                    "/sales/delete/**",
                    "/expenses/new",
                    "/expenses/edit/**",
                    "/expenses/delete/**"
                )
                .hasRole("ADMIN")

                // 利益・工事別利益は両方が閲覧可能
                .requestMatchers(
                    "/profit",
                    "/project-profit/**"
                )
                .hasAnyRole("ADMIN", "USER")

                // その他はログインしていれば利用可能
                .anyRequest().authenticated()
            )

            // ログイン画面
            .formLogin(form -> form
                .permitAll()
            )

            // 権限がない場合の画面
            .exceptionHandling(exception -> exception
                .accessDeniedPage("/accessDenied")
            )

            // ログアウト
            .logout(logout -> logout
                .permitAll()
            );

        return http.build();
    }
}

