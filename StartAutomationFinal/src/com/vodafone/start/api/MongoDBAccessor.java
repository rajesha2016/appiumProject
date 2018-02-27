package com.vodafone.start.api;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MongoDBAccessor {

	private static Logger log = Logger.getLogger(MongoDBAccessor.class);
	private static final String PROFILE_DATABASE = "profile";
	private static final String IMEI_DATABASE = "imei";
	private static final String USERS_DATABASE = "users";
	private String urlPrefix = null;
	private String basicAuth = null;

	  /**
	   * Constructor to setup a conection to the mongodb.
	   * @param host host of the mongodb
	   * @param dbname dbname of the mongodb on the host
	   * @param user user used for authenticaton
	   * @param passwd password used for authentication
	   */
	public MongoDBAccessor(String host, String dbName, String user, String passwd) {
		urlPrefix = new StringWriter().append("https://").append(host)
				.append("/mongod/").append(dbName).append("/").toString();
		String userpass = user + ":" + passwd;
		basicAuth = "Basic "
				+ new String(new Base64().encode(userpass.getBytes()));
	}

	private JSONObject getObject(String table, JSONObject criteria) throws Exception {
		JSONObject value = null;
		String https_url = new StringWriter().append(urlPrefix)
				.append(USERS_DATABASE).append("/_find?criteria=")
				.append(criteria.toString()).toString();
		URL url;
		HttpsURLConnection con = null;
		try {
			url = new URL(https_url);
			con = (HttpsURLConnection) url.openConnection();
			con.setRequestProperty("Authorization", basicAuth);
			int response = con.getResponseCode();
			if (response != 200) {
				throw new IOException("HTTP error: " + response + " ("
						+ con.getResponseMessage() + ")");
			}
			StringBuilder sb = readStream(con.getInputStream());
			value = new JSONObject(sb.toString());
		} catch (IOException e) {
			throw new IOException("Could not read from MongoDB (" + https_url
					+ ")", e);
		} finally {
			if (con != null) {
				con.disconnect();
			}
		}
		return value;
	}

	
	
	private JSONObject deleteObject(String table, JSONObject criteria) throws Exception {
		JSONObject value = null;
		String https_url = new StringWriter().append(urlPrefix)
				.append(table).append("/_remove").toString();
		String urlParameters = new StringWriter().append("criteria=")
				.append(criteria.toString()).toString();
		URL url;
		HttpsURLConnection con = null;
		try {
			url = new URL(https_url);
			con = (HttpsURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Authorization", basicAuth);
			con.setDoOutput(true);
			// Send request
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();
			
			int response = con.getResponseCode();
			if (response != 200) {
				throw new IOException("HTTP error: " + response + " ("
						+ con.getResponseMessage() + ")");
			}
			StringBuilder sb = readStream(con.getInputStream());
			value = new JSONObject(sb.toString());
			if (value.getInt("ok") != 1) {
				throw new IllegalStateException("Could not delete object from "
						+ table + " with " + criteria.toString());
			}
			
			log.info("MongoDB :: "+ table +" table cleared successfully for ID - " + criteria.get("_id"));
		} catch (IOException e) {
			throw new IOException("Could not read from MongoDB (" + https_url
					+ ")", e);
		} finally {
			if (con != null) {
				con.disconnect();
			}
		}
		return value;
	}

	private JSONObject insertObject(String table, JSONObject criteria) throws Exception {
		JSONObject value = null;
		String https_url = new StringWriter().append(urlPrefix)
				.append(USERS_DATABASE).append("/_insert").toString();
		String urlParameters = new StringWriter().append("criteria=")
				.append(criteria.toString()).toString();
		String docs = new StringWriter().append("'docs=")
				.append("[{\"sample\":\"1\"}]'").toString(); 
		URL url;
		HttpsURLConnection con = null;
		try {
			url = new URL(https_url);
			con = (HttpsURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Authorization", basicAuth);
			con.setDoOutput(true);
			// Send request
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.writeBytes(docs);
			wr.flush();
			wr.close();
			
			int response = con.getResponseCode();
			if (response != 200) {
				throw new IOException("HTTP error: " + response + " ("
						+ con.getResponseMessage() + ")");
			}
			StringBuilder sb = readStream(con.getInputStream());
			value = new JSONObject(sb.toString());
			if (value.getInt("ok") != 1) {
				throw new IllegalStateException("Could not delete object from "
						+ table + " with " + criteria.toString());
			}
			
			log.info("MongoDB cleared successfully for ID - " + criteria.get("_id"));
		} catch (IOException e) {
			throw new IOException("Could not read from MongoDB (" + https_url
					+ ")", e);
		} finally {
			if (con != null) {
				con.disconnect();
			}
		}
		return value;
	}

	
	private JSONObject getOneObject(String table, JSONObject criteria) throws Exception {
		JSONObject dbResult = getObject(table, criteria);
		if (!dbResult.get("ok").equals(new Integer(1))) {
			throw new IOException("Got error for request to mongodb " + table
					+ " " + criteria.toString());
		}
		JSONArray results = dbResult.getJSONArray("results");
		if (results.length() == 0) {
			throw new IllegalStateException("Got no result from mongodb "
					+ table + " for " + criteria.toString());
		}
		if (results.length() > 1) {
			throw new IllegalArgumentException(
					"Got to much results from mongodb" + table + " for "
							+ criteria.toString());
		}
		System.out.println( results.getJSONObject(0));
		return results.getJSONObject(0);
	}

	private JSONObject createMsisdnCriteria(long msisdn) {
		JSONObject criteria = new JSONObject();
		criteria.put("_id", new Long(msisdn).toString());
		return criteria;
	}

	JSONObject getUser(JSONObject criteria) throws Exception {
		return getOneObject(USERS_DATABASE, criteria);
	}

	  /**
	   * This method reads one user from the MongoDB
	   * @param msisdn  Specifies the user
	   * @return JSONObject the user
	   * @exception IOException On input error.
	   * @exception IllegalStateException No object for the msisdn in the database
	   * @exception IllegalArgumentException Several objects are found for the msisdn
	   */
	public JSONObject getUser(long msisdn) throws Exception {
		JSONObject criteria = createMsisdnCriteria(msisdn);
		return getUser(criteria);
	}

	JSONObject getImei(JSONObject criteria) throws Exception {
		return getOneObject(IMEI_DATABASE, criteria);
	}

	  /**
	   * This method reads one imei from the MongoDB
	   * @param msisdn  Specifies the imei
	   * @return JSONObject the imei
	   * @exception IOException On input error.
	   * @exception IllegalStateException No object for the msisdn in the database
	   * @exception IllegalArgumentException Several objects are found for the msisdn
	   */
	public JSONObject getImei(long msisdn) throws Exception {
		JSONObject criteria = createMsisdnCriteria(msisdn);
		return getImei(criteria);
	}

	JSONObject getProfile(JSONObject criteria) throws Exception {
		return getOneObject(PROFILE_DATABASE, criteria);
	}

	  /**
	   * This method reads one profile from the MongoDB
	   * @param msisdn  Specifies the profile
	   * @return JSONObject the profile
	   * @exception IOException On input error.
	   * @exception IllegalStateException No object for the msisdn in the database
	   * @exception IllegalArgumentException Several objects are found for the msisdn
	   */
	public JSONObject getProfile(long msisdn) throws Exception {
		JSONObject criteria = createMsisdnCriteria(msisdn);
		return getProfile(criteria);
	}

	void deleteUser(JSONObject criteria) throws Exception {
		deleteObject(USERS_DATABASE, criteria);
	}

	void deleteProfile(JSONObject criteria) throws Exception {
		deleteObject(PROFILE_DATABASE, criteria);
	}

	  /**
	   * This method deletes one user from the MongoDB
	   * @param msisdn  Specifies the user
	   * @exception IOException On input error.
	   * @exception IllegalStateException No object for the msisdn in the database
	   */
	public void deleteImei(JSONObject criteria) throws Exception {
		deleteObject(IMEI_DATABASE, criteria);
	}

	public void deleteUser(long msisdn) throws Exception {
		JSONObject criteria = createMsisdnCriteria(msisdn);
		deleteObject(USERS_DATABASE, criteria);
		//doPostSync(criteria);
	}

	  /**
	   * This method deletes one profile from the MongoDB
	   * @param msisdn  Specifies the profile
	   * @exception IOException On input error.
	   * @exception IllegalStateException No object for the msisdn in the database
	   */
	public void deleteProfile(long msisdn) throws Exception {
		JSONObject criteria = createMsisdnCriteria(msisdn);
		deleteObject(PROFILE_DATABASE, criteria);
	}
	  /**
	   * This method deletes one imei from the MongoDB
	   * @param msisdn  Specifies the imei
	   * @exception IOException On input error.
	   * @exception IllegalStateException No object for the msisdn in the database
	   */

	public void deleteImei(long msisdn) throws Exception {
		JSONObject criteria = createMsisdnCriteria(msisdn);
		deleteObject(IMEI_DATABASE, criteria);
	}

	  /**
	   * This method deletes one user, imie and profile from the MongoDB with the given MSISDN
	   * When deletion in one table fails, because of the missing object, 
	   * the deletion is continued for the other table
	   * @param msisdn  Specifies the user
	   * @exception IOException On input error.
	   */

	public void deleteAll(long msisdn) throws Exception {
		try {
			deleteUser(msisdn);
		} catch (IllegalStateException e) {
			System.out
					.println("User could not be deleted continue with remaining"
							+ e);
		}
		try {
			deleteProfile(msisdn);
		} catch (IllegalStateException e) {
			System.out
					.println("Profile could not be deleted continue with remaining"
							+ e);
		}
		try {
			deleteImei(msisdn);
		} catch (IllegalStateException e) {
			System.out
					.println("Imei could not be deleted continue with remaining"
							+ e);
		}
	}

	private StringBuilder readStream(InputStream in) throws IOException {
		InputStreamReader is = new InputStreamReader(in);
		StringBuilder sb = new StringBuilder();
		BufferedReader br = new BufferedReader(is);
		String read = br.readLine();
		while (read != null) {
			sb.append(read);
			read = br.readLine();
		}
		return sb;
	}

	public static void main(String[] args) {
		try {
			MongoDBAccessor mongodb = new MongoDBAccessor("tools.bc.sp.vodafone.com",
					"SspUserRepositoryPre1", "mongod", "sspmongo");
			JSONObject criteria = new JSONObject();
			criteria.put("_id", "31646635166");
//			JSONObject object = mongodb.getUser(criteria);
//			
//			String id = object.getString("_id");
			
			mongodb.insertObject(USERS_DATABASE,criteria);
		
			
//			object = mongodb.getUser(491723106501l);
//			
//			System.out.println("\nUsers\n");
//			keys = (Iterator<String>) object.keys();
//			while (keys.hasNext()) {
//				String key = keys.next();
//				System.out.println(key + ":" + object.get(key));
//
//			}
//			object = mongodb.getImei(491723106501l);
//			keys = (Iterator<String>) object.keys();
//			System.out.println("\nImei\n");
//			while (keys.hasNext()) {
//				String key = keys.next();
//				System.out.println(key + ":" + object.get(key));
//
//			}
//
//			object = mongodb.getProfile(491723106501l);
//			keys = (Iterator<String>) object.keys();
//			System.out.println("\nProfile\n");
//			while (keys.hasNext()) {
//				String key = keys.next();
//				System.out.println(key + ":" + object.get(key));
//
//			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
