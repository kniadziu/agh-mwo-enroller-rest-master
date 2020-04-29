package com.company.enroller.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.company.enroller.model.Participant;
import com.company.enroller.persistence.ParticipantService;

@RestController
@RequestMapping("/participants")
public class ParticipantRestController {

	@Autowired
	ParticipantService participantService; //podpina komponent

	//Pobiera liste wszystkich uczestników
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<?> getParticipants() {
		Collection<Participant> participants = participantService.getAll();
		return new ResponseEntity<Collection<Participant>>(participants, HttpStatus.OK);
	}


	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getParticipantbyLogin(@PathVariable("id") String login) {
		Participant participant = participantService.findByLogin(login);
		if (participant == null) {  //jesli nie znalazl uczestnika
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Participant>(participant, HttpStatus.OK);
	}

//Dodaawanie nowego uczestnika
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<?> addParticipant(@RequestBody Participant participant){
		Participant foundParticipant = participantService.findByLogin(participant.getLogin());
		if (foundParticipant != null) {  //jesli nie znalazl uczestnika
			return new ResponseEntity("Nie można było utworzyć użytkownika. Login "
					+ participant.getLogin() + " już istnieje.",
					HttpStatus.CONFLICT);
		}
		participantService.add(participant);
		return  new ResponseEntity<Participant>(participant, HttpStatus.CREATED);
	}


	// Kasowanie uczestnika

	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<?> deleteParticipant(@PathVariable("id") String login){
		Participant participant = participantService.findByLogin(login);
		if (participant == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		participantService.delete(participant);
		return new ResponseEntity<Participant>(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value="/{id}" , method=RequestMethod.PUT)
	public ResponseEntity<?> update(@PathVariable("id") String login,
											   @RequestBody Participant updateParticipant
											  ){
	Participant foundParticipant=participantService.findByLogin(login);
	if (foundParticipant==null){
		return new ResponseEntity(HttpStatus.NOT_FOUND);
	}

	foundParticipant.setPassword(updateParticipant.getPassword());

	participantService.update(foundParticipant);
		//System.out.println(login);
	return new ResponseEntity<Participant>(HttpStatus.OK);
	}



}