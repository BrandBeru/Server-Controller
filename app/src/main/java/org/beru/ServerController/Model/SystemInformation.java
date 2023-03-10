/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.beru.ServerController.Model;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HardwareAbstractionLayer;
/**
 *
 * @author brand
 */
public class SystemInformation {
    
    public static String cpu(){
        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hardware = systemInfo.getHardware();
        CentralProcessor processor = hardware.getProcessor();
        
        CentralProcessor.ProcessorIdentifier processorIdentifier = processor.getProcessorIdentifier();
        
        return processorIdentifier.getName();
        
    }
    
}
