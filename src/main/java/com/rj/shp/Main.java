package com.rj.shp;

import com.rj.shp.exception.ConversionException;
import java.io.File;

class Main {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: <binary> <source-shp-file> <target-csv-file>");
            System.exit(1);
        }

        File source = new File(args[0]);
        File target = new File(args[1]);

        try {
            Converter.convert(source, target);
            System.out.println(String.format("File converted successfully to \"%s\"", target.getAbsolutePath()));
        }
        catch (ConversionException ex) {
            System.err.println(String.format("Failed to convert: %s", ex.getMessage()));
            System.exit(1);
        }

        System.exit(0);
    }
}