package com.company.enroller.controllers;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;
import com.company.enroller.persistence.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/meetings")
public class MeetingRestController {

    @Autowired
    MeetingService meetingService; //podłączenie komponentu

    @Autowired
    ParticipantService participantService;

    //1. Pobierz wszystkie spotkania
    @RequestMapping(value="", method= RequestMethod.GET)
    public ResponseEntity<?> getMeetings(){
        Collection<Meeting> meetings=meetingService.getAll();
        return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
    }

    //2. Pobierz spotkanie o id:
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getMeeting(@PathVariable("id") long meetingId) {
        Meeting meeting = meetingService.findById(meetingId);
        if (meeting == null) {  //jesli nie znalazl uczestnika
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
    }

    //Dodawanie nowego spotkania
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> addMeeting(@RequestBody Meeting meeting){
        Meeting foundMeeting = meetingService.findById(meeting.getId());
        if (foundMeeting != null) {  //jesli nie znalazl spotkania
            return new ResponseEntity("Nie można było utworzyć spotkania. Spotkanie o id: "
                    + meeting.getId() + " już istnieje.",
                    HttpStatus.CONFLICT);
        }
        meetingService.add(meeting);
        return  new ResponseEntity<Meeting>(meeting, HttpStatus.CREATED);
    }

    // Usuwanie spotkania o danym id
    @RequestMapping(value="/{id}", method=RequestMethod.DELETE)
    public ResponseEntity<?> deleteMeeting(@PathVariable("id") long meetingId){
        Meeting meeting = meetingService.findById(meetingId);
        if (meeting == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        meetingService.delete(meeting);
        return new ResponseEntity<Meeting>(HttpStatus.NO_CONTENT);
    }

}
