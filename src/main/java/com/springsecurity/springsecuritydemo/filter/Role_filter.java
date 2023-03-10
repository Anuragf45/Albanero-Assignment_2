package com.springsecurity.springsecuritydemo.filter;

import com.springsecurity.springsecuritydemo.Model.User;
import com.springsecurity.springsecuritydemo.repository.UserRepo;
import com.springsecurity.springsecuritydemo.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

 @Component
public class Role_filter {
     @Autowired
     UserRepo repo2;
     @Autowired
     JwtService jwtService;
    public String verifyRole(String userId,String token){
        Optional<User> optional1=repo2.findById(userId);
        //System.out.println(optional1.get());
        String userName= jwtService.extractUsername(token);
        //System.out.println(userName);
        if(optional1.get().getUserName().equals(userName)){
            if(optional1.get().getRoles().contains("ROLE_ADMIN")){
                return "ROLE_ADMIN";
            }else{
                return "ROLE USER";
            }
        }else{
            return "ROLE is yet to be permitted";
        }
    }
}


