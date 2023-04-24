CREATE TABLE IF NOT EXISTS doctorLogin (
  username varchar(255) NOT NULL,
  password varchar(255) NOT NULL
);
INSERT INTO `doctorLogin` VALUES ('Jones150','Wallop123#');
INSERT INTO `doctorLogin` VALUES ('Philip129','Magnum21!');
INSERT INTO `doctorLogin` VALUES ('Saint192','Cola47@');
INSERT INTO `doctorLogin` VALUES ('Bennet319','Fanta99$');
INSERT INTO `doctorLogin` VALUES ('Dover937','Sprite145%');


CREATE TABLE IF NOT EXISTS bookings (
    name varchar(255) NOT NULL,
    bookingDay  INT NOT NULL,
    bookingMonth INT NOT NULL,
    bookingYear INT NOT NULL
);
/* Random Data */
INSERT INTO `bookings` VALUES ('Dr Jones','Tuesday','November','2022');
INSERT INTO `bookings` VALUES ('Dr Philip','Thursday','December','2022');
INSERT INTO `bookings` VALUES ('Dr Jones','Wednesday','January','2023');
INSERT INTO `bookings` VALUES ('Dr Saint','Friday','February','2023');
INSERT INTO `bookings` VALUES ('Dr Jones','Monday','February','2023');
INSERT INTO `bookings` VALUES ('Dr Saint','Tuesday','February','2023');
INSERT INTO `bookings` VALUES ('Dr Bennet','Wednesday','February','2023');
INSERT INTO `bookings` VALUES ('Dr Jones','Thursday','March','2023');
INSERT INTO `bookings` VALUES ('Dr Dover','Friday','March','2023');
