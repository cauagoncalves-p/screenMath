package br.com.alura.screenMath;
import br.com.alura.screenMath.principal.Principal;
import br.com.alura.screenMath.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenMathApplication {
	public static void main(String[] args) {
		SpringApplication.run(ScreenMathApplication.class, args);
	}
}
