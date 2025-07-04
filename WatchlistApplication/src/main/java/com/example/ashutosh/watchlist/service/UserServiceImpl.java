    package com.example.ashutosh.watchlist.service;

    import java.util.Arrays;
    import java.util.Collection;
    import java.util.stream.Collectors;

    import org.springframework.security.core.GrantedAuthority;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.security.core.userdetails.UserDetailsService;
    import org.springframework.security.core.userdetails.UsernameNotFoundException;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.stereotype.Service;
    import org.springframework.security.core.authority.SimpleGrantedAuthority;
    import com.example.ashutosh.watchlist.Entity.Role;
    import com.example.ashutosh.watchlist.Entity.User;
    import com.example.ashutosh.watchlist.dto.UserRegistrationDto;
    import com.example.ashutosh.watchlist.repository.RoleRepository;
    import com.example.ashutosh.watchlist.repository.UserRepository;

    @Service
    public class UserServiceImpl implements UserService, UserDetailsService {

        private final UserRepository userRepository;
        private final RoleRepository roleRepository;
        private final BCryptPasswordEncoder passwordEncoder;

        public UserServiceImpl(UserRepository userRepository,
                             RoleRepository roleRepository,
                             BCryptPasswordEncoder passwordEncoder) {
            this.userRepository = userRepository;
            this.roleRepository = roleRepository;
            this.passwordEncoder = passwordEncoder;
        }

        @Override
        public User save(UserRegistrationDto registrationDto) {
            System.out.println("Attempting to save new user: " + registrationDto.getUsername());
            User user = new User();
            user.setUsername(registrationDto.getUsername());
            user.setEmail(registrationDto.getEmail());
            user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));

            // Ensure ROLE_USER exists or handle its creation if not found
            Role userRole = roleRepository.findByName("ROLE_USER");
            if (userRole == null) {
                System.out.println("ROLE_USER not found, creating it.");
                userRole = new Role();
                userRole.setName("ROLE_USER");
                userRole = roleRepository.save(userRole); // Save the new role
            }
            user.setRoles(Arrays.asList(userRole));
            User savedUser = userRepository.save(user);
            System.out.println("User saved successfully: " + savedUser.getUsername());
            return savedUser;
        }

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            System.out.println("Attempting to load user by username: " + username);
            User user = userRepository.findByUsername(username);

            if (user == null) {
                System.out.println("User not found for username: " + username);
                throw new UsernameNotFoundException("Invalid username or password");
            }
            System.out.println("User found: " + user.getUsername() + " with roles: " + user.getRoles().stream().map(Role::getName).collect(Collectors.joining(", ")));
            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    mapRolesToAuthorities(user.getRoles())
            );
        }

        private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
            return roles.stream()
                .map(role -> {
                    System.out.println("Mapping role: " + role.getName() + " to authority.");
                    return new SimpleGrantedAuthority(role.getName());
                })
                .collect(Collectors.toList());
        }
    }
    