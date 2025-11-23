package com.domaincrud.domaincrud.service;

import com.domaincrud.domaincrud.entity.Domain;
import com.domaincrud.domaincrud.repository.DomainRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DomainService {

    private final DomainRepository domainRepository;

    public DomainService(DomainRepository domainRepository) {
        this.domainRepository = domainRepository;
    }

    // Create / Update Domain
    public Domain saveDomain(Domain domain) {
        return domainRepository.save(domain);
    }

    // Get all domains
    public List<Domain> getAllDomains() {
        return domainRepository.findAll();
    }

    // Get domain by ID
    public Optional<Domain> getDomainById(Long id) {
        return domainRepository.findById(id);
    }

    // Delete domain by ID
    public void deleteDomain(Long id) {
        domainRepository.deleteById(id);
    }
}

