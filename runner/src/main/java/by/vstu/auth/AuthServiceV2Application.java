package by.vstu.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.UUID;

@SpringBootApplication
@RestControllerAdvice
public class AuthServiceV2Application {

	public static void main(String[] args) {
		SpringApplication.run(AuthServiceV2Application.class, args);
	}


	@ExceptionHandler(Throwable.class)
	public ResponseEntity<Object> handleException(Exception ex, WebRequest req) {

		ex.printStackTrace();

		String errorCode = UUID.randomUUID().toString().split("-")[0];

		ResponseStatus responseStatus = AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class);

		return ResponseEntity
				.status(responseStatus == null ? HttpStatus.INTERNAL_SERVER_ERROR : responseStatus.value())
				.contentType(MediaType.APPLICATION_JSON)
				.body(String.format(
						"""
                        {
                        "error": "%s",
                        "code": %s,
                        "description": "An unexpected error occurred. Contact with CIT-team for more information and say that errorCode.",
                        "errorCode": "%S"
                        }
                        """
						, responseStatus == null ? ex.getMessage() : responseStatus.reason()
						, responseStatus == null ? 500 : responseStatus.value().value()
						, errorCode));

	}
}
