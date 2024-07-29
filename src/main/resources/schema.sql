CREATE TABLE location (
  city_name VARCHAR(255) PRIMARY KEY,
  lat DOUBLE NOT NULL,
  lon DOUBLE NOT NULL
);
CREATE TABLE pollution (
   id BIGINT AUTO_INCREMENT PRIMARY KEY,
   city VARCHAR(255) NOT NULL,
   carbonmonoxide VARCHAR(255),
   sulphurdioxide VARCHAR(255),
   ozone VARCHAR(255),
   date DATE
);
