package com.nhlstenden.amazonsimulatie.models;

import com.nhlstenden.amazonsimulatie.models.generated.Node;

public interface RobotTaskStrategy {
  void execute(RobotLogic robotLogic);

  Node getDestination();
}
