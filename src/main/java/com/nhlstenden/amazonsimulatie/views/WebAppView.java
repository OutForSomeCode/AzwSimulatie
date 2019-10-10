package com.nhlstenden.amazonsimulatie.views;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhlstenden.amazonsimulatie.base.Command;
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
public class WebAppView implements View {
  private WebSocketSession sesion;
  private Command onClose;

  public WebAppView(WebSocketSession sesion) {
    this.sesion = sesion;
  }

  /*
   * Deze methode wordt aangroepen vanuit de controller wanneer er een update voor
   * de views is. Op elke view wordt dan de update methode aangroepen, welke een
   * JSON pakketje maakt van de informatie die verstuurd moet worden. Deze JSON
   * wordt naar de browser verstuurd, welke de informatie weer afhandeld.
   */
  @Override
  public void update(BinaryMessage bin) {
    try {
      if (this.sesion.isOpen()) {
        this.sesion.sendMessage(bin);
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
}
