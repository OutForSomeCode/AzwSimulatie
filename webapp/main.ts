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


let camera: PerspectiveCamera;
let renderer: WebGLRenderer;

let cameraControls: OrbitControls;
let _socketService: SocketService;
let _worldObjectManger:  WorldObjectManger;

function init() {
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
  _worldObjectManger = new WorldObjectManger();
  _worldObjectManger.initWorld();
  _socketService = new SocketService(_worldObjectManger);
  _socketService.connect();

  frameStep();
}

function onWindowResize() {
  camera.aspect = window.innerWidth / window.innerHeight;
  camera.updateProjectionMatrix();
  renderer.setSize(window.innerWidth, window.innerHeight);
}

function frameStep() {
  requestAnimationFrame(frameStep);
  cameraControls.update();
  renderer.render(_worldObjectManger.getScene(), camera);
}

window.onload = init;
