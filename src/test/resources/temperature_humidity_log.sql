-- CREATE TABLE temperature_humidity_log
-- (
--     sensor VARCHAR(256) NOT NULL,
--     temperature DECIMAL(10) NOT NULL,
--     humidity DECIMAL(10),
--     datetime DATETIME DEFAULT CURRENT_TIMESTAMP
-- );

CREATE TABLE temperature_humidity_log
(
  id          INT                                IDENTITY,
  sensor      VARCHAR(256)                       NOT NULL,
  temperature DECIMAL(5, 2)                      NOT NULL,
  humidity    DECIMAL(5, 2)                      NULL,
  datetime    DATETIME DEFAULT CURRENT_TIMESTAMP NULL
);

