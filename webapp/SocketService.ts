// @ts-ignore Word geinject vanuit webpack
const whost = WEBSOCKETHOST;

import {WorldObjectManger} from "./WorldObjectManager";
//import {decode, ExtensionCodec} from "@msgpack/msgpack";
import * as MessagePack from 'what-the-pack';

class SocketService {
  private socket: WebSocket;
  private _worldObjectManger: WorldObjectManger;
  private messagePack;

  constructor(w){
    this._worldObjectManger = w ; // making use of Object injection
    this.messagePack = MessagePack.initialize(2**30);
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

      var dat = Buffer.from(e.data);
      const command = this.messagePack.decode(dat);
      //const data = decode(dat);
      //console.log(command);

      // Wanneer het commando is "object_update", dan wordt deze code uitgevoerd. Bekijk ook de servercode om dit goed te begrijpen.
      if (command.command === 'object_update') {
        this._worldObjectManger.updateObject(command);
      }
      //this._worldObjectManger.updateWorldPosition(command);

    };
    this.socket.onclose = e => {
      console.log('Socket is closed. Reconnect will be attempted in 1 second.', e.reason);
      this._worldObjectManger.CleanupAll();
      setTimeout(() => {
        this.connect();
      }, 1000);
    };
  }

}

export {SocketService};
