package com.karolmalinowski.election;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
//This one runs javaFx application
public class ElectionApplication extends Application{
private ConfigurableApplicationContext context;

	@Override
	public void init(){
		ApplicationContextInitializer<GenericApplicationContext> initializer =
		new ApplicationContextInitializer<GenericApplicationContext>() {
			@Override
			public void initialize(GenericApplicationContext ac) {
				ac.registerBean(Application.class, () -> ElectionApplication.this);
				ac.registerBean(Parameters.class, () -> getParameters());
				ac.registerBean(HostServices.class, () -> getHostServices());
			}
		}	;
		this.context = new SpringApplicationBuilder()
				.sources(Main.class)
				.initializers(initializer)
				.run(getParameters().getRaw().toArray(new String[0]));
	}

	@Override
	public void start(Stage primaryStage){
		//when initialization is finished, then create stage etc.
		this.context.publishEvent(new StageReadyEvent(primaryStage));
	}

	@Override
	public void stop(){
		//close spring application
		this.context.close();
		//and close platform of javaFx application
		Platform.exit();
	}
	public void showNewWindow()  {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/votingPage.fxml"));
			fxmlLoader.setControllerFactory(context::getBean);
			Parent root1 = fxmlLoader.load();
			Stage stage = new Stage();
			stage.setScene(new Scene(root1));
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
class StageReadyEvent extends ApplicationEvent {
	public Stage getStage(){
		return Stage.class.cast(getSource());
	}
	public StageReadyEvent(Stage source) {
		super(source);
	}
}
