package br.com.alura.screenMath;

import br.com.alura.screenMath.model.DadosSerie;
import br.com.alura.screenMath.service.ConsumoAPI;
import br.com.alura.screenMath.service.ConvertDado;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenMathApplication implements CommandLineRunner {

	public static void main(String[] args) {

		SpringApplication.run(ScreenMathApplication.class, args);

	}
	@Override
	public void
	run(String... args) throws Exception {
		ConsumoAPI consumoAPI = new ConsumoAPI();
		var json = consumoAPI.obterDados("https://www.omdbapi.com/?t=gilmore+girls&apikey=b2f8e6b");
		System.out.println(json);

		ConvertDado conversor = new ConvertDado();
		DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
		System.out.println(dados);
//		json = consumoAPI.obterDados("https://coffee.alexflipnote.dev/random.json");
//		System.out.println(json);
	}
}
