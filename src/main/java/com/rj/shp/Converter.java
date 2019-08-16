package com.rj.shp;

import com.rj.shp.exception.ConversionException;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.opengis.feature.simple.SimpleFeature;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.stream.Collectors;

public class Converter {

    public static void convert(File source, File target) throws ConversionException {
        if (!source.exists()) {
            throw new ConversionException(String.format("Source file \"%s\" does not exist", source.getAbsolutePath()));
        }

        try {
            FileDataStore store = FileDataStoreFinder.getDataStore(source);
            SimpleFeatureSource featureSource = store.getFeatureSource();
            SimpleFeatureCollection featureCollection = featureSource.getFeatures();
            SimpleFeatureIterator featureIterator = featureCollection.features();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(target))) {
                while (featureIterator.hasNext()) {
                    SimpleFeature feature = featureIterator.next();
                    String id = feature.getID().substring(feature.getID().lastIndexOf('.') + 1);

                    writer.write(String.format("%s,%s", id, feature.getAttributes()
                            .stream()
                            .map(Object::toString)
                            .collect(Collectors.joining(",", "", "\n"))
                    ));
                }
            }
        }
        catch (IOException ex) {
            throw new ConversionException(String.format("IO exception during conversion (%s)", ex.getMessage()), ex);
        }
    }

}
