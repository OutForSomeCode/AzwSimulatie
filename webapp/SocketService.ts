import {
  BoxGeometry,
  DoubleSide,
  Group,
  Mesh,
  MeshBasicMaterial,
  Scene,
  TextureLoader
} from "three";
import {WorldObjectManger} from "./WorldObjectManager";

class SocketService {
  private socket: WebSocket;

  connect() {
    /*
   * Hier wordt de socketcommunicatie geregeld. Er wordt een nieuwe websocket aangemaakt voor het webadres dat we in
   * de server geconfigureerd hebben als connectiepunt (/connectToSimulation). Op de socket wordt een .onmessage
   * functie geregistreerd waarmee binnenkomende berichten worden afgehandeld.
   */
    this.socket = new WebSocket('ws://' + window.location.hostname + ':8081/connectToSimulation');
    this.socket.onmessage = (event) => {
      // Hier wordt het commando dat vanuit de server wordt gegeven uit elkaar gehaald
      var command = JSON.parse(event.data);

      // Wanneer het commando is "object_update", dan wordt deze code uitgevoerd. Bekijk ook de servercode om dit goed te begrijpen.
      if (command.command === 'object_update') {
        WorldObjectManger.getInstance().updateObject(command);
      }
      WorldObjectManger.getInstance().updateWorldPosition(command);

    };
    this.socket.onclose = e => {
      console.log('Socket is closed. Reconnect will be attempted in 1 second.', e.reason);
      WorldObjectManger.getInstance().CleanupAll();
      setTimeout(() => {
        this.connect();
      }, 1000);
    };
  }


}

export {SocketService};
