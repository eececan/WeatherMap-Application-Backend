package backend.weatherApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;

@AutoConfiguration
@SpringBootApplication
public class WeatherAppApplication
{
	public static void main(String[] args)
	{
		SpringApplication.run(WeatherAppApplication.class, args);
	}
}
