package User;

import Entities.User;
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
public class UserTests {

    private static User user; //variável que vai ser reutilizada nos testes

    public static Faker faker;
    public static RequestSpecification request;
    @BeforeAll //quer dizer que vai ser executado antes de tudo
    public static void setup(){
        RestAssured.baseURI = "https://petstore.swagger.io/v2";

        //Instanciação do Faker
        faker = new Faker();

        //Geração do usuário com o uso do Faker
        user = new User(
                    faker.name().username(),
                    faker.name().firstName(),
                    faker.name().lastName(),
                    faker.internet().safeEmailAddress(),
                    faker.internet().password(8,10),
                    faker.phoneNumber().toString()
                );
    }

    //Executa uma função antes que cada um dos testes neste arquivo seja executado.
    @BeforeEach
    void setRequest (){
        request = given().header("api-key", "special-key") //com given, vai ser a primeira pre condição
                .contentType(ContentType.JSON);

    }

    @Test
    public void CreateNewUser_WithValidData_ReturnOk(){
        request.body(user)
                .when()
                .post("/user")
                .then()
                .assertThat().statusCode(200).and()
                .body("code", equalTo(200))
                .body("type", equalTo("unknown"))
                .body("message", isA(String.class))
                .body("size()", equalTo(3));
    }
    @Test
    public void GetLogin_ValidUser_ReturnOn(){
        request.param("username", user.getUsername())
                .param("password", user.getPassword())
                .when()
                .get("/user/login")
                .then()
                .assertThat().statusCode(200)
                .and().time(lessThan(2000L))
                .and().body(matchesJsonSchemaInClasspath("loginResponseSchema.json")); //validação do schema com o json schema validator instalado no pom.xml
    }

    @Test
    public void GetUserByUsername_userIsValid_ReturnOn(){
        request.when()
                .get("/user/" + user.getUsername())
                .then()
                .assertThat().statusCode(200).and().time(lessThan(2000L))
                .and().body("username", equalTo(user.getUsername()));
    }
}
