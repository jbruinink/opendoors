package com.jdriven.opendoors;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DoorController {

    private final MessageService messageService;

    public DoorController(MessageService messageService) {
        this.messageService = messageService;
    }

    @RequestMapping("/")
    @ResponseBody
    public String openDoor() {
        messageService.sendMessage("open");
        return "Opening door";
    }
}
