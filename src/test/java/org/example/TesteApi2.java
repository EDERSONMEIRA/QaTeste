package org.example;

import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonMapFormatVisitor;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class TesteApi2 {


    /**------------- Config -------------------*/
    private  static String token;
    @BeforeClass
    public static  void setup(){
        RestAssured.baseURI ="https://restful-booker.herokuapp.com/";
        token();
    }


    /**-------------Retorna Lista de Reserva-------------------*/
    @Test
    public void retornarListaReserva(){

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("https://restful-booker.herokuapp.com/booking")
                .then()
                .statusCode(200)
                .body("$", Matchers.notNullValue())
                .body("$.size()", Matchers.greaterThan(0))
        ;
    }

    /**-------------Retorna Lista de Reserva-------------------*/
    @Test
    public void criarReserva(){
         String reserva ="{\n" +
                "    \"firstname\" : \"Ederson\",\n" +
                "    \"lastname\" : \"ghgj\",\n" +
                "    \"totalprice\" : 111,\n" +
                "    \"depositpaid\" : true,\n" +
                "    \"bookingdates\" : {\n" +
                "        \"checkin\" : \"2018-01-01\",\n" +
                "        \"checkout\" : \"2019-01-01\"\n" +
                "    },\n" +
                "    \"additionalneeds\" : \"Breakfast\"\n" +
                "}";

                 given()
                    .contentType(ContentType.JSON)
                    .body(reserva)
                    .when()
                    .post("https://restful-booker.herokuapp.com/booking")
                    .then()
                    .statusCode(200)
                    .body("bookingid",Matchers.notNullValue())
                    .body("booking.firstname", is("Ederson"))
                 .log().body()
        ;
    }


    /**-------------Validar Reserva Criada-------------------*/
    @Test
    public void validarReservaCriada(){
        String reserva ="{\n" +
                "    \"firstname\" : \"Ederson\",\n" +
                "    \"lastname\" : \"ghgj\",\n" +
                "    \"totalprice\" : 111,\n" +
                "    \"depositpaid\" : true,\n" +
                "    \"bookingdates\" : {\n" +
                "        \"checkin\" : \"2018-01-01\",\n" +
                "        \"checkout\" : \"2019-01-01\"\n" +
                "    },\n" +
                "    \"additionalneeds\" : \"Breakfast\"\n" +
                "}";

        Integer idReserva =
                given()
                .contentType(ContentType.JSON)
                .body(reserva)
                .when()
                .post("https://restful-booker.herokuapp.com/booking")
                .then()
                .statusCode(200)
                .body("bookingid",notNullValue())
                .extract().path("bookingid")
        ;

        given()
                .contentType(ContentType.JSON)
                .when()
                .log().all()
                .get("https://restful-booker.herokuapp.com/booking/{idReserva}",idReserva)
                .then()
                .statusCode(200)
                .body("firstname", is("Ederson"))
                .log().body()
        ;
    }


    /**-------------Deletar Reserva-------------------*/
    @Test
    public void deletarReserva(){

        /**Map<String, String> params = new HashMap<String, String>();
        params.put("username", "admin");
        params.put("password", "password123");

        String token =
                given()
                        .contentType(ContentType.JSON)
                        .body(params)
                        .when()
                        .post("https://restful-booker.herokuapp.com/auth")
                        .then()
                        .statusCode(200)
                        .body("token",Matchers.notNullValue())
                        .extract().path("token")
                ;

        String reserva ="{\n" +
                "    \"firstname\" : \"Ederson\",\n" +
                "    \"lastname\" : \"ghgj\",\n" +
                "    \"totalprice\" : 111,\n" +
                "    \"depositpaid\" : true,\n" +
                "    \"bookingdates\" : {\n" +
                "        \"checkin\" : \"2018-01-01\",\n" +
                "        \"checkout\" : \"2019-01-01\"\n" +
                "    },\n" +
                "    \"additionalneeds\" : \"Breakfast\"\n" +
                "}";

        Integer idReserva =
                given()
                        .contentType(ContentType.JSON)
                        .body(reserva)
                        .when()
                        .post("https://restful-booker.herokuapp.com/booking")
                        .then()
                        .statusCode(200)
                        .body("bookingid",Matchers.notNullValue())
                        .extract().path("bookingid")
                ;*/

        Integer idReserva =  criarReservaParaDeletar();
        System.out.println(idReserva);

        //String token = token();

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer {token}",token)
                //.header("Authorization", "Bearer {token}",token)
                .log().all()
                .when()
                .delete("https://restful-booker.herokuapp.com/booking/{idReserva}",idReserva)
                //.delete("/booking/{idReserva}",idReserva)
                .then()
                .statusCode(403)
        ;
    }

    /**------------- Metodos -------------------*/
    public static void token(){

        Map<String, String> params = new HashMap<String, String>();
        params.put("username", "admin");
        params.put("password", "password123");

            token =
                given()
                        .contentType(ContentType.JSON)
                        .body(params)
                        .when()
                        .post("https://restful-booker.herokuapp.com/auth")
                        .then()
                        .statusCode(200)
                        .body("token", notNullValue())
                        .extract().path("token")
                ;

       // return idReserva;
    }


    public int criarReservaParaDeletar(){
        String reserva ="{\n" +
                "    \"firstname\" : \"Ederson\",\n" +
                "    \"lastname\" : \"ghgj\",\n" +
                "    \"totalprice\" : 111,\n" +
                "    \"depositpaid\" : true,\n" +
                "    \"bookingdates\" : {\n" +
                "        \"checkin\" : \"2018-01-01\",\n" +
                "        \"checkout\" : \"2019-01-01\"\n" +
                "    },\n" +
                "    \"additionalneeds\" : \"Breakfast\"\n" +
                "}";

        Integer idReserva =
                given()
                        .contentType(ContentType.JSON)
                        .body(reserva)
                        .when()
                        .post("https://restful-booker.herokuapp.com/booking")
                        .then()
                        .statusCode(200)
                        .body("bookingid", notNullValue())
                        .extract().path("bookingid")
                ;
        return idReserva;
    }

}
