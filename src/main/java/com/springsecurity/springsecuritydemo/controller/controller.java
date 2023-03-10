package com.springsecurity.springsecuritydemo.controller;

import com.springsecurity.springsecuritydemo.Model.Login;
import com.springsecurity.springsecuritydemo.Model.Notes;
import com.springsecurity.springsecuritydemo.Model.User;
import com.springsecurity.springsecuritydemo.service.NotesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;


import com.springsecurity.springsecuritydemo.exception.NoUserFound;
import com.springsecurity.springsecuritydemo.service.JwtService;
import com.springsecurity.springsecuritydemo.service.UserService;

@RestController
public class controller {

    Logger logger= LoggerFactory.getLogger(controller.class);
    @Autowired
    UserService userService;
    @Autowired
    JwtService jwtService;

    @Autowired
    NotesService notesService;
    @Autowired
    AuthenticationManager authenticationManager;

    @PostMapping("/create")
    ResponseEntity<?> addUser(@RequestBody User u) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUser(u));
    }

    @PostMapping("/login")
    ResponseEntity<?> login(@RequestBody Login login) {
        System.out.println("Hello");
     //   logger.info("Testing the login api");
        Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(login.getUserName(),
        login.getPassword()));
        if (authentication.isAuthenticated()) {
        System.out.println("authenticated");

        return ResponseEntity.status(HttpStatus.OK).body(jwtService.generateToken(login.getUserName()));
        } else {
        return new ResponseEntity<>("No User Found",HttpStatus.BAD_REQUEST) ;
        }
    }




    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/users")
    ResponseEntity<?> getAllusers() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAllusers());
    }

    @GetMapping("/users/{userName}")
    ResponseEntity<?> getuser(@PathVariable("userName") String userName) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getuser(userName));
    }
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/user")
    String user() {
        return "in user";
    }
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/admin")
    String admin() {
        return "in admin";
    }




    //api's for notes

    @PostMapping("/createNote")

    public ResponseEntity<?> createUser(@RequestBody Notes notes){
        logger.info("creating notes");
        return new ResponseEntity<>(notesService.createNotes(notes),HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/getAllNotes")
    public ResponseEntity<?> getAllNotes(){
        logger.info("Getting all the notes");
        return new ResponseEntity<>(notesService.getNotes(),HttpStatus.OK);
    }

    @PostMapping("/getByTitle/{userId}")
    public ResponseEntity<?> getByTitle(@PathVariable String userId,@RequestHeader String token,@RequestBody Notes notes){
        logger.info("Getting note by title");
        return new ResponseEntity<>(notesService.getNotesByTitle(notes.getTitle(),userId,token),HttpStatus.OK);
    }

    @PutMapping("/updateNote/{userId}")
    public ResponseEntity<?> updateNote(@PathVariable String userId,@RequestBody Notes notes ,@RequestHeader String token){
       logger.info("updating user");
       String title=notes.getTitle();
    return new ResponseEntity<>(notesService.updateNotesByTitle(userId, notes.getDescription(), notes.getTitle(),token),HttpStatus.OK);
    }




    @DeleteMapping("/deleteByUser/{id}/{userId}")
    public ResponseEntity<?> deleteByUser(@PathVariable String id,@PathVariable String userId,@RequestHeader String token){
        logger.info("deleting notes");
        return new ResponseEntity<>(notesService.deleteByUser(id, userId,token),HttpStatus.OK);
    }

}
