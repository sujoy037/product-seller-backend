package com.sha.security;

import java.util.Set;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sha.model.User;
import com.sha.service.UserService;
import com.sha.utils.SecurityUtils;

@Service
public class CustomUserDetailsService  implements UserDetailsService{
	
	   private final UserService userService;
	   public CustomUserDetailsService(@Lazy UserService userService)
	    {
	        this.userService = userService;
	    }
	   
	   @Override
	    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
	    {
	        User user = userService.findByUsername(username)
	                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

	        Set<GrantedAuthority> authorities = Set.of(SecurityUtils.convertToAuthority(user.getRole().name()));

	        return UserPrincipal.builder()
	                .user(user)
	                .id(user.getId())
	                .username(user.getUsername())
	                .password(user.getPassword())
	                .authorities(authorities)
	                .build();
	    }

}
