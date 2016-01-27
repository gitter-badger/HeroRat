--
-- Database: `heroserv`
--
CREATE DATABASE IF NOT EXISTS `heroserv`;
USE `heroserv`;

-- --------------------------------------------------------

--
-- Table structure for table `access`
--

CREATE TABLE IF NOT EXISTS `access` (
  `key_` text NOT NULL,
  `hwid` text NOT NULL,
  `date_limit` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `customers`
--

CREATE TABLE IF NOT EXISTS `customers` (
  `key_` text NOT NULL,
  `ip` int(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
