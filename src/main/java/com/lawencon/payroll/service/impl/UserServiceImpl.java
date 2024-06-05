package com.lawencon.payroll.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lawencon.payroll.constant.Roles;
import com.lawencon.payroll.dto.generalResponse.DeleteResDto;
import com.lawencon.payroll.dto.generalResponse.InsertResDto;
import com.lawencon.payroll.dto.generalResponse.UpdateResDto;
import com.lawencon.payroll.dto.user.ClientListResDto;
import com.lawencon.payroll.dto.user.ClientResDto;
import com.lawencon.payroll.dto.user.LoginReqDto;
import com.lawencon.payroll.dto.user.LoginResDto;
import com.lawencon.payroll.dto.user.PasswordReqDto;
import com.lawencon.payroll.dto.user.ProfileResDto;
import com.lawencon.payroll.dto.user.PsListResDto;
import com.lawencon.payroll.dto.user.UpdateUserReqDto;
import com.lawencon.payroll.dto.user.UserReqDto;
import com.lawencon.payroll.dto.user.UserResDto;
import com.lawencon.payroll.exception.ComparisonNotMatchException;
import com.lawencon.payroll.model.Company;
import com.lawencon.payroll.model.User;
import com.lawencon.payroll.repository.UserRepository;
import com.lawencon.payroll.service.ClientAssignmentService;
import com.lawencon.payroll.service.CompanyService;
import com.lawencon.payroll.service.EmailService;
import com.lawencon.payroll.service.FileService;
import com.lawencon.payroll.service.JwtService;
import com.lawencon.payroll.service.PrincipalService;
import com.lawencon.payroll.service.RoleService;
import com.lawencon.payroll.service.UserService;
import com.lawencon.payroll.util.FtpUtil;
import com.lawencon.payroll.util.GenerateUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final ClientAssignmentService clientAssignmentService;
    private final CompanyService companyService;
    private final EmailService emailService;
    private final FileService fileService;
    private final JwtService jwtService;
    private final PrincipalService principalService;
    private final RoleService roleService;

    @Override
    public LoginResDto loginUser(LoginReqDto data) {
        final var loginRes = new LoginResDto();

        final var email = data.getEmail();
        final Optional<User> user = Optional.ofNullable(userRepository.findByEmail(email));

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

        return loginRes;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        final var user = userRepository.findByEmail(email);
        if (user != null) {
            return new org.springframework.security.core.userdetails.User(email, user.getPassword(),
                    new ArrayList<>());
        }
        throw new UsernameNotFoundException("Invalid input!");
    }

    @Override
    @Transactional
    public InsertResDto createUser(UserReqDto data) {

        System.out.println(principalService.getUserId());

        final var insertRes = new InsertResDto();

        final var rawPassword = GenerateUtil.generateCode();
        final var password = passwordEncoder.encode(rawPassword);

        var user = new User();

        final var role = roleService.getById(data.getRoleId());

        final var email = data.getEmail();

        final var file = fileService.saveFile(data.getFileContent(), data.getFileExtension());

        Company company = null;
        if(Roles.RL003.name().equals(role.getRoleCode())) {
            final var companyReq = data.getCompanyReq();
            company = companyService.createCompany(companyReq);
        } else {
            company = companyService.findByCompanyName("PT. Lawencon International");
        }

        user.setUserName(data.getUserName());
        user.setEmail(email);
        user.setPassword(password);
        user.setRoleId(role);
        user.setPhoneNumber(data.getPhoneNumber());
        user.setProfilePictureId(file);
        user.setCompanyId(company);

        user.setCreatedBy(principalService.getUserId());

        user = userRepository.save(user);

        if (Roles.RL003.name().equals(role.getRoleCode())) {
            FtpUtil.createDirectory(user.getId());
        }

        final var subject = "New User Information";

        final var body = "Hello" + role.getRoleName() + "!\n"
        + "Here's your email and password :"
        + "Email : " + email + "\n" 
        + "Password : " + rawPassword + "\n";

        final Runnable runnable = () -> {
        emailService.sendEmail(email, subject, body);
        };

        final var mailThread = new Thread(runnable);
        mailThread.start();

        insertRes.setId(user.getId());
        insertRes.setMessage("User has been created");

        return insertRes;
    }

    @Override
    public List<UserResDto> getAllUsers() {
        final var usersRes = new ArrayList<UserResDto>();

        final var users = userRepository.getUsers("RL001");
        users.forEach(user -> {
            final var userRes = new UserResDto();

            userRes.setId(user.getId());
            userRes.setUserName(user.getUserName());
            userRes.setEmail(user.getEmail());
            userRes.setRoleName(user.getRoleId().getRoleName());
            userRes.setPhoneNumber(user.getPhoneNumber());
            userRes.setProfilePictureId(user.getProfilePictureId().getId());

            usersRes.add(userRes);
        });

        return usersRes;
    }

    @Override
    public List<PsListResDto> getAllPs() {
        final var psListRes = new ArrayList<PsListResDto>();

        final var psList = userRepository.findByRoleRoleCode(Roles.RL002.name());

        psList.forEach(ps -> {
            final var psRes = new PsListResDto();

            final var psId = ps.getId();

            psRes.setPsId(psId);
            psRes.setUserName(ps.getUserName());
            psRes.setEmail(ps.getEmail());
            psRes.setPhoneNo(ps.getPhoneNumber());
            psRes.setTotalClients(clientAssignmentService.getTotalClients(psId));
            psRes.setProfilePictureId(ps.getProfilePictureId().getId());

            psListRes.add(psRes);
        });

        return psListRes;
    }

    @Override
    public List<UserResDto> getAllUsersByCode(String code) {
        final var usersRes = new ArrayList<UserResDto>();

        final var users = userRepository.findByRoleRoleCode(code);

        users.forEach(user -> {
            final var userRes = new UserResDto();

            userRes.setId(user.getId());
            userRes.setUserName(user.getUserName());
            userRes.setEmail(user.getEmail());
            userRes.setRoleName(user.getRoleId().getRoleName());
            userRes.setPhoneNumber(user.getPhoneNumber());
            userRes.setProfilePictureId(user.getProfilePictureId().getId());

            usersRes.add(userRes);
        });

        return usersRes;
    }

    @Override
    public ClientListResDto getAllClients(String id) {
        final var clientListRes = new ClientListResDto();

        final var assignedClientsList = userRepository.findAllByRoleCodeAndId(Roles.RL003.name(), id);
        final var assignedClientsListRes = new ArrayList<ClientResDto>();

        assignedClientsList.forEach(assignedClient -> {
            final var client = new ClientResDto();

            client.setId(assignedClient.getId());
            client.setFullName(assignedClient.getUserName());
            client.setEmail(assignedClient.getEmail());
            client.setPhoneNumber(assignedClient.getPhoneNumber());
            client.setProfilePictureId(assignedClient.getProfilePictureId().getId());

            assignedClientsListRes.add(client);
        });

        final var unassignedClientsList = userRepository.findAllByRoleCode(Roles.RL003.name());
        final var unassignedClientsListRes = new ArrayList<ClientResDto>();

        unassignedClientsList.forEach(unassignedClient -> {
            final var client = new ClientResDto();

            client.setId(unassignedClient.getId());
            client.setFullName(unassignedClient.getUserName());
            client.setEmail(unassignedClient.getEmail());
            client.setPhoneNumber(unassignedClient.getPhoneNumber());
            client.setProfilePictureId(unassignedClient.getProfilePictureId().getId());
            
            unassignedClientsListRes.add(client);
        });

        clientListRes.setPsUserName(userRepository.getUserNameById(id));
        clientListRes.setAssignedClients(assignedClientsListRes);
        clientListRes.setUnassignedClients(unassignedClientsListRes);

        return clientListRes;
    }

    @Override
    public List<UserResDto> getAllUsersByPsId(String id) {
        final var usersRes = new ArrayList<UserResDto>();

        final var users = userRepository.findAllByRoleCodeAndId(Roles.RL003.name(), id);

        users.forEach(user -> {
            final var userRes = new UserResDto();

            userRes.setId(user.getId());
            userRes.setUserName(user.getUserName());
            userRes.setEmail(user.getEmail());
            userRes.setRoleName(user.getRoleId().getRoleName());
            userRes.setPhoneNumber(user.getPhoneNumber());
            userRes.setProfilePictureId(user.getProfilePictureId().getId());

            usersRes.add(userRes);
        });

        return usersRes;
    }

    @Override
    public List<UserResDto> getAllUsersByPsIdExcept(String id) {
        final var usersRes = new ArrayList<UserResDto>();

        final var users = userRepository.findAllByRoleCode(Roles.RL003.name());

        users.forEach(user -> {
            final var userRes = new UserResDto();

            userRes.setId(user.getId());
            userRes.setUserName(user.getUserName());
            userRes.setEmail(user.getEmail());
            userRes.setRoleName(user.getRoleId().getRoleName());
            userRes.setPhoneNumber(user.getPhoneNumber());
            userRes.setProfilePictureId(user.getProfilePictureId().getId());

            usersRes.add(userRes);
        });

        return usersRes;
    }

    @Override
    @Transactional
    public UpdateResDto updateUser(UpdateUserReqDto data) {
        var user = userRepository.findById(data.getId()).get();
        var counter = 0;
        final var oldVersion = user.getVer();

        var name = Optional.ofNullable(data.getUserName());
        if (name.isPresent() && name.get() != "") {
            user.setUserName(name.get());
            counter++;
        }

        var email = Optional.ofNullable(data.getEmail());
        if (email.isPresent() && email.get() != "") {
            user.setEmail(email.get());
            counter++;
        }

        var phoneNo = Optional.ofNullable(data.getPhoneNumber());
        if (phoneNo.isPresent() && phoneNo.get() != "") {
            user.setPhoneNumber(phoneNo.get());
            counter++;
        }

        var content = Optional.ofNullable(data.getFileContent());
        if (content.isPresent() && content.get() != "") {
            var file = user.getProfilePictureId();

            file.setFileContent(content.get());
            file.setFileExtension(data.getFileExtension());

            file = fileService.updateFile(file);

            user.setProfilePictureId(file);
        }

        var inputRole = Optional.ofNullable(data.getRoleId());
        if(inputRole.isPresent() && inputRole.get() != "") {
            final var role = roleService.getById(inputRole.get());

            user.setRoleId(role);
        }

        if(counter > 0) {
            user.setUpdatedBy(principalService.getUserId());
        }

        user = userRepository.saveAndFlush(user);

        final var newVersion = user.getVer();

        final var updateRes = new UpdateResDto();
        
        if (newVersion != oldVersion) {
            updateRes.setVersion(user.getVer());
        }
        
        updateRes.setMessage("User data has been updated");

        return updateRes;
    }

    @Override
    @Transactional
    public DeleteResDto deleteUserById(String id) {
        userRepository.deleteById(id);

        final var deleteRes = new DeleteResDto();

        deleteRes.setMessage("User has been deleted!");
        return deleteRes;
    }

    @Override
    @Transactional
    public UpdateResDto updatePassword(PasswordReqDto data) {
        final var updateRes = new UpdateResDto();

        final var userId = principalService.getUserId();
        var user = userRepository.findById(userId).get();
        final var oldPassword = data.getOldPassword();
        final var currentPassword = user.getPassword();
        
        if(!passwordEncoder.matches(oldPassword, currentPassword)) {
            throw new ComparisonNotMatchException("Incorrect Old Password!", HttpStatus.BAD_REQUEST);
        } else {
            final var newPassword = passwordEncoder.encode(data.getNewPassword());

            user.setPassword(newPassword);
            user.setUpdatedBy(userId);

            user = userRepository.saveAndFlush(user);

            updateRes.setMessage("Successfully update password!");
            updateRes.setVersion(user.getVer());
    
            return updateRes;
        }
        
    }

    @Override
    public ProfileResDto getProfile() {
        final var profileRes = new ProfileResDto();

        final var user = userRepository.findById(principalService.getUserId()).get();

        profileRes.setUserId(user.getId());
        profileRes.setUserName(user.getUserName());
        profileRes.setEmail(user.getEmail());
        profileRes.setPhoneNumber(user.getPhoneNumber());
        profileRes.setRoleName(user.getRoleId().getRoleName());
        profileRes.setProfilePictureId(user.getProfilePictureId().getId());

        return profileRes;
    }

    @Override
    public UserResDto getById(String id) {
        final UserResDto userResDto = new UserResDto();
        Optional<User> user = userRepository.findById(id);
        userResDto.setUserName(user.get().getUserName());
        userResDto.setEmail(user.get().getEmail());
        userResDto.setId(id);
        userResDto.setPhoneNumber(user.get().getPhoneNumber());
        userResDto.setProfilePictureId(user.get().getProfilePictureId().getId());
        userResDto.setRoleName(user.get().getRoleId().getRoleName());
        return userResDto;
    }
}
