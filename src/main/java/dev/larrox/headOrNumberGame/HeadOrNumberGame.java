package dev.larrox.headOrNumberGame;

import org.bukkit.plugin.java.JavaPlugin;

public final class HeadOrNumberGame extends JavaPlugin {

    private static HeadOrNumberGame instance;

    @Override
    public void onEnable() {
        instance = this; // Speichert die Instanz
    }

    @Override
    public void onDisable() {
        // Bereinige Ressourcen oder mache Aufräumarbeiten hier
    }

    // Statische Methode, um die Plugin-Instanz zu bekommen
    public static HeadOrNumberGame getInstance() {
        return instance; // Gibt die gespeicherte Instanz zurück
    }
}
