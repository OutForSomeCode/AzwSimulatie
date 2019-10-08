import { SocketService } from './SocketService';
import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls';
import {
  AmbientLight, DoubleSide,
  Mesh,
  MeshBasicMaterial,
  PerspectiveCamera,
  PlaneGeometry,
  Scene,
  SphereGeometry,
  TextureLoader,
  WebGLRenderer
} from 'three';

var camera, scene, renderer;
var cameraControls;
var worldObjects = {};
var socketService;

window.onload = function () {
  function init () {
    camera = new PerspectiveCamera(70, window.innerWidth / window.innerHeight, 1, 1000);
    cameraControls = new OrbitControls(camera);
    camera.position.z = 15;
    camera.position.y = 5;
    camera.position.x = 15;
    cameraControls.update();
    scene = new Scene();

    renderer = new WebGLRenderer({ antialias: true });
    renderer.setPixelRatio(window.devicePixelRatio);
    renderer.setSize(window.innerWidth, window.innerHeight + 5);
    document.body.appendChild(renderer.domElement);

    window.addEventListener('resize', onWindowResize, false);

    var sphericalSkyboxGeometry = new SphereGeometry(900, 32, 32);
    var sphericalSkyboxMaterial = new MeshBasicMaterial({
      map: new TextureLoader().load('assets/textures/yellow_field_2k.jpg'),
      side: DoubleSide
    });
    var sphericalSkybox = new Mesh(sphericalSkyboxGeometry, sphericalSkyboxMaterial);
    scene.add(sphericalSkybox);

    var geometry = new PlaneGeometry(30, 30, 32);
    var material = new MeshBasicMaterial({
      color: 0xf75b23,
      side: DoubleSide
    });
    var plane = new Mesh(geometry, material);
    plane.rotation.x = Math.PI / 2.0;
    plane.position.x = 15;
    plane.position.z = 15;
    scene.add(plane);

    var light = new AmbientLight(0x404040);
    light.intensity = 4;
    scene.add(light);

    socketService = new SocketService(worldObjects, scene);
    socketService.connect();
  }

  function onWindowResize () {
    camera.aspect = window.innerWidth / window.innerHeight;
    camera.updateProjectionMatrix();
    renderer.setSize(window.innerWidth, window.innerHeight);
  }

  function animate () {
    requestAnimationFrame(animate);
    cameraControls.update();
    renderer.render(scene, camera);
  }

  init();
  animate();
};
