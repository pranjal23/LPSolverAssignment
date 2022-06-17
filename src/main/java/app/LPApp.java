package app;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LPApp {
	
	public static void main(String[] args) {
		Application.launch(LPApplicationFX.class, args);
	}
}
