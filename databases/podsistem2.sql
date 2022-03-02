CREATE DATABASE  IF NOT EXISTS `podsistem2` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `podsistem2`;
-- MySQL dump 10.13  Distrib 8.0.27, for Win64 (x86_64)
--
-- Host: localhost    Database: podsistem2
-- ------------------------------------------------------
-- Server version	8.0.27

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `komitent`
--

DROP TABLE IF EXISTS `komitent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `komitent` (
  `IdKom` int NOT NULL AUTO_INCREMENT,
  `Naziv` varchar(45) NOT NULL,
  `Adresa` varchar(45) NOT NULL,
  `IdMes` int NOT NULL,
  PRIMARY KEY (`IdKom`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `komitent`
--

LOCK TABLES `komitent` WRITE;
/*!40000 ALTER TABLE `komitent` DISABLE KEYS */;
INSERT INTO `komitent` VALUES (1,'Dejan Kovacevic','Karadjordjeva 140',1),(2,'Ivan Pesic','Cara Dusana 20',4),(3,'Miljan Markovic','Kralja Petra 23',8),(4,'Srdjan Kuzmanovic','Njegoseva 16',5),(5,'Pera Peric','Svetozara Markovica 34',2),(6,'Aleksa Markovic','Igora Tartalje 75',4),(7,'Zarko Petrovic','Mladena Stojanovica 78',1),(8,'Marica Jankovic','Novosadska 36',1),(9,'Marko Zivanovic','Cara Dusana 87',3),(10,'Milos Maric','Nikole Tesle 43',2);
/*!40000 ALTER TABLE `komitent` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `racun`
--

DROP TABLE IF EXISTS `racun`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `racun` (
  `IdRac` int NOT NULL AUTO_INCREMENT,
  `Stanje` int NOT NULL DEFAULT '0',
  `DozvoljeniMinus` int NOT NULL,
  `Status` char(1) NOT NULL DEFAULT 'A',
  `DatumVreme` datetime NOT NULL,
  `BrojTransakcija` int NOT NULL DEFAULT '0',
  `IdKom` int NOT NULL,
  `IdMes` int NOT NULL,
  PRIMARY KEY (`IdRac`),
  KEY `FK_racun_IdKom_idx` (`IdKom`),
  CONSTRAINT `FK_racun_IdKom` FOREIGN KEY (`IdKom`) REFERENCES `komitent` (`IdKom`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `racun`
--

LOCK TABLES `racun` WRITE;
/*!40000 ALTER TABLE `racun` DISABLE KEYS */;
INSERT INTO `racun` VALUES (1,9000,-2000,'A','2022-02-11 20:51:52',6,1,1),(2,8000,-5000,'A','2022-02-11 20:59:28',4,2,4),(3,12000,-8000,'A','2022-02-11 21:14:37',7,3,2),(4,0,-10000,'U','2022-02-11 21:25:06',0,1,2),(5,-15000,-10000,'B','2022-02-11 21:49:51',1,1,2),(6,0,-10000,'U','2022-02-11 21:49:51',0,1,2),(7,-6000,-5000,'B','2022-02-24 23:16:28',2,6,5),(8,-5000,-3000,'B','2022-02-24 23:16:57',4,7,7),(9,16500,-8500,'A','2022-02-24 23:18:13',2,6,4),(10,12600,-15000,'A','2022-02-24 23:19:00',3,8,5);
/*!40000 ALTER TABLE `racun` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transakcija`
--

DROP TABLE IF EXISTS `transakcija`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transakcija` (
  `IdTra` int NOT NULL AUTO_INCREMENT,
  `RedniBroj` int NOT NULL,
  `Iznos` int NOT NULL,
  `Tip` char(1) NOT NULL,
  `Svrha` varchar(45) NOT NULL,
  `DatumVreme` datetime NOT NULL,
  `IdRac1` int NOT NULL,
  `IdRac2` int DEFAULT NULL,
  `IdFil` int DEFAULT NULL,
  PRIMARY KEY (`IdTra`),
  KEY `FK_transakcija_IdRac1_idx` (`IdRac1`),
  KEY `FK_transakcija_IdRac2_idx` (`IdRac2`),
  CONSTRAINT `FK_transakcija_IdRac1` FOREIGN KEY (`IdRac1`) REFERENCES `racun` (`IdRac`) ON UPDATE CASCADE,
  CONSTRAINT `FK_transakcija_IdRac2` FOREIGN KEY (`IdRac2`) REFERENCES `racun` (`IdRac`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transakcija`
--

LOCK TABLES `transakcija` WRITE;
/*!40000 ALTER TABLE `transakcija` DISABLE KEYS */;
INSERT INTO `transakcija` VALUES (1,1,2000,'U','Svrha nebitna.','2022-02-12 00:54:06',1,NULL,2),(2,1,3000,'U','Svrha nebitna.','2022-02-12 00:56:17',2,NULL,1),(3,2,4000,'U','Svrha nebitna.','2022-02-12 00:57:30',1,NULL,3),(4,1,4000,'U','Svrha nebitna.','2022-02-12 00:58:36',3,NULL,3),(5,2,4000,'I','Svrha nebitna.','2022-02-12 00:59:20',3,NULL,3),(6,3,4000,'I','Svrha nebitna.','2022-02-12 01:00:23',3,NULL,3),(7,4,4000,'I','Svrha nebitna.','2022-02-12 01:00:46',3,NULL,3),(8,5,4000,'I','Svrha nebitna.','2022-02-12 01:01:30',3,NULL,3),(9,6,14000,'U','Svrha nebitna.','2022-02-12 01:02:13',3,NULL,3),(10,3,1000,'U','Svrha nebitna.','2022-02-12 01:03:00',1,NULL,3),(11,2,10000,'U','Svrha nebitna.','2022-02-12 01:03:19',2,NULL,2),(12,4,10000,'P','Svrha nebitna.','2022-02-12 01:05:44',1,3,NULL),(13,3,10000,'P','Svrha nebitna.','2022-02-12 01:06:51',2,1,NULL),(14,1,15000,'I','Svrha nebitna.','2022-02-12 15:58:06',5,NULL,2),(15,6,2000,'U','Svrha nebitna.','2022-02-24 19:06:41',1,NULL,2),(16,1,5000,'U','Svrha nebitna.','2022-02-24 23:26:34',7,NULL,5),(17,1,13000,'U','Svrha nebitna.','2022-02-24 23:30:44',8,NULL,7),(18,1,6500,'U','Svrha nebitna.','2022-02-24 23:31:48',9,NULL,3),(19,1,9300,'U','Svrha nebitna.','2022-02-24 23:32:36',10,NULL,6),(20,2,300,'U','Svrha nebitna.','2022-02-24 23:40:04',10,NULL,2),(21,2,11000,'I','Svrha nebitna.','2022-02-25 00:00:58',7,NULL,1),(22,2,10000,'P','Svrha nebitna.','2022-02-25 00:02:51',8,9,NULL),(23,3,3000,'P','Svrha nebitna.','2022-02-25 00:06:29',8,10,NULL),(24,4,5000,'P','Svrha nebitna.','2022-02-25 00:07:09',8,2,NULL);
/*!40000 ALTER TABLE `transakcija` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-02-26  0:40:45
