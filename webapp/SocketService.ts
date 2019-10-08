import {BoxGeometry, DoubleSide, Group, Mesh, MeshBasicMaterial, Scene, TextureLoader} from "three";

class SocketService {
  private worldObjects: Array<Group> = [];
  private scene: Scene;
  private socket: WebSocket;

  constructor(w, s: Scene) {
    this.worldObjects = w;
    this.scene = s;
  }

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
        this.UpdateObject(command);
      }

      /*
         * Deze code wordt elke update uitgevoerd. Het update alle positiegegevens van het 3D object.
         */
      var object = this.worldObjects[command.parameters.uuid];

      object.position.x = command.parameters.x;
      object.position.y = command.parameters.y;
      object.position.z = command.parameters.z;

      object.rotation.x = command.parameters.rotationX;
      object.rotation.y = command.parameters.rotationY;
      object.rotation.z = command.parameters.rotationZ;
    };
    this.socket.onclose = e => {
      console.log('Socket is closed. Reconnect will be attempted in 1 second.', e.reason);
      this.Cleanup();
      setTimeout(() => {
        this.connect();
      }, 1000);
    };
  }

  Cleanup() {
    for (let e in this.worldObjects) {
      this.scene.remove(this.worldObjects[e])
    }
  }

  UpdateObject(command) {
    // Wanneer het object dat moet worden geupdate nog niet bestaat (komt niet voor in de lijst met worldObjects op de client),
    // dan wordt het 3D model eerst aangemaakt in de 3D wereld.
    if (Object.keys(this.worldObjects).indexOf(command.parameters.uuid) < 0) {
      // Wanneer het object een robot is, wordt de code hieronder uitgevoerd.
      if (command.parameters.type === 'robot') {
        var geometry = new BoxGeometry(0.9, 0.3, 0.9);
        var cubeMaterials = [
          new MeshBasicMaterial({
            map: new TextureLoader().load('assets/textures/robot_side.png'),
            side: DoubleSide
          }), // LEFT
          new MeshBasicMaterial({
            map: new TextureLoader().load('assets/textures/robot_side.png'),
            side: DoubleSide
          }), // RIGHT
          new MeshBasicMaterial({
            map: new TextureLoader().load('assets/textures/robot_top.png'),
            side: DoubleSide
          }), // TOP
          new MeshBasicMaterial({
            map: new TextureLoader().load('assets/textures/robot_bottom.png'),
            side: DoubleSide
          }), // BOTTOM
          new MeshBasicMaterial({
            map: new TextureLoader().load('assets/textures/robot_front.png'),
            side: DoubleSide
          }), // FRONT
          new MeshBasicMaterial({
            map: new TextureLoader().load('assets/textures/robot_front.png'),
            side: DoubleSide
          }) // BACK
        ];
        var robot = new Mesh(geometry, cubeMaterials);
        robot.position.y = 0.15;

        var group = new Group();
        group.add(robot);

        this.scene.add(group);
        this.worldObjects[command.parameters.uuid] = group;
      }
    }
  }
}

export {SocketService};
