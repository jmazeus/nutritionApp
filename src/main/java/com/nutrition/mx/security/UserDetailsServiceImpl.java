package com.nutrition.mx.security;

import com.nutrition.mx.model.User;
import com.nutrition.mx.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserRepository userRepository;
	private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

	@Override
	public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
	    User user = userRepository.findByUsername(usernameOrEmail)
	            .or(() -> userRepository.findByEmail(usernameOrEmail))
	            .orElseThrow(() -> new UsernameNotFoundException("Usuario o email no encontrado: " + usernameOrEmail));

	    UserDetails uDetails = new org.springframework.security.core.userdetails.User(
	            user.getUsername(),
	            user.getPassword(),
	            user.getRoles().stream()
	                    .map(role -> new SimpleGrantedAuthority(role.name()))
	                    .collect(Collectors.toList())
	    );
	    
	    log.info("" + uDetails.getAuthorities());
	    
	    return uDetails;
	}
}
