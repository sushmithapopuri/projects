package com.example.timezone.controller;

import org.springframework.web.bind.annotation.*;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TimezoneController {

    @GetMapping("/convert")
    public Map<String, String> convertTime(
            @RequestParam String dateTime,
            @RequestParam String fromZone,
            @RequestParam String toZone) {

        try {
            // Parse the input date-time with the source timezone
            // Expected format: ISO_LOCAL_DATE_TIME (e.g., 2023-10-27T10:00:00)
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            
            ZoneId fromId = ZoneId.of(fromZone);
            ZoneId toId = ZoneId.of(toZone);
            
            ZonedDateTime sourceDateTime = ZonedDateTime.parse(dateTime + "[" + fromZone + "]", 
                                            DateTimeFormatter.ISO_ZONED_DATE_TIME);
            
            // Or more robustly:
            // LocalDateTime ldt = LocalDateTime.parse(dateTime, formatter);
            // ZonedDateTime zdt = ldt.atZone(fromId);
            
            ZonedDateTime convertedDateTime = sourceDateTime.withZoneSameInstant(toId);
            
            Map<String, String> response = new HashMap<>();
            response.put("originalTime", sourceDateTime.toString());
            response.put("convertedTime", convertedDateTime.toString());
            response.put("fromZone", fromZone);
            response.put("toZone", toZone);
            
            return response;
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid input: " + e.getMessage());
            return errorResponse;
        }
    }

    @GetMapping("/zones")
    public String[] getAvailableZones() {
        return ZoneId.getAvailableZoneIds().toArray(new String[0]);
    }

    @GetMapping("/health")
    public Map<String, String> getHealth() {
        Map<String, String> status = new HashMap<>();
        status.put("status", "UP");
        return status;
    }
}
