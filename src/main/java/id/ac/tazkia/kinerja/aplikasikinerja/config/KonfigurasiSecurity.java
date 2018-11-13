package id.ac.tazkia.kinerja.aplikasikinerja.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class KonfigurasiSecurity extends WebSecurityConfigurerAdapter {
    private static final String SQL_LOGIN
            = "select u.username as username,p.password as password, active\n"
            + "FROM s_user u\n"
            + "inner join s_user_password p on p.id_user = u.id\n"
            + "WHERE u.username = ?";

    private static final String SQL_PERMISSION
            = "select u.username, p.permission_value as authority "
            + "from s_user u "
            + "inner join s_role r on u.id_role = r.id "
            + "inner join s_role_permission rp on rp.id_role = r.id "
            + "inner join s_permission p on rp.id_permission = p.id "
            + "where u.username = ?";

    @Autowired
    private DataSource ds;

    @Value("${security.bcrypt.strength}") private Integer bcryptStrength;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        auth
                .jdbcAuthentication()
                .dataSource(ds)
                .usersByUsernameQuery(SQL_LOGIN)
                .authoritiesByUsernameQuery(SQL_PERMISSION)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/daftarbawahan/list").hasAnyAuthority("VIEW_SUPERIOR","VIEW_KABAG","VIEW_CHECKER","VIEW_SUPERADMIN")
                .antMatchers("/daftarbawahan/komen").hasAnyAuthority("VIEW_SUPERIOR","VIEW_KABAG","VIEW_CHECKER","VIEW_SUPERADMIN")
                .antMatchers("/daftarbawahan/detail").hasAnyAuthority("VIEW_SUPERIOR","VIEW_KABAG","VIEW_CHECKER","VIEW_SUPERADMIN")
                .antMatchers("/staff/list").hasAnyAuthority("VIEW_STAFF","VIEW_KABAG","VIEW_SUPERADMIN")
                .antMatchers("/staff/form").hasAnyAuthority("VIEW_STAFF","VIEW_KABAG","VIEW_SUPERADMIN")
                .antMatchers("/staff/update").hasAnyAuthority("VIEW_STAFF","VIEW_KABAG","VIEW_SUPERADMIN")
                .antMatchers("/staff/detail").hasAnyAuthority("VIEW_STAFF","VIEW_KABAG","VIEW_SUPERADMIN")
                .antMatchers("/role/form").hasAnyAuthority("VIEW_SUPERIOR","VIEW_STAFF","VIEW_KABAG","VIEW_SUPERADMIN")
                .antMatchers("/role/list").hasAnyAuthority("VIEW_STAFF","VIEW_KABAG","VIEW_SUPERADMIN")
                .antMatchers("/periode/list").hasAnyAuthority("VIEW_STAFF","VIEW_KABAG","VIEW_SUPERADMIN")
                .antMatchers("/periode/form").hasAnyAuthority("VIEW_STAFF","VIEW_KABAG","VIEW_SUPERADMIN")
                .antMatchers("/kpi/form").hasAnyAuthority("VIEW_SUPERIOR","VIEW_STAFF","VIEW_KABAG","VIEW_SUPERADMIN")
                .antMatchers("/kpi/hapus").hasAnyAuthority("VIEW_SUPERIOR","VIEW_STAFF","VIEW_KABAG","VIEW_SUPERADMIN")
                .antMatchers("/kpi/hasil").hasAnyAuthority("VIEW_SUPERIOR","VIEW_STAFF","VIEW_KABAG","VIEW_SUPERADMIN")
                .antMatchers("/kpi/list").hasAnyAuthority("VIEW_SUPERIOR","VIEW_STAFF","VIEW_KABAG","VIEW_SUPERADMIN")
                .antMatchers("/kpi/update").hasAnyAuthority("VIEW_SUPERIOR","VIEW_STAFF","VIEW_KABAG","VIEW_SUPERADMIN")
                .antMatchers("/pengecek/list").hasAnyAuthority("VIEW_CHECKER","VIEW_SUPERADMIN")
                .anyRequest().authenticated()
                .and().logout().permitAll()
                .and().formLogin().defaultSuccessUrl("/index")
                .loginPage("/login")
                .permitAll();

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/favicon.ico")
                .antMatchers("/info")
                .antMatchers("/js/*")
                .antMatchers("/img/**")
                .antMatchers("/images/*")
                .antMatchers("/")
                .antMatchers("/reset-sukses")
                .antMatchers("/reset")
                .antMatchers("/404")
                .antMatchers("/confirm")
                .antMatchers("/fonts/*")
                .antMatchers("/css/*");

    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(bcryptStrength);
    }

}