package com.cartodb;

import com.cartodb.impl.ApiKeyCartoDBClient;
import org.junit.Before;

public class CartoDBApiKeyClient extends CartoDBTestClient {

    @Before
    public void setUp() throws Exception {
        this.cartoDBCLient = new ApiKeyCartoDBClient(
                Secret.USER,
                Secret.API_KEY);
    }
}
