-- phpMyAdmin SQL Dump
-- version 4.7.0
-- https://www.phpmyadmin.net/
--
-- Φιλοξενητής: 127.0.0.1
-- Χρόνος δημιουργίας: 21 Φεβ 2019 στις 17:28:20
-- Έκδοση διακομιστή: 10.1.24-MariaDB
-- Έκδοση PHP: 7.1.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Βάση δεδομένων: `database`
--

DELIMITER $$
--
-- Συναρτήσεις
--
CREATE DEFINER=`root`@`localhost` FUNCTION `distanceOf` (`src_lng` DECIMAL(9,6), `src_lat` DECIMAL(9,6), `dst_lng` DECIMAL(9,6), `dst_lat` DECIMAL(9,6)) RETURNS DECIMAL(16,10) return 111.111*DEGREES(ACOS(LEAST(COS(RADIANS(src_lat))*COS(RADIANS(dst_lat))* COS(RADIANS(src_lng) - RADIANS(dst_lng))+ SIN(RADIANS(src_lat))* SIN(RADIANS(dst_lat)), 1.0)))$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `price`
--

CREATE TABLE `price` (
  `id` int(11) NOT NULL,
  `value` decimal(5,2) NOT NULL,
  `dateFrom` date NOT NULL,
  `dateTo` date NOT NULL,
  `productId` int(11) NOT NULL,
  `shopId` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Άδειασμα δεδομένων του πίνακα `price`
--

INSERT INTO `price` (`id`, `value`, `dateFrom`, `dateTo`, `productId`, `shopId`) VALUES
(1, '15.35', '2017-03-09', '2018-09-09', 1, 1),
(3, '13.35', '2017-03-09', '2019-09-09', 3, 1),
(5, '4.00', '2017-03-09', '2019-09-09', 1, 5),
(6, '4.00', '2017-03-09', '2019-09-09', 1, 2),
(7, '4.80', '2019-02-02', '2019-02-02', 1, 5);

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `product`
--

CREATE TABLE `product` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `description` mediumtext,
  `category` varchar(255) NOT NULL,
  `withdrawn` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Άδειασμα δεδομένων του πίνακα `product`
--

INSERT INTO `product` (`id`, `name`, `description`, `category`, `withdrawn`) VALUES
(1, '1-h Park', 'Up to 1 hour parking', 'Short-term', 1),
(2, '2-h Park', '1-2 hours parking', 'Short-term', 0),
(3, 'helen', 'neighboraki', 'oven', 0),
(4, 'caesar', 'rome', 'emperor', 1),
(5, 'Birds', 'Boxes', 'netflix', 1),
(6, 'birds', 'ss', 's', 0),
(7, 'birds', 'love', 'stgrg', 0);

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `product_tag`
--

CREATE TABLE `product_tag` (
  `pid` int(11) NOT NULL DEFAULT '0',
  `tag` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Άδειασμα δεδομένων του πίνακα `product_tag`
--

INSERT INTO `product_tag` (`pid`, `tag`) VALUES
(1, 'first hour'),
(1, 'minimum stay'),
(2, 'second hour'),
(2, 'two hours'),
(3, 'religion'),
(4, 'veni'),
(4, 'vici'),
(4, 'vidi'),
(5, 'love'),
(5, 'movies'),
(5, 'series'),
(6, 'life'),
(6, 'love'),
(7, 'life'),
(7, 'love');

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `shop`
--

CREATE TABLE `shop` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `address` varchar(255) NOT NULL,
  `lng` decimal(9,6) NOT NULL,
  `lat` decimal(9,6) NOT NULL,
  `withdrawn` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Άδειασμα δεδομένων του πίνακα `shop`
--

INSERT INTO `shop` (`id`, `name`, `address`, `lng`, `lat`, `withdrawn`) VALUES
(1, 'omonoiaPark', 'Π. Τσαλδάρη 32', '23.727539', '37.983810', 0),
(2, 'myPark', 'Λεωφ. Αλεξάνδρας 34', '23.727510', '37.983750', 0),
(5, 'vroumvroum', 'Attikis 24', '24.727530', '35.983818', 0);

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `shop_tag`
--

CREATE TABLE `shop_tag` (
  `sid` int(11) NOT NULL DEFAULT '0',
  `tag` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Άδειασμα δεδομένων του πίνακα `shop_tag`
--

INSERT INTO `shop_tag` (`sid`, `tag`) VALUES
(1, 'Omonoia'),
(1, 'Tsaldari'),
(1, 'Ομόνοια'),
(2, 'Ampelokipi'),
(2, 'Αλεξάνδρας'),
(5, 'cars'),
(5, 'city'),
(5, 'people');

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `tdate`
--

CREATE TABLE `tdate` (
  `tempdate` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Άδειασμα δεδομένων του πίνακα `tdate`
--

INSERT INTO `tdate` (`tempdate`) VALUES
('2017-03-09');

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `users`
--

CREATE TABLE `users` (
  `username` varchar(20) NOT NULL,
  `password` varchar(255) NOT NULL,
  `name` varchar(60) NOT NULL,
  `email` varchar(40) NOT NULL,
  `admin` tinyint(1) NOT NULL,
  `token` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Άδειασμα δεδομένων του πίνακα `users`
--

INSERT INTO `users` (`username`, `password`, `name`, `email`, `admin`, `token`) VALUES
('diminiki', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'dimitra', 'diminiki@gmail.com', 0, NULL),
('kikikoko', 'de34ddf5af5bcbda0219a7280880a0b7c6ae7b12885160996fe3effaa67733a3', 'kiki', 'klik@gmail.com', 0, 'kikikokotoula'),
('rootkoko', '0b4d0fe74cb01c2b2797dd58ec57ac060e9f9e13dc5bde0bfd697e24f8fa9d35', 'koko', 'koko@gmail.com', 1, 'cm9vdGtva286cm9vdGtva28=');

--
-- Ευρετήρια για άχρηστους πίνακες
--

--
-- Ευρετήρια για πίνακα `price`
--
ALTER TABLE `price`
  ADD PRIMARY KEY (`id`) KEY_BLOCK_SIZE=11,
  ADD KEY `price_ibfk_1` (`productId`),
  ADD KEY `price_ibfk_2` (`shopId`);

--
-- Ευρετήρια για πίνακα `product`
--
ALTER TABLE `product`
  ADD PRIMARY KEY (`id`) KEY_BLOCK_SIZE=11;

--
-- Ευρετήρια για πίνακα `product_tag`
--
ALTER TABLE `product_tag`
  ADD PRIMARY KEY (`pid`,`tag`);

--
-- Ευρετήρια για πίνακα `shop`
--
ALTER TABLE `shop`
  ADD PRIMARY KEY (`id`) KEY_BLOCK_SIZE=11;

--
-- Ευρετήρια για πίνακα `shop_tag`
--
ALTER TABLE `shop_tag`
  ADD PRIMARY KEY (`sid`,`tag`);

--
-- Ευρετήρια για πίνακα `tdate`
--
ALTER TABLE `tdate`
  ADD PRIMARY KEY (`tempdate`);

--
-- Ευρετήρια για πίνακα `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`username`);

--
-- AUTO_INCREMENT για άχρηστους πίνακες
--

--
-- AUTO_INCREMENT για πίνακα `price`
--
ALTER TABLE `price`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;
--
-- AUTO_INCREMENT για πίνακα `product`
--
ALTER TABLE `product`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;
--
-- AUTO_INCREMENT για πίνακα `shop`
--
ALTER TABLE `shop`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
--
-- Περιορισμοί για άχρηστους πίνακες
--

--
-- Περιορισμοί για πίνακα `price`
--
ALTER TABLE `price`
  ADD CONSTRAINT `price_ibfk_1` FOREIGN KEY (`productId`) REFERENCES `product` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `price_ibfk_2` FOREIGN KEY (`shopId`) REFERENCES `shop` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Περιορισμοί για πίνακα `product_tag`
--
ALTER TABLE `product_tag`
  ADD CONSTRAINT `fk_prod` FOREIGN KEY (`pid`) REFERENCES `product` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Περιορισμοί για πίνακα `shop_tag`
--
ALTER TABLE `shop_tag`
  ADD CONSTRAINT `fk_shop` FOREIGN KEY (`sid`) REFERENCES `shop` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
