package com.storefront.user.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.storefront.user.config.UserConstants;
import com.storefront.user.dto.AddressDto;
import com.storefront.user.dto.AuthenticationRequest;
import com.storefront.user.dto.AuthenticationResponse;
import com.storefront.user.dto.JwtTokenResult;
import com.storefront.user.dto.RefreshTokenDto;
import com.storefront.user.dto.RefreshTokenRequest;
import com.storefront.user.dto.UserDto;
import com.storefront.user.entity.Address;
import com.storefront.user.entity.RefreshToken;
import com.storefront.user.entity.User;
import com.storefront.user.exception.RefreshTokenReuseDetectedException;
import com.storefront.user.exception.ResourceNotFoundException;
import com.storefront.user.exception.UserAlreadyExistsException;
import com.storefront.user.mapper.UserMapper;
import com.storefront.user.repository.AddressRepository;
import com.storefront.user.repository.UserRepository;
import com.storefront.user.security.RequestContext;
import com.storefront.user.security.UserPrincipal;
import com.storefront.user.security.context.SessionContext;
import com.storefront.user.security.context.SessionContextResolver;
import com.storefront.user.security.jwt.JwtTokenService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final AddressRepository addressRepository;
	private final AuthenticationManager authenticationManager;
    private final LoginAttemptService loginAttemptService;
    private final JwtTokenService jwtTokenService;
    private final RefreshTokenService refreshTokenService;
    private final SecurityAuditService securityAuditService;
    private final SessionService sessionService;
    private final SessionContextResolver sessionContextResolver;

	@Override
	public void register(UserDto userDto) {
		Optional<User> optUser =  userRepository.findByEmail(userDto.getEmail());
		if(optUser.isPresent())
			throw new UserAlreadyExistsException("User already registered with given email "
                    +userDto.getEmail());
		User user = UserMapper.mapToUser(userDto, new User());
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRole("CUSTOMER");
		userRepository.save(user);	
	}

	@Override
	public AuthenticationResponse authenticate(AuthenticationRequest request, HttpServletRequest servletRequest) {
		RequestContext context = RequestContext.builder()
                .ipAddress(servletRequest.getRemoteAddr()).userAgent(servletRequest.getHeader("User-Agent"))
                .correlationId(servletRequest.getHeader("X-Correlation-Id")).build();
	    try {
	    	SessionContext sessionContext = sessionContextResolver.resolve(servletRequest);
            Authentication authentication = authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
            securityAuditService.log(UserConstants.SECURITY_AUDIT_EVENT_LOGIN_SUCCESS, principal.getUser(), "User logged in successfully.");
            loginAttemptService.recordSuccessfulLogin(principal.getId(), request.getEmail(), context);
            JwtTokenResult jwtTokenResult = jwtTokenService.generateAccessToken(principal);
            RefreshTokenDto refreshTokenDto = refreshTokenService.create(principal.getUser());
            securityAuditService.log(UserConstants.SECURITY_AUDIT_EVENT_ACCESS_TOKEN_CREATED, principal.getUser(), "JWT access token generated.");
            sessionService.createSession(principal.getUser(), refreshTokenDto.getTokenId().toString(), sessionContext);
            return AuthenticationResponse.builder()
                    .accessToken(jwtTokenResult.getAccessToken())
                    .tokenType(UserConstants.SECURITY_TOKEN_TYPE_BEARER)
                    .accessTokenExpiresAt(jwtTokenResult.getExpiresAt())
                    .refreshToken(refreshTokenDto.getRefreshToken())
                    .refreshTokenExpiresAt(refreshTokenDto.getExpiresAt())
                    .build();
        } catch (LockedException ex) {
            loginAttemptService.recordFailedLogin(null, request.getEmail(), context, UserConstants.LOGIN_FAILURE_REASON_ACCOUNT_DISABLED);
            throw new LockedException("Account has been locked.");
        } catch (BadCredentialsException ex) {
            loginAttemptService.recordFailedLogin(null, request.getEmail(), context, UserConstants.LOGIN_FAILURE_REASON_INVALID_CREDENTIALS);
            throw new BadCredentialsException("Invalid username or password.");
        }  catch (Exception ex) {
            loginAttemptService.recordFailedLogin(null, request.getEmail(), context, UserConstants.LOGIN_FAILURE_REASON_LOGIN_ERROR);
            throw new BadCredentialsException("Error while login.");
        }
	}


	@Override
	public UserDto getUser(String email) {
		User user =  userRepository.findByEmail(email).orElseThrow(
				() -> new ResourceNotFoundException("User", "email", email));
		return UserMapper.mapToUserDto(user, new UserDto());
	}

	@Override
	public boolean updateUser(UserDto userDto) {
		boolean isUpdated = false;
		User user =  userRepository.findByEmail(userDto.getEmail()).orElseThrow(
				() -> new ResourceNotFoundException("User", "email", userDto.getEmail()));
		user.setEmail(userDto.getEmail());
		user.setName(userDto.getName());
		userRepository.save(user);
		isUpdated = true;
		return isUpdated;
	}

	@Override
	public List<AddressDto> getAddress(String userId) {
		User user =  userRepository.findById(Long.valueOf(userId)).orElseThrow(
				() -> new ResourceNotFoundException("User", "Id", userId));
		return UserMapper.mapToUserDto(user, new UserDto()).getAddress();
	}

	@Override
	public boolean addAddress(String userId, AddressDto addressDto) {
		boolean isUpdated = false;
		User user =  userRepository.findById(Long.valueOf(userId)).orElseThrow(
				() -> new ResourceNotFoundException("User", "Id", userId));
		Address address = UserMapper.mapToAddress(addressDto, user);
		user.getAddress().add(address);
		userRepository.save(user);
		isUpdated = true;
		return isUpdated;
	}

	@Override
	public boolean updateAddress(AddressDto addressDto) {
		boolean isUpdated = false;
		Address address = addressRepository.findById(addressDto.getAddressId()).orElseThrow(
				() -> new ResourceNotFoundException("Address", "id", String.valueOf(addressDto.getAddressId())));
		address.setAddress(addressDto.getAddress());
        address.setLandmark(addressDto.getLandmark());
        address.setCity(addressDto.getCity());
        address.setState(addressDto.getState());
        address.setCountry(addressDto.getCountry());
        address.setPincode(addressDto.getPincode());
        address.setPhoneNumber(addressDto.getPhoneNumber());
        address.setReceiverName(addressDto.getReceiverName());
        address.setDefault(addressDto.isDefault());
        addressRepository.save(address);
        isUpdated = true;
		return isUpdated;
	}

	@Override
	public boolean deleteAddress(String addressId) {
		boolean isdeleted = false;
		Address address = addressRepository.findById(Long.valueOf(addressId)).orElseThrow(
				() -> new ResourceNotFoundException("Address", "id", addressId));
		addressRepository.delete(address);
		isdeleted = true;
		return isdeleted;
	}

	@Override
	@Transactional(noRollbackFor = RefreshTokenReuseDetectedException.class)
	public AuthenticationResponse refreshToken(RefreshTokenRequest request, HttpServletRequest servletRequest) {
		try {
			SessionContext sessionContext = sessionContextResolver.resolve(servletRequest);
			RefreshToken refreshToken = refreshTokenService.validate(request.getRefreshToken());
			User user = refreshToken.getUser();
			sessionService.terminateSession(refreshToken.getTokenId().toString());
			JwtTokenResult jwtToken = jwtTokenService.generateAccessToken(UserPrincipal.fromUser(user));
			RefreshTokenDto newRefreshToken = refreshTokenService.rotate(refreshToken.getHashedToken());
			sessionService.createSession(refreshToken.getUser(), refreshToken.getTokenId().toString(), sessionContext);
			return AuthenticationResponse.builder()
					.tokenType(UserConstants.SECURITY_TOKEN_TYPE_BEARER)
					.accessToken(jwtToken.getAccessToken())
					.accessTokenExpiresAt(jwtToken.getExpiresAt())
					.refreshToken(newRefreshToken.getRefreshToken())
					.refreshTokenExpiresAt(newRefreshToken.getExpiresAt())
					.build();
		}
		catch (RefreshTokenReuseDetectedException ex) {
			securityAuditService.log(UserConstants.SECURITY_AUDIT_EVENT_REFRESH_REUSE_DETECTED, ex.getUser(), "Refresh token reuse detected.");
			refreshTokenService.revokeAll(ex.getUser());
			throw ex;
		}
	}
	
	@Override
	public String logout(RefreshTokenRequest request) {
		RefreshToken refreshToken = refreshTokenService.validate(request.getRefreshToken());
		refreshTokenService.revoke(request.getRefreshToken());
		sessionService.terminateSession(refreshToken.getTokenId().toString());
		securityAuditService.log(UserConstants.SECURITY_AUDIT_EVENT_LOGOUT, refreshToken.getUser(), "User logged out successfully.");
		return UserConstants.LOGGED_OUT_SUCCESSFULLY_MSG;

	}
	
	@Override
	public String logoutAll() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    if (authentication == null || !authentication.isAuthenticated()) 
	        throw new InsufficientAuthenticationException("User is not authenticated.");
	    String email = authentication.getName();
	    User user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new UsernameNotFoundException("User not found."));
	    refreshTokenService.revokeAll(user);
	    sessionService.terminateAllSessions(user);
	    return UserConstants.LOGGED_OUT_ALL_SUCCESSFULLY_MSG;
	}

}