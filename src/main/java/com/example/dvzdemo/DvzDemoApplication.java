package com.example.dvzdemo;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.aura.Aura;
import com.vaadin.flow.component.dependency.StyleSheet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@StyleSheet(Aura.STYLESHEET)
public class DvzDemoApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(DvzDemoApplication.class, args);
    }
}
