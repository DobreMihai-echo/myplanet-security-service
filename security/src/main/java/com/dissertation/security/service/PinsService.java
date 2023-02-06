package com.dissertation.security.service;

import com.dissertation.security.model.Pins;
import com.dissertation.security.repository.PinsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PinsService {

    @Autowired
    private PinsRepository pinsRepository;

    public List<Pins> getPins() {
        return pinsRepository.findAll();
    }

    public Pins addPin(Pins pin) throws Exception {
        List<Pins> allPins = new ArrayList<>();
        try {
            pinsRepository.save(pin);
        } catch (Exception e) {
            throw new Exception("There was an error adding the pin");
        }
        return pin;
    }

    public void removePin(String id) {
        pinsRepository.delete(pinsRepository.getById(Long.valueOf(id)));
    }
}
