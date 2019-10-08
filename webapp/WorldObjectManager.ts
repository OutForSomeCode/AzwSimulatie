import {
  AmbientLight,
  BoxGeometry,
  DoubleSide,
  Group,
  Mesh,
  MeshBasicMaterial,
  PerspectiveCamera, PlaneGeometry,
  Scene,
  SphereGeometry,
  TextureLoader
} from "three";

class Objectmanger {
  private worldObjects = {};
  private scene = new Scene();
  private static instance: Objectmanger;
  private camera: PerspectiveCamera;
  private constructor() {
  }

  public initworld(){
    const sphericalSkyboxGeometry = new SphereGeometry(900, 32, 32);
    const sphericalSkyboxMaterial = new MeshBasicMaterial({
      map: new TextureLoader().load('assets/textures/yellow_field_2k.jpg'),
      side: DoubleSide
    });
    const sphericalSkybox = new Mesh(sphericalSkyboxGeometry, sphericalSkyboxMaterial);
    this.scene.add(sphericalSkybox);

    const geometry = new PlaneGeometry(30, 30, 32);
    const material = new MeshBasicMaterial({
      color: 0xf75b23,
      side: DoubleSide
    });
    const plane = new Mesh(geometry, material);
    plane.rotation.x = Math.PI / 2.0;
    plane.position.x = 15;
    plane.position.z = 15;
   this.scene.add(plane);

    const light = new AmbientLight(0x404040);
    light.intensity = 4;
    this.scene.add(light);
  }
  static getInstance(): Objectmanger {
    if (!Objectmanger.instance) {
      Objectmanger.instance = new Objectmanger();
    }

    return Objectmanger.instance;
  }
  public getworldobjects(){
    return this.worldObjects
  }
  public getscene(){return this.scene}

  UpdateObject(command) {
    // Wanneer het object dat moet worden geupdate nog niet bestaat (komt niet voor in de lijst met worldObjects op de client),
    // dan wordt het 3D model eerst aangemaakt in de 3D wereld.
    if (Object.keys(this.worldObjects).indexOf(command.parameters.uuid) < 0) {
      // Wanneer het object een robot is, wordt de code hieronder uitgevoerd.
      if (command.parameters.type === 'robot') {
        Objectmanger.getInstance().Makerobot(command)
      }
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

  }
  public Makerobot(command): void {
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
    console.log(command.parameters.uuid);

  }

}

export {Objectmanger}
