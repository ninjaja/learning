USE `learning`;

DELIMITER $$

DROP TRIGGER IF EXISTS learning.students_AFTER_INSERT$$
USE `learning`$$
CREATE DEFINER = CURRENT_USER TRIGGER `learning`.`students_AFTER_INSERT` AFTER INSERT ON `students` FOR EACH ROW
BEGIN
	INSERT INTO students_audit
    SET action = 'insert',
        studentId = NEW.id,
        surname = NEW.surname,
        name = NEW.name,
        changedate = NOW();
END$$
DELIMITER ;