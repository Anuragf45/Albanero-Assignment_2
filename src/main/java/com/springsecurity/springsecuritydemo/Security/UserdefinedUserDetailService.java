
package com.springsecurity.springsecuritydemo.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.springsecurity.springsecuritydemo.Model.User;
import com.springsecurity.springsecuritydemo.repository.UserRepo;

@Component
public class UserdefinedUserDetailService implements UserDetailsService {
    @Autowired
    UserRepo db;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = db.findByuserName(userName);
        return new UserdefinedUserDetails(user);
    }
}
