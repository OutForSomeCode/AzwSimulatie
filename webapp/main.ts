import {SocketService} from './SocketService';
import {OrbitControls} from 'three/examples/jsm/controls/OrbitControls';
import {BasicShadowMap, PerspectiveCamera, WebGLRenderer} from 'three';
import {WorldObjectManger} from "./WorldObjectManager";
import TWEEN from '@tweenjs/tween.js';

let camera: PerspectiveCamera;
let renderer: WebGLRenderer;

let cameraControls: OrbitControls;
let _socketService: SocketService;
let _worldObjectManger: WorldObjectManger;


/*
  function init initialized the world and all its objects
 */
function init() {
  _worldObjectManger = new WorldObjectManger();
  _worldObjectManger.loadModels(renderer, () => {
    _worldObjectManger.initWorld();
    _socketService = new SocketService(_worldObjectManger);
    _socketService.connect();
    frameStep();
  });
  camera = new PerspectiveCamera(70, window.innerWidth / window.innerHeight, 1, 1000);

  camera.position.z = 15;
  camera.position.y = 5;
  camera.position.x = 15;

  renderer = new WebGLRenderer({antialias: true});
  renderer.setPixelRatio(window.devicePixelRatio);
  renderer.setSize(window.innerWidth, window.innerHeight + 5);
  renderer.shadowMap.enabled = true;
  renderer.shadowMap.type = BasicShadowMap;
  document.body.appendChild(renderer.domElement);

  cameraControls = new OrbitControls(camera, renderer.domElement);
  cameraControls.update();

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
  TWEEN.update();
}

window.onload = init;
