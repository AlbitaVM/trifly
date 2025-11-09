package es.albavm.tfg.trifly.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import es.albavm.tfg.trifly.Model.User;
import es.albavm.tfg.trifly.Repository.UserRepository;
import jakarta.annotation.PostConstruct;

@Service
public class DatabaseInitializerService {
    
    @Autowired
    UserRepository userRepository;

    @PostConstruct
    public void init(){
        User user1 = new User("Alba","alba@gmail.com","1234");
        userRepository.save(user1);
    }
}
