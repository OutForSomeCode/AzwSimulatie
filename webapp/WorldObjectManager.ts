// @ts-ignore Word geinject vanuit webpack
const apiHost = HOST;

import {
  AmbientLight,
  CubeTextureLoader,
  GridHelper,
  Group,
  Mesh,
  MeshStandardMaterial,
  Renderer,
  Scene,
  TextureLoader
} from "three";
import {OBJLoader} from "three/examples/jsm/loaders/OBJLoader";
import TWEEN from '@tweenjs/tween.js';
import Dat from "dat.gui";
import "three-dat.gui";
import axios from "axios";

class WorldObjectManger {
  private worldObjects: Array<Group> = [];
  private preloadedModels: Record<string, Group> = {};
  private reqModels: Array<Array<string>> = [
    ["Robot", "Robot_Robot"],
    ["Rack", "Rack_RackMat"],
    ["Warehouse", "Warehouse_Concrete"],
    ["Box", "BoxMat"],
    ["Table", "TableMat"],
    ["Cone4D", "ConeMat"],
    ["kaas", "CheeseMat"]
  ];
  private scene = new Scene();
  private truck;
  private objLoader = new OBJLoader();
  textureCube;


  public loadModels(renderer: Renderer, callback: () => void) {
    const r = "https://threejs.org/examples/textures/cube/skyboxsun25deg/";
    const urls = [r + "px.jpg", r + "nx.jpg",
      r + "py.jpg", r + "ny.jpg",
      r + "pz.jpg", r + "nz.jpg"];

    this.textureCube = new CubeTextureLoader().load(urls);

    /*const hdrUrls = ['px.hdr', 'nx.hdr', 'py.hdr', 'ny.hdr', 'pz.hdr', 'nz.hdr'];
    let kaas = this.textureCube;
    this.textureCube = new HDRCubeTextureLoader()
      .setPath( 'https://threejs.org/examples/textures/cube/pisaHDR/' )
      .setDataType( UnsignedByteType )
      .load( hdrUrls, function () {
        var pmremGenerator = new PMREMGenerator(  kaas );
        pmremGenerator.update( renderer );
        var pmremCubeUVPacker = new PMREMCubeUVPacker( pmremGenerator.cubeLods );
        pmremCubeUVPacker.update( renderer );
        //hdrCubeRenderTarget = pmremCubeUVPacker.CubeUVRenderTarget;
        kaas.magFilter = LinearFilter;
        kaas.needsUpdate = true;
        pmremGenerator.dispose();
        pmremCubeUVPacker.dispose();
      } );*/

    this.scene.background = this.textureCube;

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

    axios.get(apiHost + "getNumberOfModules").then(e => {
      this.createWarehouse(e.data);
    });

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

    this.truck = this.getModel("Cone4D");
    this.scene.add(this.truck);

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
    robot.position.set(command.parameters.x, command.parameters.z, command.parameters.y);
    this.scene.add(robot);
    this.worldObjects[command.parameters.id] = robot;
  }

  public createRack(command): void {
    let rackItemPos = [0.65, 1.15, 1.65, 2.15];
    let rack = this.getModel("Rack");

    for (let p of rackItemPos) {
      let item = this.getModel(command.parameters.item);
      item.scale.set(0.45, 0.45, 0.45);
      item.rotateY(Math.floor((Math.random() * 10) + 1));
      item.position.y = p;
      rack.add(item);
    }

    rack.position.y = -1000;
    this.scene.add(rack);
    this.worldObjects[command.parameters.id] = rack;
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
