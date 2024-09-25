package com.example.demo.service;

import com.example.demo.entity.Membership;
import com.example.demo.repository.MembershipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MembershipService {

    @Autowired
    private MembershipRepository membershipRepository;

    // Method to save a membership
    public Membership saveMembership(Membership membership) {
        return membershipRepository.save(membership);
    }

    // Method to find all memberships
    public List<Membership> getAllMemberships() {
        return membershipRepository.findAll();
    }

    public Membership getMembershipById(Long membershipId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMembershipById'");
    }

    // Additional methods for updating, deleting, etc. can be added here
}
