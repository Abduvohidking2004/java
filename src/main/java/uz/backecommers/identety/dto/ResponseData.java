package uz.backecommers.identety.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseData<T> {
    T data;

    int code;
    String message;

    String date = dateNow();

    public ResponseData(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResponseData(T data) {
        this.data = data;
        this.date = dateNow();
    }


    private static String dateNow() {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
    }

    public static ResponseEntity<ResponseData<?>> ok() {
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    public static <T> ResponseEntity<ResponseData<T>> ok(T data) {
        return new ResponseEntity<>(new ResponseData<>(data), HttpStatus.OK);
    }

    public static <T> ResponseEntity<ResponseData<T>> badRequest(int code, String message) {
        return new ResponseEntity<>(new ResponseData<>(code, message), HttpStatus.BAD_REQUEST);
    }

    public static <T> ResponseEntity<ResponseData<T>> error(int code, String message, HttpStatus status) {
        return new ResponseEntity<>(new ResponseData<>(code, message), status);
    }
}
