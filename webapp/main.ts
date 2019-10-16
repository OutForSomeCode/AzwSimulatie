import {SocketService} from './SocketService';
import {OrbitControls} from 'three/examples/jsm/controls/OrbitControls';
import {
  AmbientLight,
  DoubleSide, Group,
  Mesh,
  MeshBasicMaterial,
  PerspectiveCamera,
  PlaneGeometry,
  Scene,
  SphereGeometry,
  TextureLoader,
  WebGLRenderer
} from 'three';
import {GLTFLoader} from "three/examples/jsm/loaders/GLTFLoader";
import {WorldObjectManger} from "./WorldObjectManager";

let time =0 ;
let camera: PerspectiveCamera;
let renderer: WebGLRenderer;

let cameraControls: OrbitControls;
let _socketService: SocketService;
let _worldObjectManger:  WorldObjectManger;


/*
  function init initialized the world and all its objects
 */
function init() {
  _worldObjectManger = new WorldObjectManger();
  _worldObjectManger.loadModels(() => {
    _worldObjectManger.initWorld();
    _socketService = new SocketService(_worldObjectManger);
    _socketService.connect();
    frameStep();
  });
  camera = new PerspectiveCamera(70, window.innerWidth / window.innerHeight, 1, 1000);
  cameraControls = new OrbitControls(camera);
  camera.position.z = 15;
  camera.position.y = 5;
  camera.position.x = 15;
  cameraControls.update();

  renderer = new WebGLRenderer({antialias: true});
  renderer.setPixelRatio(window.devicePixelRatio);
  renderer.setSize(window.innerWidth, window.innerHeight + 5);
  document.body.appendChild(renderer.domElement);

  window.addEventListener('resize', onWindowResize, false);


}

function onWindowResize() {
  camera.aspect = window.innerWidth / window.innerHeight;
  camera.updateProjectionMatrix();
  renderer.setSize(window.innerWidth, window.innerHeight);
}
/*
frameStep is updates every frame
 */
function frameStep() {
  requestAnimationFrame(frameStep);
  cameraControls.update();
  renderer.render(_worldObjectManger.getScene(), camera);
  time += 0.01;
  _worldObjectManger.movetruck(time);
}

window.onload = init;
