package com.domaincrud.domaincrud.controller;

import com.domaincrud.domaincrud.entity.Domain;
import com.domaincrud.domaincrud.entity.Student;
import com.domaincrud.domaincrud.repository.StudentRepository;
import com.domaincrud.domaincrud.service.DomainService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/domains")
public class DomainController {

    private final DomainService domainService;
    private final StudentRepository studentRepository;

    public DomainController(DomainService domainService, StudentRepository studentRepository) {
        this.domainService = domainService;
        this.studentRepository = studentRepository;
    }

    // 1. Create a new domain
    @PostMapping
    public ResponseEntity<Domain> createDomain(@RequestBody Domain domain) {
        Domain saved = domainService.saveDomain(domain);
        return ResponseEntity.ok(saved);
    }

    // 2. Get all domains
    @GetMapping
    public ResponseEntity<List<Domain>> getAllDomains() {
        List<Domain> domains = domainService.getAllDomains();
        return ResponseEntity.ok(domains);
    }

    // 3. Get single domain by ID
    @GetMapping("/{id}")
    public ResponseEntity<Domain> getDomainById(@PathVariable Long id) {
        Optional<Domain> domainOpt = domainService.getDomainById(id);
        return domainOpt
                .map(ResponseEntity::ok)          // agar mila to 200 + body
                .orElseGet(() -> ResponseEntity.notFound().build()); // nahi mila to 404
    }

    // 4. Update domain by ID
    @PutMapping("/{id}")
    public ResponseEntity<Domain> updateDomain(@PathVariable Long id,
                                               @RequestBody Domain updatedDomain) {
        Optional<Domain> existingOpt = domainService.getDomainById(id);
        if (existingOpt.isEmpty()) {
            return ResponseEntity.notFound().build();   // 404 if not found
        }

        Domain existing = existingOpt.get();
        existing.setProgram(updatedDomain.getProgram());
        existing.setBatch(updatedDomain.getBatch());
        existing.setCapacity(updatedDomain.getCapacity());
        existing.setQualification(updatedDomain.getQualification());

        Domain saved = domainService.saveDomain(existing);
        return ResponseEntity.ok(saved);
    }

    // 5. Delete domain by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDomain(@PathVariable Long id) {
        Optional<Domain> existingOpt = domainService.getDomainById(id);
        if (existingOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        domainService.deleteDomain(id);
        return ResponseEntity.noContent().build();  // 204 No Content
    }

    // 6. Get students belonging to a particular domain
    @GetMapping("/{id}/students")
    public ResponseEntity<List<Student>> getStudentsByDomain(@PathVariable Long id) {
        List<Student> students = studentRepository.findByDomain_DomainId(id);
        return ResponseEntity.ok(students);
    }
}
