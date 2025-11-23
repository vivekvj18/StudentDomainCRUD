package com.domaincrud.domaincrud.service;

import com.domaincrud.domaincrud.entity.Employee;
import com.domaincrud.domaincrud.repository.EmployeeRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class EmployeeUserDetailsService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;

    // Constructor injection
    public EmployeeUserDetailsService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 1) DB se employee nikaalna
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Employee not found with email: " + email));

        // 2) Rule: sirf Admin department (department_id = 1) login kar sakta hai
        if (employee.getDepartmentId() == null || !employee.getDepartmentId().equals(1L)) {
            throw new UsernameNotFoundException("Only Admin department employees can login");
        }

        // 3) Role set karna
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_ADMIN");

        // 4) Spring Security ka User object banana
        return new User(
                employee.getEmail(),      // username
                employee.getPassword(),   // DB me stored password (jaise "{noop}admin123")
                Collections.singleton(authority)
        );
    }
}
