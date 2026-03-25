import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {

    public String timestamp() {
        LocalDateTime timestamp = LocalDateTime.now();
        DateTimeFormatter formatado = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return timestamp.format(formatado);
    }

}
