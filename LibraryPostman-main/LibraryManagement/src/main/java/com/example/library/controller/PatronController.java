package com.example.library.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import com.example.library.model.Patron;
import com.example.library.repository.PatronRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/patrons")
public class PatronController {

    @Autowired
    private PatronRepository patronRepository;

    @Operation(summary = "Crea un nuevo Patron")
    @ApiResponse(responseCode = "200", description = "Se creo exitosamente el patron")

    @PostMapping
    public Patron createPatron(@RequestBody Patron patron) {
        return patronRepository.save(patron);
    }

    

   
    
}
