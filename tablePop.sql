/* Clears the database and repopulates with the "default" database data */

delete from company;
delete from course;
delete from covers;
delete from has_skill;
delete from job;
delete from job_profile;
delete from knowledge_skill;
delete from pays;
delete from person;
delete from requires;
delete from section;
delete from works_job;
/* person(per_id, name, street, city, address, gender, email)*/
insert into person values('0001', 'Doe', 'Oak', 'New Orleans', '1000','M', 'doe@123.com' );
insert into person values('0002', 'Brown', 'Pine', 'Baton Rouge', '104','F', 'brown@gmail.com' );
insert into person values('0003', 'Smith', 'Bourbon', 'New Orleans', '2413', 'M', 'smith@yahoo.com');
insert into person values('0004', 'Rogers', 'Canal', 'New Orleans','972', 'M', 'rogers@gmail.com');
insert into person values('0005', 'Craig', 'Canal', 'New Orleans','1373', 'F', 'craig@gmail.com'); 
insert into person values('0006', 'White', 'Veterans', 'Metarie', '1000', 'M', 'white@yahoo.com'); 
insert into person values('0007', 'Green', 'Napoleon', 'New Orleans', '35', 'F', 'green@aol.com');
/* job_profile(jp_code, job_title, description, avg_pay *//
insert into job_profile values('1234', 'Software Engineer', 'none', '60000');
insert into job_profile values('1111', 'Janitor', 'none', '20');
insert into job_profile values('2145', 'Project Manager', 'none', '100000');
insert into job_profile values('0001', 'sales person', 'none', '20');
/* job(job_code, jp_code, type, pay_rate, pay_type, company, salary_change, date_started) */
insert into job values('A12','1234','Full Time','60000','salary','ABC inc', 2000,'21-JAN-2010' );
insert into job values('A99', '1111', 'Part Time', '19', 'wage', 'Lowes', 1, '12-MAY-2015');
insert into job values('A22', '2145', 'Full Time', '100000', 'salary', 'ABC inc', 5000, '18-MAY-2013');
insert into job values('A00', '0001', 'Full Time', '25', 'wage', 'Home Depot', -2, '21-MAR-2014');
insert into job values('A01', '0001', 'Full Time', '25', 'wage', 'Home Depot', 1, '21-FEB-2011');
insert into job values('A02', '0001', 'Full Time', '25', 'wage', 'Home Depot', 0, '10-MAY-2012');
insert into job values('A13', '1234', 'Full Time', '65000', 'salary', 'XYZ Software', -2000, '21-OCT-2009');
insert into job values('A14', '1234', 'Full Time', '67000', 'salary', 'XYZ Software', 350, '28-JAN-2015');
/* works_job(job_code, per_id) */
insert into works_job values('A12', '0001');
insert into works_job values ('A99', '0002');
insert into works_job values('A22', '0003');
insert into works_job values('A22', '0004');
/* company(comp_id, company_name, company_street, company_city, company_address, company_zip, primary_sector, website) */
insert into company values ('aa00', 'ABC inc','St. Charles', 'New Orleans', '123', '70123', 'Technology', 'none');
insert into company values ('ca12','Lowes','first','madeville','123456','70000','construction','www.lowes.com');
/* pays(job_code, comp_id) */
insert into pays values ('A12', 'aa00');
insert into pays values ('A99', 'ca12');
insert into pays values ('A22', 'aa00');
/* knowledge_skill(ks_code, skill_title, skill_description, ks_level) */
insert into knowledge_skill values ('ks011', 'java1', 'java coding', '1');
insert into knowledge_skill values ('ks021', 'sweeping', 'sweep floors', '1');
insert into knowledge_skill values ('ks101', 'SQL', 'sql coding', '1');
insert into knowledge_skill values ('ks012', 'java2', 'advanced java', '2');
insert into knowledge_skill values ('ks201', 'databases', 'sql, rel. alg,', '1');
/* has_skill(per_id, ks_code) */
insert into has_skill values ('0001', 'ks011');
insert into has_skill values ('0001', 'ks101');
insert into has_skill values ('0001', 'ks201');
insert into has_skill values ('0002', 'ks021');
insert into has_skill values ('0002', 'ks101');
insert into has_skill values ('0003', 'ks011');
insert into has_skill values ('0003', 'ks101');
insert into has_skill values ('0003', 'ks012');
insert into has_skill values ('0006', 'ks021');
insert into has_skill values ('0007', 'ks011');
insert into has_skill values ('0007', 'ks101');
insert into has_skill values ('0007', 'ks012');
insert into has_skill values ('0007', 'ks201');
/* [job]requires(jp_code, ks_code) */
insert into requires values ('2145', 'ks201');
insert into requires values ('2145', 'ks101');
insert into requires values ('1234', 'ks011');
insert into requires values ('1234', 'ks101');
insert into requires values ('1234', 'ks012');
insert into requires values ('1234', 'ks201');
insert into requires values ('1111', 'ks021');
insert into requires values ('0001', 'ks021');
/* course(c_code, course_title, c_level, course_description, status, retail_price*/
insert into course values (0001, 'Java2', '2', 'none', 'open', '750');
insert into course values (0002, 'Software Engineering', '1','none', 'open', '1250');
insert into course values (0003, 'Systems', '2', 'none', 'open', '850');
insert into course values (0004, 'Java 1', '1', 'none', 'open', '650');
insert into course values (0005, 'Sweeping', '1', 'none', 'open', '100');
insert into course values (0006, 'Crash Course', '2', 'none', 'open', '2500');
/* [course]covers(c_code, ks_code) */
insert into covers values (0001, 'ks012');
insert into covers values (0001, 'ks201');
insert into covers values (0002, 'ks012');
insert into covers values (0002, 'ks201'); 
insert into covers values (0004, 'ks101'); 
insert into covers values (0005, 'ks021');  
insert into covers values (0006, 'ks011');
insert into covers values (0006, 'ks101');
insert into covers values (0006, 'ks012');
insert into covers values (0006, 'ks201');
/* has_worked (per_id, job_code)*/
insert into has_worked values ('A99','0001');
insert into has_worked values ('A22','0001');
insert into has_worked values ('A99','0004');
/*section (sec_id, c_code, sec_no, complete_date, year, offered_by, format, price)*/
insert into section values ('sec101', 0001, '01', '21-MAY-2017', '2017', 'UNO', 'on campus', '750');
insert into section values ('sec102', 0001, '02', '21-DEC-2017', '2017', 'UNO', 'on campus', '750');
insert into section values ('sec103', 0001, '03', '21-MAY-2018', '2018', 'UNO', 'on campus', '750');
insert into section values ('sec201', 0002, '01', '21-AUG-2017', '2017', 'UNO', 'on campus', '1250');
insert into section values ('sec202', 0002, '02', '21-NOV-2017', '2017', 'UNO', 'on campus', '1250');
insert into section values ('sec203', 0002, '03', '21-MAY-2018', '2018', 'UNO', 'on campus', '1250');
insert into section values ('sec301', 0003, '01', '21-MAY-2018', '2018', 'UNO', 'on campus', '1250');
insert into section values ('sec302', 0004, '01', '21-MAY-2018', '2018', 'UNO', 'on campus', '1250');
insert into section values ('sec601', 0006, '01', '21-MAY-2017', '2017', 'UNO', 'on campus', '2500');
insert into section values ('sec602', 0006, '02', '21-MAY-2018', '2018', 'UNO', 'on campus', '2500');
INSERT INTO CourseSet
		SELECT CourseSet_seq.NEXTVAL, C1.c_code, C2.c_code, null, 2 
		FROM Course C1, Course C2
		WHERE C1.c_code < C2.c_code;
INSERT INTO CourseSet
	SELECT CourseSet_seq.NEXTVAL, C1.c_code, C2.c_code, C3.c_code, 3 
	FROM Course C1, Course C2, Course C3
	WHERE C1.c_code < C2.c_code AND C2.c_code < C3.c_code;






