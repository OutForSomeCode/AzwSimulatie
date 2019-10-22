package com.nhlstenden.amazonsimulatie.base;

import com.nhlstenden.amazonsimulatie.controllers.Controller;
import com.nhlstenden.amazonsimulatie.controllers.DocumentStoreHolder;
import com.nhlstenden.amazonsimulatie.controllers.RESTController;
import com.nhlstenden.amazonsimulatie.controllers.SimulationController;
import com.nhlstenden.amazonsimulatie.views.WebAppView;
import net.ravendb.client.documents.operations.Operation;
import net.ravendb.client.documents.operations.PatchByQueryOperation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

import java.io.IOException;


/*
 * Dit is de hoofdklasse van de applicatie. De app werkt met Spring, een Java
 * framework welke handig is, bijvoorbeeld om een simpele server te schrijven.
 * De main methode zet de app in werking en er wordt een nieuw object gemaakt
 * van de class WebSocketServer. Dit gedeelte handeld Spring voor je af.
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackageClasses = RESTController.class)
@EnableWebSocket
public class WebSocketServer extends SpringBootServletInitializer implements WebSocketConfigurer {

  //De WebSocketServer is de applicatie en heeft de controller voor de simulatie in zich
  private Controller simController;

  /*
   * De constructor wordt uitgevoerd wanneer de app wordt opgebouwd. Je zult alleen
   * geen new WebSocketServer() tegenkomen. Dit doet Spring namelijk al voor je bij
   * SpringApplication.run().
   */
  public WebSocketServer() {
    this.simController = new SimulationController();
    this.simController.start();
  }

  /*
   * De main methode regelt het starten van de Spring applicatie. Dit gebeurd
   * middels SpringApplication.run(). Deze zorgt ervoor dat onze WebSocketServer gerund
   * wordt. Dit kan doordat de class WebSocketServer de class SpringBootServletInitializer
   * extend. Dit is een class van Spring welke een server voor ons maakt.
   * De WebSocketServer class is daardoor dus een server.
   */
  public static void main(String[] args) {
    DocumentStoreHolder.getStore().initialize();
    Operation operation = DocumentStoreHolder.getStore().operations()
      .sendAsync(
        new PatchByQueryOperation("from RobotPOJOs as r update { r.status=\"IDLE\"; r.rack = null }")
      );

    operation.waitForCompletion();
    Operation operation1 = DocumentStoreHolder.getStore().operations()
      .sendAsync(
        new PatchByQueryOperation("from Racks as r update { r.status=\"POOLED\"; r.z = -10;}")
      );

    operation1.waitForCompletion();
    SpringApplication.run(WebSocketServer.class, args);
  }

  /*
   * Dit is een standaardmethode van Spring en heeft te maken met het SpringApplication.run()
   * proces.
   */
  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(WebSocketServer.class);
  }

  /*
   * Deze methode moet worden geïmplementeerd omdat de class WebSocketServer de interface
   * WebSocketConfigurer implementeerd. Deze interface maakt het mogelijk
   * om zogenaamde WebSocketHandlers te registreren in het systeem. Dit
   * zijn onderdelen in de software die binnenkomende websocket verbindingen
   * afhandelen en iets met de binnenkomende informatie doen, dan wel nieuwe
   * informatie terugsturen naar de client.
   */
  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    registry.addHandler(new DefaultWebSocketHandler(), "/connectToSimulation").setAllowedOrigins("*");
  }

  /*
   * Deze class is de standaard WebSocketHandler van dit systeem. Wat hier gebeurd
   * is dat elke keer wanneer een connectie wordt gemaakt met het systeem via een
   * websocket waar deze WebSocketHandler voor is geregistreerd (zie registerWebSocketHandlers),
   * dan wordt de methode afterConnectionEstablished aangeroepen.
   */
  private class DefaultWebSocketHandler extends BinaryWebSocketHandler {
    /*
     * Binnen deze methode wordt, wanneer een nieuwe websocket connectie wordt gemaakt met
     * de server, een nieuwe view aangemaakt voor die connectie. De view is een
     * DefaultWebSocketView en is de view die wordt gebruikt wanneer we een browser als
     * front-end gebruiken. De sesion variabele is het onderdeel waarmee we informatie
     * kunnen sturen.
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession sesion) {
      simController.addView(new WebAppView(sesion));
    }

    /*
     * Via deze methode kunnen binnenkomende berichten van een websocket worden
     * afgehandeld. De berichten zijn gekoppeld aan een bepaalde sessie. Deze methode
     * is niet in gebruik voor de standaardcode die gegeven is. Je kunt zelf wel een
     * implementatie maken.
     */
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
      //Do something to handle user input here
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws IOException {
      session.close(CloseStatus.SERVER_ERROR);
    }
  }
}
