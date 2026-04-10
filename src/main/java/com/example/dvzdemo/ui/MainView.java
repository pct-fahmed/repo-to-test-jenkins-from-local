package com.example.dvzdemo.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

@Route("")
@PageTitle("DVZ Demo")
public class MainView extends VerticalLayout {

    public MainView(JdbcTemplate jdbcTemplate) {
        List<String> messages = jdbcTemplate.queryForList(
            "select message from app_message order by id",
            String.class
        );

        Grid<String> grid = new Grid<>();
        grid.setItems(messages);
        grid.addColumn(value -> value).setHeader("Database Messages");
        grid.setAllRowsVisible(true);

        Button refreshButton = new Button("Check database", event ->
            Notification.show("Loaded " + messages.size() + " message(s) from PostgreSQL")
        );

        add(
            new H1("DVZ Demo"),
            refreshButton,
            grid
        );
    }
}
