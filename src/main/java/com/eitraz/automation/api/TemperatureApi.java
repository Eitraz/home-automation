package com.eitraz.automation.api;

import com.eitraz.automation.database.dao.TemperatureHumidityLogEntityDao;
import com.eitraz.automation.database.model.TemperatureHumidityLogEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/temperatures")
public class TemperatureApi {
    private static final Pattern HISTORY_PATTERN = Pattern.compile("(\\d+)([m|h|d|w|y])");
    private final TemperatureHumidityLogEntityDao dao;

    @Autowired
    public TemperatureApi(TemperatureHumidityLogEntityDao dao) {
        this.dao = dao;
    }

    // Duplicate of GET:/sensors, but from database instead of memory
    @SuppressWarnings("unchecked")
    @GetMapping("/")
    public Map<String, Map<String, Object>> temperatures() {
        return dao.lastEntries().stream()
                  .collect(Collectors.toMap(
                          TemperatureHumidityLogEntity::getSensor,
                          e -> new HashMap() {
                              {
                                  put("time", e.getDatetime().toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                                  put("temperature", e.getTemperature());
                                  put("humidity", e.getHumidity());
                              }
                          }));
    }

    @GetMapping("/history/{length}")
    public Map<String, List<Map<String, Object>>> history(@PathVariable("length") String length) {
        long offsetMillis = 1000 * 60;
        long dateTimeGroupDividerMillis = 1000 * 60;   // Min

        Matcher matcher = HISTORY_PATTERN.matcher(length);

        if (matcher.matches()) {
            long value = Long.parseLong(matcher.group(1));
            String type = matcher.group(2);

            // Minute
            if ("m".equalsIgnoreCase(type)) {
                offsetMillis *= value;
            }
            // Hour
            else if ("h".equalsIgnoreCase(type)) {
                offsetMillis *= 60 * value;
            }
            // Day
            else if ("d".equalsIgnoreCase(type)) {
                offsetMillis *= 60 * 24 * value;
            }
            // Week
            else if ("w".equalsIgnoreCase(type)) {
                offsetMillis *= 60 * 24 * 7 * value;
            }
            // Year
            else if ("y".equalsIgnoreCase(type)) {
                offsetMillis *= 60 * 24 * 365 * value;
            }

            dateTimeGroupDividerMillis = offsetMillis / (50-1);
        }
        // Invalid length
        else {
            throw new UnsupportedOperationException(String.format("Unsupported length '%s'", length));
        }

        List<TemperatureHumidityLogEntity> entries = dao.listFromDateTime(new Timestamp(System.currentTimeMillis() - offsetMillis));

        long finalDateTimeGroupDividerMillis = dateTimeGroupDividerMillis;

        // Group by sensor
        Map<String, List<TemperatureHumidityLogEntity>> groupedBySensor = entries
                .stream()
                .collect(Collectors.groupingBy(TemperatureHumidityLogEntity::getSensor));

        return groupedBySensor
                .entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,

                        // Group by date time divider (for example minute, hour or day)
                        e -> toOrderedList(groupEntriesByTime(e.getValue(), finalDateTimeGroupDividerMillis))
                ));
    }

    private Map<LocalDateTime, Double> groupEntriesByTime(List<TemperatureHumidityLogEntity> entries, long dateTimeGroupDividerMillis) {
        return entries.stream()
                      .collect(Collectors.groupingBy(t -> LocalDateTime.ofInstant(
                              Instant.ofEpochMilli(Math.round(t.getDatetime().getTime() / (double) dateTimeGroupDividerMillis) * dateTimeGroupDividerMillis), ZoneId.systemDefault()
                      )))

                      // Calculate average value for group
                      .entrySet().stream()
                      .collect(Collectors.toMap(
                              Map.Entry::getKey,
                              d -> d.getValue().stream()
                                    .map(dt -> dt.getTemperature().doubleValue())
                                    .mapToDouble(dv -> dv)
                                    .average().orElse(0d)
                      ));
    }

    private List<Map<String, Object>> toOrderedList(Map<LocalDateTime, Double> entries) {
        return new TreeMap<>(entries)
                .entrySet().stream()
                .map(e -> new HashMap<String, Object>() {{
                    put("time", e.getKey().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                    put("temperature", e.getValue());
                }})
                .collect(Collectors.toList());
    }
}
