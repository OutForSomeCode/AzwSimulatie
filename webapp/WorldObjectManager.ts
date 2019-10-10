import {
  AmbientLight,
  BoxGeometry,
  DoubleSide,
  Group,
  Mesh,
  MeshBasicMaterial,
  PlaneGeometry,
  Scene,
  SphereGeometry,
  TextureLoader
} from "three";
import {GLTFLoader} from "three/examples/jsm/loaders/GLTFLoader";

class WorldObjectManger {
  private worldObjects: Array<Group> = [];
  private scene = new Scene();
  private gltfLoader = new GLTFLoader();
  private loader = new GLTFLoader();



  public initWorld() {
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

    /*const url = 'assets/models/Warehousepart1.gltf';
    this.gltfLoader.load(url,(gltf) => {
      this.addscene(gltf)
    });*/

    const light = new AmbientLight(0x404040);
    light.intensity = 4;
    this.scene.add(light);
  }

  private addscene(gltf){
    this.scene.add( gltf.scene );
  }
  public getWorldObjects(): Array<Group> {
    return this.worldObjects;
  }

  public getScene(): Scene {
    return this.scene;
  }

  public updateObject(command): void {
    // Wanneer het object dat moet worden geupdate nog niet bestaat (komt niet voor in de lijst met worldObjects op de client),
    // dan wordt het 3D model eerst aangemaakt in de 3D wereld.
    if (Object.keys(this.worldObjects).indexOf(command.parameters.uuid) < 0) {
      // Wanneer het object een robot is, wordt de code hieronder uitgevoerd.
      if (command.parameters.type === 'robot') {
        this.makeRobot(command)
        //this.makeRack(command)
      }
    }
    /*
   * Deze code wordt elke update uitgevoerd. Het update alle positiegegevens van het 3D object.
   */
    const object = this.worldObjects[command.parameters.uuid];

    if(object == null)
      return;

    object.position.x = command.parameters.x;
    object.position.y = command.parameters.y;
    object.position.z = command.parameters.z;

    object.rotation.x = command.parameters.rotationX;
    object.rotation.y = command.parameters.rotationY;
    object.rotation.z = command.parameters.rotationZ;
  }

  public makeRobot(command): void {
    const geometry = new BoxGeometry(0.9, 0.3, 0.9);
    const cubeMaterials = [
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
    const robot = new Mesh(geometry, cubeMaterials);
    robot.position.y = 0.15;

    const group = new Group();
    group.add(robot);
    this.scene.add(group);
    this.worldObjects[command.parameters.uuid] = group;
  }
  public makeRack(command): void{

    const url = 'assets/models/Rack.gltf';
    this.gltfLoader.load(url,(gltf) => {
     this.dmake(gltf,command)
    });


  }
  private dmake(gltf,command) {
    const rack = gltf.scene;
    rack.position.y = 0.15;
    rack.position.x = 15;
    rack.position.z = 15;
    const group = new Group();
    this.scene.add(rack);
    this.worldObjects[command.parameters.uuid] = group;
  }
  public makeWarehouse(command): void {
      const url = 'assets/models/Warehousepart1.gltf';
      this.gltfLoader.load(url,(gltf) => {
        this.dmake(gltf,command)
      });
  }

  CleanupAll(): void {
    for (let e in this.worldObjects) {
      this.scene.remove(this.worldObjects[e])
    }
  }

}

export {WorldObjectManger}
