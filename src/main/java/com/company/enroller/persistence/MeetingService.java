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

	//================================================================================
	//1. WERSJA BASIC
	//1.1. Pobieranie listy wszystkich spotkań
	public Collection<Meeting> getAll() {
		String hql = "FROM Meeting";
		Query query = this.session.createQuery(hql);
		return query.list();
	}

	//1.2. Pobieranie listy pojedyncznego spotkania
	public Meeting findById(long id){
		return (Meeting) this.session.get(Meeting.class, id);
	}

	//1.3 Dodawanie spotkań
	public Meeting add(Meeting meeting){
		Transaction transaction = this.session.beginTransaction();
		session.save(meeting);
		transaction.commit();
		return meeting;
	}

//================================================================
	//3/. Wersja GOLD
	//3.1. Usuwanie spotkań
	public Meeting delete(Meeting meeting){
		Transaction transaction = this.session.beginTransaction();
		session.delete(meeting);
		transaction.commit();
		return meeting;
	}

	//3.2. Aktualizację spotkań
	public Meeting update(Meeting meeting) {
		Transaction transaction = this.session.beginTransaction();
		session.update(meeting);
		transaction.commit();
		return meeting;
	}


	//4. Wersja PREMIUM (dodatkowo do GOLD)
	//4.1 Sortowanie listy spotkań po tytule spotkania
	public Collection<Meeting> getSortetMeetingsByTitle() {
		String hql = "FROM Meeting ORDER BY LOWER(title)";
		Query query = this.session.createQuery(hql);
		return query.list();
	}



	//4.2. Przeszukiwanie listy spotkań po tytule i opisie (na zasadzie substring)
	public Collection<Meeting> findMeetingsByTitleAndDescription(String title, String description){
		String hql="FROM Meeting as meeting WHERE title LIKE :title AND description LIKE :description" ;
		Query query=this.session.createQuery(hql);
		query.setParameter("title","%"+title+"%");
		query.setParameter("description","%"+description+"%");
		return query.list();
	}

	//4.3 Przeszukiwanie listy spotkań po zapisanym uczestniku spotkania

	public Collection<Meeting> findMeetingsByLogin(String login){
		String hql="FROM Meeting as meeting WHERE :login in elements(participants)" ;
		Query query=this.session.createQuery(hql);
		query.setParameter("login","%"+login+"%");
		return query.list();
	}

}



