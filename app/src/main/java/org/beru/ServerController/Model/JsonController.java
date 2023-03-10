/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.beru.ServerController.Model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author brand
 */
public class JsonController {
    
    private static byte[] json_data;
    private static File f = new File(Dirs.CACHE_PATH);
    
    public static CacheDatas get(String host_name){
        try{
            json_data = Files.readAllBytes(Path.of(f.getAbsolutePath()));
            
            ObjectMapper om = new ObjectMapper();            
            
            List<CacheDatas> datas = om.readValue(json_data, new TypeReference<>() {});
            
            for(CacheDatas c : datas){
                if(c.getConnection_name().equals(host_name))
                    return c;
            }
            
            return null;            
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }
        
    }
    public static List<CacheDatas> get(){
        try{
            json_data = Files.readAllBytes(Path.of(f.getAbsolutePath()));
            
            ObjectMapper om = new ObjectMapper();            
            
            List<CacheDatas> datas = om.readValue(json_data, new TypeReference<>() {});
            
            
            return datas;
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }
        
    }
    public static void set(CacheDatas cd){
        try{            
            ObjectMapper om = new ObjectMapper();
            
            List<CacheDatas> datas = (get() != null) ? get() : new ArrayList<>();
            datas.add(cd);
              
            BufferedWriter writer = new BufferedWriter(new FileWriter(f));
            writer.write(om.writeValueAsString(datas));
            
            writer.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
}
