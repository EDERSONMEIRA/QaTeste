package org.example;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertTrue;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Unit test for simple App.
 */
public class AppTest
{

    /**------------- Config -------------------*/
    @BeforeClass
    public static  void setup(){
        RestAssured.baseURI ="https://randomuser.me/";
        RestAssured.basePath = "/api";
    }


    /**-------------Retorna 20 usuarios-------------------*/
    @Test
    public void retornarVinteUsuario(){

             given()
                .param("results",20)
                .when()
                .get("")
                .then()
                .statusCode(200)
                .body("info.results", Matchers.is(20))
             ;

    }

    /**-------------Retorna 1 usuarios Brasileiro-------------------*/
    @Test
    public void retornarUsuarioBrasileiro(){

        given()
                .param("nat","BR")
                .when()
                .get("")
                .then()
                .statusCode(200)
                .body("results[0].nat", Matchers.is("BR"))
        ;

    }

    /**-------------Retorna 1 usuarios da Pagina 3-------------------*/

    @Test
    public void retornarUsuarioPaginado(){

        given()
                .param("page",3)
                .param("results",1)
                .when()
                .get("")
                .then()
                .statusCode(200)
        ;

    }

    /**-------------Retorna Nome e Email-------------------*/

    @Test
    public void retornarNomeEmail(){

        given()
                .param("inc","name,email")
                .when()
                .get("")
                .then()
                .statusCode(200)
                .body("results[0]", Matchers.hasKey("name"))
                .body("results[0]", Matchers.hasKey("email"))
        ;
    }

    /**-------------Retorna Usuario BR,US,ES OU CA -------------------*/

    @Test
    public void retornarUsuarioBrUsEsCa(){

        List<String> nascionalidades = Arrays.asList("BR","US","ES","CA");

        String nat =
                given()
                .param("nat","BR,US,ES,CA")
                .when()
                .get("")
                .then()
                .statusCode(200)
                .extract().path("results[0].nat ")
        ;

        System.out.println(nat);
        Assert.assertThat(nascionalidades, Matchers.hasItem(nat));

    }


}
