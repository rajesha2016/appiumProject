package com.appium.start.api;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.glassfish.jersey.client.ClientConfig;

public class IdmAccessor {


	private static Logger log = Logger.getLogger(IdmAccessor.class);

	private static String findIdForMsisdn(Long msisdn) throws IOException, Exception {

		ClientConfig clientConfig = new ClientConfig();

		Client client = ClientBuilder.newClient(clientConfig);
		String id = null;

		WebTarget webTarget = client
				.target("http://dgig-de.sp.vodafone.com:30503")
				.path("lifecycle").path("individual")
				.queryParam("query", "show")
				.queryParam("msisdn", msisdn.toString())
				.queryParam("accountType", "ALL");

		Invocation.Builder invocationBuilder = webTarget
				.request(MediaType.APPLICATION_XML);
		invocationBuilder
		.header("Authorization", "Basic VkZURVNUOlZGODcyR0tKUw==")
		.header("vf-gig-ci-type", "ZYB ID")
		.header("vf-gig-ti-reference-id", "12345")
		.header("vf-gig-ti-service-version", "1.0")
		.header("vf-gig-ti-application-id", "ABC")
		.header("vf-gig-ti-service-id", "1")
		.header("vf-gig-ti-correlation-id",
				"postIndividual_001|putIndividual_001")
				.header("vf-gig-ti-identity-id", "abc12345")
				.header("vf-gig-ti-timestamp", "2011-01-01T01:01:01Z")
				.header("vf-gig-ci-identifier", "1001")
				.header("x-vf-audit-partner-id", "CAMBOI");

		Response response = invocationBuilder.get();
		
		if (response.getStatus() != 200) {
			throw new IOException("HTTP error: " + response + " ("
					+ response.getStatusInfo() + ")");
		}
		DocumentBuilderFactory builderFactory =
				DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		builder = builderFactory.newDocumentBuilder();
		String xml = response.readEntity(String.class);

		System.out.println(xml);

		id = getId("<individualId>(\\d*)</individualId>", xml);

		if(null == id){

			log.info("Couldn't find ID for " + msisdn +". "
					+ "Please check that user with "+msisdn+" exists in IDM. ");
		}

		return id;
	}

	 static void createIDMEntry(Long msisdn) throws IOException, Exception {

		ClientConfig clientConfig = new ClientConfig();

		Client client = ClientBuilder.newClient(clientConfig);
		String id = null;

		WebTarget webTarget = client
				.target("http://dgig-de.sp.vodafone.com:30503")
				.path("lifecycle").path("individual")
				.queryParam("query", "show")
				.queryParam("msisdn", msisdn.toString())
				.queryParam("accountType", "ALL");

		Invocation.Builder invocationBuilder = webTarget
				.request(MediaType.APPLICATION_XML);
		invocationBuilder
		.header("Authorization", "Basic VkZURVNUOlZGODcyR0tKUw==")
		.header("vf-gig-ci-type", "ZYB ID")
		.header("vf-gig-ti-reference-id", "12345")
		.header("vf-gig-ti-service-version", "1.0")
		.header("vf-gig-ti-application-id", "ABC")
		.header("vf-gig-ti-service-id", "1")
		.header("vf-gig-ti-correlation-id",
				"postIndividual_001|putIndividual_001")
				.header("vf-gig-ti-identity-id", "abc12345")
				.header("vf-gig-ti-timestamp", "2011-01-01T01:01:01Z")
				.header("vf-gig-ci-identifier", "1001")
				.header("x-vf-audit-partner-id", "CAMBOI");

		String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"+
				"<lifecycle:individual xmlns:lifecycle=\"http://www.vodafone.com/vf/gig/lifecycle/types/v1_0\" xmlns:core=\"http://www.vodafone.com/vf/core_common/types/v0_4\">"+
				"  <services> "+
				"  <service>"+
				"    <name>CONTACTS</name>"+
				"   </service>"+
				" </services>"+
				" <msisdns>"+
				"  <msisdn>"+
				"    <msisdn>"+msisdn+"</msisdn>"+
				"   <attributes>"+
				"     <attribute>"+
				"    <core:name>country</core:name>"+
				"      <core:value>DE</core:value>"+
				"    </attribute>"+
				"   </attributes>"+
				"  </msisdn>"+
				" </msisdns>"+
				" <attributes>"+
				"  <attribute>"+
				"    <core:name>channel</core:name>"+
				"     <core:value>WEB</core:value>"+
				"   </attribute>"+
				" </attributes>"+
				"</lifecycle:individual>";

		Response response = invocationBuilder.post(Entity.entity(xml, MediaType.APPLICATION_XML));
		
		if (response.getStatus() != 200) {
			throw new IOException("HTTP error: " + response + " ("
					+ response.getStatusInfo() + ")");
		}

	}

	private static void deleteId(Long id) throws IOException {
		ClientConfig clientConfig = new ClientConfig();

		Client client = ClientBuilder.newClient(clientConfig);

		WebTarget webTarget = client
				.target("http://dgig-de.sp.vodafone.com:30503")
				.path("lifecycle").path("individual").path(id.toString())
				.queryParam("type", "purge");

		Invocation.Builder invocationBuilder = webTarget
				.request(MediaType.APPLICATION_XML);
		invocationBuilder
		.header("Authorization", "Basic VkZURVNUOlZGODcyR0tKUw==")
		.header("vf-gig-ci-type", "ZYB ID")
		.header("vf-gig-ti-reference-id", "12345")
		.header("vf-gig-ti-service-version", "1.0")
		.header("vf-gig-ti-application-id", "ABC")
		.header("vf-gig-ti-service-id", "1")
		.header("vf-gig-ti-correlation-id",
				"deleteIndividual_101")
				.header("vf-gig-ti-identity-id", "abc12345")
				.header("vf-gig-ti-timestamp", "2011-01-01T01:01:01Z")
				.header("vf-gig-ci-identifier", "1001")
				.header("x-vf-audit-partner-id", "ZYB");
		Response response = invocationBuilder.delete();
		

		if (response.getStatus() != 200) {
			throw new IOException("HTTP error: " + response + " ("
					+ response.getStatusInfo() + ")");
		}
		
		log.info(id + " deleted successfully from IDM.");
	}

	public static final void deleteMsisdn(Long l)
	{
		try{
		String id = findIdForMsisdn(l);

		
		if(!(id.equals(null))){

			deleteId(Long.parseLong(id));
		}
		}catch(Exception n){
			
			log.error("Error occurred while deleting IDM entry for msisdn - "+ l + 
					"\r\n Error Message is - " + n.getMessage());
		}
	}

	private static String getId(String regex, String inputString){

		Pattern pattern = Pattern.compile(regex);
		Matcher match = pattern.matcher(inputString);
		String id = null;

		if (match.find()){

			id = match.group(1);
		}

		return id;

	}


	public static void main(String[] args) throws IOException, Exception{
		
		//IdmAccessor.deleteId(31646635166l);
		IdmAccessor.createIDMEntry(31646635166l);
	}
}
