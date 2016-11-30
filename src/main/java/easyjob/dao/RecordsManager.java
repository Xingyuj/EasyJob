package easyjob.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import easyjob.pojo.Records;
import easyjob.util.HibernateUtil;

public class RecordsManager {
	private Session session;
	
	public RecordsManager() {
		this.setSession(HibernateUtil.getSession());
		// TODO Auto-generated constructor stub
	}
	
	public void closeSession(){
		HibernateUtil.closeSession();
	}
	public void saveRecord(Records record){
		Transaction tx = getSession().beginTransaction();
		getSession().save(record);
		tx.commit();  
	}
	public void updateRecord(Records record){
		Transaction tx = getSession().beginTransaction();
		getSession().update(record);
		tx.commit();  
	}
	public void deleteRecord(Records record){
		Transaction tx = getSession().beginTransaction();
		getSession().delete(record);
		tx.commit();  
	}
	public List<Records> findRecodeByCompName(String name){
		String hql = "FROM Records WHERE companyName = :NAME";
		@SuppressWarnings("unchecked")
		Query<Records> query = getSession().createQuery(hql); 
		query.setParameter("NAME",name);
		return query.getResultList();
	}
	
	public static void main(String[] args) {
		RecordsManager aa = new RecordsManager();
		aa.findRecodeByCompName("Nimendoushidashabi");
		System.out.println(aa);
		aa.closeSession();
		HibernateUtil.closeSessionFactory();
	}
	
	public boolean jobAlreadyApplied(String compName, String position){
		String hql = "FROM Records WHERE companyName = :NAME and position = :POSITION";
		@SuppressWarnings("unchecked")
		Query<Records> query = getSession().createQuery(hql); 
		query.setParameter("NAME",compName);
		query.setParameter("POSITION",position);
		return query.getResultList().size() == 0 ? false : true;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}
}
