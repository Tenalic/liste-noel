package sc.liste.noel.liste_noel.back.ressource;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sc.liste.noel.liste_noel.back.dto.response.CompteResponse;

import static sc.liste.noel.liste_noel.back.mapper.Constantes.RETOUR_API_KO;

@RestControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CompteResponse> handleValidationErrors(
            MethodArgumentNotValidException ex) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(FieldError::getDefaultMessage)
                .orElse("Erreur de validation");

        return ResponseEntity.badRequest()
                .body(new CompteResponse(null, message, RETOUR_API_KO));
    }

}
