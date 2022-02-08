package com.company;

public class OsCheck {
    public enum OSType{
        Windows, MacOs, Linux, FreeBSD, Other
    }

    protected static OSType detectedOS;

    public static OSType getOSType(){
        if(detectedOS==null){
            String os = System.getProperty("os.name", "generic").toLowerCase();
            if((os.contains("mac")) || (os.contains("darwin"))){
                detectedOS = OSType.MacOs;
            }else if(os.contains("win")){
                detectedOS = OSType.Windows;
            }else if(os.contains("nux")){
                detectedOS = OSType.Linux;
            }else if(os.contains("bsd")){
                detectedOS = OSType.FreeBSD;
            }else{
                detectedOS = OSType.Other;
            }
        }
        return detectedOS;
    }
}
