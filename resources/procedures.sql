CREATE DEFINER=`root`@`localhost` PROCEDURE `get_student_data`(IN student_id INT)
BEGIN
	SELECT students.id, surname, name
    FROM students
    WHERE students.id = student_id;
END

CREATE DEFINER=`root`@`localhost` PROCEDURE `insertStudent`(IN in_surname varchar(30),
 IN in_name varchar(20))
BEGIN
  INSERT INTO STUDENTS (SURNAME, NAME)
  values (in_surname,in_name);
END