package com.company.enroller.persistence;

import com.company.enroller.model.Meeting;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component("meetingService")
public class MeetingService {

	//DatabaseConnector connector;
	Session session;

	public MeetingService() {
		//connector = DatabaseConnector.getInstance();
		session = DatabaseConnector.getInstance().getSession();
	}
//pobierz wszystkie spotkania
	public Collection<Meeting> getAll() {
		String hql = "FROM Meeting";
		Query query = this.session.createQuery(hql);
		return query.list();
	}

//znajdz spotkanie o danym id
	public Meeting findById(long id){
		return (Meeting) this.session.get(Meeting.class, id);
	}

//dodaj nowe spotkanie
	public Meeting add(Meeting meeting){
		Transaction transaction = this.session.beginTransaction();
		session.save(meeting);
		transaction.commit();
		return meeting;
	}


	public Meeting delete(Meeting meeting){
		Transaction transaction = this.session.beginTransaction();
		session.delete(meeting);
		transaction.commit();
		return meeting;
	}


}



