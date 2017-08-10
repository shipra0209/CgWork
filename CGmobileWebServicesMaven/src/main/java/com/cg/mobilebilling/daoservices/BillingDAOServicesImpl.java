package com.cg.mobilebilling.daoservices;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.cg.mobilebilling.beans.Bill;
import com.cg.mobilebilling.beans.Customer;
import com.cg.mobilebilling.beans.Plan;
import com.cg.mobilebilling.beans.PostpaidAccount;
import com.cg.mobilebilling.beans.StandardPlan;
import com.cg.mobilebilling.exceptions.BillingServicesDownException;
import com.cg.mobilebilling.exceptions.PlanDetailsNotFoundException;

@Repository
public class BillingDAOServicesImpl implements BillingDAOServices {


@PersistenceContext
private EntityManager em;

	@Override
	public Customer insertCustomer(Customer customer) throws BillingServicesDownException {
		em.persist(customer);
		 em.flush();
		 return customer;
	}
	
	@Override
	public List<Customer> getAllCustomers() {
		TypedQuery<Customer> query = em.createQuery("select c from Customer c",Customer.class);

		return query.getResultList(); 
	}
	
	@Override
	public Customer getCustomer(int customerID) {
		Customer customer = em.find(Customer.class,customerID);
		return customer;
	}
	

	@Override
	public boolean deleteCustomer(int customerID) {
		em.remove(getCustomer(customerID));
		return true;
	}
	@Override
	public PostpaidAccount insertPostPaidAccount(int customerID, PostpaidAccount account) {
		Customer customer=em.find(Customer.class,customerID);
				account.setCustomer(customer);
				em.persist(account);
				customer.setPostpaidAccounts(account);
				return account; 

	}
	@Override
	public PostpaidAccount getCustomerPostPaidAccount(int customerID, long mobileNo) {
		PostpaidAccount postpaidAccount= em.find(PostpaidAccount.class, mobileNo);
		return postpaidAccount;
	}
	/*@Override
	public boolean deletePostPaidAccount(int customerID, PostpaidAccount account) {
		Customer customer=em.find(Customer.class,customerID);
		//account.setCustomer(customer);
		em.remove(getCustomerPostPaidAccount(customerID, account);
		return false;
	}*/
	
	@Override
	public StandardPlan insertStdPlan(StandardPlan standardPlan) {
		em.persist(standardPlan);
		em.flush();
		return standardPlan;
	}
	

	@Override
	public StandardPlan getStdPlan(int planID) {
		StandardPlan standardPlan=em.find(StandardPlan.class, planID);
		return standardPlan;
	}

	@Override
	public Bill insertMonthlybill(int customerID, long mobileNo, Bill bill) {
			PostpaidAccount paccount=em.find(PostpaidAccount.class, mobileNo);
			bill.setPostpaidaccount(paccount);
			em.persist(bill);
			paccount.setBills(bill);
			return bill;
	}


	@Override
	public PostpaidAccount updatePostPaidAccount(int customerID, PostpaidAccount account) {
		PostpaidAccount acc=em.merge(account);
		return acc;
	}


	@Override
	public int insertPlan(Plan plan) throws PlanDetailsNotFoundException {
		// TODO Auto-generated method stub
		return 0;
	}

	

	@Override
	public Bill getMonthlyBill(int customerID, long mobileNo, String billMonth) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Bill> getCustomerPostPaidAccountAllBills(int customerID, long mobileNo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PostpaidAccount> getCustomerPostPaidAccounts(int customerID) {
		// TODO Auto-generated method stub
		return null;
	}

	

	

	@Override
	public List<Plan> getAllPlans() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Plan getPlan(int planID) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public Plan getPlanDetails(int customerID, long mobileNo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deletePostPaidAccount(int customerID, PostpaidAccount account) {
		// TODO Auto-generated method stub
		return false;
	}



}