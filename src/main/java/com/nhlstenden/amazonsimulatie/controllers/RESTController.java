package com.nhlstenden.amazonsimulatie.controllers;

import com.nhlstenden.amazonsimulatie.models.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
public class RESTController {
  @Autowired
  private ApplicationContext context;

  @RequestMapping("/poweroff")
  public String powerOff(@RequestParam(value = "pwk", defaultValue = "None") String key) {
    if (key.equals(System.getenv("POWEROFF-KEY"))) {
      int exitCode = SpringApplication.exit(context, () -> 0);
      System.exit(exitCode);
      return "OK";
    } else {
      return "pwk is invalid";
    }
  }

  //frontend can request number of modules from the Data class
  @RequestMapping("/getNumberOfModules")
  public int amountOfModules() {
    return Data.modules;
  }
}
