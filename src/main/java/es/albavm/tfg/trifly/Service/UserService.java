package es.albavm.tfg.trifly.Service;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import es.albavm.tfg.trifly.Model.User;
import es.albavm.tfg.trifly.Repository.UserRepository;
import es.albavm.tfg.trifly.Security.CSRFHandlerConfiguration;
import es.albavm.tfg.trifly.dto.User.UserDto;
import es.albavm.tfg.trifly.dto.User.UserMapper;

@Service
public class UserService {
    
    private final CSRFHandlerConfiguration CSRFHandlerConfiguration;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    UserService(CSRFHandlerConfiguration CSRFHandlerConfiguration) {
        this.CSRFHandlerConfiguration = CSRFHandlerConfiguration;
    }

    private UserDto toDTO(User user){
        return userMapper.toDto(user);
    }

    
    private Collection<UserDto> toDTOs(Collection<User> users) {
		return userMapper.toDTOs(users);
	}


    public Optional<User> findByEmail(String username) {
        return userRepository.findByEmail(username);
    }

    public boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }

    public UserDto getUserDto(Long id){
        return toDTO(userRepository.findById(id).orElseThrow());
    }

    public void save(User user){
        userRepository.save(user);
    }
}
