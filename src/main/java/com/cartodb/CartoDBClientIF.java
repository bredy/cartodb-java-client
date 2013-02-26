package com.cartodb;

import com.cartodb.model.CartoDBResponse;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

/**
 * Interface for CartoDB client implementation
 * @author canadensys
 */
public abstract class CartoDBClientIF {
	
	public int MAX_SQL_GET_LENGTH = 1024;
	
	ObjectMapper jsonMapper = new ObjectMapper();
	/**
	 * Executes a query on the CartoDB server
	 * @param sqlQuery do not URL encode the query, this function will do it.
	 * @return response as JSON string or null if the query cannot be completed
	 * @throws CartoDBException 
	 */
	public abstract String executeQuery(String sqlQuery) throws CartoDBException;
	
	/**
	 * return a CartoDBResponse object with the response json parsed
	 * @param <T> object to map the columns
	 * @param sqlQuery
	 * @return CartoDBResponse object
	 * @throws CartoDBException
	 */
	public <T> CartoDBResponse<T> request(String sqlQuery) throws CartoDBException {
		String json = executeQuery(sqlQuery);
		CartoDBResponse<T> response;
		try {
			response = jsonMapper.readValue(json, new TypeReference<CartoDBResponse<T>>(){});
			
		} catch (Exception e) {
			throw new CartoDBException(e.getMessage());
		}
		return response;
	}
	
	/**
	 * return true if the query writes
	 * @param sql
	 * @return
	 */
	public static boolean isWriteQuery(String sql) {
		String sqlLower = sql.toLowerCase();
        return sqlLower.contains("insert") ||
                sqlLower.contains("update") ||
                sqlLower.contains("delete") ||
                sqlLower.contains("create");
    }
	

}
