/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.beru.ServerController.Model;

import java.io.File;

/**
 *
 * @author brand
 */
public class Dirs {

    public final static String HOME_PATH = System.getProperty("user.home");
    public static String CONFIG_PATH;
    public static String CACHE_PATH;

    public Dirs() {
        try {
            File config = new File(HOME_PATH + "/.brand_beru");
            System.out.println(config.getAbsolutePath());
            if (config.mkdir()) {
                System.out.println("-> Global Path created");
            }
            CONFIG_PATH = config.getAbsolutePath();

            config = new File(CONFIG_PATH + "/cache.json");
            if (config.createNewFile()) {
                System.out.println("-> Cache file created");
            }
            CACHE_PATH = config.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
