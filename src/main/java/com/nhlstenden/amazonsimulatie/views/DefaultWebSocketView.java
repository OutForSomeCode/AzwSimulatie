package com.nhlstenden.amazonsimulatie.views;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhlstenden.amazonsimulatie.base.Command;
import com.nhlstenden.amazonsimulatie.models.Object3D;
import org.msgpack.jackson.dataformat.MessagePackFactory;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.WebSocketSession;

/*
 * Deze class is de standaard websocketview. De class is een andere variant
 * van een gewone view. Een "normale" view is meestal een schermpje op de PC,
 * maar in dit geval is het wat de gebruiker ziet in de browser. Het behandelen
 * van een webpagina als view zie je vaker wanneer je te maken hebt met
 * serversystemen. In deze class wordt de WebSocketSession van de client opgeslagen,
 * waarmee de view class kan communiceren met de browser.
 */
public class DefaultWebSocketView implements View {
  private WebSocketSession sesion;
  private Command onClose;
  private ObjectMapper objectMapper;

  public DefaultWebSocketView(WebSocketSession sesion) {
    this.sesion = sesion;
    this.objectMapper= new ObjectMapper(new MessagePackFactory());
  }

  /*
   * Deze methode wordt aangroepen vanuit de controller wanneer er een update voor
   * de views is. Op elke view wordt dan de update methode aangroepen, welke een
   * JSON pakketje maakt van de informatie die verstuurd moet worden. Deze JSON
   * wordt naar de browser verstuurd, welke de informatie weer afhandeld.
   */
  @Override
  public void update(String event, Object3D data) {
    try {
      if (this.sesion.isOpen()) {
        this.sesion.sendMessage(
          new BinaryMessage(
            objectMapper.writeValueAsBytes(
              new packData(event, data)
            )
          )
        );
      } else {
        this.onClose.execute();
      }

    } catch (Exception e) {
      this.onClose.execute();
    }
  }

  @Override
  public void onViewClose(Command command) {
    onClose = command;
  }

  static class packData{
    public String command;
    public Object3D parameters;

    public packData(String c, Object3D d) {
      command = c;
      parameters = d;
    }
  }
}
