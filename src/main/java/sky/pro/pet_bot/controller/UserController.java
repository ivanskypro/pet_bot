package sky.pro.pet_bot.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sky.pro.pet_bot.dao.UserRepository;
import sky.pro.pet_bot.model.Pet;
import sky.pro.pet_bot.model.User;
import sky.pro.pet_bot.service.impl.UserServiceInterfaceImpl;

import java.util.Collection;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserServiceInterfaceImpl userServiceInterface;
    private final UserRepository userRepository;

    public UserController(UserServiceInterfaceImpl userServiceInterface, UserRepository userRepository) {
        this.userServiceInterface = userServiceInterface;
        this.userRepository = userRepository;
    }

    @GetMapping("/{id}")
    public User getUserById (@PathVariable Long id){
        return userRepository.getById(id);
    }

    @PostMapping
    public ResponseEntity<User> addUser (@RequestBody User user){
        return ResponseEntity.ok(userServiceInterface.addUser(user));
    }

    @GetMapping ("/getAll")
    public Collection<User> getAllUsers (){
        return userServiceInterface.getAllUsers();
    }

    @PutMapping
    public ResponseEntity <User> updateUser (@RequestBody User user){
        return ResponseEntity.ok(userRepository.save(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity <User> deletePet (@PathVariable Long id){
        userRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
