import {SocketService} from './SocketService';
import {OrbitControls} from 'three/examples/jsm/controls/OrbitControls';
import {BasicShadowMap, PerspectiveCamera, WebGLRenderer} from 'three';
import {WorldObjectManger} from "./WorldObjectManager";
import TWEEN from '@tweenjs/tween.js';
import * as Stats from "stats.js";

let camera: PerspectiveCamera;
let renderer: WebGLRenderer;

let cameraControls: OrbitControls;
let _socketService: SocketService;
let _worldObjectManger: WorldObjectManger;
let stats: Stats;

/**
 * initializes all components
 */
function init() {

  stats = new Stats();
  stats.showPanel(0); // 0: fps, 1: ms, 2: mb, 3+: custom
  document.body.appendChild(stats.dom);

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

/**
 * resize the window
 */
function onWindowResize() {
  camera.aspect = window.innerWidth / window.innerHeight;
  camera.updateProjectionMatrix();
  renderer.setSize(window.innerWidth, window.innerHeight);
}

/**
 * framestep updates every frame
 */
function frameStep() {
  stats.begin();
  cameraControls.update();
  renderer.render(_worldObjectManger.getScene(), camera);
  TWEEN.update();
  stats.end();
  requestAnimationFrame(frameStep);
}

window.onload = init;
