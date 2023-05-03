package com.simulation.ConfigHandler;

import com.simulation.Bee.MaleBee;
import com.simulation.Bee.WorkerBee;
import com.simulation.Model.Habitat;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigHandler {
    private static final Habitat h = Habitat.getHabitat();
    private static final String CONFIG_PATH = "src/main/resources/com/simulation/ConfigHandler/config.properties";

    public void saveConfig() {
        try (FileOutputStream fos = new FileOutputStream(CONFIG_PATH)) {
            Properties properties = new Properties();

            properties.setProperty("worker_spawn_chance", Integer.toString(h.getWorkerSpawnChance()));
            properties.setProperty("male_spawn_chance", Integer.toString(h.getMaleSpawnChance()));

            properties.setProperty("worker_spawn_time", Integer.toString(h.getWorkerSpawnTime()));
            properties.setProperty("male_spawn_time", Integer.toString(h.getMaleSpawnTime()));

            properties.setProperty("worker_lifetime", Integer.toString(WorkerBee.getLifeTime()));
            properties.setProperty("male_lifetime", Integer.toString(MaleBee.getLifeTime()));

            properties.setProperty("worker_thread_priority", Integer.toString(h.getWorkerThreadPriority()));
            properties.setProperty("male_thread_priority", Integer.toString(h.getMaleThreadPriority()));

            properties.store(fos, "cfg");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadConfig() {
        try (FileInputStream fis = new FileInputStream(CONFIG_PATH)) {
            Properties properties = new Properties();
            properties.load(fis);
            configReader(properties);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void configReader(Properties properties) {
        int num;

        num = Integer.parseInt(properties.getProperty("worker_spawn_chance", "60"));
        if (num >= 0 && num <= 100 && num % 10 == 0) {
            h.setWorkerSpawnChance(num);
        }

        num = Integer.parseInt(properties.getProperty("male_spawn_chance", "30"));
        if (num >= 0 && num <= 100 && num % 10 == 0) {
            h.setMaleSpawnChance(num);
        }

        num = Integer.parseInt(properties.getProperty("worker_spawn_time", "2"));
        if (num >= 0 && num <= 60) {
            h.setWorkerSpawnTime(num);
        }

        num = Integer.parseInt(properties.getProperty("male_spawn_time", "2"));
        if (num >= 0 && num <= 60) {
            h.setMaleSpawnTime(num);
        }

        num = Integer.parseInt(properties.getProperty("worker_lifetime", "5"));
        if (num >= 0 && num <= 999) {
            WorkerBee.setLifeTime(num);
        }

        num = Integer.parseInt(properties.getProperty("male_lifetime", "3"));
        if (num >= 0 && num <= 999) {
            MaleBee.setLifeTime(num);
        }

        num = Integer.parseInt(properties.getProperty("worker_thread_priority", "5"));
        if (num >= 0 && num <= 10) {
            h.setWorkerThreadPriority(num);
        }

        num = Integer.parseInt(properties.getProperty("male_thread_priority", "5"));
        if (num >= 0 && num <= 10) {
            h.setMaleThreadPriority(num);
        }
    }
}
