package services;

import com.google.gson.Gson;
import groovyjarjarantlr.Token;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class TestBooking {
    String token;
    String bookingId;

    @BeforeTest
    public void CreateToken(){
        String postData="{ \n"+
                "\"username\" : \"admin\",\n" +
                "    \"password\" : \"password123\"\n" +"}";

        Response response= given()
                .log().all()
                .header("Content-Type","application/json")
                .body(postData).
                when()
                .post("https://restful-booker.herokuapp.com/auth").
                then()
                .statusCode(200)
                .log().all().extract().response();
        token=response.jsonPath().getString("token");
    }
    @Test (priority = 1)
    public void HealthCheck(){

    }
    @Test (priority = 2)
    public void CreateBooking(){
        //OrderRequest orderRequest =new OrderRequest("Özgül","Doğanay",1500,true,2013.03.10 10:13:46,2013.03.10 10:13:46,"OK");
        // String request= new Gson().toJson(orderRequest);
        String postData="{ \n"+
                    "\"firstname\" : \"HEYY\",\n" +
                "    \"lastname\" : \"HEYY\",\n" +
                "    \"totalprice\" : 1500,\n" +
                "    \"depositpaid\" : true,\n" +
                "    \"bookingdates\" : {\n" +
                "        \"checkin\" : \"2022-02-31\",\n" +
                "        \"checkout\" : \"2022-03-12\"\n" +
                "    },\n" +
                "    \"additionalneeds\" : \"Breakfast\"\n"+"}";

        Response response= given()
                .log().all()
                .header("Content-Type","application/json")
                .body(postData).
                when()
                .post("https://restful-booker.herokuapp.com/booking").
                then()
                .statusCode(200)
                .log().all().extract().response();

        bookingId=response.jsonPath().getString("bookingid");

    }
    @Test(priority = 3)
    public void getBookingIds(){
        given().log().all().
                when()
                .get("https://restful-booker.herokuapp.com/booking/").
                then()
                .statusCode(200)
                .log().all();

    }
    @DataProvider(name="dataProvider")
    public Object[][] DataProvider() {

        return new Object[][]{
                {11},
                {12},
                {13}
        };
    }

    @Test(dataProvider="dataProvider",priority = 4)
    public void getBooking(int BookingId){
        given().log().all().
                when()
                .get("https://restful-booker.herokuapp.com/booking/" + BookingId).
                then()
                .statusCode(200)
                .log().all();
    }

    @Test(priority = 5)
    public  void PartialUpdateBooking(){
       // int BookingId = 20 ;
        Map<String,Object> queryParams = new HashMap<>();
        queryParams.put("firstname","Doğanay");
        queryParams.put("lastname","Özgül");

        given()
                .log().all()
                .header("Content-Type","application/json")
                .header("Cookie", "token="+ token)
                //header("Authorisation", "Basic")
                .body(queryParams).
                when()
                .patch("https://restful-booker.herokuapp.com/booking/"+bookingId ).
                then()
                .statusCode(200)
                .log().all();

    }
    @Test (priority = 6)
    public void updateBooking(){
        String putData="{ \n"+
                "\"firstname\" : \"Özgül\",\n" +
                "    \"lastname\" : \"Doğanay\",\n" +
                "    \"totalprice\" : 1000,\n" +
                "    \"depositpaid\" : false,\n" +
                "    \"bookingdates\" : {\n" +
                "        \"checkin\" : \"2022-02-30\",\n" +
                "        \"checkout\" : \"2022-03-13\"\n" +
                "    },\n" +
                "    \"additionalneeds\" : \"NoBreakfast\"\n"+"}";

        given()
                .log().all()
                .header("Content-Type","application/json")
                .header("Cookie", "token="+ token)
                .body(putData).
                when()
                .put("https://restful-booker.herokuapp.com/booking/"+bookingId ).
                then()
                .statusCode(200)
                .log().all();

    }
    @AfterTest
    public void deleteBooking(){
        given()
                .log().all()
                .header("Content-Type","application/json")
                .header("Cookie","token="+token).
                when()
                .delete("https://restful-booker.herokuapp.com/booking/"+bookingId).
                then()
                .statusCode(201)
                .log().all();

    }
}

