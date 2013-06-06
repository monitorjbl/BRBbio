CREATE TABLE IF NOT EXISTS `plates` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `plate_id` varchar(128) NOT NULL DEFAULT '',
  `positive_control` float DEFAULT NULL,
  `negative_control` float DEFAULT NULL,
  `time_marker` int(11) DEFAULT NULL,
  `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `raw_data` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `plate_id` varchar(128) DEFAULT NULL,
  `identifier` varchar(128) DEFAULT NULL,
  `time_marker` int(11) DEFAULT NULL,
  `data` float DEFAULT NULL,
  `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;