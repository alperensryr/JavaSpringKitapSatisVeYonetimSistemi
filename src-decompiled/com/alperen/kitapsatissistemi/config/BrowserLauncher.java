/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.config.BrowserLauncher
 *  org.springframework.beans.factory.annotation.Value
 *  org.springframework.boot.context.event.ApplicationReadyEvent
 *  org.springframework.context.annotation.Profile
 *  org.springframework.context.event.EventListener
 *  org.springframework.stereotype.Component
 */
package com.alperen.kitapsatissistemi.config;

import java.awt.Desktop;
import java.net.URI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Profile(value={"!test"})
public class BrowserLauncher {
    @Value(value="${server.port:8080}")
    private int serverPort;

    @EventListener(value={ApplicationReadyEvent.class})
    public void openHomePage() {
        try {
            Desktop desktop;
            Thread.sleep(500L);
            if (Desktop.isDesktopSupported() && (desktop = Desktop.getDesktop()).isSupported(Desktop.Action.BROWSE)) {
                desktop.browse(new URI("http://localhost:" + this.serverPort));
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }
}

