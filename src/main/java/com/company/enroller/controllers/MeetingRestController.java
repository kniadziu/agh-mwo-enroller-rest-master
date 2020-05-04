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


    //1.1 Pobieranie listy wszystkich spotkań
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getMeetings() {
        Collection<Meeting> meetings = meetingService.getAll();
        return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
    }

    //1.2. Pobierz spotkanie o id:
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getMeeting(@PathVariable("id") long meetingId) {
        Meeting meeting = meetingService.findById(meetingId);
        if (meeting == null) {  //jesli nie znalazl uczestnika
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
    }

    //1.3. Dodawanie nowego spotkania
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> addMeeting(@RequestBody Meeting meeting) {
        Meeting foundMeeting = meetingService.findById(meeting.getId());
        if (foundMeeting != null) {  //jesli nie znalazl spotkania
            return new ResponseEntity("Nie można było utworzyć spotkania. Spotkanie o id: "
                    + meeting.getId() + " już istnieje.",
                    HttpStatus.CONFLICT);
        }
        meetingService.add(meeting);
        return new ResponseEntity<Meeting>(meeting, HttpStatus.CREATED);
    }

    // 3.1 Usuwanie spotkania o danym id
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteMeeting(@PathVariable("id") long meetingId) {
        Meeting meeting = meetingService.findById(meetingId);
        if (meeting == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        meetingService.delete(meeting);
        return new ResponseEntity<Meeting>(HttpStatus.NO_CONTENT);
    }


    //2.1 - Dodawanie uczestnika do spotkania
    @RequestMapping(value = "/{Id}/participants", method = RequestMethod.POST)
    public ResponseEntity<?> addParticipantToMeeting(@PathVariable("Id") long meetingId,
                                                     @RequestBody Participant participant) {
        Meeting meeting = meetingService.findById(meetingId);
        if (meeting == null) {
            return new ResponseEntity<String>("Spotkanie o id: " + meetingId + " nie istnieje.", HttpStatus.NOT_FOUND);
        }

        Participant existingParticipant = participantService.findByLogin(participant.getLogin());
        if (existingParticipant == null) {
            return new ResponseEntity<String>("Uczestnik taki nie istnieje.", HttpStatus.NOT_FOUND);
        }

        if (meeting.getParticipants().contains(participant)) {
            return new ResponseEntity<String>("Dodano uczestnika " + existingParticipant.getLogin() +
                    "do spotknia o id: " + meeting.getId(), HttpStatus.CONFLICT);
        }

        meeting.addParticipant(participant);
        meetingService.update(meeting);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    // 2.2. Pobieranie uczestników spotkania
    @RequestMapping(value = "/{Id}/participants", method = RequestMethod.GET)
    public ResponseEntity<?> getParticipants(@PathVariable("Id") long meetingId) {
        Meeting meeting = meetingService.findById(meetingId);
        if (meeting == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Collection<Participant> participants = meeting.getParticipants();
        return new ResponseEntity<Collection<Participant>>(participants, HttpStatus.OK);
    }

    //2.3 usun uczestnika ze spotkania

    @RequestMapping(value = "/{meetingId}/participants/{login}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteParticipantFormMeeting(@PathVariable("meetingId") long meetingId, @PathVariable("login") String login){
        Meeting meeting = meetingService.findById(meetingId);
        if (meeting==null){
        return new ResponseEntity<String> ("Uczestnik nie jest zpisany na spotkanie o id: " + meetingId + ".", HttpStatus.NOT_FOUND);
        }

        Participant foundParticipant = participantService.findByLogin(login);
        if (foundParticipant == null) {
            return new ResponseEntity<String>("Uczestnik nie istnieje.", HttpStatus.NOT_FOUND);
        }

        if (!meeting.getParticipants().contains(foundParticipant)) {
            return new ResponseEntity<String>("id" + meeting.getId(), HttpStatus.CONFLICT);
        }

        meeting.removeParticipant(foundParticipant);

        meetingService.update(meeting);
        return new ResponseEntity<Participant>(foundParticipant, HttpStatus.OK);
    }
}