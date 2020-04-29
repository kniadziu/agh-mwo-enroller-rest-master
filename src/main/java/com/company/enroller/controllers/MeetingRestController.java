package com.company.enroller.controllers;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;
import com.company.enroller.persistence.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/meetings")
public class MeetingRestController {

    @Autowired
    MeetingService meetingService; //podłączenie komponentu

    ParticipantService participantService;

    @RequestMapping(value="", method= RequestMethod.GET)
    public ResponseEntity<?> getMeetings(){
        Collection<Meeting> meetings=meetingService.getAll();
        return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getMeeting(@PathVariable("id") String login) {
        Participant participant = participantService.findByLogin(login);
        if (participant == null) {  //jesli nie znalazl uczestnika
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Participant>(participant, HttpStatus.OK);
    }

}
