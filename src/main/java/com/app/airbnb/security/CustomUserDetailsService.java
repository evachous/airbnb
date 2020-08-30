package com.app.airbnb.security;

import com.app.airbnb.repositories.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.Collections.emptyList;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.app.airbnb.model.User applicationUser = this.userRepository.findByUsername(username);
        if (applicationUser == null || !applicationUser.getIsApproved()) {
            throw new UsernameNotFoundException(username);
        }
        return buildUserForAuthentication(applicationUser);
        //return new User(applicationUser.getUsername(), applicationUser.getPassword(), buildUserAuthority(applicationUser));
    }

    public User buildUserForAuthentication(com.app.airbnb.model.User applicationUser) {
        List<GrantedAuthority> authorities = buildUserAuthority(applicationUser);
        return new User(applicationUser.getUsername(), applicationUser.getPassword(),
                true, true, true, true, authorities);
    }

    public List<GrantedAuthority> buildUserAuthority(com.app.airbnb.model.User applicationUser) {

        Set<GrantedAuthority> setAuths = new HashSet<GrantedAuthority>();

        if (applicationUser.getIsAdmin())
            setAuths.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        if (applicationUser.getIsHost())
            setAuths.add(new SimpleGrantedAuthority("ROLE_HOST"));
        if (applicationUser.getIsGuest())
            setAuths.add(new SimpleGrantedAuthority("ROLE_GUEST"));

        return new ArrayList<GrantedAuthority>(setAuths);
    }
}
