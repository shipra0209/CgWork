package com.cg.mobilebilling.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cg.mobilebilling.beans.Customer;
import com.cg.mobilebilling.beans.PostpaidAccount;
import com.cg.mobilebilling.beans.StandardPlan;
import com.cg.mobilebilling.exceptions.BillingServicesDownException;
import com.cg.mobilebilling.exceptions.CustomerDetailsNotFoundException;
import com.cg.mobilebilling.exceptions.InvalidBillMonthException;
import com.cg.mobilebilling.exceptions.PlanDetailsNotFoundException;
import com.cg.mobilebilling.exceptions.PostpaidAccountNotFoundException;
import com.cg.mobilebilling.services.BillingServices;

@RestController
public class MobileBillingController {

	@Autowired
	private BillingServices services;
	
	
	public MobileBillingController(){
		System.out.println("Mobile Billing Controller");
	}

	
	
	@RequestMapping(value="/acceptCustomerDetails",method=RequestMethod.POST,consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<String>acceptProductDetail(@ModelAttribute Customer customer) throws BillingServicesDownException{
		services.acceptCustomerDetails(customer);
		return new ResponseEntity<>("Customer details succesfully added",HttpStatus.OK);
		
	}
	@RequestMapping(value="/acceptStdPlan",method=RequestMethod.POST,consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<String>acceptStdPlan(@ModelAttribute StandardPlan standardPlan) throws BillingServicesDownException{
		services.insertStdPlan(standardPlan);
		return new ResponseEntity<>("StandardPlan details succesfully added",HttpStatus.OK);
		
	}
	
	@RequestMapping(value={"/CustomerDetailsRequestParam"},headers="Accept=application/json")
	public ResponseEntity<Customer>getCustomerDetails(@RequestParam("customerID")int customerID) throws CustomerDetailsNotFoundException, BillingServicesDownException{
		Customer customer=services.getCustomerDetails(customerID);
		System.out.println("details "+customer);
		
		return new ResponseEntity<>(customer,HttpStatus.OK);
	
	}

	@RequestMapping(value={"/PostPaidDetailsRequestParam"},headers="Accept=application/json")
	public ResponseEntity<PostpaidAccount> getPostpaidDetails(@RequestParam("customerID")int customerID,@RequestParam("mobileNO")long mobileNo) throws CustomerDetailsNotFoundException, BillingServicesDownException, PostpaidAccountNotFoundException{
		PostpaidAccount postpaidAccount=services.getPostPaidAccountDetails(customerID, mobileNo);
		System.out.println("details "+postpaidAccount);
		return new ResponseEntity<>(postpaidAccount,HttpStatus.OK);
	
	}
	
	@RequestMapping(value={"/StdPlanDetailsRequestParam"},headers="Accept=application/json")
	public ResponseEntity<StandardPlan> StdPlanDetails(@RequestParam("planID")int planID) throws CustomerDetailsNotFoundException, BillingServicesDownException{
		StandardPlan standardPlan=services.getStdPlan(planID);
		System.out.println("details "+standardPlan);
		
		return new ResponseEntity<>(standardPlan,HttpStatus.OK);
	
	}
	@RequestMapping(value={"/allCustomerDetailsJSON"},headers="Accept=application/json")
	public ResponseEntity<ArrayList<Customer>> getAllCustomerDetailsJSON() throws CustomerDetailsNotFoundException, BillingServicesDownException{
	ArrayList<Customer> customerList=(ArrayList<Customer>) services.getAllCustomerDetails();
	return new ResponseEntity<>(customerList,HttpStatus.OK);
	}

	@RequestMapping(value="/deleteCustomerDetail/{customerID}",method=RequestMethod.DELETE)
	
	public ResponseEntity<String>deleteCustomerDetail(@PathVariable("customerID")int customerID)throws CustomerDetailsNotFoundException, BillingServicesDownException{
		boolean customer = services.deleteCustomer(customerID);
			if(customer==false)throw new CustomerDetailsNotFoundException("Customer detail not found with product code"+customerID);
		return new ResponseEntity<>("Customer details succesfully deleted",HttpStatus.OK);
		
	}
	
@RequestMapping(value="/updatePostpaidAccountDetail",method=RequestMethod.PUT,consumes=MediaType.ALL_VALUE)
	
	public ResponseEntity<String>updatePostpaidAccountDetail(@RequestParam("customerID") int customerID, @RequestParam("mobileNo") long mobileNo, @RequestParam("planID") int planID )throws CustomerDetailsNotFoundException, BillingServicesDownException, PostpaidAccountNotFoundException, PlanDetailsNotFoundException{
		services.changePlan(customerID, mobileNo, planID);
		return new ResponseEntity<>("Postpaid  details succesfully updated",HttpStatus.OK);
		
	}
	@RequestMapping(value="/acceptPostpaidAccountDetails",method=RequestMethod.POST,consumes=MediaType.ALL_VALUE)
	public ResponseEntity<String>acceptPostpaidAccountDetails(@RequestParam("customerID") int customerID, @RequestParam("planID") int planID ) throws BillingServicesDownException, PlanDetailsNotFoundException, CustomerDetailsNotFoundException{
		services.openPostpaidMobileAccount(customerID, planID);
		return new ResponseEntity<>("Postpaid Account details succesfully added",HttpStatus.OK);
		
	}
	
	@RequestMapping(value="/insertMonthlyBill",method=RequestMethod.POST,consumes=MediaType.ALL_VALUE)
	public ResponseEntity<String>insertMonthlyBill(@RequestParam("customerID") int customerID, @RequestParam("mobileNo") long mobileNo, @RequestParam("billMonth") String billMonth, @RequestParam("noOfLocalSMS") int noOfLocalSMS, @RequestParam("noOfStdSMS") int noOfStdSMS , @RequestParam("noOfLocalCalls") int noOfLocalCalls, @RequestParam("noOfStdCalls") int noOfStdCalls, @RequestParam("internetDataUsageUnits") int internetDataUsageUnits) throws BillingServicesDownException, PlanDetailsNotFoundException, CustomerDetailsNotFoundException, PostpaidAccountNotFoundException, InvalidBillMonthException{
		services.generateMonthlyMobileBill(customerID, mobileNo, billMonth, noOfLocalSMS, noOfStdSMS, noOfLocalCalls, noOfStdCalls, internetDataUsageUnits);
		return new ResponseEntity<>("Monthly Bill generated succesfully added",HttpStatus.OK);
		
	}
	
	
	/*@RequestMapping(value={"/allCustomerDetailsJSON"},headers="Accept=application/json")
	public ResponseEntity<ArrayList<Customer>> getAllCustomerJSON() throws CustomerDetailsNotFoundException{
		ArrayList<Customer> custList= new ArrayList<>(lue());
		return new ResponseEntity<>(custList,HttpStatus.OK);
	}*/
	
}
