import * as THREE from 'three';
import { SocketService } from './SocketService';
require('./OrbitControls');

var camera, scene, renderer;
var cameraControls;
var worldObjects = {};
var socketService;

window.onload = function () {
  function init () {
    camera = new THREE.PerspectiveCamera(70, window.innerWidth / window.innerHeight, 1, 1000);
    cameraControls = new THREE.OrbitControls(camera);
    camera.position.z = 15;
    camera.position.y = 5;
    camera.position.x = 15;
    cameraControls.update();
    scene = new THREE.Scene();

    renderer = new THREE.WebGLRenderer({ antialias: true });
    renderer.setPixelRatio(window.devicePixelRatio);
    renderer.setSize(window.innerWidth, window.innerHeight + 5);
    document.body.appendChild(renderer.domElement);

    window.addEventListener('resize', onWindowResize, false);

    var sphericalSkyboxGeometry = new THREE.SphereGeometry(900, 32, 32);
    var sphericalSkyboxMaterial = new THREE.MeshBasicMaterial({ map: new THREE.TextureLoader().load('assets/textures/yellow_field_2k.jpg'), side: THREE.DoubleSide });
    var sphericalSkybox = new THREE.Mesh(sphericalSkyboxGeometry, sphericalSkyboxMaterial);
    scene.add(sphericalSkybox);

    var geometry = new THREE.PlaneGeometry(30, 30, 32);
    var material = new THREE.MeshBasicMaterial({
      color: 0xf75b23,
      side: THREE.DoubleSide
    });
    var plane = new THREE.Mesh(geometry, material);
    plane.rotation.x = Math.PI / 2.0;
    plane.position.x = 15;
    plane.position.z = 15;
    scene.add(plane);

    var light = new THREE.AmbientLight(0x404040);
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
