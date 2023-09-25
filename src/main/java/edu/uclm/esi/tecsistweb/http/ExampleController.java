package edu.uclm.esi.tecsistweb.http;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("example")
public class ExampleController {


    @GetMapping("/start-example")
    public String start(HttpServletRequest request, @RequestParam(required = false) String name, @RequestParam(required = false) String pwd) {

        List<String> headers = Collections.list(request.getHeaderNames());

        headers.stream().forEach((head) -> {
            System.out.println(head + "=" + request.getHeaders(head));
        });

        return "name:" + name + "\t pwd:" + pwd;
    }

}
