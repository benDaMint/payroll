package com.lawencon.payroll.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lawencon.payroll.dto.user.LoginReqDto;
import com.lawencon.payroll.dto.user.LoginResDto;
import com.lawencon.payroll.exception.FailCheckException;
import com.lawencon.payroll.model.User;
import com.lawencon.payroll.repository.UserRepository;
import com.lawencon.payroll.service.JwtService;
import com.lawencon.payroll.service.LoginService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final JwtService jwtService;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public LoginResDto loginUser(LoginReqDto data) {
        final var loginRes = new LoginResDto();

        final var email = data.getEmail();
        try {
            final Optional<User> user = Optional.ofNullable(userRepository.findByEmail(email).get());

            if (user.isPresent()) {
                final var loginPassword = data.getPassword();
                final var currentPassword = user.get().getPassword();

                if (!passwordEncoder.matches(loginPassword, currentPassword)) {
                    throw new FailCheckException("Incorrect Password", HttpStatus.BAD_REQUEST);
                } else {
                    final var cal = Calendar.getInstance();
                    cal.setTime(new Date());
                    cal.add(Calendar.HOUR_OF_DAY, 1);

                    final var claims = new HashMap<String, Object>();
                    claims.put("exp", cal.getTime());
                    claims.put("id", user.get().getId());

                    final var token = jwtService.generateJwt(claims);

                    final var role = user.get().getRoleId();

                    final var file = user.get().getProfilePictureId();

                    loginRes.setUserId(user.get().getId());
                    loginRes.setUserName(user.get().getUserName());
                    loginRes.setRoleCode(role.getRoleCode());
                    loginRes.setToken(token);

                    if (file != null) {
                        loginRes.setFileId(file.getId());
                    }
                }

                return loginRes;
            } else {
                throw new FailCheckException("Email Not Found", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            throw new FailCheckException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
