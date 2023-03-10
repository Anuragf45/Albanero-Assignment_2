package com.springsecurity.springsecuritydemo.service;

import com.springsecurity.springsecuritydemo.Model.Notes;
import com.springsecurity.springsecuritydemo.Model.User;
import com.springsecurity.springsecuritydemo.filter.Role_filter;
import com.springsecurity.springsecuritydemo.repository.ToDoRepo;
import com.springsecurity.springsecuritydemo.repository.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class NotesService {

    Logger logger = LoggerFactory.getLogger(NotesService.class);
    @Autowired
    ToDoRepo repo;

    @Autowired
    UserRepo repo2;

    @Autowired
    JwtService jwtService;

    @Autowired
    Role_filter roleFilter;

    public ResponseEntity<?> createNotes(Notes notes){
        logger.info("Creating notes");
        if(notes.getDescription().length()>36){
            return new ResponseEntity<>("Description should not extend more than 36 characters",HttpStatus.BAD_REQUEST);
        }else{
            return new ResponseEntity<>(repo.save(notes), HttpStatus.CREATED);
        }
    }

    public ResponseEntity<?> getNotes(){
        logger.info("getting all notes");
        return new ResponseEntity<>(repo.findAll(),HttpStatus.OK);
    }

    //need to work on
    public ResponseEntity<?> getNotesByTitle(String title,String userId,String token){

        if(roleFilter.verifyRole( userId,token).equals("ROLE_USER")){
            logger.info("Getting notes by title");
            Optional<Notes> optional1= Optional.ofNullable(repo.findByTitle(title));
            if(optional1.isPresent()){
                if(optional1.get().getUserId().equals(userId)){
                    return new ResponseEntity<>(repo.findByTitle(title),HttpStatus.OK);
                }else{
                    return new ResponseEntity<>("You don't have any note with this title",HttpStatus.BAD_REQUEST);
                }
            }
            return new ResponseEntity<>("No notes found with this title",HttpStatus.OK);
        }else if(roleFilter.verifyRole( userId,token).equals("ROLE_ADMIN")){
            logger.info("Getting note by user ");
            Optional<Notes> optional1= Optional.ofNullable(repo.findByTitle(title));
            if(optional1.isPresent()){
                return new ResponseEntity<>(repo.findByTitle(title),HttpStatus.OK);
            }else{
                return new ResponseEntity<>("No notes found",HttpStatus.BAD_REQUEST);
            }
        }else{
            return new ResponseEntity<>("role yet to be verified",HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> updateNotesByTitle(String userId, String description,String title,String token){

        String username=jwtService.extractUsername(token);
        System.out.println(username);
        Optional<User> optional2=repo2.findById(userId);
        List list= optional2.get().getRoles();

        if(list.contains("ROLE_ADMIN")==true  && optional2.get().getUserName().equals(username)){
            logger.info("Updating note");
            Optional<Notes> optional1= Optional.ofNullable(repo.findByTitle(title));
            if(optional1.isPresent()){
                optional1.get().setDescription(description);
                repo.save(optional1.get());
                return new ResponseEntity<>("Note updated successfully by the admin",HttpStatus.OK);
            }else{
                return new ResponseEntity<>("No notes found with this title",HttpStatus.BAD_REQUEST);
            }
        }else{
            logger.info("updating a note with the given title");
            Optional<Notes> optional1= Optional.ofNullable(repo.findByTitle(title));

            if(optional1.isPresent()){
                if(userId.equals(optional1.get().getUserId())){
                    System.out.println(optional1.get());
                    Notes note1=  optional1.get();
                    note1.setDescription(description);
                    repo.save(note1);
                    return new ResponseEntity<>("Notes description have been updated successfully by the user",HttpStatus.OK);
                }else{
                    return new ResponseEntity<>("You're not authorized to fetch these details.Try to access with your userId",HttpStatus.BAD_REQUEST);
                }
            }else{
                return new ResponseEntity<>("No Notes found with this title",HttpStatus.BAD_REQUEST);
            }
        }
    }
    public ResponseEntity<?> deleteByUser(String id,String userId,String token){

        String username=jwtService.extractUsername(token);
        System.out.println(username);
          Optional<Notes> optional1=repo.findById(id);

        Optional<User> optional2=repo2.findById(userId);
       List list= optional2.get().getRoles();
        System.out.println(list.contains("ROLE_ADMIN"));
        if(list.contains("ROLE_ADMIN")==true  && optional2.get().getUserName().equals(username)){
            logger.info("deleting note");
//            Optional<Notes> optional1=repo.findById(id);
            if(optional1.isPresent()){
                repo.deleteById(id);
                return new ResponseEntity<>("note deleted successfully by admin",HttpStatus.OK);
            }else{
                return new ResponseEntity<>("No note found with this ID",HttpStatus.BAD_REQUEST);
            }
        }else{
            if(optional1.isPresent()){
                if(optional1.get().getUserId().equals(userId)){
                    repo.deleteById(id);
                    return new ResponseEntity<>("Note deleted successfully by user",HttpStatus.OK);
                }else{
                    return new ResponseEntity<>("You're not authorized to do so",HttpStatus.BAD_REQUEST);
                }
            }else{
                return new ResponseEntity<>("No notes found",HttpStatus.BAD_REQUEST);
            }
        }
    }
}
