package com.axiomatics.main;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Properties;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import com.axiomatics.data.Connection;

public class SalesForceDeveloperAPI {
    private static final String RESOURCES_PATH = "resources";
    // property file containing the SalesForce instance connection details
    private static final String PROPERTIES_FILE = RESOURCES_PATH + "/salesforce.properties";

	public static void main(String[] args) throws Exception {
		
		SalesForceDeveloperAPI 	http 						= new SalesForceDeveloperAPI();
		Properties 				salesForceProperties 		= new Properties();
		//SalesForceContact		contact						= new SalesForceContact();
		Connection 				connParameters 				= new Connection();
		String					accessToken 				= null;
		String 					info						= null;
		
		System.out.println("Processing - Send Http requests");
		
		/*******************************************************************************/
		/*************************  SalesForce Instance Connection *********************/
		/*******************************************************************************/
		Scanner scanner = new Scanner (System.in);
		System.out.print("Enter Table (example : \"contact\" table): ");  
		String table = scanner.nextLine();
				
		System.out.print("Enter Column (example : \"title\" column): ");
		String column = scanner.nextLine();
		
		System.out.print("Enter condition (example : \"name = 'Tim Barr'\": ");  
		String condition = scanner.nextLine();
		
		
		/*******************************************************************************/
		/*************************  SalesForce Instance Connection *********************/
		/*******************************************************************************/		
		salesForceProperties.load(new FileInputStream(PROPERTIES_FILE));
		
		connParameters.setURL(salesForceProperties.getProperty("token_url"));
		connParameters.setClientID(salesForceProperties.getProperty("client_id"));
		connParameters.setClientSecret(salesForceProperties.getProperty("client_secret"));
		connParameters.setUserName(salesForceProperties.getProperty("username"));
		connParameters.setPassword(salesForceProperties.getProperty("password"));
		/*******************************************************************************/
		
		// get access Token
		accessToken = http.getAccessToken(connParameters);
		System.out.println("Access Token: " + accessToken);
		
		// get contact info
		Connection 	connContact 	= new Connection();
		//String query = "query?q=SELECT+" + column+ "+from+" +table + "+where+" + condition.replaceAll(" ", "+");
		//String query = "SELECT+" + column+ "+from+" +table + "+where+" + condition.replaceAll(" ", "+");
		String query = "SELECT " + column+ " from " +table;
		
		//String query = "query?q=SELECT+Role__c+from+Contact+where+name+=";
		//String query = "query?q=SELECT+Role__c+from+Contact";
		
		connContact.setURL(salesForceProperties.getProperty("data_url"));
		connContact.setQuery(query);
		connContact.setCondition(" where " + condition);
		
		info = http.getInfo(connContact, column, accessToken);
		//info = http.getInfo(connContact, "", accessToken);
		//info = http.getInfo(connContact, "\'Rose+Gonzalez\'", accessToken);
		//info = http.getInfo(connContact, "\'Tim+Barr\'", accessToken);
		
		System.out.println("Role: " + info);
		System.out.println("Processing - End");

	}
	
	
	public String getInfo(Connection connContact, String column, String accessToken) throws Exception {
		
		JSONArray 			recordsArray 	= null;
		JSONObject 			recordsObject 	= null;
		String				info			= null;
		
		HttpGet request = buildRequest(connContact, accessToken);
		StringBuffer result = getResponse(request);
		
		JSONObject responseObject = new JSONObject(result.toString());
		Iterator<?> keys = responseObject.keys();
	    String key = "";
	    
	    while (keys.hasNext() && !key.equalsIgnoreCase("records")) {	        
	    	key = (String) keys.next();
	    	
	        if (key.equalsIgnoreCase("records")) {
	        	recordsArray 	= responseObject.getJSONArray(key);
	        	recordsObject 	= recordsArray.getJSONObject(0);
	        	info			= recordsObject.getString(column.substring(0, 1).toUpperCase() + column.substring(1).toLowerCase());
	        	
	        }
	    }

		return info;
	}


	public HttpGet buildRequest(Connection connContact, String accessToken) throws Exception{	
		
		//String getUrlCustomersList = connContact.getURL() + connContact.getQuery();
		//String getUrlCustomersList = connContact.getURL();
		//HttpGet request = new HttpGet(getUrlCustomersList);
		
		
		URIBuilder builder = new URIBuilder(connContact.getURL());
		//builder.setParameter("q", connContact.getQuery()).setFragment(connContact.getCondition());
		builder.setParameter("q", connContact.getQuery() + connContact.getCondition());
		
		HttpGet request = new HttpGet(builder.build());	
		request.addHeader("Authorization", "Bearer " + accessToken);
		
		System.out.println("\nSending 'GET' request to URL : " + builder.build());
		//System.out.println("\nSending 'GET' request to URL : " + getUrlCustomersList);
		
		return request;
	
	}


	public StringBuffer getResponse(HttpGet request) throws ClientProtocolException, IOException{
		
		HttpClient client = HttpClientBuilder.create().build();
		HttpResponse response = client.execute(request);
	
		System.out.println("Response Code : " +
	                   response.getStatusLine().getStatusCode());
	
		BufferedReader rd = new BufferedReader(
	                   new InputStreamReader(response.getEntity().getContent()));
	
		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		
		return result;
		
	}
	
	public String getAccessToken(Connection connParameters) throws Exception {
		
		String 	accessToken	= null;
		
		HttpPost request = postBuildRequest(connParameters, "");
		StringBuffer result = postResponse(request);
		
		JSONObject responseObject = new JSONObject(result.toString());
		Iterator<?> keys = responseObject.keys();
	    String key = "";
	    
	    while (keys.hasNext() && !key.equalsIgnoreCase("access_token")) {	        
	    	key = (String) keys.next();
	    	
	        if (key.equalsIgnoreCase("access_token")) {
	        	accessToken = responseObject.getString(key);
	        }
	    }

		return accessToken;
	}


	public HttpPost postBuildRequest(Connection connParameters, String strRequest){
		
		String getUrlCustomersList = connParameters.getURL() + "&client_id=" + connParameters.getClientID() + "&client_secret=" 
				+ connParameters.getClientSecret() + "&username=" + connParameters.getUserName() + "&password=" + connParameters.getPassword();
		
		HttpPost request = new HttpPost(getUrlCustomersList);
		
		System.out.println("\nSending 'GET' request to URL : " + getUrlCustomersList);
		
		return request;
	
	}


	public StringBuffer postResponse(HttpPost request) throws ClientProtocolException, IOException{
		
		HttpClient client = HttpClientBuilder.create().build();
		HttpResponse response = client.execute(request);
	
		System.out.println("Response Code : " +
	                   response.getStatusLine().getStatusCode());
	
		BufferedReader rd = new BufferedReader(
	                   new InputStreamReader(response.getEntity().getContent()));
	
		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		
		return result;
		
	}

}
