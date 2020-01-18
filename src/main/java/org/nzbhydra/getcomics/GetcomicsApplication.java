package org.nzbhydra.getcomics;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class GetcomicsApplication {


    public static void main(String[] args) throws Exception {
        new SpringApplicationBuilder(GetcomicsApplication.class).headless(false).run(args);
    }

}
