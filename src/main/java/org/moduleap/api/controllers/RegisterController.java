package org.moduleap.api.controllers;

import org.moduleap.api.models.User;
import org.moduleap.api.models.VerifyUser;
import org.moduleap.api.repositories.UserRepository;
import org.moduleap.api.repositories.VerifyUserRepository;
import org.moduleap.api.services.EmailServiceImpl;
import org.moduleap.api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@PropertySource("classpath:general.properties")
public class RegisterController {

    private UserRepository userRepository;
    private VerifyUserRepository verifyUserRepository;
    private UserService userService;
    private EmailServiceImpl emailService;
    @Value("${verification.enabled:false}")
    private boolean verificationEnabled;
    @Value("${register.passwordMinLength:8}")
    private int passwordMinLength;

    //bonusrequirements
    @Value("${register.firstNameRequired:false}")
    private boolean firstNameRequired;
    @Value("${register.lastNameRequired:false}")
    private boolean lastNameRequired;
    @Value("${register.postalCodeRequired:false}")
    private boolean postalCodeRequired;
    @Value("${register.streetRequired:false}")
    private boolean streetRequired;
    @Value("${register.countryRequired:false}")
    private boolean countryRequired;

    @Autowired
    public RegisterController(UserRepository userRepository, VerifyUserRepository verifyUserRepository, UserService userService, EmailServiceImpl emailService) {
        this.userRepository = userRepository;
        this.verifyUserRepository = verifyUserRepository;
        this.userService = userService;
        this.emailService = emailService;
    }

    @PostMapping(value = "/register")
    public ResponseEntity<?> login(@RequestBody User user) {
        //verification of userdata
        if (user.getUsername() == null || user.getPassword() == null || user.getPassword().length() < passwordMinLength || user.getEmail() == null) {
            return new ResponseEntity<>("Bad username or password", HttpStatus.BAD_REQUEST);
        }

        //custom requirements
        if(firstNameRequired && user.getFirstName().equals("")) return new ResponseEntity<>("First Name is required", HttpStatus.BAD_REQUEST);
        if(lastNameRequired && user.getLastName().equals("")) return new ResponseEntity<>("Last Name is required", HttpStatus.BAD_REQUEST);
        if(postalCodeRequired && user.getPostalCode() == null) return new ResponseEntity<>("Postalcode is required", HttpStatus.BAD_REQUEST);
        if(streetRequired && user.getStreet().equals("")) return new ResponseEntity<>("Street is required", HttpStatus.BAD_REQUEST);
        if(countryRequired && user.getCountry().equals("")) return new ResponseEntity<>("Country is required", HttpStatus.BAD_REQUEST);

        //check if the user already exists
        if (userRepository.getByUsername(user.getUsername()) != null || userRepository.getByEmail(user.getEmail()) != null) {
            return new ResponseEntity<>("exists", HttpStatus.CONFLICT);
        }
        //check if verification is enabled
        if(!verificationEnabled) user.setActive(true);
        else{
            emailService.sendVerifyEmail(user.getEmail(), user.getVerify().getVerifyKey(), user.getUsername());
            user.setActive(false);
        }
        //create the user either activated or not

        userService.createUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/verify")
    public ResponseEntity<?> verify(@RequestParam(value = "key") String key) {
        VerifyUser fullKey = verifyUserRepository.getByVerifyKey(key);
        if (fullKey == null) {
            return new ResponseEntity<>("Key not valid", HttpStatus.BAD_REQUEST);
        }
        User user = fullKey.getUser();
        //Check if the user or key exists
        if (user == null) {
            return new ResponseEntity<>("Key not valid", HttpStatus.BAD_REQUEST);
        }
        user.setActive(true);
        verifyUserRepository.deleteById(user.getVerify().getId());
        user.setVerify(null);
        userRepository.save(user);
        return new ResponseEntity<>(HttpStatus.OK);

    }
}
