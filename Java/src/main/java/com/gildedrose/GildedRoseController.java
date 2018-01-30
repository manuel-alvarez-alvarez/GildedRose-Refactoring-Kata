package com.gildedrose;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = "/items")
public class GildedRoseController {

    private static final Logger LOG = LoggerFactory.getLogger(GildedRoseController.class);

    private final GildedRoseService service;

    public GildedRoseController(final GildedRoseService service) {
        this.service = service;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<Item> getItems() {
        return service.findAll();
    }

    @PostMapping(path = "/update")
    public void update() {
        this.service.updateQuality();
    }

    @ExceptionHandler(GildedRoseException.class)
    public void onGildedRoseException(final GildedRoseException exception, final HttpServletResponse response) throws IOException {
        LOG.error("Controlled error in the controller", exception);
        response.sendError(exception.getStatusCode(), exception.getStatusMessage());
    }

    @ExceptionHandler(Throwable.class)
    public void onThrowable(final Throwable exception, final HttpServletResponse response) throws IOException {
        LOG.error("Unhandled error in the controller", exception);
        response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error in the application");
    }
}
