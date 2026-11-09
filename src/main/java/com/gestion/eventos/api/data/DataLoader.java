package com.gestion.eventos.api.data;

import com.gestion.eventos.api.domain.Category;
import com.gestion.eventos.api.domain.Role;
import com.gestion.eventos.api.domain.Speaker;
import com.gestion.eventos.api.domain.User;
import com.gestion.eventos.api.repository.CategoryRepository;
import com.gestion.eventos.api.repository.RoleRepository;
import com.gestion.eventos.api.repository.SpeakerRepository;
import com.gestion.eventos.api.repository.UserRepository;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * DataLoader
 */

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;
  private final CategoryRepository categoryRepository;
  private final SpeakerRepository speakerRepository;

  @Override
  @Transactional
  public void run(String... args) throws Exception {
    Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseGet(() -> {
      Role newRole = new Role();
      newRole.setName("ROLE_ADMIN");
      return roleRepository.save(newRole);
    });
    Role userRole = roleRepository.findByName("ROLE_USER").orElseGet(() -> {
      Role newRole = new Role();
      newRole.setName("ROLE_USER");
      return roleRepository.save(newRole);
    });

    if (userRepository.findByUsername("admin").isEmpty()) {
      User admin = new User();
      admin.setName("Administrador");
      admin.setUsername("admin");
      admin.setEmail("admin@eventos.com");
      admin.setPassword(passwordEncoder.encode("admin"));

      Set<Role> adminRoles = new HashSet<>();
      adminRoles.add(adminRole);
      adminRoles.add(userRole);

      admin.setRoles(adminRoles);

      userRepository.save(admin);
    }

    if (userRepository.findByUsername("user").isEmpty()) {
      User user = new User();
      user.setName("Usuario");
      user.setUsername("user");
      user.setEmail("user@eventos.com");
      user.setPassword(passwordEncoder.encode("user"));

      Set<Role> userRoles = new HashSet<>();
      userRoles.add(userRole);

      user.setRoles(userRoles);

      userRepository.save(user);
    }

    if (!categoryRepository.existsByName("Tecnologia")) {
      Category category = new Category(
        null,
        "Tecnología",
        "Tecnología es una categoría de eventos relacionados a tecnología y innovación."
      );
      categoryRepository.save(category);
    }

    if (!categoryRepository.existsByName("Negocios")) {
      Category category = new Category(
        null,
        "Negocios",
        "Negocios es una categoría de eventos relacionados a negocios y finanzas."
      );
      categoryRepository.save(category);
    }

    if (!categoryRepository.existsByName("Cultura"));
    {
      Category category = new Category(
        null,
        "Cultura",
        "Cultura es una categoría de eventos relacionados a arte, música, teatro y cultura general."
      );
      categoryRepository.save(category);
    }

    if (!speakerRepository.existsByEmail("john.doe@example.com")) {
      Speaker speaker = new Speaker(
        null,
        "John Doe",
        "john.doe@example.com",
        "John Doe is a renowned speaker and author.",
        new HashSet<>()
      );
      speakerRepository.save(speaker);
    }

    if (!speakerRepository.existsByEmail("jane.smith@example.com")) {
      Speaker speaker = new Speaker(
        null,
        "Jane Smith",
        "jane.smith@example.com",
        "Jane Smith is a passionate speaker and advocate.",
        new HashSet<>()
      );
      speakerRepository.save(speaker);
    }
  }
}
