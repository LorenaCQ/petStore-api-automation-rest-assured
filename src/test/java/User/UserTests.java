package User;

import Entities.User;
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
    @BeforeAll //quer dizer que vai ser executado antes de tudo
    public void setup(){
        RestAssured.baseURI = "https://petstore.swagger.io/v2";

    }
}
