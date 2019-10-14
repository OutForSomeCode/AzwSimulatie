import {
  AmbientLight,
  BoxGeometry,
  CubeTextureLoader,
  DoubleSide,
  GridHelper,
  Group,
  Mesh,
  MeshBasicMaterial,
  MeshStandardMaterial,
  Scene,
  SphereGeometry,
  TextureLoader
} from "three";
import {GLTFLoader} from "three/examples/jsm/loaders/GLTFLoader";
import {OBJLoader} from "three/examples/jsm/loaders/OBJLoader";

class WorldObjectManger {
  private worldObjects: Array<Group> = [];
  private scene = new Scene();
  private objloader = new OBJLoader();
  textureCube;

  public initWorld() {

    var r = "https://threejs.org/examples/textures/cube/Bridge2/";
    var urls = [r + "posx.jpg", r + "negx.jpg",
      r + "posy.jpg", r + "negy.jpg",
      r + "posz.jpg", r + "negz.jpg"];

    this.textureCube = new CubeTextureLoader().load(urls);

    const sphericalSkyboxGeometry = new SphereGeometry(900, 32, 32);
    const sphericalSkyboxMaterial = new MeshBasicMaterial({
      map: new TextureLoader().load('assets/textures/lebombo_2k.jpg'),
      side: DoubleSide
    });
    const sphericalSkybox = new Mesh(sphericalSkyboxGeometry, sphericalSkyboxMaterial);
    this.scene.add(sphericalSkybox);

    /* const geometry = new PlaneGeometry(42, 42, 32);
     const material = new MeshBasicMaterial({
       color: 0xffffff,
       side: DoubleSide
     });
     const plane = new Mesh(geometry, material);
     plane.rotation.x = Math.PI / 2.0;
     plane.position.x = 21;
     plane.position.z = 21;
     this.scene.add(plane);*/

    this.createWarehouse(4);

    const gridHelper = new GridHelper(42, 42);
    gridHelper.position.x = 21;
    gridHelper.position.z = 21;
    this.scene.add(gridHelper);

    const light = new AmbientLight(0x404040);
    light.intensity = 4;
    this.scene.add(light);
  }

  private createWarehouse(numberOfModules) {
    if (numberOfModules != 0) {
      for (let i = 0; i < numberOfModules; i++) {
        this.objloader.load('assets/models/Warehouse.obj', obj => {
          var textureCube = this.textureCube;
          obj.rotation.y = Math.PI;
          obj.position.x = 11.5;
          obj.position.y = 0;
          obj.position.z = 2.5 + (i * 6);
          obj.traverse( child => {
            if (child instanceof Mesh)
              child.material = new MeshStandardMaterial({
                map: new TextureLoader().load('assets/textures/Warehouse_Concrete_BaseColor.png'),
                normalMap: new TextureLoader().load('assets/textures/Warehouse_Concrete_Normal.png'),
                metalnessMap: new TextureLoader().load('assets/textures/Warehouse_Concrete_Metallic.png'),
                roughnessMap: new TextureLoader().load('assets/textures/Warehouse_Concrete_Roughness.png'),
                envMap: textureCube
              });
          });
          this.addScene(obj)
        });
      }
    }
  }

  private addScene(obj) {
    this.scene.add(obj);
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
        this.createRobot(command);
      }
      if (command.parameters.type === 'rack') {
        this.createRack(command);
      }
    }
    /*
   * Deze code wordt elke update uitgevoerd. Het update alle positiegegevens van het 3D object.
   */
    const object = this.worldObjects[command.parameters.uuid];

    if (object == null)
      return;

    object.position.x = command.parameters.x;
    object.position.y = command.parameters.y;
    object.position.z = command.parameters.z;

    object.rotation.x = command.parameters.rotationX;
    object.rotation.y = command.parameters.rotationY;
    object.rotation.z = command.parameters.rotationZ;
  }

  public createRobot(command): void {
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

  public createRack(command): void {
    var scene = this.scene;
    var worldObjects = this.worldObjects;
    var textureCube = this.textureCube;

    this.objloader.load('assets/models/Rack.obj', rack => {
      rack.traverse(function (child) {
        if (child instanceof Mesh)
          child.material = new MeshStandardMaterial({
            map: new TextureLoader().load('assets/textures/Rack_RackMat_BaseColor.png'),
            normalMap: new TextureLoader().load('assets/textures/Rack_RackMat_Normal.png'),
            metalnessMap: new TextureLoader().load('assets/textures/Rack_RackMat_Metallic.png'),
            roughnessMap: new TextureLoader().load('assets/textures/Rack_RackMat_Roughness.png'),
            envMap: textureCube
          });
      });

      const group = new Group();
      group.add(rack);
      //initial spawn position of the racks
      group.position.y = -1000;
      scene.add(group);
      worldObjects[command.parameters.uuid] = group;

    });

  }


  CleanupAll(): void {
    for (let e in this.worldObjects) {
      this.scene.remove(this.worldObjects[e])
    }
  }
}

export {WorldObjectManger}
