import {
  AmbientLight,
  BoxGeometry,
  BufferGeometry,
  CubeTextureLoader,
  DoubleSide,
  GridHelper,
  Group,
  Line,
  LineBasicMaterial,
  Mesh,
  MeshBasicMaterial,
  MeshStandardMaterial,
  Scene,
  SphereGeometry,
  SplineCurve,
  TextureLoader,
  Vector2
} from "three";
import {OBJLoader} from "three/examples/jsm/loaders/OBJLoader";
import TWEEN from '@tweenjs/tween.js';
import Dat from "dat.gui";
import "three-dat.gui";

class WorldObjectManger {
  private worldObjects: Array<Group> = [];
  private preloadedModels: Record<string, Group> = {};
  private reqModels: Array<Array<string>> = [
    ["Robot", "Robot_Robot"],
    ["Rack", "Rack_RackMat"],
    ["Warehouse", "Warehouse_Concrete"],
    ["Box", "BoxMat"],
    ["Table", "TableMat"],
    ["Cone4D", "ConeMat"]
  ];
  private scene = new Scene();
  private truck;
  private objLoader = new OBJLoader();
  textureCube;


  public loadModels(callback: () => void) {
    const r = "https://threejs.org/examples/textures/cube/Bridge2/";
    const urls = [r + "posx.jpg", r + "negx.jpg",
      r + "posy.jpg", r + "negy.jpg",
      r + "posz.jpg", r + "negz.jpg"];

    this.textureCube = new CubeTextureLoader().load(urls);

    const promises = [];
    this.reqModels.forEach(e => {
      promises.push(this.LoadObj(e));
    });
    Promise.all(promises).then(callback);
  }

  private LoadObj(dat: Array<string>) {
    return new Promise(resolve => {
      this.objLoader.load(`assets/models/${dat[0]}.obj`, obj => {
        obj.traverse(m => {
          if (m instanceof Mesh)
            m.material = new MeshStandardMaterial({
              map: new TextureLoader().load(`assets/textures/${dat[1]}_BaseColor.png`),
              normalMap: new TextureLoader().load(`assets/textures/${dat[1]}_Normal.png`),
              metalnessMap: new TextureLoader().load(`assets/textures/${dat[1]}_Metallic.png`),
              roughnessMap: new TextureLoader().load(`assets/textures/${dat[1]}_Roughness.png`),
              bumpMap: new TextureLoader().load(`assets/textures/${dat[1]}_Height.png`),
              envMap: this.textureCube
            });
        });
        this.preloadedModels[dat[0]] = obj;
        resolve();
      });
    });
  }

  public initWorld() {

    var gui = new Dat.GUI();
    gui.addMaterial("amount of warehouse modules", 0, 20, 1);
    const sphericalSkyboxGeometry = new SphereGeometry(900, 32, 32);
    const sphericalSkyboxMaterial = new MeshBasicMaterial({
      map: new TextureLoader().load('assets/textures/lebombo_2k.jpg'),
      side: DoubleSide
    });
    const sphericalSkybox = new Mesh(sphericalSkyboxGeometry, sphericalSkyboxMaterial);
    this.scene.add(sphericalSkybox);

    this.createWarehouse(10);

    const gridHelper = new GridHelper(42, 42);
    gridHelper.position.x = 21;
    gridHelper.position.z = 21;
    this.scene.add(gridHelper);

    const light = new AmbientLight(0x404040);
    light.intensity = 4;
    this.scene.add(light);

    let obj = this.getModel("Table");
    obj.position.x = 21;
    obj.position.y = -35;
    obj.position.z = 60;
    obj.rotation.y = Math.PI / 2;
    obj.scale.x = 20;
    obj.scale.y = 20;
    obj.scale.z = 20;
    this.addScene(obj);

    /*this.truck = this.getModel("Cone4D");
    this.scene.add(this.truck);*/

  }

  private getModel(name: string) {
    return this.preloadedModels[name].clone();
  }

  private createWarehouse(numberOfModules) {
    for (let i = 0; i < numberOfModules; i++) {
      let obj = this.getModel("Warehouse");
      obj.rotation.y = Math.PI;
      obj.position.x = 11.5;
      obj.position.y = 0;
      obj.position.z = 2.5 + (i * 6);
      this.addScene(obj)
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
    if (Object.keys(this.worldObjects).indexOf(command.parameters.id) < 0) {
      // Wanneer het object een robot is, wordt de code hieronder uitgevoerd.
      if (command.parameters.type === 'robotlogic') {
        this.createRobot(command);
      }
      if (command.parameters.type === 'rack') {
        this.createRack(command);
      }
    }
    /*
   * Deze code wordt elke update uitgevoerd. Het update alle positiegegevens van het 3D object.
   */
    const object = this.worldObjects[command.parameters.id];

    if (object == null)
      return;

    if (command.parameters.type === 'robotlogic') {
      object.lookAt(command.parameters.x, command.parameters.z, command.parameters.y);
      var tween = new TWEEN.Tween(object.position)
        .to({
          x: command.parameters.x,
          y: command.parameters.z,
          z: command.parameters.y
        }, 525);
      tween.autoDestroy = true;
      tween.start();
    } else {
      object.position.x = command.parameters.x;
      object.position.y = command.parameters.z;
      object.position.z = command.parameters.y;
    }
  }

  public createRobot(command): void {
    let robot = this.getModel("Robot");
    this.scene.add(robot);
    this.worldObjects[command.parameters.id] = robot;
  }

  public createRack(command): void {
    const scene = this.scene;
    const worldObjects = this.worldObjects;

    let rack = this.getModel("Rack");
    //initial spawn position of the racks
    rack.position.y = -1000;
    scene.add(rack);
    worldObjects[command.parameters.id] = rack;
  }


  public movetruck(time, modulePosition): void {
    const truckPosition = new Vector2();   // the x y coordinates of the truck
    const tankTarget = new Vector2();  //the position where the front of the truck is
    var moduleMultiplier = modulePosition * 6;    // the difference distance between modules

    //creates the line where the truck moves over
    const curve = new SplineCurve([
      new Vector2(30, 100),
      new Vector2(30, moduleMultiplier + 14),
      new Vector2(30, moduleMultiplier + 4),
      new Vector2(31, moduleMultiplier + 1.5),
      new Vector2(32, moduleMultiplier + -1),
      new Vector2(35, moduleMultiplier + -3),
      new Vector2(37, moduleMultiplier + -3.2),
      new Vector2(40, moduleMultiplier + -3.5),
      new Vector2(30, moduleMultiplier + -3.6),
      new Vector2(25, moduleMultiplier + -3.7),
      new Vector2(26, moduleMultiplier + -4),
      new Vector2(28, moduleMultiplier + -4.5),
      new Vector2(29, moduleMultiplier + -8),
      new Vector2(29, moduleMultiplier + -13),

      new Vector2(30, moduleMultiplier + -20),
      new Vector2(30, -20),
      new Vector2(50, -5),
      new Vector2(50, 10),
      new Vector2(50, 100),
      new Vector2(30, 100),
    ]);

    const points = curve.getPoints(50);
    const geometry = new BufferGeometry().setFromPoints(points);
    const material = new LineBasicMaterial({color: 0xff0000});
    const splineObject = new Line(geometry, material);
    splineObject.rotation.x = Math.PI * .5;
    splineObject.position.y = 0.05;
    this.scene.add(splineObject);
    const tankTime = time * .05;
    curve.getPointAt(tankTime % 1, truckPosition);
    curve.getPointAt((tankTime + 0.01) % 1, tankTarget);
    this.truck.position.set(truckPosition.x, 0, truckPosition.y); // put the tuck to the x y coordinates
    let maxz = (-3.9 + moduleMultiplier);
    let minz = (-3.38 + moduleMultiplier);
    if (this.truck.position.x < 41 && this.truck.position.z >= maxz && this.truck.position.z <= minz) //checks if the truck is between the given parameters and make it look backwards
    {
      this.truck.lookAt(41, 0, -4.1 + moduleMultiplier);
    } else //look at the line
    {
      this.truck.lookAt(tankTarget.x, 0, tankTarget.y);
    }
  }

  parentObject(command) {
    var objs = command.parameters.split("|");
    let robot = this.worldObjects[objs[0]];
    let rack = this.worldObjects[objs[1]];

    rack.parent = robot;
    rack.position.set(0, 0.1, 0);
  }

  unparentObject(command) {
    var objs = command.parameters.split("|");
    let robot = this.worldObjects[objs[0]];
    let rack = this.worldObjects[objs[1]];
    rack.parent = null;
    rack.position.set(robot.position.x, robot.position.y, robot.position.z);
  }
}

export {WorldObjectManger}
