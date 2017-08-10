package com.cg.mobilebilling.services;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cg.mobilebilling.beans.Bill;
import com.cg.mobilebilling.beans.Customer;
import com.cg.mobilebilling.beans.Plan;
import com.cg.mobilebilling.beans.PostpaidAccount;
import com.cg.mobilebilling.beans.StandardPlan;
import com.cg.mobilebilling.daoservices.BillingDAOServices;
import com.cg.mobilebilling.exceptions.BillDetailsNotFoundException;
import com.cg.mobilebilling.exceptions.BillingServicesDownException;
import com.cg.mobilebilling.exceptions.CustomerDetailsNotFoundException;
import com.cg.mobilebilling.exceptions.InvalidBillMonthException;
import com.cg.mobilebilling.exceptions.PlanDetailsNotFoundException;
import com.cg.mobilebilling.exceptions.PostpaidAccountNotFoundException;
@Component
@Transactional
public class BillingServicesImpl implements BillingServices {

	@Autowired
	BillingDAOServices dao;

	@Override
	public Customer acceptCustomerDetails(Customer customer) throws BillingServicesDownException {
		return dao.insertCustomer(customer);
	}

	@Override
	public List<Customer> getAllCustomerDetails() throws BillingServicesDownException {
		return dao.getAllCustomers();
	}

	@Override
	public Customer getCustomerDetails(int customerID)
			throws CustomerDetailsNotFoundException, BillingServicesDownException {
		// TODO Auto-generated method stub
		return dao.getCustomer(customerID);
	}

	@Override
	public boolean deleteCustomer(int customerID)
			throws BillingServicesDownException, CustomerDetailsNotFoundException {
		// TODO Auto-generated method stub
		return dao.deleteCustomer(customerID);
	}

	@Override
	public PostpaidAccount openPostpaidMobileAccount(int customerID, int planID)
			throws PlanDetailsNotFoundException, CustomerDetailsNotFoundException, BillingServicesDownException {
		StandardPlan splan= dao.getStdPlan(planID);
		Plan plan = new Plan(splan.getPlanID(), splan.getFreeLocalSMS(), splan.getFreeLocalCalls(), splan.getFreeStdCalls(), splan.getFreeLocalSMS(), splan.getFreeStdSMS(), splan.getFreeInternetDataUsageUnits(), splan.getLocalCallRate(), splan.getStdCallRate(), splan.getLocalSMSRate(), splan.getStdSMSRate(), splan.getInternetDataUsageRate(), splan.getPlanCircle(), splan.getPlanName());
		PostpaidAccount account= new PostpaidAccount();
		account.setPlan(plan);
		return dao.insertPostPaidAccount(customerID, account);
	}

	@Override
	public Bill generateMonthlyMobileBill(int customerID, long mobileNo, String billMonth, int noOfLocalSMS,
			int noOfStdSMS, int noOfLocalCalls, int noOfStdCalls, int internetDataUsageUnits)
					throws CustomerDetailsNotFoundException, PostpaidAccountNotFoundException, InvalidBillMonthException,BillingServicesDownException, PlanDetailsNotFoundException {

		PostpaidAccount p= dao.getCustomerPostPaidAccount(customerID, mobileNo);
		int BillednoOfLocalSMS = (noOfLocalSMS-p.getPlan().getFreeLocalSMS());
		int BillednoOfStdSMS = (noOfStdSMS-p.getPlan().getFreeStdSMS());
		int BillednoOfLocalCalls = (noOfLocalCalls-p.getPlan().getFreeLocalCalls());
		int BillednoOfStdCalls = (noOfStdCalls-p.getPlan().getFreeStdCalls());
		int BilledinternetDataUsageUnits = (internetDataUsageUnits-p.getPlan().getFreeInternetDataUsageUnits());

		float localCallAmount= BillednoOfLocalCalls*p.getPlan().getLocalCallRate();
		float StdCallAmount = BillednoOfStdCalls*p.getPlan().getStdCallRate();
		float LocalSMSAmount= BillednoOfLocalSMS*p.getPlan().getLocalSMSRate();
		float StdSMSAmount = BillednoOfStdSMS*p.getPlan().getStdSMSRate();
		float internetAmount = BilledinternetDataUsageUnits*p.getPlan().getInternetDataUsageRate();

		float vat=  0.08f;
		float serviceTax= 0.15f;
		float Amount= localCallAmount+StdCallAmount+LocalSMSAmount+StdSMSAmount+internetAmount; 
		float VatAmount=  Amount*vat;
		float serviceTaxAmount = serviceTax*Amount;

		float TotalBillAmount= Amount+VatAmount+serviceTaxAmount;

		Bill bill =new Bill(noOfLocalSMS, noOfStdSMS, noOfLocalCalls, noOfStdCalls, internetDataUsageUnits,billMonth,TotalBillAmount,LocalSMSAmount, StdSMSAmount, localCallAmount, StdCallAmount,internetAmount, serviceTaxAmount, VatAmount );


		return dao.insertMonthlybill(customerID, mobileNo, bill);

	}

	@Override
	public PostpaidAccount getPostPaidAccountDetails(int customerID, long mobileNo)
			throws CustomerDetailsNotFoundException, PostpaidAccountNotFoundException, BillingServicesDownException {
		return dao.getCustomerPostPaidAccount(customerID, mobileNo);
	}

	@Override
	public StandardPlan insertStdPlan(StandardPlan standardPlan) throws BillingServicesDownException {
		// TODO Auto-generated method stub
		return dao.insertStdPlan(standardPlan);
	}
	@Override
	public StandardPlan getStdPlan(int planID) {
		// TODO Auto-generated method stub
		return dao.getStdPlan(planID);
	}


	@Override
	public List<Plan> getPlanAllDetails() throws BillingServicesDownException {
		// TODO Auto-generated method stub
		return null;
	}

	/*@Override
	public Customer  acceptCustomerDetails(String firstName, String lastName, String emailID, String dateOfBirth,
			String billingAddressCity, String billingAddressState, int billingAddressPinCode, String homeAddressCity,
			String homeAddressState, int homeAddressPinCode) throws BillingServicesDownException {
		// TODO Auto-generated method stub
		return dao.insertCustomer(customer);
	}*/









	@Override
	public List<PostpaidAccount> getCustomerAllPostpaidAccountsDetails(int customerID)
			throws CustomerDetailsNotFoundException, BillingServicesDownException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bill getMobileBillDetails(int customerID, long mobileNo, String billMonth)
			throws CustomerDetailsNotFoundException, PostpaidAccountNotFoundException, InvalidBillMonthException,
			BillDetailsNotFoundException, BillingServicesDownException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Bill> getCustomerPostPaidAccountAllBillDetails(int customerID, long mobileNo)
			throws CustomerDetailsNotFoundException, PostpaidAccountNotFoundException, BillingServicesDownException,
			BillDetailsNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PostpaidAccount changePlan(int customerID, long mobileNo, int planID) throws CustomerDetailsNotFoundException,
	PostpaidAccountNotFoundException, PlanDetailsNotFoundException, BillingServicesDownException {
		StandardPlan splan= dao.getStdPlan(planID);
		Plan plan=new Plan(splan.getPlanID(),splan.getMonthlyRental(),splan.getFreeLocalCalls(), splan.getFreeStdCalls(), splan.getFreeLocalSMS(), splan.getFreeStdSMS(), splan.getFreeInternetDataUsageUnits(), splan.getLocalCallRate(), splan.getStdCallRate(), splan.getLocalSMSRate(), splan.getStdSMSRate(), splan.getInternetDataUsageRate(), splan.getPlanCircle(), splan.getPlanName());
		Customer cust =dao.getCustomer(customerID);
		PostpaidAccount acc= new PostpaidAccount();
		acc.setCustomer(cust);
		acc.setMobileNo(mobileNo);
		acc.setPlan(plan);
		return dao.updatePostPaidAccount(customerID, acc);
	} 

	@Override
	public boolean closeCustomerPostPaidAccount(int customerID, long mobileNo)
			throws CustomerDetailsNotFoundException, PostpaidAccountNotFoundException, BillingServicesDownException {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public Plan getCustomerPostPaidAccountPlanDetails(int customerID, long mobileNo)
			throws CustomerDetailsNotFoundException, PostpaidAccountNotFoundException, BillingServicesDownException,
			PlanDetailsNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deletePostPaidAccount(int customerID, PostpaidAccount account) {
		// TODO Auto-generated method stub
		return false;
	}






}