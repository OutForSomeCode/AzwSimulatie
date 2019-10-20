package com.nhlstenden.amazonsimulatie.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhlstenden.amazonsimulatie.views.View;
import org.msgpack.jackson.dataformat.MessagePackFactory;
import org.springframework.web.socket.BinaryMessage;

import java.util.ArrayList;
import java.util.List;

class Queue {
  private List<Object> queue = new ArrayList<Object>();
  private ObjectMapper objectMapper;

  // private constructor restricted to this class itself
  Queue() {
    this.objectMapper = new ObjectMapper(new MessagePackFactory());
  }

  void addCommandToQueue(String s, Object o) {
    queue.add(new packData(s, o));
  }

  void flush(List<View> views) {
    try {
      if (queue.size() == 0)
        return;
      BinaryMessage m = new BinaryMessage(objectMapper.writeValueAsBytes(queue));
      for (int i = 0; i < views.size(); i++) {
        views.get(i).update(m);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    queue.clear();
  }

  void flush(View view) {
    try {
      if (queue.size() == 0)
        return;
      BinaryMessage m = new BinaryMessage(objectMapper.writeValueAsBytes(queue));
      view.update(m);
    } catch (Exception e) {
      e.printStackTrace();
    }

    queue.clear();
  }

  static class packData {
    public String command;
    public Object parameters;

    public packData(String c, Object d) {
      command = c;
      parameters = d;
    }
  }
}
