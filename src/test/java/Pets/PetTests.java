package Pets;

import Entities.Pet;
import Entities.Pet;
import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.filter.log.ErrorLoggingFilter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static io.restassured.config.LogConfig.logConfig;
import static io.restassured.module.jsv.JsonSchemaValidator.*;
import static org.hamcrest.Matchers.*;

/*esse label "TestMethodOrder" é responsavel pela ordenação dos testes, ou seja, marca com a tag quais seriam executados primeiro,
os que não possuem são executados depois.
A vantagem do seu uso: alguns testes dependem um do outro, porem tem alguns que são independentes.
* */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class PetTests {
    private static Pet pet; //variável que vai ser reutilizada nos testes

    public static Faker faker;
    public static RequestSpecification request;
    @BeforeAll //quer dizer que vai ser executado antes de tudo
    public static void setup(){
        RestAssured.baseURI = "https://petstore.swagger.io/v2";

        //Instanciação do Faker
        faker = new Faker();

        //Geração do usuário com o uso do Faker
        pet = new Pet(
                faker.number().numberBetween(1,1),
                faker.animal().name()
                );
    }
    //Executa uma função antes que cada um dos testes neste arquivo seja executado.
    @BeforeEach
    void setRequest (){
        request = given().config(RestAssured.config().logConfig(logConfig().enableLoggingOfRequestAndResponseIfValidationFails()))
                .header("api-key", "special-key") //com given, vai ser a primeira pre condição
                .contentType(ContentType.JSON);

    }

    @Test
    @Order(1)
    public void CreateNewPet_WithValidData_ReturnOk(){
        request.body(pet)
                .when()
                .post("/pet")
                .then()
                .assertThat().statusCode(200).time(lessThan(2000L)).log();
    }
    @Test
    @Order(2)
    public void GetPetById_ValidPet_ReturnOk(){
        request.when()
                .get("/pet/" + pet.getIdpet())
                .then()
                .assertThat().statusCode(200).and().time(lessThan(2000L))
                .and().body("petid", equalTo(pet.getIdpet()));
    }

    @Test
    @Order(3)
    public void CreateNewPet_withInvalidBody_ReturnBadRequest(){
        Response response = request.body("teste")
                .when()
                .post("/pet/")
                .then()
                .extract().response();

        Assertions.assertNotNull(response);
        Assertions.assertEquals(400, response.statusCode());
        Assertions.assertEquals(true, response.getBody().asPrettyString().contains("unknown"));
        Assertions.assertEquals(3, response.body().jsonPath().getMap("$").size()); // O $ indica ser o caminho raiz do body, com o size recupera o tamanho do body, que seria 3
    }

    @Test
    @Order(4)
    public void DeletePet_petExists_Returnok(){
        request.when()
                .delete("/pet/" + pet.getIdpet())
                .then()
                .assertThat().statusCode(200).and().time(lessThan(2000L))
                .log();
    }
}
