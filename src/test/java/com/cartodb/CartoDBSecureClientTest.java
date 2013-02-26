package com.cartodb;

import com.cartodb.impl.SecuredCartoDBClient;
import org.junit.Before;

public class CartoDBSecureClientTest extends CartoDBTestClient {

    @Before
    public void setUp() throws Exception {
        this.cartoDBCLient = new SecuredCartoDBClient(
                Secret.USER,
                Secret.PASSWORD,
                Secret.CONSUMER_KEY,
                Secret.CONSUMER_SECRET);
    }
}
