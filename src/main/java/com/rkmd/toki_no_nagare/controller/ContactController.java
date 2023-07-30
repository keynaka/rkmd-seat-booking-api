package com.rkmd.toki_no_nagare.controller;

import com.rkmd.toki_no_nagare.entities.contact.Contact;
import com.rkmd.toki_no_nagare.service.ContactService;
import com.rkmd.toki_no_nagare.utils.ValidationUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/contact")
public class ContactController {
    @Autowired
    private ContactService contactService;

    @GetMapping("/{dni}")
    public ResponseEntity<Contact> getContact(@PathVariable("dni") Long dni) {
        Optional<Contact> contact = contactService.getContactByDni(dni);
        if (!contact.isPresent())
           return ResponseEntity.notFound().build();
        return ResponseEntity.ok().body(contact.get());
    }

    @PostMapping("")
    public ResponseEntity<Contact> createContact(@RequestBody @Valid Map<String, Object> json) {
        ValidationUtils.checkParam(json.containsKey("dni"), "dni_missing", "Dni is missing and is mandatory");
        ValidationUtils.checkParam(json.containsKey("name"), "name_missing", "Name is missing and is mandatory");
        ValidationUtils.checkParam(json.containsKey("last_name"), "last_name_missing", "Last Name is missing and is mandatory");

        if (json.containsKey("username") || json.containsKey("password"))
            ValidationUtils.checkParam(json.containsKey("username"), "username_missing", "Username is missing and is mandatory");
            ValidationUtils.checkParam(json.containsKey("password"), "password_missing", "Password is missing and is mandatory");

        Contact newContact = contactService.createContact(json);

        return ResponseEntity.ok().body(newContact);
    }
}
