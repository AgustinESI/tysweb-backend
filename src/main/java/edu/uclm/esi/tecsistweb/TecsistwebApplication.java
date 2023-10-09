package edu.uclm.esi.tecsistweb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@ServletComponentScan
public class TecsistwebApplication extends SpringBootServletInitializer {

    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger("TySWeb");

    public static void main(String[] args) {
        SpringApplication.run(TecsistwebApplication.class, args);
        LOGGER.info("Service TySWeb - backend started!");
    }

}
