package com.cartodb.impl;

import com.cartodb.CartoDBClientIF;
import com.cartodb.CartoDBException;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * CartoDB client implementation to access protected resources on CartoDB using OAuth.
 * You can perform any SQL queries so please, be careful.
 * @author canadensys
 *
 */
public class ApiKeyCartoDBClient extends CartoDBClientIF {

    private static final String DEFAULT_API_VERSION = "1";
    private static final String ENCODING = "UTF-8";

    private static final String SQL_API_BASE_URL = "https://%s.cartodb.com/api/v%s/sql/";

    private String user;
    private String apiVersion = DEFAULT_API_VERSION;
    private String apiURL = null;

    /**
     * aki key, it can be found in your dashboard
     */
    private String apiKey;


    /**
     * Default constructor
     */
    public ApiKeyCartoDBClient(){}

    /**
     * After this constructor, the object is ready to use.
     * @param user
     * @param apiKey api provided by cartodb to access to secured resources
     * @throws CartoDBException
     */
    public ApiKeyCartoDBClient(String user, String apiKey) throws CartoDBException {
        this.user = user;
        this.apiKey = apiKey;
        if(this.apiKey == null || this.apiKey.length() == 0) {
            throw new CartoDBException("provided API key is not valid");
        }
        init();
    }

    /**
     * Initialization method for a regular CartoDB client.
     * You only need it if you're using the default constructor.
     */
    public void init(){
        apiURL = String.format(SQL_API_BASE_URL, user, apiVersion);
    }

    /**
     * Send a sqlQuery to the CartoDB server.
     * The query will be sent in a URL parameter of a GET so, you should avoid very large query string.
     * @param sqlQuery
     * @throws CartoDBException
     */
    public String executeQuery(String sqlQuery) throws CartoDBException {

        if(apiURL == null){
            throw new CartoDBException("Error : uninitialized " + getClass().getName());
        }

        String json;
        try {
            sqlQuery = URLEncoder.encode(sqlQuery, ENCODING);
            String params = "q=" + sqlQuery + "&api_key=" + this.apiKey;

            URL url = new URL(apiURL);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(params);
            wr.flush();
            wr.close();
            json = IOUtils.toString(conn.getInputStream(), ENCODING);
            
        } catch (MalformedURLException e) {
            throw new CartoDBException("Could not get URL " + apiURL + sqlQuery);
        }catch (IOException e) {
        	e.printStackTrace();
            throw new CartoDBException("Could not execute " + sqlQuery + " on CartoDB : ");
        }
        return json;
    }

    public void setUser(String user){
        this.user = user;
    }

    /**
     * Set the API version to use.
     * @param apiVersion number part only as String
     */
    public void setApiVersion(String apiVersion){
        this.apiVersion = apiVersion;
    }

}
