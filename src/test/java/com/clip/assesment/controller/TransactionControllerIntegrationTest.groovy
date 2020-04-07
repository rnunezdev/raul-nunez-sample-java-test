package com.clip.assesment.controller

import groovy.json.JsonSlurper
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.annotation.DirtiesContext.MethodMode
import org.springframework.test.context.junit4.SpringRunner


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TransactionControllerIntegrationTest {

    @LocalServerPort
    private int port

    TestRestTemplate restTemplate = new TestRestTemplate()

    HttpHeaders headers = new HttpHeaders()

    def jsonSlurper = new JsonSlurper()

    @Test
    void testGetAllByUser() throws Exception {
        HttpEntity<String> entity = new HttpEntity<>(null, headers)
        def response = restTemplate.exchange(createURLWithPort("/transaction/1/all"), HttpMethod.GET, entity, String.class)
        def body = jsonSlurper.parseText(response.body)

        assert response.status == HttpStatus.OK.value()
        assert body.size() == 9
        assert body[0].userId == 1
        assert body[0].date == "2018-01-01"
        assert body[0].amount == 34.67
        assert body[0].description == "Description 5"
    }

    @Test
    void testGetAllByUserNotFound() throws Exception {
        HttpEntity<String> entity = new HttpEntity<>(null, headers)
        def response = restTemplate.exchange(createURLWithPort("/transaction/13/all"), HttpMethod.GET, entity, String.class)
        def body = jsonSlurper.parseText(response.body)

        assert response.status == HttpStatus.OK.value()
        assert body.size() == 0 // returns empty list
    }

    @Test
    void testGetTransactionById() throws Exception {
        HttpEntity<String> entity = new HttpEntity<>(null, headers)
        def response1 = restTemplate.exchange(createURLWithPort("/transaction/1/all"), HttpMethod.GET, entity, String.class)
        def body1 = jsonSlurper.parseText(response1.body)

        assert response1.status == HttpStatus.OK.value()
        assert body1.size() == 9

        def id = body1[0].transactionId

        def response2 = restTemplate.exchange(createURLWithPort("/transaction/1/" + id), HttpMethod.GET, entity, String.class)
        def body2 = jsonSlurper.parseText(response2.body)

        assert body2 != null
        assert body2.transactionId == id
        assert body2.userId == 1
        assert body2.date == "2018-01-01"
        assert body2.amount == 34.67
        assert body2.description == "Description 5"

    }

    @Test
    void testGetTransactionByIdUserNotFound() throws Exception {
        HttpEntity<String> entity = new HttpEntity<>(null, headers)
        def response1 = restTemplate.exchange(createURLWithPort("/transaction/1/all"), HttpMethod.GET, entity, String.class)
        def body1 = jsonSlurper.parseText(response1.body)

        assert response1.status == HttpStatus.OK.value()
        assert body1.size() == 9

        def id = body1[0].transactionId

        def response2 = restTemplate.exchange(createURLWithPort("/transaction/13/" + id), HttpMethod.GET, entity, String.class)
        def body2 = jsonSlurper.parseText(response2.body)

        assert response2.status == HttpStatus.NOT_FOUND.value()
        assert body2.message == "Transaction not found"
    }

    @Test
    void testGetTransactionByIdUserNotMatchingTx() throws Exception {
        HttpEntity<String> entity = new HttpEntity<>(null, headers)
        def response1 = restTemplate.exchange(createURLWithPort("/transaction/1/all"), HttpMethod.GET, entity, String.class)
        def body1 = jsonSlurper.parseText(response1.body)

        assert response1.status == HttpStatus.OK.value()
        assert body1.size() == 9

        def id = body1[0].transactionId

        def response2 = restTemplate.exchange(createURLWithPort("/transaction/2/" + id), HttpMethod.GET, entity, String.class)
        def body2 = jsonSlurper.parseText(response2.body)

        assert response2.status == HttpStatus.NOT_FOUND.value()
        assert body2.message == "Transaction not found"
    }

    @Test
    void testGetTransactionByIdTxNotFound() throws Exception {
        HttpEntity<String> entity = new HttpEntity<>(null, headers)
        def response1 = restTemplate.exchange(createURLWithPort("/transaction/1/all"), HttpMethod.GET, entity, String.class)
        def body1 = jsonSlurper.parseText(response1.body)

        assert response1.status == HttpStatus.OK.value()
        assert body1.size() == 9

        def id = body1[0].transactionId

        def response2 = restTemplate.exchange(createURLWithPort("/transaction/1/" + id + "123"), HttpMethod.GET, entity, String.class)
        def body2 = jsonSlurper.parseText(response2.body)

        assert response2.status == HttpStatus.NOT_FOUND.value()
        assert body2.message == "Transaction not found"
    }

    @Test
    void testGetSumAll() throws Exception {
        HttpEntity<String> entity = new HttpEntity<>(null, headers)
        def response = restTemplate.exchange(createURLWithPort("/transaction/1/sum"), HttpMethod.GET, entity, String.class)
        def body = jsonSlurper.parseText(response.body)

        assert response.status == HttpStatus.OK.value()
        assert body.userId == 1
        assert body.sum == 952.71
    }

    @Test
    void testGetSumAllUserNotFound() throws Exception {
        HttpEntity<String> entity = new HttpEntity<>(null, headers)
        def response = restTemplate.exchange(createURLWithPort("/transaction/13/sum"), HttpMethod.GET, entity, String.class)
        def body = jsonSlurper.parseText(response.body)

        assert response.status == HttpStatus.OK.value()
        assert body.userId == 13
        assert body.sum == 0
    }

    @Test
    void testGetRandomTransaction() throws Exception {
        HttpEntity<String> entity = new HttpEntity<>(null, headers)
        def response1 = restTemplate.exchange(createURLWithPort("/transaction/random"), HttpMethod.GET, entity, String.class)
        def body1 = jsonSlurper.parseText(response1.body)

        def response2 = restTemplate.exchange(createURLWithPort("/transaction/random"), HttpMethod.GET, entity, String.class)
        def body2 = jsonSlurper.parseText(response2.body)

        assert body1.transactionId != body2.transactionId

    }

    @Test
    void testGetWeeklyReport() throws Exception {
        HttpEntity<String> entity = new HttpEntity<>(null, headers)
        def response = restTemplate.exchange(createURLWithPort("/transaction/1/weekly"), HttpMethod.GET, entity, String.class)
        def body = jsonSlurper.parseText(response.body)

        assert response.status == HttpStatus.OK.value()

        assert body[0].weekStart == "2018-01-01 MONDAY"
        assert body[0].weekEnd == "2018-01-04 THURSDAY"
        assert body[0].amount == 34.67
        assert body[0].totalAmount == 0
        assert body[0].quantity == 1

        assert body[1].weekStart == "2018-01-26 FRIDAY"
        assert body[1].weekEnd == "2018-01-31 WEDNESDAY"
        assert body[1].amount == 265.56
        assert body[1].totalAmount == 34.67
        assert body[1].quantity == 2

        assert body[2].weekStart == "2018-02-01 THURSDAY"
        assert body[2].weekEnd == "2018-02-01 THURSDAY"
        assert body[2].amount == 0.14
        assert body[2].totalAmount == 300.23
        assert body[2].quantity == 1

        assert body[3].weekStart == "2018-02-02 FRIDAY"
        assert body[3].weekEnd == "2018-02-08 THURSDAY"
        assert body[3].amount == 0.14
        assert body[3].totalAmount == 300.37
        assert body[3].quantity == 1

        assert body[4].weekStart == "2019-02-15 FRIDAY"
        assert body[4].weekEnd == "2019-02-21 THURSDAY"
        assert body[4].amount == 345.55
        assert body[4].totalAmount == 300.51
        assert body[4].quantity == 1

        assert body[5].weekStart == "2020-01-01 WEDNESDAY"
        assert body[5].weekEnd == "2020-01-02 THURSDAY"
        assert body[5].amount == 34.56
        assert body[5].totalAmount == 646.06
        assert body[5].quantity == 1

        assert body[6].weekStart == "2020-02-14 FRIDAY"
        assert body[6].weekEnd == "2020-02-20 THURSDAY"
        assert body[6].amount == 40.56
        assert body[6].totalAmount == 680.62
        assert body[6].quantity == 1

        assert body[7].weekStart == "2020-03-13 FRIDAY"
        assert body[7].weekEnd == "2020-03-19 THURSDAY"
        assert body[7].amount == 231.53
        assert body[7].totalAmount == 721.18
        assert body[7].quantity == 1

    }

    @Test
    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
    void testCreateTransaction() throws Exception {
        def bodyPost = """
            {
                "amount": 567.367,
                "description": "My description of insert",
                "date": "2020-04-06"
            }
            """
        HttpEntity<String> entity = new HttpEntity<>(bodyPost, new HttpHeaders(contentType: MediaType.APPLICATION_JSON))
        def response1 = restTemplate.exchange(createURLWithPort("/transaction/1"), HttpMethod.POST, entity, String.class)
        def body1 = jsonSlurper.parseText(response1.body)

        assert response1.status == HttpStatus.OK.value()
        assert body1.transactionId != null
        assert body1.userId == 1
        assert body1.date == "2020-04-06"
        assert body1.amount == 567.37
        assert body1.description == "My description of insert"


        entity = new HttpEntity<>(null, headers)
        def response2 = restTemplate.exchange(createURLWithPort("/transaction/1/all"), HttpMethod.GET, entity, String.class)
        def body2 = jsonSlurper.parseText(response2.body)

        assert response2.status == HttpStatus.OK.value()
        assert body2.size() == 10

    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri
    }
}
