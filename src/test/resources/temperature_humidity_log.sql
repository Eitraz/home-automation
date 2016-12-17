CREATE TABLE temperature_humidity_log
(
    sensor VARCHAR(256) NOT NULL,
    temperature DECIMAL(10) NOT NULL,
    humidity DECIMAL(10),
    datetime DATETIME DEFAULT CURRENT_TIMESTAMP
);