package com.rkmd.toki_no_nagare.service;

import com.rkmd.toki_no_nagare.dto.Contact.ContactRequestDto;
import com.rkmd.toki_no_nagare.entities.contact.Contact;
import com.rkmd.toki_no_nagare.exception.BadRequestException;
import com.rkmd.toki_no_nagare.repositories.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class ContactService {
    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private UserService userService;

    public Optional<Contact> getContactByDni(Long dni) {
        return contactRepository.findById(dni);
    }

    public Contact createContact(Map<String, Object> json) {
        if (contactRepository.findById(Long.valueOf((String) json.get("dni"))).isPresent())
            throw new BadRequestException("contact_already_exists", "This contact already exists");

        Contact newContact = new Contact();
        newContact.setName((String) json.get("name"));
        newContact.setLastName((String) json.get("last_name"));
        newContact.setDni(Long.valueOf((String) json.get("dni")));

        if (json.containsKey("username") && json.containsKey("password")) {
            if (userService.get((String) json.get("username")).isPresent())
                throw new BadRequestException("username_already_exists", "This username already exists");
        }

        newContact.setBookings(null);
        try {
            return contactRepository.save(newContact);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException("contact_already_exists", "This contact already exists");
        } catch (Exception e) {
            throw new BadRequestException("bad_request", e.getMessage());
        }
    }

    /** This method creates a Contact if it doesn't exist in the database. If existed, update the existing data.
     * @param request Contact data
     * @return Contact
     * */
    public Contact createOrUpdate(ContactRequestDto request){
        Optional<Contact> optionalContact = contactRepository.findById(request.getDni());

        Contact contact = optionalContact.orElseGet(() -> new Contact(request.getDni()));
        contact.setName(request.getName());
        contact.setLastName(request.getLastName());
        contact.setEmail(request.getEmail());
        contact.setPhone(request.getPhone());

        return contactRepository.saveAndFlush(contact);
    }

}
