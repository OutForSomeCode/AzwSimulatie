// @ts-ignore Word geinject vanuit webpack
const whost = WEBSOCKETHOST;

import {WorldObjectManger} from "./WorldObjectManager";
//import {decode, ExtensionCodec} from "@msgpack/msgpack";
import * as MessagePack from 'what-the-pack';

class SocketService {
  private socket: WebSocket;
  private _worldObjectManger: WorldObjectManger;
  private messagePack;

  /**
   * the constructor of the socket service
   * @param w gives the world object manger and making use of object injection
   */
  constructor(w) {
    this._worldObjectManger = w; // making use of Object injection
    this.messagePack = MessagePack.initialize(2 ** 20);
  }

  /*
    * Hier wordt de socketcommunicatie geregeld. Er wordt een nieuwe websocket aangemaakt voor het webadres dat we in
    * de server geconfigureerd hebben als connectiepunt (/connectToSimulation). Op de socket wordt een .onmessage
    * functie geregistreerd waarmee binnenkomende berichten worden afgehandeld.
    */
  connect() {
    this.socket = new WebSocket(whost);

    // Connection opened
    this.socket.addEventListener('open', (event) => {
      this.socket.binaryType = 'arraybuffer'; // important
      console.log('Connected to server.');
    });

    this.socket.onmessage = e => {
      // Hier wordt het commando dat vanuit de server wordt gegeven uit elkaar gehaald
      let commands = this.messagePack.decode(Buffer.from(e.data));
      //const data = decode(dat);
      //console.log(commands);
      for (let com of commands) {
        //console.log(com);
        // Wanneer het commando is "object_update", dan wordt deze code uitgevoerd. Bekijk ook de servercode om dit goed te begrijpen.
        if (com.command === 'update') {
          this._worldObjectManger.updateObject(com);
        } else if (com.command === 'parent') {
          this._worldObjectManger.parentObject(com);
        } else if (com.command === 'unparent') {
          this._worldObjectManger.unparentObject(com);
        }
      }

    };
    this.socket.onclose = e => {
      console.log('Socket is closed. Reconnect will be attempted in 5 second.', e.reason);
      setTimeout(() => {
        window.location.reload();
      }, 5000);
    };
  }

}

export {SocketService};
