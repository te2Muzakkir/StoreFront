package com.storefront.user.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.storefront.user.entity.User;
import com.storefront.user.repository.UserRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(readOnly = true)
@Slf4j
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
	
	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		log.debug("Loading user with email [{}]", email); 
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> {
					log.warn("Authentication failed. User not found: {}", email); 
					return new UsernameNotFoundException( "Invalid username or password."); 
				}); 
		log.debug("Successfully loaded user [{}]", email); 
		return UserPrincipal.fromUser(user);
	}

}