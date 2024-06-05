package com.lawencon.payroll.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lawencon.payroll.dto.generalResponse.DeleteResDto;
import com.lawencon.payroll.dto.generalResponse.InsertResDto;
import com.lawencon.payroll.dto.generalResponse.UpdateResDto;
import com.lawencon.payroll.dto.user.ClientListResDto;
import com.lawencon.payroll.dto.user.LoginReqDto;
import com.lawencon.payroll.dto.user.LoginResDto;
import com.lawencon.payroll.dto.user.PasswordReqDto;
import com.lawencon.payroll.dto.user.ProfileResDto;
import com.lawencon.payroll.dto.user.PsListResDto;
import com.lawencon.payroll.dto.user.UpdateUserReqDto;
import com.lawencon.payroll.dto.user.UserReqDto;
import com.lawencon.payroll.dto.user.UserResDto;
import com.lawencon.payroll.exception.ComparisonNotMatchException;
import com.lawencon.payroll.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("")
    public ResponseEntity<InsertResDto> createUser(@RequestBody UserReqDto data) {
        final var insertRes = userService.createUser(data);
        return new ResponseEntity<>(insertRes, HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<List<UserResDto>> getAllUsers() {
        final var usersRes = userService.getAllUsers();
        return new ResponseEntity<>(usersRes, HttpStatus.OK);
    }

    @GetMapping("payroll-service")
    public ResponseEntity<List<PsListResDto>> getPayrollServiceUsers() {
        final var usersRes = userService.getAllPs();
        return new ResponseEntity<>(usersRes, HttpStatus.OK);
    }

    @GetMapping("client-assignments/{id}")
    public ResponseEntity<ClientListResDto> getClientList(@PathVariable String id) {
        final var clientListRes = userService.getAllClients(id);
        return new ResponseEntity<>(clientListRes, HttpStatus.OK);
    }

    @GetMapping("assigned/{id}")
    public ResponseEntity<List<UserResDto>> getAssignedPayrollService(@PathVariable String id) {
        final var usersRes = userService.getAllUsersByPsId(id);
        return new ResponseEntity<>(usersRes, HttpStatus.OK);
    }

    @GetMapping("not-assigned/{id}")
    public ResponseEntity<List<UserResDto>> getNotAssignedPayrollService(@PathVariable String id) {
        final var usersRes = userService.getAllUsersByPsIdExcept(id);
        return new ResponseEntity<>(usersRes, HttpStatus.OK);
    }

    @PatchMapping("")
    public ResponseEntity<UpdateResDto> updateUser(@RequestBody UpdateUserReqDto data) {
        final var updateRes = userService.updateUser(data);
        return new ResponseEntity<>(updateRes, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<DeleteResDto> deleteUser(@PathVariable String id) {
        final var deleteRes = userService.deleteUserById(id);
        return new ResponseEntity<>(deleteRes, HttpStatus.OK);
    }

    @PatchMapping("password")
    public ResponseEntity<UpdateResDto> updatePassword(@RequestBody PasswordReqDto data) {
        final var updateRes = userService.updatePassword(data);
        return new ResponseEntity<>(updateRes, HttpStatus.OK);
    }

    @GetMapping("profile")
    public ResponseEntity<ProfileResDto>getProfile() {
        final var profileRes = userService.getProfile();
        return new ResponseEntity<>(profileRes, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<UserResDto> getMethodName(@PathVariable String id) {
        final var userRes = userService.getById(id);
        return new ResponseEntity<>(userRes, HttpStatus.OK);
    }

}
