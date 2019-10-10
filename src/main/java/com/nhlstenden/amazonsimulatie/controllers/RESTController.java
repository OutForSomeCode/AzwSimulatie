package com.nhlstenden.amazonsimulatie.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RESTController {
  @Autowired
  private ApplicationContext context;

  @RequestMapping("/poweroff")
  public String poweroff(@RequestParam(value = "pwk", defaultValue = "None") String key) {
    if (key.equals(System.getenv("POWEROFF-KEY"))) {
      int exitCode = SpringApplication.exit(context, (ExitCodeGenerator) () -> 0);
      System.exit(exitCode);
      return "OK";
    } else {
      return "pwk is invalid";
    }
  }
}
