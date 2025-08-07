package com.by.dallinday.common.gpx;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import io.jenetics.jpx.GPX;
import io.jenetics.jpx.WayPoint;

public class GpxParser {
    public static List<WayPoint> extractCoordinates(String fileUrl) throws Exception {
        // 1. URL로부터 파일 다운로드
        URL url = new URL(fileUrl);
        Path tempFile = Files.createTempFile("temp-", ".gpx");

        try (InputStream in = url.openStream()) {
            Files.copy(in, tempFile, StandardCopyOption.REPLACE_EXISTING);
        }

        // 2. 임시 파일에서 GPX 파싱
        GPX gpx = GPX.read(tempFile);
        Files.deleteIfExists(tempFile); // 사용 후 임시 파일 삭제

        return gpx.getTracks().stream()
                .flatMap(track -> track.getSegments().stream())
                .flatMap(segment -> segment.getPoints().stream())
                .toList();
    }
}
