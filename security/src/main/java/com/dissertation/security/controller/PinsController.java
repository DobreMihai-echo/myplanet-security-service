package com.dissertation.security.controller;

import com.dissertation.security.service.PinsService;
import com.dissertation.security.model.Pins;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class PinsController {

    @Autowired
    PinsService pinsService;

    @GetMapping("/pins")
    public ResponseEntity getAllPins() {
        return ResponseEntity.status(HttpStatus.OK).body(pinsService.getPins());
    }

    @PostMapping("/pins")
    public ResponseEntity addPin(@RequestBody Pins pins) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(pinsService.addPin(pins));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There was an error adding the event");
        }
    }

    @PutMapping("/pins/join/{id}")
    public ResponseEntity joinEvent(@PathVariable(name = "id") String id) {
        return ResponseEntity.ok("ok");
    }

    @DeleteMapping("/pins/{id}")
    public ResponseEntity removePin(@PathVariable(name = "id") String id) {
        try {
            pinsService.removePin(id);
            return ResponseEntity.ok("Pin deleted");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
    }
}
