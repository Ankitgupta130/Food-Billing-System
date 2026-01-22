package com.avion.billing_system.service;

import com.avion.billing_system.dto.ActiveDeactiveReq;
import com.avion.billing_system.dto.LoginResponse;
import com.avion.billing_system.dto.Userdto;
import com.avion.billing_system.entity.User;
import com.avion.billing_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String EMPLOYEE_CACHE_KEY = "ALL_EMPLOYEES";

    public LoginResponse login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String hashedPassword = DigestUtils.md5DigestAsHex(password.getBytes());

        if (!user.getPassword().equals(hashedPassword)) {
            throw new RuntimeException("Invalid credentials");
        }


        LoginResponse response = new LoginResponse();
        response.setUserId(user.getId());
        response.setUsername(user.getUsername());
        response.setRole(user.getRole().name());
        response.setStatus(user.getStatus());
        response.setMessage("Login Successful");

        return response;

    }

    public Userdto setUserStatus(ActiveDeactiveReq activeDeactiveReq) {



        redisTemplate.delete(EMPLOYEE_CACHE_KEY);

        Optional <User> user = userRepository.findById(activeDeactiveReq.getId());
        User user1 = new User();
        user1.setId(user.get().getId());
        user1.setUsername(user.get().getUsername());
        user1.setPassword(user.get().getPassword());
        user1.setRole(user.get().getRole());
        user1.setStatus(activeDeactiveReq.getAction());

        User user2 = userRepository.save(user1);

        Userdto userdto = new Userdto();
        userdto.setId(user2.getId());
        userdto.setUsername(user2.getUsername());
        userdto.setPassword(user2.getPassword());
        userdto.setRole(user2.getRole());
        userdto.setStatus(user2.getStatus());

        return userdto;
    }

}
