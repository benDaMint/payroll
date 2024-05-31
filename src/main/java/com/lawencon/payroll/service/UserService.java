package com.lawencon.payroll.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

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

public interface UserService extends UserDetailsService {
    LoginResDto loginUser(LoginReqDto data);

    InsertResDto createUser(UserReqDto data);

    List<UserResDto> getAllUsers();

    List<PsListResDto> getAllPs();

    ClientListResDto getAllClients(String id);

    List<UserResDto> getAllUsersByCode(String code);

    List<UserResDto> getAllUsersByPsId(String id);

    List<UserResDto> getAllUsersByPsIdExcept(String id);
    
    UpdateResDto updateUser(UpdateUserReqDto data);
    
    DeleteResDto deleteUserById(String id);

    UpdateResDto updatePassword(PasswordReqDto data);

    ProfileResDto getProfile();
}
