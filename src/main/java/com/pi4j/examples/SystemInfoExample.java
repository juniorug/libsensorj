package com.pi4j.examples;

// START SNIPPET: system-info-snippet

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  SystemInfoExample.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2014 Pi4J
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.io.IOException;
import java.text.ParseException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pi4j.system.NetworkInfo;
import com.pi4j.system.SystemInfo;

/**
 * This example code demonstrates how to access a few of the system information
 * properties and network information from the Raspberry Pi.
 * 
 * @author Robert Savage
 */
public class SystemInfoExample {

    private static final Logger LOGGER = LogManager
            .getLogger(SystemInfoExample.class.getName());

    public static void main(String[] args) throws InterruptedException,
            IOException, ParseException {

        // display a few of the available system information properties
        LOGGER.info("----------------------------------------------------");
        LOGGER.info("HARDWARE INFO");
        LOGGER.info("----------------------------------------------------");
        LOGGER.info("Serial Number     :  " + SystemInfo.getSerial());
        LOGGER.info("CPU Revision      :  " + SystemInfo.getCpuRevision());
        LOGGER.info("CPU Architecture  :  " + SystemInfo.getCpuArchitecture());
        LOGGER.info("CPU Part          :  " + SystemInfo.getCpuPart());
        LOGGER.info("CPU Temperature   :  " + SystemInfo.getCpuTemperature());
        LOGGER.info("CPU Core Voltage  :  " + SystemInfo.getCpuVoltage());
        LOGGER.info("CPU Model Name    :  " + SystemInfo.getModelName());
        LOGGER.info("Processor         :  " + SystemInfo.getProcessor());
        LOGGER.info("Hardware Revision :  " + SystemInfo.getRevision());
        LOGGER.info("Is Hard Float ABI :  " + SystemInfo.isHardFloatAbi());
        LOGGER.info("Board Type        :  " + SystemInfo.getBoardType().name());

        LOGGER.info("----------------------------------------------------");
        LOGGER.info("MEMORY INFO");
        LOGGER.info("----------------------------------------------------");
        LOGGER.info("Total Memory      :  " + SystemInfo.getMemoryTotal());
        LOGGER.info("Used Memory       :  " + SystemInfo.getMemoryUsed());
        LOGGER.info("Free Memory       :  " + SystemInfo.getMemoryFree());
        LOGGER.info("Shared Memory     :  " + SystemInfo.getMemoryShared());
        LOGGER.info("Memory Buffers    :  " + SystemInfo.getMemoryBuffers());
        LOGGER.info("Cached Memory     :  " + SystemInfo.getMemoryCached());
        LOGGER.info("SDRAM_C Voltage   :  "
                + SystemInfo.getMemoryVoltageSDRam_C());
        LOGGER.info("SDRAM_I Voltage   :  "
                + SystemInfo.getMemoryVoltageSDRam_I());
        LOGGER.info("SDRAM_P Voltage   :  "
                + SystemInfo.getMemoryVoltageSDRam_P());

        LOGGER.info("----------------------------------------------------");
        LOGGER.info("OPERATING SYSTEM INFO");
        LOGGER.info("----------------------------------------------------");
        LOGGER.info("OS Name           :  " + SystemInfo.getOsName());
        LOGGER.info("OS Version        :  " + SystemInfo.getOsVersion());
        LOGGER.info("OS Architecture   :  " + SystemInfo.getOsArch());
        LOGGER.info("OS Firmware Build :  " + SystemInfo.getOsFirmwareBuild());
        LOGGER.info("OS Firmware Date  :  " + SystemInfo.getOsFirmwareDate());

        LOGGER.info("----------------------------------------------------");
        LOGGER.info("JAVA ENVIRONMENT INFO");
        LOGGER.info("----------------------------------------------------");
        LOGGER.info("Java Vendor       :  " + SystemInfo.getJavaVendor());
        LOGGER.info("Java Vendor URL   :  " + SystemInfo.getJavaVendorUrl());
        LOGGER.info("Java Version      :  " + SystemInfo.getJavaVersion());
        LOGGER.info("Java VM           :  "
                + SystemInfo.getJavaVirtualMachine());
        LOGGER.info("Java Runtime      :  " + SystemInfo.getJavaRuntime());

        LOGGER.info("----------------------------------------------------");
        LOGGER.info("NETWORK INFO");
        LOGGER.info("----------------------------------------------------");

        // display some of the network information
        LOGGER.info("Hostname          :  " + NetworkInfo.getHostname());
        for (String ipAddress : NetworkInfo.getIPAddresses()) {
            LOGGER.info("IP Addresses      :  " + ipAddress);
        }
        for (String fqdn : NetworkInfo.getFQDNs()) {
            LOGGER.info("FQDN              :  " + fqdn);
        }
        for (String nameserver : NetworkInfo.getNameservers()) {
            LOGGER.info("Nameserver        :  " + nameserver);
        }

        LOGGER.info("----------------------------------------------------");
        LOGGER.info("CODEC INFO");
        LOGGER.info("----------------------------------------------------");
        LOGGER.info("H264 Codec Enabled:  " + SystemInfo.getCodecH264Enabled());
        LOGGER.info("MPG2 Codec Enabled:  " + SystemInfo.getCodecMPG2Enabled());
        LOGGER.info("WVC1 Codec Enabled:  " + SystemInfo.getCodecWVC1Enabled());

        LOGGER.info("----------------------------------------------------");
        LOGGER.info("CLOCK INFO");
        LOGGER.info("----------------------------------------------------");
        LOGGER.info("ARM Frequency     :  " + SystemInfo.getClockFrequencyArm());
        LOGGER.info("CORE Frequency    :  "
                + SystemInfo.getClockFrequencyCore());
        LOGGER.info("H264 Frequency    :  "
                + SystemInfo.getClockFrequencyH264());
        LOGGER.info("ISP Frequency     :  " + SystemInfo.getClockFrequencyISP());
        LOGGER.info("V3D Frequency     :  " + SystemInfo.getClockFrequencyV3D());
        LOGGER.info("UART Frequency    :  "
                + SystemInfo.getClockFrequencyUART());
        LOGGER.info("PWM Frequency     :  " + SystemInfo.getClockFrequencyPWM());
        LOGGER.info("EMMC Frequency    :  "
                + SystemInfo.getClockFrequencyEMMC());
        LOGGER.info("Pixel Frequency   :  "
                + SystemInfo.getClockFrequencyPixel());
        LOGGER.info("VEC Frequency     :  " + SystemInfo.getClockFrequencyVEC());
        LOGGER.info("HDMI Frequency    :  "
                + SystemInfo.getClockFrequencyHDMI());
        LOGGER.info("DPI Frequency     :  " + SystemInfo.getClockFrequencyDPI());

        LOGGER.info("");
        LOGGER.info("");
    }
}
// END SNIPPET: system-info-snippet
