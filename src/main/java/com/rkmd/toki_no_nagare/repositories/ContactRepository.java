package com.rkmd.toki_no_nagare.repositories;

import com.rkmd.toki_no_nagare.entities.contact.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Long> {
}
